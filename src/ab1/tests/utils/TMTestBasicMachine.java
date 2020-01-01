package ab1.tests.utils;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;

import ab1.Movement;
import ab1.TM;
import ab1.TMConfig;

public class TMTestBasicMachine {

    private TM machine;

    public TMTestBasicMachine(TM tm) {
	machine = tm;
    }

    private void setup() {
	machine.reset();

	machine.setSymbols(new HashSet<Character>(Arrays.asList('a', '#')));
	machine.addTransition(3, '#', 1, '#', Movement.LEFT);
	machine.addTransition(1, 'a', 2, '#', Movement.STAY);
	machine.addTransition(2, '#', 1, '#', Movement.LEFT);
	machine.addTransition(1, '#', 0, '#', Movement.STAY);
	machine.setInitialState(3);
	machine.setInitialTapeContent("#aaaaa".toCharArray());

	while (!machine.isHalt())
	    machine.doNextStep();
    }

    public void testLeft() {
	setup();

	TMConfig config = machine.getTMConfig();
	Assert.assertArrayEquals(config.getLeftOfHead(), new char[0]);
    }

    public void testHead() {
	setup();

	TMConfig config = machine.getTMConfig();
	Assert.assertEquals(config.getBelowHead(), '#');
    }

    public void testRight() {
	setup();

	TMConfig config = machine.getTMConfig();
	Assert.assertArrayEquals(config.getRightOfHead(), new char[0]);
    }

}
