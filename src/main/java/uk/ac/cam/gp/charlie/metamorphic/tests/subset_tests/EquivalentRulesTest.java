package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import java.util.ArrayList;
import java.util.List;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.properties.SubSetProperty;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

public class EquivalentRulesTest implements TestGenerator {

  @Override
  public List<GraqlQuery> generate(int seed) {
    List<GraqlQuery> result = new ArrayList<>();
    result.add(Graql.parse("insert $x isa person, has name \"A\";"));
    result.add(Graql.parse("insert $x isa person, has name \"B\";"));
    result.add(Graql.parse("insert $x isa person, has name \"C\";"));
    result.add(Graql.parse("insert $x isa person, has name \"D\";"));
    result.add(Graql.parse("insert $x isa person, has name \"E\";"));
    return result;
  }

  @Override
  public Property getTestingProperty() {
    return new SubSetProperty();
  }

  @Override
  public SchemaGenerator getSchemaGenerator() {
    return new EquivalentRulesSchema();
  }
}
