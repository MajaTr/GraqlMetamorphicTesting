package uk.ac.cam.gp.charlie.metamorphic.tests.example_schema_test;

import static graql.lang.Graql.var;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import graql.lang.statement.StatementInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.properties.SubSetProperty;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

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
    for (int i = 0; i < numInserts; i++) {
      StatementInstance si = var("v" + i).isa("vertex");
      for (int j = 0; j < randomAttributesSchema.getNumAttributes(); j++) {
        if (random.nextBoolean()) {
          si = si.has("label" + j, "label" + j);
        }
      }
      result.add(Graql.insert(si));
    }
    int attribute1 = random.nextInt(randomAttributesSchema.getNumAttributes());
    int attribute2 = random.nextInt(randomAttributesSchema.getNumAttributes());
    while (attribute1 == attribute2) {
      attribute2 = random.nextInt(randomAttributesSchema.getNumAttributes());
    }

    result.add(Graql.match(
        var("v").isa("vertex").has("label" + attribute1, var("a1")).
            has("label" + attribute2, var("a2"))
    ).get("v"));
    result.add(Graql.match(
        var("v").isa("vertex").has("label" + attribute1, var("a1"))
    ).get("v"));

    return result;
  }

  @Override
  public Property getTestingProperty() {
    return new SubSetProperty();
  }
}
