package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import java.util.ArrayList;
import java.util.List;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;

public class EquivalentRulesSchema implements SchemaGenerator {

  @Override
  public List<GraqlQuery> generate(int seed) {
    List<GraqlQuery> result = new ArrayList<>();
    result.add(Graql.parse("define parentship sub relation, relates parent, relates child;"));
    result.add(Graql.parse("define siblings sub relation, relates child1, relates child2;"));
    result.add(Graql.parse("define grandparentship sub relation, relates grandparent, relates child;"));
    result.add(Graql.parse("define name sub attribute, datatype string;"));
    result.add(Graql.parse("define person sub entity, plays child, plays parent, plays grandparent, has name;"));
    result.add(Graql.parse("define people-with-same-parent-are-siblings sub rule, when {"
        + "(parent: $m, $x) isa parentship;"
        + "(parent: $m, $y) isa parentship;"
        + "$x != $y;"
        + "}, then {"
        + "($x, $y) isa siblings;"
        + "};"));
    result.add(Graql.parse("define parent-of-parent-is-grandparent sub rule, when {"
        + "(parent: $x, $y) isa parentship;"
        + "(parent: $y, $z) isa parentship;"
        + "}, then {"
        + "($x, $z) isa grandparentship;"
        + "};"));

    return result;
  }
}
