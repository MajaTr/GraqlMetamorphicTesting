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

public class RulesSubsetTest implements TestGenerator {

  private int numNodes;

  public RulesSubsetTest() { numNodes = 30; }

  @Override
  public List<GraqlQuery> generate(int seed) {
    Random random = new Random(seed);
    List<GraqlQuery> result = new ArrayList<>();

    for (int i = 0; i < numNodes; i++) {
      result.add(Graql.parse("insert $x isa person, has name \"i" + i + "\";"));
    }

    for (int i = 1; i < numNodes; i++) {
      int parent = random.nextInt(i);
      result.add(Graql.match(
          var("x").isa("person").has("name", "i" + parent),
          var("y").isa("person").has("name", "i" + i)
      ).insert(
          var("edge").isa("parentship").rel("parent", "x").rel("child", "y")
      ));
    }

    result.add(Graql.match(
        var("C").isa("cousins").rel(var("c")).rel(var("c2")),
        var("P").isa("parentship").rel("parent", var("u")).rel("child", var("c2")),
        var("u").has("name", var("unclename")),
        var("c").has("name", var("childname"))
    ).get("unclename", "childname"));

    result.add(Graql.match(
        var("P").isa("parentship").rel("parent", var("p")).rel("child", var("c")),
        var("S").isa("siblings").rel(var("p")).rel(var("u")),
        var("u").has("name", var("unclename")),
        var("c").has("name", var("childname"))
    ).get("unclename", "childname"));

    return result;
  }

  @Override
  public Property getTestingProperty() { return new SubSetProperty(); }

  @Override
  public SchemaGenerator getSchemaGenerator() {
    return new RulesSubsetSchema();
  }
}
