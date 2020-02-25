package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;

import java.util.Arrays;
import java.util.List;

import static graql.lang.Graql.type;

public class SubEntityAttributesSchema implements SchemaGenerator {
    @Override
    public List<GraqlQuery> generate(int seed) {
        return Arrays.asList(
                Graql.define(type("name").sub("attribute").datatype("string")),
                Graql.define(type("animal").sub("entity").has("name")),
                Graql.define(type("dog").sub("animal")),
                Graql.define(type("cat").sub("animal"))

        );
    }
}
