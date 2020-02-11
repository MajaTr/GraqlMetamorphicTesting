package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.properties.SubSetProperty;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static graql.lang.Graql.type;
import static graql.lang.Graql.var;

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
