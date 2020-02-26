package uk.ac.cam.gp.charlie.metamorphic.abstract_tests;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.Utils;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static graql.lang.Graql.var;

public abstract class RandomGraph implements TestGenerator {
    int n, m;

    public RandomGraph(int n, int m) {
        this.n = n;
        this.m = m;
    }

    @Override
    public List<GraqlQuery> generate(int seed) {
        Random random = new Random(seed);
        ArrayList<GraqlQuery> result = new ArrayList<>();

        for(int i=0; i<n; ++i) {
            result.add(
                    Graql.insert(var("v"+i).isa("vertex").has("label", Integer.toString(i)))
            );
        }

        for(int i=0; i<m; ++i) {
            int s = random.nextInt(n);
            int t = random.nextInt(n);
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
}
