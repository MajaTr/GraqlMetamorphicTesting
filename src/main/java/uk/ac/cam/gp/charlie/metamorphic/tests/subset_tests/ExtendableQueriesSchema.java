/*
    Definition of a layered schema that allows generating extendable queries.
    It has a base entity "A" with a pre-defined nBSubEntities subentities B1, B2,...,
    each B_i having one subentity Ci.
 */

package uk.ac.cam.gp.charlie.metamorphic.tests.subset_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import graql.lang.statement.StatementType;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;

import java.util.ArrayList;
import java.util.List;

import static graql.lang.Graql.type;

public class ExtendableQueriesSchema implements SchemaGenerator {

    private int nAttributes = 20;
    private int nBSubEntities = 20;

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

        return result;
    }
}
