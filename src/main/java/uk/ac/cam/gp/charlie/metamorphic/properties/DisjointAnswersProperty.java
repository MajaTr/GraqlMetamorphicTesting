package uk.ac.cam.gp.charlie.metamorphic.properties;

import grakn.client.answer.ConceptMap;
import grakn.client.concept.ConceptId;
import graql.lang.statement.Variable;
import uk.ac.cam.gp.charlie.metamorphic.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DisjointAnswersProperty implements Property{
    @Override
    public boolean test(List<List<ConceptMap>> answers) {
        List<Set<Map<Variable, ConceptId>>> answerSets = answers.stream().map(Utils::mapConceptsToIdsFromListToSet).collect(Collectors.toList());

        Set<Map<Variable, ConceptId>> answerSet = answerSets.get(0);

        Set<ConceptId> allConceptIds = new HashSet<>();
        int sum = 0;
        for(Map<Variable, ConceptId> map: answerSet) {
            sum += map.size();
            allConceptIds.addAll(map.values());
        }

        return sum == allConceptIds.size();

    }
}
