package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import graql.lang.statement.StatementInstance;
import uk.ac.cam.gp.charlie.metamorphic.Utils;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.properties.SubSetProperty;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static graql.lang.Graql.var;

public class ExtendableQueriesTest implements TestGenerator {

    private String baseIsa;
    private List<String> baseAttributesValues;
    private int nAttributes = 20;
    private int nBSubEntities = 20;
    private int nInserts = 50;

    //Generate a query asking for all the elements of a given entity, having
    private GraqlQuery generateQuery(String isaIn, List<String> attributesValues){
        StatementInstance si = var("v").isa(isaIn);
        for(int i = 0; i < attributesValues.size(); i++){
            si = si.has("attr" + i, attributesValues.get(i));
        }

        return Graql.match(si).get("v");
    }

    private GraqlQuery extendSubentityB(int seed){
            Random random = new Random(seed);
            int i = random.nextInt(nBSubEntities);
            return generateQuery("B" + i, baseAttributesValues);
    }

    private GraqlQuery extendSubentityC(int seed){
            Random random = new Random(seed);
            int i = random.nextInt(nBSubEntities);
            Utils.DebugPrinter.print("C" + i);
            return generateQuery("C" + i, baseAttributesValues);
    }

    private List<GraqlQuery> generateInserts(int seed) {
        List<GraqlQuery> result = new ArrayList<>();
        Random random = new Random(seed);
        int bindex, cindex ;
        for(int i = 0 ; i < nInserts; i++){
            StatementInstance si;
            if(random.nextBoolean()){
                bindex = random.nextInt(nBSubEntities);
                si = var("v" + i).isa("B" + bindex);
            }
            else{
                cindex = random.nextInt(nBSubEntities);
                si = var("v" + i).isa("C" + cindex);
            }
            result.add(
                    Graql.insert(
                            si
                    )
            );
        }

        return result;
    }

    @Override
    public List<GraqlQuery> generate(int seed) {
        List<GraqlQuery> result = generateInserts(seed);

        Random random = new Random(seed);
        //The initial query will have A as the entity and nAttributes attributes
        // called i, for i = 0 to nAttributes
        baseIsa = "A";
        baseAttributesValues = new ArrayList<>();
        for(int i = 0; i < nAttributes; i++){
            baseAttributesValues.add("" + i);
        }

        GraqlQuery baseQuery = generateQuery(baseIsa, baseAttributesValues);
        GraqlQuery extendedQuery;
        result.add(baseQuery);

        //We can extend the base query by replacing the base entity A with either B or C
        //(and we choose randomly).
        //The result will be a subset of the result of the base query.
        boolean borc = random.nextBoolean();
        if(borc == true){
            extendedQuery = extendSubentityB(seed);
        }
        else{
            extendedQuery = extendSubentityC(seed);
        }
        result.add(extendedQuery);

        return result;

    }

    @Override
    public Property getTestingProperty() {
        return new SubSetProperty();
    }

    @Override
    public SchemaGenerator getSchemaGenerator() {
        return new ExtendableQueriesSchema();
    }
}
