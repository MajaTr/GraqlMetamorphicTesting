package uk.ac.cam.gp.charlie.metamorphic.tests;

import graql.lang.query.GraqlQuery;

import java.util.List;

public interface SchemaGenerator {
    List<GraqlQuery> generate(int seed);
}
