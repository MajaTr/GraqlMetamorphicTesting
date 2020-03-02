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

    //creating a tree

    //inserting numNodes many nodes / people

    for (int i = 0; i < numNodes; i++) {
      result.add(Graql.parse("insert $x isa person, has name \"i" + i + "\";"));
    }

    //creating the edges

    for (int i = 1; i < numNodes; i++) {

      /*
      i0 is the root, and every node's parent is randomly chosen
      from the nodes which have a smaller ID.
       */

      int parent = random.nextInt(i);
      result.add(Graql.match(
          var("x").isa("person").has("name", "i" + parent),
          var("y").isa("person").has("name", "i" + i)
      ).insert(
          var("edge").isa("parentship").rel("parent", "x").rel("child", "y")
      ));
    }

    /*
    This test relies on the following fact:
    An uncle can be deduced 2 ways:
    - parent of a cousin
    - sibling of a parent
    However, the second one should give us all the uncles, while the first one
    will just give a subset of them, namely the uncles who also have children.
    The test could also be extended to verify other relations between nodes in a tree
    with the help of rules.
     */

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
