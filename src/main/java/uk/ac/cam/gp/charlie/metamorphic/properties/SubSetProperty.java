package uk.ac.cam.gp.charlie.metamorphic.properties;

import static graql.lang.Graql.var;

import grakn.client.answer.ConceptMap;
import grakn.client.concept.ConceptId;
import graql.lang.statement.Variable;
import sun.security.ssl.Debug;
import uk.ac.cam.gp.charlie.metamorphic.Utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import uk.ac.cam.gp.charlie.metamorphic.Utils.DebugPrinter;

public class SubSetProperty implements Property {
    @Override
    public boolean test(List<List<ConceptMap>> answers) {
        List<Set<Map<Variable, ConceptId>>> answerSets = answers.stream().map(Utils::mapConceptsToIdsFromListToSet).collect(Collectors.toList());
        return answerSets.get(1).containsAll(answerSets.get(0));
    }
}
