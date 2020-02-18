package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.properties.SubSetProperty;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static graql.lang.Graql.var;

public class SubEntityAttributesTest implements TestGenerator {
    @Override
    public List<GraqlQuery> generate(int seed) {
        List<GraqlQuery> result = new ArrayList<>();
        Random random = new Random(seed);
        int nAnimals = 20;

        for(int i = 0; i < nAnimals; i++){
            String animalType = "dog";
            if(random.nextBoolean()){
                animalType = "cat";
            }
            result.add(
                    Graql.insert(
                            var("a").isa(animalType).has("name", Integer.toString(i))
                    )
            );
        }
        result.add(
                Graql.match(
                        var("d").isa("dog").has("name", var("n"))
                ).get("n")
        );

        result.add(
                Graql.match(
                        var("a").isa("animal").has("name", var("n"))
                ).get("n")
        );

        return result;

    }

    @Override
    public Property getTestingProperty() {
        return new SubSetProperty();
    }

    @Override
    public SchemaGenerator getSchemaGenerator() { return new SubEntityAttributesSchema(); }
}
