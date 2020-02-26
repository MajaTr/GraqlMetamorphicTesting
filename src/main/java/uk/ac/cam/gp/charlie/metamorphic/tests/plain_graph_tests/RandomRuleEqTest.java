package uk.ac.cam.gp.charlie.metamorphic.tests.plain_graph_tests;

import graql.lang.Graql;
import graql.lang.pattern.Pattern;
import graql.lang.query.GraqlQuery;
import graql.lang.statement.StatementInstance;
import graql.lang.statement.StatementType;
import uk.ac.cam.gp.charlie.metamorphic.abstract_tests.RandomGraph;
import uk.ac.cam.gp.charlie.metamorphic.general_schemas.PlainGraphSchema;
import uk.ac.cam.gp.charlie.metamorphic.properties.EqProperty;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static graql.lang.Graql.*;

public class RandomRuleEqTest extends RandomGraph implements TestGenerator {

    public RandomRuleEqTest() {
        super(5, 10);
    }

    public RandomRuleEqTest(int n, int m) {
        super(n, m);
    }


    @Override
    public List<GraqlQuery> generate(int seed) {
        List<GraqlQuery> result = super.generate(seed);

        Random random = new Random(seed);
        int rule_size = 2+random.nextInt(2);
        int[][] rule_description = new int[rule_size][rule_size];
        for(int i=0; i<rule_size; ++i) for(int j=0; j<rule_size; ++j) {
            rule_description[i][j] = random.nextInt(2);
        }
        for(int i=0; i<rule_size; ++i) {
            if(random.nextInt(2)==1) {
                rule_description[i][random.nextInt(rule_size)] = 1;
            }
            else {
                rule_description[random.nextInt(rule_size)][i] = 1;
            }
        }

        List<Pattern> conditions = new ArrayList<>();
        for(int i=0; i<rule_size; ++i) for(int j=0; j<rule_size; ++j) {
            StatementInstance condition = var().rel("source", Integer.toString(i)).rel("destination", Integer.toString(j)).isa("edge");
            if(rule_description[i][j] == 1) {
                conditions.add(condition);
            }
        }


        StatementType something = type("something").sub("relation");
        for(int i=0; i<rule_size; ++i) {
            something = something.relates("role"+i );
        }

        result.add(Graql.define(something));


        StatementInstance resulting_rule = var().isa("something");
        for(int i=0; i<rule_size; ++i) {
            resulting_rule = resulting_rule.rel("role"+i,Integer.toString(i));
        }

        result.add(Graql.define(
                type("random-rule").sub("rule").when(and(conditions))
                        .then(resulting_rule)));

        String[] answers = new String[rule_size];
        for(int i=0; i<answers.length; ++i) {
            answers[i] = Integer.toString(i);
        }

        result.add(Graql.match(resulting_rule).get(answers[0], Arrays.copyOfRange(answers, 1, answers.length)));

        result.add(Graql.match(conditions).get(answers[0], Arrays.copyOfRange(answers, 1, answers.length)));


        return result;
    }

    @Override
    public Property getTestingProperty() {
        return new EqProperty();
    }

    @Override
    public SchemaGenerator getSchemaGenerator() {
        return new PlainGraphSchema();
    }
}