/*
 * Copyright (C) Lennart Martens
 * 
 * Contact: lennart.martens AT UGent.be (' AT ' to be replaced with '@')
 */

/*
 * Created by IntelliJ IDEA.
 * User: Lennart
 * Date: 20-okt-02
 * Time: 12:56:38
 */
package com.compomics.dbtoolkit.test.gui.workerthreads;

import com.compomics.dbtoolkit.gui.workerthreads.ProcessThread;
import com.compomics.dbtoolkit.io.UnknownDBFormatException;
import com.compomics.dbtoolkit.io.implementations.AutoDBLoader;
import com.compomics.dbtoolkit.io.implementations.ProteinSequenceRegExpFilter;
import com.compomics.dbtoolkit.io.interfaces.DBLoader;
import com.compomics.dbtoolkit.io.interfaces.ProteinFilter;
import com.compomics.util.junit.TestCaseLM;
import com.compomics.util.protein.Enzyme;
import junit.framework.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/*
 * CVS information:
 *
 * $Revision: 1.2 $
 * $Date: 2007/07/06 09:52:03 $
 */

/**
 * This class tests the ProcessThread in full, although not in Threading mode.
 *
 * @author Lennart Martens
 * @see com.compomics.dbtoolkit.gui.workerthreads.ProcessThread
 */
public class TestProcessThread extends TestCase {

    public TestProcessThread() {
        this("Test for the ProcessThread class - not in Threading mode, 'though!");
    }

    public TestProcessThread(String aName) {
        super(aName);
    }

    /**
     * This method test the factory-like creation of an N-terminal
     * ragging task, as well as the N-terminal ragging process itself.
     */
    public void testNtermRagging() {
        File input = null;
        File output = null;
        File control = null;
        try {
            // Necessary variables.
            input = new File(TestCaseLM.getFullFilePath("testFASTA.fas"));
            control = new File(TestCaseLM.getFullFilePath("controlOfNTermRaggingTest.fas"));
            output = new File(input.getParent() + "/outputOfNTermRaggingTest.fas");

            AutoDBLoader auto = new AutoDBLoader(new String[] {"com.compomics.dbtoolkit.io.implementations.FASTADBLoader"});
            DBLoader loader = auto.getLoaderForFile(input.getAbsolutePath());

            ProcessThread pt = ProcessThread.getRaggingTask(loader, output, null, null, new Enzyme("TestEnzyme", "KR", "P", "Cterm", 1), true, 600, 4000, ProcessThread.NTERMINUS, true, 100);
            pt.run();
            loader = null;

            BufferedReader brControle = new BufferedReader(new FileReader(control));
            BufferedReader brOutput = new BufferedReader(new FileReader(output));

            String line = null;
            while((line = brControle.readLine()) != null) {
                Assert.assertEquals(line, brOutput.readLine());
            }
            brControle.close();
            brOutput.close();
        } catch(IOException ioe) {
            fail("IOException thrown when testing the creation of an N-terminal ragging ProcessThread: " + ioe.getMessage());
        } catch(UnknownDBFormatException udfe) {
            fail("UnknownDBFormatException thrown when testing the creation of an N-terminal ragging ProcessThread with a FASTA DB: " + udfe.getMessage());
        }finally {
            if(output != null && output.exists()) {
                output.delete();
            }
        }
    }

    /**
     * This method test the factory-like creation of a C-terminal
     * ragging task, as well as the C-terminal ragging process itself.
     */
    public void testCtermRagging() {
        File input = null;
        File output = null;
        File control = null;
        try {
            // Necessary variables.
            input = new File(TestCaseLM.getFullFilePath("testFASTA.fas"));
            control = new File(TestCaseLM.getFullFilePath("controlOfCTermRaggingTest.fas"));
            output = new File(input.getParent() + "/outputOfCTermRaggingTest.fas");

            AutoDBLoader auto = new AutoDBLoader(new String[] {"com.compomics.dbtoolkit.io.implementations.FASTADBLoader"});
            DBLoader loader = auto.getLoaderForFile(input.getAbsolutePath());

            ProcessThread pt = ProcessThread.getRaggingTask(loader, output, null, null, new Enzyme("TestEnzyme", "KR", "P", "Cterm", 1), true, 600, 4000, ProcessThread.CTERMINUS, true, 100);
            pt.run();
            loader = null;

            BufferedReader brControle = new BufferedReader(new FileReader(control));
            BufferedReader brOutput = new BufferedReader(new FileReader(output));

            String line = null;
            while((line = brControle.readLine()) != null) {
                Assert.assertEquals(line, brOutput.readLine());
            }
            brControle.close();
            brOutput.close();
        } catch(IOException ioe) {
            fail("IOException thrown when testing the creation of a C-terminal ragging ProcessThread: " + ioe.getMessage());
        } catch(UnknownDBFormatException udfe) {
            fail("UnknownDBFormatException thrown when testing the creation of a C-terminal ragging ProcessThread with a FASTA DB: " + udfe.getMessage());
        }finally {
            if(output != null && output.exists()) {
                output.delete();
            }
        }
    }

    /**
     * This method tests the truncations without mass limits.
     * (Both N-terminal and C-terminal ragging).
     */
    public void testWithoutMassLimits() {
        File output = null;
        File input = new File(TestCaseLM.getFullFilePath("NoMassLimitsRagFile.fas"));
        File control = null;
        try {
            // Necessary variables.
            control = new File(TestCaseLM.getFullFilePath("controlOfNTermRaggingTest_noMassLimits.fas"));
            output = new File(input.getParent() + "/outputOfNTermRaggingTest_noMassLimits.fas");

            AutoDBLoader auto = new AutoDBLoader(new String[] {"com.compomics.dbtoolkit.io.implementations.FASTADBLoader"});
            DBLoader loader = auto.getLoaderForFile(input.getAbsolutePath());

            ProcessThread pt = ProcessThread.getRaggingTask(loader, output, null, null, new Enzyme("TestEnzyme", "KR", "P", "Cterm", 1), false, 0, 0, ProcessThread.NTERMINUS, false, 0);
            pt.run();
            loader = null;

            BufferedReader brControle = new BufferedReader(new FileReader(control));
            BufferedReader brOutput = new BufferedReader(new FileReader(output));

            String line = null;
            while((line = brControle.readLine()) != null) {
                Assert.assertEquals(line, brOutput.readLine());
            }
            brControle.close();
            brOutput.close();
        } catch(IOException ioe) {
            fail("IOException thrown when testing the creation of a N-terminal ragging ProcessThread without mass limits: " + ioe.getMessage());
        } catch(UnknownDBFormatException udfe) {
            fail("UnknownDBFormatException thrown when testing the creation of a N-terminal ragging ProcessThread without mass limits with a FASTA DB: " + udfe.getMessage());
        }finally {
            if(output != null && output.exists()) {
                output.delete();
            }
        }

        try {
            // Necessary variables.
            control = new File(TestCaseLM.getFullFilePath("controlOfCTermRaggingTest_noMassLimits.fas"));
            output = new File(input.getParent() + "/outputOfCTermRaggingTest_noMassLimits.fas");

            AutoDBLoader auto = new AutoDBLoader(new String[] {"com.compomics.dbtoolkit.io.implementations.FASTADBLoader"});
            DBLoader loader = auto.getLoaderForFile(input.getAbsolutePath());

            ProcessThread pt = ProcessThread.getRaggingTask(loader, output, null, null, new Enzyme("TestEnzyme", "KR", "P", "Cterm", 1), false, 0, 0, ProcessThread.CTERMINUS, false, 0);
            pt.run();
            loader = null;

            BufferedReader brControle = new BufferedReader(new FileReader(control));
            BufferedReader brOutput = new BufferedReader(new FileReader(output));

            String line = null;
            while((line = brControle.readLine()) != null) {
                Assert.assertEquals(line, brOutput.readLine());
            }
            brControle.close();
            brOutput.close();
        } catch(IOException ioe) {
            fail("IOException thrown when testing the creation of a C-terminal ragging ProcessThread without mass limits: " + ioe.getMessage());
        } catch(UnknownDBFormatException udfe) {
            fail("UnknownDBFormatException thrown when testing the creation of a C-terminal ragging ProcessThread without mass limits with a FASTA DB: " + udfe.getMessage());
        }finally {
            if(output != null && output.exists()) {
                output.delete();
            }
        }
    }

    /**
     * This method test the factory-like creation of a subset selection
     * task, as well as the subset selection process itself, both from
     * a query string and a regular expression.
     */
    public void testSubsetSelection() {
        File input = null;
        File output = null;
        File control = null;

        // First String-based (own query style) subset selection.
        try {
            // Necessary variables.
            input = new File(TestCaseLM.getFullFilePath("testFASTA.fas"));
            control = new File(TestCaseLM.getFullFilePath("controlOfStringSubsetTest.fas"));
            output = new File(input.getParent() + "/outputOfStringSubsetTest.fas");

            AutoDBLoader auto = new AutoDBLoader(new String[] {"com.compomics.dbtoolkit.io.implementations.FASTADBLoader"});
            DBLoader loader = auto.getLoaderForFile(input.getAbsolutePath());

            ProcessThread pt = ProcessThread.getSubsetTask(loader, output, null, null, null, false, -1.0, -1.0, ".SKQ and L");
            pt.run();
            loader = null;

            BufferedReader brControle = new BufferedReader(new FileReader(control));
            BufferedReader brOutput = new BufferedReader(new FileReader(output));

            String line = null;
            while((line = brControle.readLine()) != null) {
                Assert.assertEquals(line, brOutput.readLine());
            }
            brControle.close();
            brOutput.close();
        } catch(IOException ioe) {
            fail("IOException thrown when testing the creation of a subset selection ProcessThread using a query string: " + ioe.getMessage());
        } catch(UnknownDBFormatException udfe) {

            fail("UnknownDBFormatException thrown when testing the creation of a subset selection ProcessThread using a query string with a FASTA DB: " + udfe.getMessage());
        }finally {
            if(output != null && output.exists()) {
                output.delete();
            }
        }


        // Now regexp-based subset selection.
        try {
            // Necessary variables.
            input = new File(TestCaseLM.getFullFilePath("testFASTA.fas"));
            control = new File(TestCaseLM.getFullFilePath("controlOfRegExpSubsetTest.fas"));
            output = new File(input.getParent() + "/outputOfRegExpSubsetTest.fas");

            AutoDBLoader auto = new AutoDBLoader(new String[] {"com.compomics.dbtoolkit.io.implementations.FASTADBLoader"});
            DBLoader loader = auto.getLoaderForFile(input.getAbsolutePath());

            ProteinFilter regExpFilter = new ProteinSequenceRegExpFilter(Pattern.compile("[QIS]{3}.LA.[PLDS]{4}"));
            ProcessThread pt = ProcessThread.getSubsetTask(loader, output, null, null, null, false, -1.0, -1.0, regExpFilter);
            pt.run();
            loader = null;

            BufferedReader brControle = new BufferedReader(new FileReader(control));
            BufferedReader brOutput = new BufferedReader(new FileReader(output));

            String line = null;
            while((line = brControle.readLine()) != null) {
                Assert.assertEquals(line, brOutput.readLine());
            }
            brControle.close();
            brOutput.close();
        } catch(IOException ioe) {
            fail("IOException thrown when testing the creation of a subset selection ProcessThread using a query string: " + ioe.getMessage());
        } catch(UnknownDBFormatException udfe) {

            fail("UnknownDBFormatException thrown when testing the creation of a subset selection ProcessThread using a query string with a FASTA DB: " + udfe.getMessage());
        }finally {
            if(output != null && output.exists()) {
                output.delete();
            }
        }
    }


}
