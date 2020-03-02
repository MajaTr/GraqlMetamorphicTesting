package uk.ac.cam.gp.charlie.metamorphic.properties;

import grakn.client.answer.ConceptMap;

import java.util.List;

/*
A property interface.

test(answers) is supposed to take a list of results from "get" queries
(a result from a single query is a List<ConceptMap>, therefore the argument is a List<List<ConceptMap>>)
and return a boolean whether they hold the desired property.
 */

public interface Property {
    boolean test(List<List<ConceptMap>> answers);
}
