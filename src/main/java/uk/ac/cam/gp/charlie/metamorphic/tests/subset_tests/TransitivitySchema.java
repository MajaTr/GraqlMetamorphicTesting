package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import java.util.ArrayList;
import java.util.List;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;

public class TransitivitySchema implements SchemaGenerator {

  @Override
  public List<GraqlQuery> generate(int seed) {

    //This is a simple schema with 1 entity, 1 relation and a transitivity rule.

    List<GraqlQuery> result = new ArrayList<>();
    result.add(Graql.parse("define label sub attribute, datatype string;"));
    result.add(Graql.parse("define connection sub relation, relates start, relates end;"));
    result.add(Graql.parse("define vertex sub entity, plays start, plays end, has label;"));
    result.add(Graql.parse("define transitivity sub rule, when {"
        + "(start: $x, end: $y) isa connection;"
        + "(start: $y, end: $z) isa connection;"
        + "}, then {"
        + "(start: $x, end: $z) isa connection;"
        + "};"));
    return result;
  }
}
