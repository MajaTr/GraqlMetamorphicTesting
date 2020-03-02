package uk.ac.cam.gp.charlie.metamorphic.tests;

import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;

import java.util.List;

/*
A test interface.

Each test consists of a schema, the data generator, and the property the results have to satisfy.

generate(seed) is supposed to deterministically return a list of Graql queries to instantiate the data.
However some definitions can be included in the TestGenerator instead of SchemaGenerator if it's more convenient.
*/
public interface TestGenerator {
    List<GraqlQuery> generate(int seed);

    Property getTestingProperty();
    SchemaGenerator getSchemaGenerator();
}
