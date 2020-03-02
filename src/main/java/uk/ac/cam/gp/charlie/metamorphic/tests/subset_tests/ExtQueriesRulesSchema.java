/*
  Extension: adding rules to the schema used for generating extendable queries.
 */

package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import graql.lang.statement.StatementType;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static graql.lang.Graql.and;
import static graql.lang.Graql.type;

public class ExtQueriesRulesSchema implements SchemaGenerator {

    private int nAttributes = 20;
    private int nBSubEntities = 20;
    private int nRules = 20;

    @Override
    public List<GraqlQuery> generate(int seed) {

        List<GraqlQuery> result = new ArrayList<>();

        result.add(Graql.define(type("A").sub("entity")));

        for(int i = 0; i < nAttributes; i++){
            result.add(Graql.define(type("attr" + i).sub("attribute").datatype("string")));
        }

        StatementType definitionA = type("A").sub("entity");

        for(int i = 0; i < nAttributes; i++){
            definitionA = definitionA.has("attr" + i);
        }

        result.add(Graql.define(definitionA));

        for(int i = 0; i < nBSubEntities; i++){
            result.add(Graql.define(type("B" + i).sub("A")));
            result.add( Graql.define(type("C" + i).sub("B" + i)));
        }

        //Adding rules between randomly chosen B_bindex and C_cindex
        Random random = new Random();
        int bindex, cindex;
        for(int i = 0; i < nRules; i++){
            bindex = random.nextInt(nBSubEntities);
            cindex = random.nextInt(nBSubEntities);
            result.add(
                    Graql.define(
                            type("relation" + i).sub("rel")
                    )
            );
            result.add(
                    Graql.define(
                            type("rule" + i).sub("rule")
                                    .when(
                                            and(
                                                Graql.var("x").isa("B"+bindex),
                                                Graql.var("y").isa("C"+cindex)
                                            )
                                    ).then(
                                    Graql.var().isa("relation" + i).rel("x").rel("y")
                            )


                            )

            );
        }

        return result;
    }
}
