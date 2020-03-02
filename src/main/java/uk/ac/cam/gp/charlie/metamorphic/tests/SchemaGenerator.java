package uk.ac.cam.gp.charlie.metamorphic.tests;

import graql.lang.query.GraqlQuery;

import java.util.List;

/*
A schema interface.

generate(seed) is supposed to deterministically return a list of Graql queries to be run
in order to define a schema.

 */

public interface SchemaGenerator {
    List<GraqlQuery> generate(int seed);
}
