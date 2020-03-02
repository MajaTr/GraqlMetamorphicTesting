package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import graql.lang.statement.StatementInstance;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.properties.SubSetProperty;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static graql.lang.Graql.var;

public class AttributesSubsetTest implements TestGenerator {

  private RandomAttributesSchema randomAttributesSchema;
  private int numInserts = 50;

  public AttributesSubsetTest(RandomAttributesSchema ras) {
    randomAttributesSchema = ras;
  }

  @Override
  public List<GraqlQuery> generate(int seed) {
    Random random = new Random(seed);
    List<GraqlQuery> result = new ArrayList<>();

    //adding the vertices

    for (int i = 0; i < numInserts; i++) {
      StatementInstance si = var("v" + i).isa("vertex");
      for (int j = 0; j < randomAttributesSchema.getNumAttributes(); j++) {

        //each vertex will have each attribute with 50% chance

        if (random.nextBoolean()) {
          si = si.has("label" + j, "label" + j);
        }
      }
      result.add(Graql.insert(si));
    }

    //randomly choosing 2 different attributes

    int attribute1 = random.nextInt(randomAttributesSchema.getNumAttributes());
    int attribute2 = random.nextInt(randomAttributesSchema.getNumAttributes());
    while (attribute1 == attribute2) {
      attribute2 = random.nextInt(randomAttributesSchema.getNumAttributes());
    }

    //get all vertices which have both of the chosen attributes

    result.add(Graql.match(
        var("v").isa("vertex").has("label" + attribute1, var("a1")).
            has("label" + attribute2, var("a2"))
    ).get("v"));

    //get all vertices which have the first chosen attribute

    result.add(Graql.match(
        var("v").isa("vertex").has("label" + attribute1, var("a1"))
    ).get("v"));

    //clearly, the first answer set should be a subset of the second one

    return result;
  }

  @Override
  public Property getTestingProperty() {
    return new SubSetProperty();
  }

  @Override
  public SchemaGenerator getSchemaGenerator() { return new RandomAttributesSchema(); }
}
