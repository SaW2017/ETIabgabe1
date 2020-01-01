package ab1.tests;

import java.util.regex.Pattern;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import ab1.Ab1;
import ab1.impl.Wachter.Ab1Impl;
import ab1.tests.utils.TMTestAdvComplex;
import ab1.tests.utils.TMTestAdvExp;
import ab1.tests.utils.TMTestAdvFunctions;
import ab1.tests.utils.TMTestAdvReset;
import ab1.tests.utils.TMTestBasicFunctions;
import ab1.tests.utils.TMTestBasicMachine;

public class Ab1Test {
    private final Ab1 sol = new Ab1Impl();

    private static double numPts = 0;

    @Test
    public void testRegexGeradeLaenge() {
	String regEx = sol.getRegExImpl().getRegexGeradeLaenge();

	Assert.assertEquals(true, Pattern.matches(regEx, "aaaaab"));
	Assert.assertEquals(true, Pattern.matches(regEx, ""));
	Assert.assertEquals(true, Pattern.matches(regEx, "ab"));
	Assert.assertEquals(true, Pattern.matches(regEx, "zzzz"));
	Assert.assertEquals(true, Pattern.matches(regEx, "ab ab ab ab ab"));
	Assert.assertEquals(true, Pattern.matches(regEx, "ab.ab.ab.ab.ab"));

	Assert.assertEquals(false, Pattern.matches(regEx, "a"));
	Assert.assertEquals(false, Pattern.matches(regEx, "."));
	Assert.assertEquals(false, Pattern.matches(regEx, ".a."));
	Assert.assertEquals(false, Pattern.matches(regEx, "hallo"));

	numPts += 0.5;
    }

    @Test
    public void testRegexGanzeZahlen() {
	String regEx = sol.getRegExImpl().getRegexGanzeZahlen();

	Assert.assertEquals(true, Pattern.matches(regEx, "1"));
	Assert.assertEquals(true, Pattern.matches(regEx, "10"));
	Assert.assertEquals(true, Pattern.matches(regEx, "+17"));
	Assert.assertEquals(true, Pattern.matches(regEx, "-20"));
	Assert.assertEquals(true, Pattern.matches(regEx, "+100"));
	Assert.assertEquals(true, Pattern.matches(regEx, "0"));

	Assert.assertEquals(false, Pattern.matches(regEx, "-0"));
	Assert.assertEquals(false, Pattern.matches(regEx, "+0"));
	Assert.assertEquals(false, Pattern.matches(regEx, "01"));
	Assert.assertEquals(false, Pattern.matches(regEx, "000"));
	Assert.assertEquals(false, Pattern.matches(regEx, "a19"));
	Assert.assertEquals(false, Pattern.matches(regEx, "0xAB"));
	Assert.assertEquals(false, Pattern.matches(regEx, "0.78f"));

	numPts += 0.5;
    }

    @Test
    public void testRegexTelnummer() {
	String regEx = sol.getRegExImpl().getRegexTelnummer();

	Assert.assertEquals(true, Pattern.matches(regEx, "+436801234567"));
	Assert.assertEquals(true, Pattern.matches(regEx, "+1 123 1234567"));
	Assert.assertEquals(true, Pattern.matches(regEx, "+43 680 1234567"));
	Assert.assertEquals(true, Pattern.matches(regEx, "0043 680 1234567"));
	Assert.assertEquals(true, Pattern.matches(regEx, "00436801234567"));
	Assert.assertEquals(true, Pattern.matches(regEx, "+11231234567"));

	Assert.assertEquals(false, Pattern.matches(regEx, "+43 680 12 34 567"));
	Assert.assertEquals(false, Pattern.matches(regEx, "+ 43 680 12 34 567"));
	Assert.assertEquals(false, Pattern.matches(regEx, "+ 4 3 6 8 0 1 2 3 4 5 6 7"));
	Assert.assertEquals(false, Pattern.matches(regEx, "0043 680 12 34 567"));
	Assert.assertEquals(false, Pattern.matches(regEx, "00 43 680 12 34 567"));
	Assert.assertEquals(false, Pattern.matches(regEx, "0 0 4 3 6 8 0 1 2 3 4 5 6 7"));
	Assert.assertEquals(false, Pattern.matches(regEx, "06801234567"));
	Assert.assertEquals(false, Pattern.matches(regEx, "+43.680.1234567"));
	Assert.assertEquals(false, Pattern.matches(regEx, "+43 680a1234567"));
	Assert.assertEquals(false, Pattern.matches(regEx, "+43-680-1234567"));

	numPts += 0.5;
    }

    @Test
    public void testRegexDatum() {
	String regEx = sol.getRegExImpl().getRegexDatum();

	Assert.assertEquals(true, Pattern.matches(regEx, "01.01.2012"));
	Assert.assertEquals(true, Pattern.matches(regEx, "1.1.2012"));
	Assert.assertEquals(true, Pattern.matches(regEx, "11.1.2012"));
	Assert.assertEquals(true, Pattern.matches(regEx, "1.11.2012"));

	Assert.assertEquals(false, Pattern.matches(regEx, "1. Jänner 2012"));
	Assert.assertEquals(false, Pattern.matches(regEx, "1.1.16"));
	Assert.assertEquals(false, Pattern.matches(regEx, "01.01.16"));

	numPts += 1;
    }

    @Test
    public void testRegexDomainName() {
	String regEx = sol.getRegExImpl().getRegexDomainName();

	Assert.assertEquals(true, Pattern.matches(regEx, "www.aau.at"));
	Assert.assertEquals(true, Pattern.matches(regEx, "aau.at"));
	Assert.assertEquals(true, Pattern.matches(regEx, "campus.aau.at"));

	Assert.assertEquals(false, Pattern.matches(regEx, "w-w-w.aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "www.aau.ataeh"));
	Assert.assertEquals(false, Pattern.matches(regEx, "www.auto"));
	Assert.assertEquals(false, Pattern.matches(regEx, "www"));
	Assert.assertEquals(false, Pattern.matches(regEx, "at"));
	Assert.assertEquals(false, Pattern.matches(regEx, ".www.aau.at"));

	numPts += 1;
    }

    @Test
    public void testRegexEmail() {
	String regEx = sol.getRegExImpl().getRegexEmail();

	Assert.assertEquals(true, Pattern.matches(regEx, "jemand@aau.at"));
	Assert.assertEquals(true, Pattern.matches(regEx, "jemand.anderes@aau.at"));
	Assert.assertEquals(true, Pattern.matches(regEx, "jemand-anderes@aau.at"));

	Assert.assertEquals(false, Pattern.matches(regEx, "aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "@aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "jemand@at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "9jemand@aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, ".jemand@aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "-jemand@aau.at"));

	numPts += 1;

    }

    @Test
    public void testRegexURL() {
	String regEx = sol.getRegExImpl().getRegexURL();

	Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at"));
	Assert.assertEquals(true, Pattern.matches(regEx, "ftp://aau.at"));
	Assert.assertEquals(true, Pattern.matches(regEx, "https://campus.aau.at"));
	Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80"));
	Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80/"));
	Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80/login"));
	Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80/login.intern"));
	Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80/login-intern"));
	Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80/sub/sub.sub/sub-sub-sub/"));

	Assert.assertEquals(false, Pattern.matches(regEx, "ssh://www.aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "://aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "http:/campus.aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "http//campus.aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "//campus.aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "w-w-w.aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "www.aau.ataeh"));
	Assert.assertEquals(false, Pattern.matches(regEx, "www.auto"));
	Assert.assertEquals(false, Pattern.matches(regEx, "www"));
	Assert.assertEquals(false, Pattern.matches(regEx, "at"));
	Assert.assertEquals(false, Pattern.matches(regEx, ".www.aau.at"));
	Assert.assertEquals(false, Pattern.matches(regEx, "http://www.aau.at:-80"));
	Assert.assertEquals(false, Pattern.matches(regEx, "http://www.aau.at:80login"));
	Assert.assertEquals(false, Pattern.matches(regEx, "http://www.aau.at:80a/login.intern"));

	numPts += 1;
    }

    @Test
    public void testRegexVereinfachen1() {
	String regEx = sol.getRegExImpl().getRegexVereinfachen1();

	Assert.assertEquals(true, regEx.length() <= 20);

	Assert.assertEquals(true, Pattern.matches(regEx, "aaaaaaaaaa"));
	Assert.assertEquals(true, Pattern.matches(regEx, "bbbbbbbbbb"));
	Assert.assertEquals(true, Pattern.matches(regEx, "bbaabbaabb"));
	Assert.assertEquals(true, Pattern.matches(regEx, "ababababab"));
	Assert.assertEquals(true, Pattern.matches(regEx, "cccccccccc"));
	Assert.assertEquals(true, Pattern.matches(regEx, "abcdabcdab"));

	Assert.assertEquals(false, Pattern.matches(regEx, "abcd"));
	Assert.assertEquals(false, Pattern.matches(regEx, "abcdabcdabcd"));
	Assert.assertEquals(false, Pattern.matches(regEx, "eeeeeeeeee"));

	numPts += 0.5;
    }

    @Test
    public void testRegexVereinfachen2() {
	String regEx = sol.getRegExImpl().getRegexVereinfachen2();

	Assert.assertEquals(true, regEx.length() <= 10);

	Assert.assertEquals(true, Pattern.matches(regEx, "abab"));
	Assert.assertEquals(true, Pattern.matches(regEx, "aa"));
	Assert.assertEquals(true, Pattern.matches(regEx, "aaa"));
	Assert.assertEquals(true, Pattern.matches(regEx, "aaaa"));
	Assert.assertEquals(true, Pattern.matches(regEx, "bbbb"));
	Assert.assertEquals(true, Pattern.matches(regEx, "baaa"));

	Assert.assertEquals(false, Pattern.matches(regEx, "a"));
	Assert.assertEquals(false, Pattern.matches(regEx, "aaaaa"));
	Assert.assertEquals(false, Pattern.matches(regEx, "dd"));

	numPts += 0.5;
    }

    @Test
    public void testTMBasic() {
	TMTestBasicMachine m = new TMTestBasicMachine(sol.getTMImpl());

	m.testLeft();

	m.testRight();

	m.testHead();

	numPts += 0.5;
    }

    @Test
    public void testTMBasicFunctions() {
	TMTestBasicFunctions m = new TMTestBasicFunctions(sol.getTMImpl());

	m.testCrash();

	m.testHalt();

	try {
	    m.testHaltTransition();
	    Assert.fail();
	} catch (IllegalArgumentException e) {

	} catch (Exception e) {
	    Assert.fail();
	}

	try {
	    m.testNoSpace();
	    Assert.fail();
	} catch (IllegalArgumentException e) {

	} catch (Exception e) {
	    Assert.fail();
	}

	try {
	    m.testNoTransition();
	    Assert.fail();
	} catch (IllegalStateException e) {

	} catch (Exception e) {
	    Assert.fail();
	}

	try {
	    m.testNoTransition2();
	    Assert.fail();
	} catch (IllegalStateException e) {

	} catch (Exception e) {
	    Assert.fail();
	}

	m.testStates();

	m.testSymbols();

	m.testTape();

	numPts += 1;
    }

    @Test
    public void testTMAdvReset() {
	TMTestAdvReset m = new TMTestAdvReset(sol.getTMImpl());

	m.testCreateCrashed();

	m.testCreateTape();

	m.testCreateTransitions();

	m.testResetCrashed();

	m.testResetTape();

	m.testResetTransitions();

	numPts += 1;
    }

    @Test
    public void testTMAdvFunctions() {
	TMTestAdvFunctions m = new TMTestAdvFunctions(sol.getTMImpl());

	try {
	    m.testAmbiguousTransitions();
	    Assert.fail();
	} catch (IllegalArgumentException e) {

	} catch (Exception e) {
	    Assert.fail();
	}

	m.testConfigCrashed();

	m.testInvalidCharsOnTape();

	try {
	    m.testInvalidRead();
	    Assert.fail();
	} catch (IllegalArgumentException e) {

	} catch (Exception e) {
	    Assert.fail();
	}

	try {
	    m.testInvalidWrite();
	    Assert.fail();
	} catch (IllegalArgumentException e) {

	} catch (Exception e) {
	    Assert.fail();
	}

	m.testLongTape();

	m.testNegativeAndBigStates();

	m.testNoMovement();

	m.testSymbolsAdv();

	numPts += 2;
    }

    @Test
    public void testTMAdvExp() {
	TMTestAdvExp m = new TMTestAdvExp(sol.getTMImpl());

	m.setup();

	m.test1();

	m.test2();

	m.test3();

	m.test4();

	m.test5();

	m.test6();

	numPts += 2;
    }

    @Test
    public void testTMAdvComplex() {
	TMTestAdvComplex m = new TMTestAdvComplex(sol.getTMImpl());

	m.setup();

	m.testAdd();

	m.testDecrement1();

	m.testDecrement2();

	m.testDecrement3();

	m.testDenormalize();

	m.testFull1();

	m.testFull2();

	m.testIncrement1();

	m.testIncrement2();

	m.testNormalize();

	m.testSub1();

	m.testSub2();

	m.testSub21();

	m.testSub22();

	m.testSub23();

	numPts += 2;
    }

    @AfterClass
    public static void printPts() {
	int pts = (int) Math.ceil(numPts);

	System.out.println(pts + " von 15 Punkten erreicht");
    }
}
