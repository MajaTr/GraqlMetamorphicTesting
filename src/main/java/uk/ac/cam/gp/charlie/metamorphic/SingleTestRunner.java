package uk.ac.cam.gp.charlie.metamorphic;

import grakn.client.GraknClient;
import grakn.client.answer.ConceptMap;
import graql.lang.query.GraqlDelete;
import graql.lang.query.GraqlGet;
import graql.lang.query.GraqlInsert;
import graql.lang.query.GraqlQuery;

import uk.ac.cam.gp.charlie.metamorphic.Utils.DebugPrinter;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.rule_eq_test.RandomRuleEqTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static graql.lang.Graql.var;

public class SingleTestRunner {

    private final String testingKeyspace;
    private static int keyspace_id = 0;
    private static final GraknClient client = new GraknClient("localhost:48555");
    private static final String keyspace_prefix = "testing"+new Date().getTime()+"_";
    private GraknClient.Session session;

    private TestGenerator test;

    public SingleTestRunner(TestGenerator test) {
        testingKeyspace = keyspace_prefix+keyspace_id++;
        this.test = test;
    }

    public void openTestingSession() {
        session = client.session(testingKeyspace);
    }

    public void closeTestingSession() {
        session.close();

    }

    public void defineSchema(SchemaGenerator gen, int seed) {
        GraknClient.Transaction transaction = session.transaction().write();
        for(GraqlQuery query: gen.generate(seed)) {
            transaction.execute(query);
        }
        transaction.commit();
    }

    public List<List<ConceptMap>> getTestResults(int seed) {
        ArrayList<List<ConceptMap>> results = new ArrayList<>();
        GraknClient.Transaction transaction = session.transaction().write();
        for(GraqlQuery query: test.generate(seed)) {
            if((query instanceof GraqlInsert) || (query instanceof GraqlDelete)) {
                transaction.execute(query);
            }
            else if(query instanceof GraqlGet) {
                transaction.commit();
                transaction = session.transaction().read();
                results.add(transaction.execute((GraqlGet)query));
                transaction.close();
                transaction = session.transaction().write();
            }
        }
        transaction.commit();
        return results;
    }


    public boolean runOnSeed(int seed) {
        openTestingSession();
        defineSchema(test.getSchemaGenerator(), seed);
        List<List<ConceptMap>> results = getTestResults(seed);
        boolean verdict = test.getTestingProperty().test(results);
        closeTestingSession();
        return verdict;
    }


    public static void deleteKeyspaces() {
        for(int i=0; i<keyspace_id; ++i) {
            if(client.keyspaces().retrieve().contains(keyspace_prefix+i)) {
                client.keyspaces().delete(keyspace_prefix+i);
            }
        }
    }
}
