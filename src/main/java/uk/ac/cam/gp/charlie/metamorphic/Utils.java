package uk.ac.cam.gp.charlie.metamorphic;

import grakn.client.answer.ConceptMap;
import grakn.client.concept.Concept;
import grakn.client.concept.ConceptId;
import graql.lang.statement.Variable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;


/*
Just a set of random functions we found useful to have globally.
*/


public class Utils {
    /*
    In order to be able to compare results from different transactions,
    a ConceptMap has to be stripped to a Map<Variable, ConceptId>.
     */

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
    public static class DebugPrinter {
        private static PrintStream ps;
        private static PrintStream console;
        public static void start() {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ps = new PrintStream(b);
            console = System.out;
            System.setOut(ps);
        }
        public static void print(String s) {
            System.setOut(console);
            System.out.println(s);
            System.setOut(ps);
        }
    }
}
