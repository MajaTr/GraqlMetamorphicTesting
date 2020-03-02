package uk.ac.cam.gp.charlie.metamorphic.abstract_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.Utils;
import uk.ac.cam.gp.charlie.metamorphic.general_schemas.PlainGraphSchema;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static graql.lang.Graql.var;

/*
An abstract class implementing the generation of a random directed graph
*/
public abstract class RandomGraph implements TestGenerator {
    int vertices, edges;

    public RandomGraph(int vertices, int edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    @Override
    public List<GraqlQuery> generate(int seed) {
        Random random = new Random(seed);
        ArrayList<GraqlQuery> result = new ArrayList<>();

        for(int i = 0; i< vertices; ++i) {
            result.add(
                    Graql.insert(var("v"+i).isa("vertex").has("label", Integer.toString(i)))
            );
        }


        /*
        Each time choose an edge randomly and add it
         */
        for(int i = 0; i< edges; ++i) {
            int s = random.nextInt(vertices);
            int t = random.nextInt(vertices);
            Utils.DebugPrinter.print(s+" "+t);
            result.add(Graql.match(
                    var("s").isa("vertex").has("label", Integer.toString(s)),
                    var("t").isa("vertex").has("label", Integer.toString(t))
            ).insert(
                    var("v"+s+"-"+t).isa("edge").rel("source", "s").rel("destination", "t")
            ));
        }

        return result;
    }

    @Override
    public SchemaGenerator getSchemaGenerator() {
        return new PlainGraphSchema();
    }
}
