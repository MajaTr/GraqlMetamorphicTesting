package uk.ac.cam.gp.charlie.metamorphic.tests.example_schema_test;

import static graql.lang.Graql.type;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import graql.lang.statement.StatementType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;

public class RandomAttributesSchema implements SchemaGenerator {

  private int numAttributes;

  public int getNumAttributes() {
    return numAttributes;
  }

  public RandomAttributesSchema () {
    numAttributes = 10;
  }

  @Override
  public List<GraqlQuery> generate(int seed) {
    List<GraqlQuery> result = new ArrayList<>();
    for (int i = 0; i < numAttributes; i++) {
      result.add(Graql.define(type("label" + i).sub("attribute").datatype("string")));
    }
    StatementType st = type("vertex").sub("entity");
    for (int i = 0; i < numAttributes; i++) {
      st = st.has("label" + i);
    }
    result.add(Graql.define(st));
    return result;
  }
}
