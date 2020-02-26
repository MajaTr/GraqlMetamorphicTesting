package uk.ac.cam.gp.charlie.metamorphic;

import uk.ac.cam.gp.charlie.metamorphic.Utils.DebugPrinter;
import uk.ac.cam.gp.charlie.metamorphic.bug_reports.Bug1;
import uk.ac.cam.gp.charlie.metamorphic.bug_reports.Bug2;
import uk.ac.cam.gp.charlie.metamorphic.bug_reports.Bug3;
import uk.ac.cam.gp.charlie.metamorphic.tests.plain_graph_tests.DisjointComponentsTest;
import uk.ac.cam.gp.charlie.metamorphic.tests.plain_graph_tests.RandomRuleEqTest;

import java.util.Date;

//temporary name
public class Main {

    public static void main(String[] args) {
        DebugPrinter.start();
        DebugPrinter.print("Program started");


        for(int i=0; i<20; ++i) {
            DebugPrinter.print(new Date().toString());
            boolean verdict = new SingleTestRunner(new Bug1(10, 6)).runOnSeed(i);
            if (verdict) DebugPrinter.print("Test " + i + " passed.");
            else DebugPrinter.print("Test " + i + " failed.");
        }

        DebugPrinter.print(new Date().toString());
    }
}
