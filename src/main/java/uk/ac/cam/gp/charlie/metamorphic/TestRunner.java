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
import uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests.SubEntityAttributesSchema;
import uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests.SubEntityAttributesTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static final String testingKeyspace = "testing";
    public static final GraknClient client = new GraknClient("localhost:48555");
    private GraknClient.Session session;

    public void openTestingSession() {
        if(client.keyspaces().retrieve().contains(testingKeyspace)) {
            client.keyspaces().delete(testingKeyspace);
        }
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

    public List<List<ConceptMap>> getTestResults(TestGenerator gen, int seed) {
        ArrayList<List<ConceptMap>> results = new ArrayList<>();
        GraknClient.Transaction transaction = session.transaction().write();
        for(GraqlQuery query: gen.generate(seed)) {
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





    public static void main(String[] args) throws IOException {
        DebugPrinter.start();
        DebugPrinter.print("Program started");
        TestRunner testRunner = new TestRunner();


        /*
        In the future add some more advanced test management
         */

        for(int i=0; i<10; ++i) {

            testRunner.openTestingSession();
            //TestGenerator test = new RandomGraphEqualityTest();
            TestGenerator test = new RandomRuleEqTest();
            //testRunner.defineSchema(schema, i);
            testRunner.defineSchema(test.getSchemaGenerator(), i);

            List<List<ConceptMap>> results = testRunner.getTestResults(test, i);

            if(test.getTestingProperty().test(results)) {
                //System.out.println("Test "+i+" passed");
                DebugPrinter.print("Test "+i+" passed");
            }
            else {
                //System.out.println("Test "+i+" failed");
                DebugPrinter.print("Test "+i+" failed");
            }
            testRunner.closeTestingSession();

        }

    }

}
