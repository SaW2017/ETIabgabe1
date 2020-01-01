package ab1.tests.utils;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;

import ab1.Movement;
import ab1.TM;
import ab1.TMConfig;

public class TMTestAdvReset {

    private final TM machine;

    public TMTestAdvReset(TM tm) {
	this.machine = tm;
    }

    public void testCreateCrashed() {
	machine.reset();
	Assert.assertFalse(machine.isCrashed());
    }

    public void testResetCrashed() {
	machine.reset();
	try {
	    machine.doNextStep();
	} catch (IllegalStateException ex) {
	}
	machine.reset();
	Assert.assertFalse(machine.isCrashed());
    }

    public void testCreateTape() {
	machine.reset();
	TMConfig config = machine.getTMConfig();
	Assert.assertTrue(config.getLeftOfHead().length == 0 && config.getBelowHead() == '#'
		&& config.getRightOfHead().length == 0);
    }

    public void testResetTape() {
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', '#')));
	machine.setInitialTapeContent("#a#b#".toCharArray());
	machine.reset();
	TMConfig config = machine.getTMConfig();
	Assert.assertTrue(config.getLeftOfHead().length == 0 && config.getBelowHead() == '#'
		&& config.getRightOfHead().length == 0);
    }

    public void testCreateTransitions() {
	machine.reset();
	Assert.assertEquals(new HashSet<Integer>(Arrays.asList(0)), machine.getStates());
    }

    public void testResetTransitions() {
	machine.reset();
	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', '#')));
	machine.addTransition(1, 'a', 2, 'a', Movement.STAY);
	machine.addTransition(2, 'a', 1, 'b', Movement.STAY);
	machine.addTransition(1, 'b', 2, '#', Movement.LEFT);
	machine.reset();
	Assert.assertEquals(new HashSet<Integer>(Arrays.asList(0)), machine.getStates());
    }

}
