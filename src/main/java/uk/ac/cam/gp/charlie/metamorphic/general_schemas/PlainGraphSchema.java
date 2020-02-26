package uk.ac.cam.gp.charlie.metamorphic.general_schemas;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static graql.lang.Graql.type;

public class PlainGraphSchema implements SchemaGenerator {
    @Override
    public List<GraqlQuery> generate(int seed)
    {
        return new ArrayList<>(Arrays.asList(
                Graql.define(type("label").sub("attribute").datatype("string")),
                Graql.define(type("edge").sub("relation").relates("source").relates("destination")),
                Graql.define(type("vertex").sub("entity").has("label").plays("source").plays("destination"))
        ));
    }
}
