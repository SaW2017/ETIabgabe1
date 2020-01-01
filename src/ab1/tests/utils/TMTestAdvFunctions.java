package ab1.tests.utils;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;

import ab1.Movement;
import ab1.TM;
import ab1.TMConfig;

public class TMTestAdvFunctions {

    private final TM machine;

    public TMTestAdvFunctions(TM tm) {
	this.machine = tm;
    }

    public void testSymbolsAdv() {
	HashSet<Character> alphabet = new HashSet<>();
	for (int i = ' '; i < 256; i++)
	    alphabet.add((char) i);
	machine.reset();
	machine.setSymbols(alphabet);
	Assert.assertEquals(alphabet, machine.getSymbols());
    }

    public void testInvalidRead() {
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.addTransition(1, 'x', 1, 'b', Movement.STAY);
    }

    public void testInvalidWrite() {
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.addTransition(1, 'a', 1, 'x', Movement.STAY);
    }

    public void testNoMovement() {
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	try {
	    machine.addTransition(1, '#', 1, '#', null);
	} catch (IllegalArgumentException ex) {
	    return;
	}
	machine.setInitialState(1);
	machine.setInitialTapeContent("#aa".toCharArray());
	machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertTrue(new String(config.getLeftOfHead()).equals("#aa") && config.getBelowHead() == '#'
		&& config.getRightOfHead().length == 0);
    }

    public void testNegativeAndBigStates() {
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.addTransition(1, 'a', -1, 'b', Movement.LEFT);
	machine.addTransition(-1, 'a', 999999999, 'c', Movement.LEFT);
	machine.addTransition(999999999, 'a', 1, 'a', Movement.RIGHT);
	machine.addTransition(999999999, '#', 1, '#', Movement.RIGHT);
	machine.addTransition(1, 'c', 1, 'c', Movement.LEFT);
	machine.addTransition(1, '#', 0, 'a', Movement.STAY);
	machine.addTransition(-2000000000, '#', 1, '#', Movement.LEFT);
	machine.setInitialState(-2000000000);
	machine.setInitialTapeContent("#aaaaaa".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	Assert.assertEquals("cbcbcb", new String(machine.getTMConfig().getRightOfHead()));
    }

    public void testLongTape() {
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('|', '#')));
	machine.addTransition(1, '#', 1, '|', Movement.RIGHT);
	machine.setInitialState(1);
	machine.setInitialTapeContent(new char[0]);
	for (int i = 0; i < (1 << 10); i++) {
	    machine.doNextStep();
	}

	Assert.assertEquals(1 << 10, machine.getTMConfig().getLeftOfHead().length);
    }

    public void testInvalidCharsOnTape() {
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.addTransition(1, '#', 1, '#', Movement.LEFT);
	machine.addTransition(1, 'a', 0, 'a', Movement.STAY);
	machine.addTransition(1, 'b', 1, 'b', Movement.LEFT);
	machine.addTransition(1, 'c', 1, 'c', Movement.LEFT);
	machine.setInitialState(1);
	try {
	    machine.setInitialTapeContent("#axb".toCharArray());
	} catch (Exception ex) {
	    return;
	}

	try {
	    while (!machine.isHalt())
		machine.doNextStep();
	    Assert.fail();
	} catch (IllegalStateException ex) {
	}
    }

    public void testConfigCrashed() {
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.addTransition(1, '#', 1, '#', Movement.LEFT);
	machine.setInitialTapeContent("#aa".toCharArray());
	machine.setInitialState(1);

	try {
	    while (!machine.isHalt())
		machine.doNextStep();
	    Assert.fail();
	} catch (IllegalStateException ex) {
	}

	Assert.assertNull(machine.getTMConfig());
    }

    public void testAmbiguousTransitions() {
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.addTransition(1, 'a', 1, 'a', Movement.LEFT);
	machine.addTransition(1, 'a', 0, 'b', Movement.LEFT);
    }
}
