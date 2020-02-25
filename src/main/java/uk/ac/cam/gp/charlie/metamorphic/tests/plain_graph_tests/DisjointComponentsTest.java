package uk.ac.cam.gp.charlie.metamorphic.tests.plain_graph_tests;

import graql.lang.Graql;
import graql.lang.pattern.Pattern;
import graql.lang.query.GraqlDefine;
import graql.lang.query.GraqlQuery;
import graql.lang.statement.Statement;
import graql.lang.statement.StatementInstance;
import graql.lang.statement.StatementType;
import uk.ac.cam.gp.charlie.metamorphic.abstract_tests.RandomGraph;
import uk.ac.cam.gp.charlie.metamorphic.general_schemas.PlainGraphSchema;
import uk.ac.cam.gp.charlie.metamorphic.properties.DisjointAnswersProperty;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static graql.lang.Graql.*;

public class DisjointComponentsTest extends RandomGraph {

    int c;

    public DisjointComponentsTest(int n, int m, int c) {
        super(n, m);
        this.c = c;
    }

    @Override
    public List<GraqlQuery> generate(int seed) {
        List<GraqlQuery> result = super.generate(seed);


        result.add(Graql.define(type("biedge").sub("relation").relates("end")));
        result.add(Graql.define(type("biedge-rule").sub("rule").when(
                var().rel("source", "x").rel("destination", "y").isa("edge")
        ).then(var().isa("biedge").rel("end", "x").rel("end", "y"))));

        result.add(Graql.define(type("hasedge").sub("relation").relates("v")));
        result.add(Graql.define(type("hasedge-rule").sub("rule").when(
                var().rel("end", "x").rel("end", "y").isa("biedge")
        ).then(var().isa("hasedge").rel("v", "x"))));



        StatementType memberRelRelation = type("memberRel").sub("relation");
        for(int i=0; i<c; ++i) {
            memberRelRelation = memberRelRelation.relates("elem"+i );
        }
        memberRelRelation = memberRelRelation.relates("member");


        result.add(Graql.define(memberRelRelation));

        List<Pattern> memberRelConditions = new ArrayList<>();
        for(int i=0; i<c; ++i) {
            memberRelConditions.add(var().rel("v", "var"+i).isa("hasedge"));
        }

        StatementInstance memberRelResult = var().isa("memberRel");
        for(int i=0; i<c; ++i) {
            memberRelResult = memberRelResult.rel("elem"+i,"var"+i);
        }
        memberRelResult = memberRelResult.rel("member", "mem");

        for(int i=0; i<1; ++i) {
            result.add(Graql.define(
                    type("memberRel-rule"+i).sub("rule").when(and(
                            and(memberRelConditions),
                            var().rel("v", "mem").isa("hasedge"),
                            var("var"+i).eq(var("mem"))
                    ))
                            .then(memberRelResult)));
        }


        result.add(Graql.match(
                var().rel("v", "x").isa("hasedge"),
                var().rel("v", "y").isa("hasedge"),
                var().rel("v", "z").isa("hasedge"),
                var().rel("v", "t").isa("hasedge"),
                var().rel("elem0", "x").rel("elem1", "y").rel("elem2", "z").rel("member", "t").isa("memberRel")
        ).get("x", "y", "z", "t"));


        StatementType componentRelation = type("component").sub("relation");
        for(int i=0; i<c; ++i) {
            componentRelation = componentRelation.relates("elem"+i );
        }

        result.add(Graql.define(componentRelation));

        List<Pattern> componentConditions = new ArrayList<>();
        for(int i=0; i<c; ++i) {
            if(i+1<c) {
                componentConditions.add(var().rel("end", "var"+i).rel("end", "var"+(i+1)).isa("biedge"));
            }
        }

        List<Pattern> componentConditionsNot = new ArrayList<>();
        for(int i=0; i<c; ++i) {
            componentConditionsNot.add(var("var"+i).neq("z"));
        }
        Statement componentConditionsNotMember = var();
        for(int i=0; i<c; ++i) {
            componentConditionsNotMember = componentConditionsNotMember.rel("elem"+i, "var"+i);
        }
        componentConditionsNotMember = componentConditionsNotMember.rel("member", "y").isa("memberRel");
        

        StatementInstance componentResult = var().isa("component");
        for(int i=0; i<c; ++i) {
            componentResult = componentResult.rel("elem"+i,"var"+i);
        }


        result.add(Graql.define(
                type("component-rule").sub("rule").when(and(and(componentConditions),not(and(
                        var().isa("biedge").rel("end", "y").rel("end", "z"),
                        componentConditionsNotMember,
                        and(componentConditionsNot))
                )))
                        .then(componentResult)));

        StatementInstance resulting_rule = var().isa("component");
        for(int i=0; i<c; ++i) {
            resulting_rule = resulting_rule.rel("elem"+i,Integer.toString(i));
        }

        String[] answers = new String[c];
        for(int i=0; i<answers.length; ++i) {
            answers[i] = Integer.toString(i);
        }


        result.add(Graql.match(resulting_rule).get(answers[0], Arrays.copyOfRange(answers, 1, answers.length)));

        return result;
    }


    @Override
    public Property getTestingProperty() {
        return new DisjointAnswersProperty();
    }

    @Override
    public SchemaGenerator getSchemaGenerator() {
        return new PlainGraphSchema();
    }
}
