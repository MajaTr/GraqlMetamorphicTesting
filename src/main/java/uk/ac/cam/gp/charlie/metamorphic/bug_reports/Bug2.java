package uk.ac.cam.gp.charlie.metamorphic.bug_reports;

import graql.lang.Graql;
import graql.lang.query.GraqlDefine;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.abstract_tests.RandomGraph;
import uk.ac.cam.gp.charlie.metamorphic.general_schemas.PlainGraphSchema;
import uk.ac.cam.gp.charlie.metamorphic.properties.EqProperty;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.List;

import static graql.lang.Graql.*;
import static graql.lang.Graql.and;

public class Bug2 extends RandomGraph implements TestGenerator {
    public Bug2(int n, int m) {
        super(n, m);
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


        result.add(Graql.define(
                type("memberRel").sub("relation").relates("elem0").relates("elem1").relates("elem2").relates("member"))
        );

        result.add(Graql.define(
                type("memberRel-rule").sub("rule").when(and(
                        and(
                                var().rel("v", "var0").isa("hasedge"),
                                var().rel("v", "var1").isa("hasedge"),
                                var().rel("v", "var2").isa("hasedge")
                        ),
                        var().rel("v", "mem").isa("hasedge"),
                        var("var0").eq(var("mem"))
                ))
                        .then(
                                var().isa("memberRel").rel("elem0","var0").rel("elem1","var1")
                                .rel("elem2","var2").rel("member", "mem")
                        )
        ));


        result.add(Graql.match(
                var().rel("v", "x").isa("hasedge"),
                var().rel("v", "y").isa("hasedge"),
                var().rel("v", "z").isa("hasedge"),
                var().rel("v", "t").isa("hasedge"),
                var().rel("elem0", "x").rel("elem1", "y").rel("elem2", "z").rel("member", "t").isa("memberRel")
        ).get("x", "y", "z", "t"));

        return result;
    }


    @Override
    public Property getTestingProperty() {
        return new EqProperty();
    }

    @Override
    public SchemaGenerator getSchemaGenerator() {
        return new PlainGraphSchema();
    }
}
