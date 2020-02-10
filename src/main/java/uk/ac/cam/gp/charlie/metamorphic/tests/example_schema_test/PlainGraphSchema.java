package uk.ac.cam.gp.charlie.metamorphic.tests.example_schema_test;

import graql.lang.Graql;
import static graql.lang.Graql.*;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;

import java.util.Arrays;
import java.util.List;

public class PlainGraphSchema implements SchemaGenerator {
    @Override
    public List<GraqlQuery> generate(int seed)
    {
        return Arrays.asList(
                Graql.define(type("label").sub("attribute").datatype("string")),
                Graql.define(type("edge").sub("relation").relates("source").relates("destination")),
                Graql.define(type("vertex").sub("entity").has("label").plays("source").plays("destination"))
        );
    }
}
