package uk.ac.cam.gp.charlie.metamorphic;

import grakn.client.answer.ConceptMap;
import grakn.client.concept.Concept;
import grakn.client.concept.ConceptId;
import graql.lang.statement.Variable;

import java.util.*;

public class Utils {
    public static Map<Variable, ConceptId> mapConceptsToIds(ConceptMap cm) {
        HashMap<Variable, ConceptId> result = new HashMap<>();
        for(Map.Entry<Variable, Concept> entry: cm.map().entrySet()) {
            result.put(entry.getKey(), entry.getValue().id());
        }
        return result;
    }
    public static Set<Map<Variable, ConceptId>> mapConceptsToIdsFromListToSet(List<ConceptMap> l) {
        HashSet<Map<Variable, ConceptId>> result = new HashSet<>();
        for(ConceptMap cm: l) {
            result.add(mapConceptsToIds(cm));
        }
        return result;
    }
}
