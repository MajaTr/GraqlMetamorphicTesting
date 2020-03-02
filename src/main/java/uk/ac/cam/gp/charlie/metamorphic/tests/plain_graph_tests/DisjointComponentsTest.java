package uk.ac.cam.gp.charlie.metamorphic.tests.plain_graph_tests;

import graql.lang.Graql;
import graql.lang.pattern.Pattern;
import graql.lang.query.GraqlQuery;
import graql.lang.statement.Statement;
import graql.lang.statement.StatementInstance;
import graql.lang.statement.StatementType;
import uk.ac.cam.gp.charlie.metamorphic.abstract_tests.RandomGraph;
import uk.ac.cam.gp.charlie.metamorphic.properties.DisjointAnswersProperty;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static graql.lang.Graql.*;

/*
This isn't much of a test, more like an attempt of a test.
Note: I found a bug in Graql when debugging the test.

The idea here is to express "these vertices form a connected component" using Graql rules.
If everything works, then the sets returned when queried for connected components
would be disjoint.

Note however that since I wanted to make the component size settable,
the code is somewhat difficult to read due to all the loops.
Moreover, since it cannot be meaningfully run yet due to a bug found,
I decided that explaining in detail what it does is not worth it.
 */

@Deprecated
public class DisjointComponentsTest extends RandomGraph {

    int componentSize;

    public DisjointComponentsTest(int vertices, int edges, int componentSize) {
        super(vertices, edges);
        this.componentSize = componentSize;
    }

    @Override
    public List<GraqlQuery> generate(int seed) {
        List<GraqlQuery> result = super.generate(seed);


        result.add(Graql.define(type("biedge").sub("relation").relates("end")));
        result.add(Graql.define(type("biedge-rule").sub("rule").when(
                var().rel("source", "x").rel("destination", "y").isa("edge")
        ).then(var().isa("biedge").rel("end", "x").rel("end", "y"))));



        result.add(Graql.define(type("hasedge").sub("relation").relates("v")));
        result.add(Graql.define(type("hasedge-rule").sub("rule").when(
                var().rel("end", "x").rel("end", "y").isa("biedge")
        ).then(var().isa("hasedge").rel("v", "x"))));


        /*
        The following part of code defines the member relation.

         */

        StatementType memberRelRelation = type("memberRel").sub("relation");
        for(int i = 0; i< componentSize; ++i) {
            memberRelRelation = memberRelRelation.relates("elem"+i );
        }
        memberRelRelation = memberRelRelation.relates("member");


        result.add(Graql.define(memberRelRelation));

        List<Pattern> memberRelConditions = new ArrayList<>();
        for(int i = 0; i< componentSize; ++i) {
            memberRelConditions.add(var().rel("v", "var"+i).isa("hasedge"));
        }

        StatementInstance memberRelResult = var().isa("memberRel");
        for(int i = 0; i< componentSize; ++i) {
            memberRelResult = memberRelResult.rel("elem"+i,"var"+i);
        }
        memberRelResult = memberRelResult.rel("member", "mem");

        for(int i=0; i<componentSize; ++i) {
            result.add(Graql.define(
                    type("memberRel-rule"+i).sub("rule").when(and(
                            and(memberRelConditions),
                            var().rel("v", "mem").isa("hasedge"),
                            var("var"+i).eq(var("mem"))
                    ))
                            .then(memberRelResult)));
        }


        /*
        A query I added for debugging and which turned out to give unexpected results.

        result.add(Graql.match(
                var().rel("v", "x").isa("hasedge"),
                var().rel("v", "y").isa("hasedge"),
                var().rel("v", "z").isa("hasedge"),
                var().rel("v", "t").isa("hasedge"),
                var().rel("elem0", "x").rel("elem1", "y").rel("elem2", "z").rel("member", "t").isa("memberRel")
        ).get("x", "y", "z", "t"));
        */

        /*
        The following code defines the connected component relation.
         */

        StatementType componentRelation = type("component").sub("relation");
        for(int i = 0; i< componentSize; ++i) {
            componentRelation = componentRelation.relates("elem"+i );
        }

        result.add(Graql.define(componentRelation));

        List<Pattern> componentConditions = new ArrayList<>();
        for(int i = 0; i< componentSize; ++i) {
            if(i+1< componentSize) {
                componentConditions.add(var().rel("end", "var"+i).rel("end", "var"+(i+1)).isa("biedge"));
            }
        }

        List<Pattern> componentConditionsNot = new ArrayList<>();
        for(int i = 0; i< componentSize; ++i) {
            componentConditionsNot.add(var("var"+i).neq("z"));
        }
        Statement componentConditionsNotMember = var();
        for(int i = 0; i< componentSize; ++i) {
            componentConditionsNotMember = componentConditionsNotMember.rel("elem"+i, "var"+i);
        }
        componentConditionsNotMember = componentConditionsNotMember.rel("member", "y").isa("memberRel");
        

        StatementInstance componentResult = var().isa("component");
        for(int i = 0; i< componentSize; ++i) {
            componentResult = componentResult.rel("elem"+i,"var"+i);
        }


        result.add(Graql.define(
                type("component-rule").sub("rule").when(and(and(componentConditions),not(and(
                        var().isa("biedge").rel("end", "y").rel("end", "z"),
                        componentConditionsNotMember,
                        and(componentConditionsNot))
                )))
                        .then(componentResult)));

        StatementInstance resulting_rule = var().isa("component");
        for(int i = 0; i< componentSize; ++i) {
            resulting_rule = resulting_rule.rel("elem"+i,Integer.toString(i));
        }

        String[] answers = new String[componentSize];
        for(int i=0; i<answers.length; ++i) {
            answers[i] = Integer.toString(i);
        }


        result.add(Graql.match(resulting_rule).get(answers[0], Arrays.copyOfRange(answers, 1, answers.length)));

        return result;
    }


    @Override
    public Property getTestingProperty() {
        return new DisjointAnswersProperty();
    }
}
