package uk.ac.cam.gp.charlie.metamorphic;

import uk.ac.cam.gp.charlie.metamorphic.tests.TestGenerator;

import java.util.ArrayList;
import java.util.Date;

import uk.ac.cam.gp.charlie.metamorphic.Utils.DebugPrinter;
import uk.ac.cam.gp.charlie.metamorphic.tests.rule_eq_test.RandomRuleEqTest;

//temporary name
public class Main {
    public static void runTests(TestGenerator test, int range) {
        ArrayList<Thread> threads = new ArrayList<>();
        for(int i=0; i<range; ++i) {
            final int index = i;
            threads.add(new Thread() {
               public void run() {
                   boolean verdict = new SingleTestRunner(test).runOnSeed(index);
                   if(verdict) DebugPrinter.print("Test "+index+" passed.");
                   else DebugPrinter.print("Test "+index+" failed.");
               }
            });
        }

        for(Thread thread: threads) {
            thread.start();
        }

        for(Thread thread: threads) {
            try {
                thread.join();
            }
            catch(InterruptedException e) {
                //ignore
            }
        }
    }


    public static void main(String[] args) {
        DebugPrinter.start();
        DebugPrinter.print("Program started");


        for(int i=0; i<20; ++i) {
            DebugPrinter.print(new Date().toString());
            boolean verdict = new SingleTestRunner(new RandomRuleEqTest(10, 15)).runOnSeed(i);
            if (verdict) DebugPrinter.print("Test " + i + " passed.");
            else DebugPrinter.print("Test " + i + " failed.");
        }

        DebugPrinter.print(new Date().toString());
    }
}
