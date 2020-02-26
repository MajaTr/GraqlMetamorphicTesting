package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import static graql.lang.Graql.var;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.properties.SubSetProperty;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

public class TransitivityTest implements TestGenerator {

  private int nodes, inserts;
  private double getQueryProbability = 0.5;

  public TransitivityTest(int nodes, int inserts) {
    this.nodes = nodes;
    this.inserts = inserts;
  }

  @Override
  public List<GraqlQuery> generate(int seed) {
    Random random = new Random(seed);
    List<GraqlQuery> result = new ArrayList<>();
    for (int i = 0; i < nodes; i++) {
      result.add(Graql.insert(var("x").isa("vertex").has("label", "v" + i)));
    }
    for (int i = 0; i < inserts; i++) {
      int v1 = random.nextInt(nodes);
      int v2 = random.nextInt(nodes);
      while (v1 == v2) {
        v2 = random.nextInt(nodes);
      }
      result.add(Graql.match(
          var("x").isa("vertex").has("label", "v" + v1),
          var("y").isa("vertex").has("label", "v" + v2)
      ).insert(
          var("edge").isa("connection").rel("start", "x").rel("end", "y")
      ));
      if (random.nextDouble() < getQueryProbability) {
        result.add(Graql.match(var("x").isa("vertex"),
            var("c").isa("connection").rel("start", "v0").rel("end", var("x")))
            .get("x"));
      }
    }
    return result;
  }

  @Override
  public Property getTestingProperty() {
    return new SubSetProperty();
  }

  @Override
  public SchemaGenerator getSchemaGenerator() {
    return new TransitivitySchema();
  }
}
