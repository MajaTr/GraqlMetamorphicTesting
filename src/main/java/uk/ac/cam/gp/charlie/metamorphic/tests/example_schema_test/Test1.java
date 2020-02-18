package uk.ac.cam.gp.charlie.metamorphic.tests.example_schema_test;


import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.properties.EqProperty;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests.RandomAttributesSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static graql.lang.Graql.var;

public class Test1 implements TestGenerator {
    @Override
    public List<GraqlQuery> generate(int seed) {
        Random random = new Random(seed);
        ArrayList<GraqlQuery> result = new ArrayList<>();

        int n = 3, m = 3;
        for(int i=0; i<n; ++i) {
            result.add(
                    Graql.insert(var("v"+i).isa("vertex").has("label", Integer.toString(i)))
            );
        }

        for(int i=0; i<m; ++i) {
            int s = random.nextInt(n);
            int t = random.nextInt(n);
            result.add(Graql.match(
                    var("s").isa("vertex").has("label", Integer.toString(s)),
                    var("t").isa("vertex").has("label", Integer.toString(t))
            ).insert(
                    var("v"+s+"-"+t).isa("edge").rel("source", "s").rel("destination", "t")
            ));
        }

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
