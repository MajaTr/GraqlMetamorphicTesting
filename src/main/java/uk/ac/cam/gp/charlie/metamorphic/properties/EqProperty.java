package uk.ac.cam.gp.charlie.metamorphic.properties;

import grakn.client.answer.ConceptMap;
import grakn.client.concept.Concept;
import grakn.client.concept.ConceptId;
import graql.lang.statement.Variable;
import uk.ac.cam.gp.charlie.metamorphic.Utils;

import java.util.*;
import java.util.stream.Collectors;
import uk.ac.cam.gp.charlie.metamorphic.Utils.DebugPrinter;

public class EqProperty implements Property {
    @Override
    public boolean test(List<List<ConceptMap>> answers) {

        List<Set<Map<Variable, ConceptId>>> answerSets = answers.stream().map(Utils::mapConceptsToIdsFromListToSet).collect(Collectors.toList());

        for(int i=1; i<answerSets.size(); ++i) {
            if(!answerSets.get(0).equals(answerSets.get(i))) {
                return false;
            }
        }

        return true;
    }
}
