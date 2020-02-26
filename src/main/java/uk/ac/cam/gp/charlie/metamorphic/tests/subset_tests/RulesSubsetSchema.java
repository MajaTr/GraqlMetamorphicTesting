package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;

import java.util.ArrayList;
import java.util.List;

public class RulesSubsetSchema implements SchemaGenerator {

  @Override
  public List<GraqlQuery> generate(int seed) {
    List<GraqlQuery> result = new ArrayList<>();
    result.add(Graql.parse("define parentship sub relation, relates parent, relates child;"));
    result.add(Graql.parse("define siblings sub relation, relates child;"));
    result.add(Graql.parse("define grandparentship sub relation, relates grandparent, relates child;"));
    result.add(Graql.parse("define cousins sub relation, relates cousin;"));
    result.add(Graql.parse("define name sub attribute, datatype string;"));
    result.add(Graql.parse("define person sub entity, plays child, plays parent, plays grandparent, plays cousin, has name;"));
    result.add(Graql.parse("define people-with-same-parent-are-siblings sub rule, when {"
        + "(parent: $m, child: $x) isa parentship;"
        + "(parent: $m, child: $y) isa parentship;"
        + "$x != $y;"
        + "}, then {"
        + "($x, $y) isa siblings;"
        + "};"));
    result.add(Graql.parse("define parent-of-parent-is-grandparent sub rule, when {"
        + "(parent: $x, child: $y) isa parentship;"
        + "(parent: $y, child: $z) isa parentship;"
        + "}, then {"
        + "(grandparent: $x, child: $z) isa grandparentship;"
        + "};"));
    result.add(Graql.parse("define people-with-same-grandparent-are-cousins sub rule, when {"
        + "(grandparent: $g, child: $x) isa grandparentship;"
        + "(grandparent: $g, child: $y) isa grandparentship;"
        + "not {($x, $y) isa siblings;};"
        + "$x != $y;"
        + "}, then {"
        + "($x, $y) isa cousins;"
        + "};"));

    return result;
  }
}
