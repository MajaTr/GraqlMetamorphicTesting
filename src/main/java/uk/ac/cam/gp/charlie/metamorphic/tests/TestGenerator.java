package uk.ac.cam.gp.charlie.metamorphic.tests;

import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;

import java.util.List;

public interface TestGenerator {
    List<GraqlQuery> generate(int seed);

    Property getTestingProperty();
    SchemaGenerator getSchemaGenerator();
}
