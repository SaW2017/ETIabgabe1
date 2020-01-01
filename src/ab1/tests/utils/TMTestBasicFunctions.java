package ab1.tests.utils;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;

import ab1.Movement;
import ab1.TM;
import ab1.TMConfig;

public class TMTestBasicFunctions {

    private final TM machine;

    public TMTestBasicFunctions(TM tm) {
	this.machine = tm;
    }

    public void testSymbols() {
	// Tests for equality of given symbols and returned symbols
	HashSet<Character> alphabet = new HashSet<>(Arrays.asList('a', 'b', 'c', '#'));
	machine.reset();
	machine.setSymbols(alphabet);
	Assert.assertEquals(alphabet, machine.getSymbols());
    }

    public void testNoSpace() {
	// Tests for acceptance of symbols without '#' among them
	HashSet<Character> alphabet = new HashSet<>(Arrays.asList('a', 'b', 'c'));
	machine.reset();
	machine.setSymbols(alphabet);
    }

    public void testStates() {
	// Tests for correct state set after adding some transitions
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.addTransition(1, 'a', 2, 'a', Movement.LEFT);
	machine.addTransition(2, 'b', 3, 'c', Movement.STAY);
	machine.addTransition(2, 'a', 3, 'c', Movement.STAY);
	Assert.assertEquals(new HashSet<Integer>(Arrays.asList(0, 1, 2, 3)), machine.getStates());
    }

    public void testNoTransition() {
	// Tries to do a step with no corresponding transition
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.addTransition(1, 'a', 2, 'a', Movement.LEFT);
	machine.addTransition(2, 'b', 3, 'c', Movement.STAY);
	machine.addTransition(2, 'a', 3, 'c', Movement.STAY);
	machine.setInitialState(1);
	machine.doNextStep();
    }

    public void testNoTransition2() {
	// Tries to do a step with no transitions at all
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.setInitialState(1);
	machine.doNextStep();
    }

    public void testHaltTransition() {
	// Tries to add a transition from halt state
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.addTransition(0, 'a', 1, 'b', Movement.STAY);
    }

    public void testHalt() {
	// Tries to reach halt state through a single transition
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.addTransition(1, '#', 0, '#', Movement.RIGHT);
	machine.setInitialState(1);
	machine.doNextStep();
	Assert.assertTrue(machine.isHalt());
    }

    public void testCrash() {
	// Produces a crash (no transitions) and checks the crashed flag afterwards
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
	machine.setInitialState(1);
	try {
	    machine.doNextStep();
	} catch (IllegalStateException ex) {
	}
	Assert.assertTrue(machine.isCrashed());
    }

    public void testTape() {
	// Tape must be empty and head position should be 0 after reset
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', '#')));
	machine.setInitialTapeContent("#a#b#".toCharArray());
	TMConfig config = machine.getTMConfig();
	Assert.assertTrue(new String(config.getLeftOfHead()).equals("#a#b#") && config.getBelowHead() == '#'
		&& config.getRightOfHead().length == 0);
    }

}
