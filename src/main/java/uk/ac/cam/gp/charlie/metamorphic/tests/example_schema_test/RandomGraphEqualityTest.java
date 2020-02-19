package uk.ac.cam.gp.charlie.metamorphic.tests.example_schema_test;


import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.abstract_tests.RandomGraph;
import uk.ac.cam.gp.charlie.metamorphic.general_schemas.PlainGraphSchema;
import uk.ac.cam.gp.charlie.metamorphic.properties.EqProperty;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static graql.lang.Graql.var;

public class RandomGraphEqualityTest extends RandomGraph implements TestGenerator {
    @Override
    public List<GraqlQuery> generate(int seed) {
        List<GraqlQuery> result = super.generate(seed);

        result.add(
                Graql.match(
                        var("s").isa("vertex"),
                        var("t").isa("vertex"),
                        var("e").isa("edge").rel("source", "s").rel("destination", "t")
                ).get("s", "t"));

        result.add(
                Graql.match(
                        var("s").isa("vertex"),
                        var("t").isa("vertex"),
                        var("e").isa("edge").rel("source", "s").rel("destination", "t")
                ).get("s", "t"));

        return result;
    }

    @Override
    public Property getTestingProperty() {
        return new EqProperty();
    }

    @Override
    public SchemaGenerator getSchemaGenerator() { return new PlainGraphSchema(); }
}