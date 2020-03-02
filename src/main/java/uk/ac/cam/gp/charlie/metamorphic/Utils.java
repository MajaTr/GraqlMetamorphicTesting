package uk.ac.cam.gp.charlie.metamorphic;

import grakn.client.answer.ConceptMap;
import grakn.client.concept.Concept;
import grakn.client.concept.ConceptId;
import graql.lang.statement.Variable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

    /*
    This class helps hiding debug messages from the Grakn servers.
    We use DebugPrinter.print instead of System.out.println in
    every part of our code.
     */
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
