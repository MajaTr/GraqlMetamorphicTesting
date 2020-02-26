package uk.ac.cam.gp.charlie.metamorphic.bug_reports;

import graql.lang.Graql;
import graql.lang.query.GraqlQuery;
import uk.ac.cam.gp.charlie.metamorphic.abstract_tests.RandomGraph;
import uk.ac.cam.gp.charlie.metamorphic.general_schemas.PlainGraphSchema;
import uk.ac.cam.gp.charlie.metamorphic.properties.EqProperty;
import uk.ac.cam.gp.charlie.metamorphic.properties.Property;
import uk.ac.cam.gp.charlie.metamorphic.tests.SchemaGenerator;
import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.List;

import static graql.lang.Graql.not;
import static graql.lang.Graql.var;

public class Bug1 extends RandomGraph implements TestGenerator {
    public Bug1(int n, int m) {
        super(n, m);
    }

    @Override
    public List<GraqlQuery> generate(int seed) {
        List<GraqlQuery> result = super.generate(seed);
        result.add(Graql.match(
                var().rel("source", "0").rel("destination", "0").isa("edge"),
                var().rel("source", "0").rel("destination", "1").isa("edge"),
                not( var().rel("source", "2").rel("destination", "0").isa("edge")),
                not( var().rel("source", "2").rel("destination", "1").isa("edge"))
        ).get("0", "1"));

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
