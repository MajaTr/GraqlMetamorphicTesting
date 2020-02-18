package uk.ac.cam.gp.charlie.metamorphic.tests;

import graql.lang.query.GraqlQuery;

import java.util.List;

import uk.ac.cam.gp.charlie.metamorphic.properties.Property;

public interface TestGenerator {
    List<GraqlQuery> generate(int seed);

    Property getTestingProperty();
    SchemaGenerator getSchemaGenerator();
}
