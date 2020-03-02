package uk.ac.cam.gp.charlie.metamorphic.bug_reports;

import grakn.client.GraknClient;
import graql.lang.Graql;
import graql.lang.query.GraqlGet;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.Utils;

import java.util.ArrayList;

import static graql.lang.Graql.*;

public class EqualityBugReport  {

    public static void main(String[] args) {
        Utils.DebugPrinter.start();

        GraknClient client = new GraknClient("localhost:48555");
        GraknClient.Session session = client.session("equality_rule_bug");

        GraknClient.Transaction writeTransaction = session.transaction().write();
        ArrayList<GraqlQuery> writeQueries = new ArrayList<>();

        //Define a graph schema
        writeQueries.add(Graql.define(type("label").sub("attribute").datatype("string")));
        writeQueries.add(Graql.define(type("edge").sub("relation").relates("source").relates("destination")));
        writeQueries.add(Graql.define(type("vertex").sub("entity").has("label").plays("source").plays("destination")));

        //Add a vertex and an edge
        writeQueries.add(
                Graql.insert(var("v").isa("vertex").has("label", "0"))
        );

        writeQueries.add(Graql.match(
                var("s").isa("vertex").has("label", "0"),
                var("t").isa("vertex").has("label", "0")
        ).insert(
                var("e").isa("edge").rel("source", "s").rel("destination", "t")
        ));

        //Add a unary relation that holds iff the argument is a vertex
        writeQueries.add(Graql.define(type("hasedge").sub("relation").relates("v")));
        writeQueries.add(Graql.define(type("hasedge-rule").sub("rule").when(
                var().rel("source", "x").rel("destination", "y").isa("edge")
        ).then(var().isa("hasedge").rel("v", "x"))));

        //Define a relation and a rule for if two vertices have equal labels
        writeQueries.add(Graql.define(
                type("equal").sub("relation").relates("l").relates("r"))
        );

        writeQueries.add(Graql.define(type("vertex").sub("entity").plays("l").plays("r")));

        writeQueries.add(Graql.define(
                type("equal-rule").sub("rule").when(and(
                        var("v1").isa("vertex"),
                        var("v2").isa("vertex"),
                        var("v1").has("label", var("l1")),
                        var("v2").has("label", var("l2")),
                        var("l1").eq(var("l2"))
                ))
                        .then(
                                var().isa("equal").rel("l","v1").rel("r", "v2")
                        )
        ));


        for (GraqlQuery query : writeQueries) {
            writeTransaction.execute(query);
        }
        writeTransaction.commit();


        //Ask multiple times for pairs of vertices with equal labels using the defined rule
        GraqlGet getQuery = Graql.match(
                var().rel("v", "x").isa("hasedge"),
                var().rel("v", "t").isa("hasedge"),
                var().rel("l", "x").rel("r", "t").isa("equal")
        ).get("x", "t");

        //Run them in different transactions, otherwise
        for(int i=0; i<20; ++i) {
            GraknClient.Transaction readTransaction = session.transaction().read();
            //The query result is sometimes empty, even though it's the exact same query
            Utils.DebugPrinter.print(readTransaction.execute(getQuery).toString());
            readTransaction.close();
        }

        session.close();
        client.close();

    }
}
