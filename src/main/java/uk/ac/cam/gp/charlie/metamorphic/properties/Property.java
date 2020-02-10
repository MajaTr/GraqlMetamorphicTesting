package uk.ac.cam.gp.charlie.metamorphic.properties;

import grakn.client.answer.ConceptMap;

import java.util.List;

public interface Property {
    boolean test(List<List<ConceptMap>> answers);
}
