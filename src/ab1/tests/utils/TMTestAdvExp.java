package ab1.tests.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;

import ab1.Movement;
import ab1.TM;
import ab1.TMConfig;
import ab1.tests.utils.TestUtils.CharMachine;
import ab1.tests.utils.TestUtils.CompositeMachine;
import ab1.tests.utils.TestUtils.CopyMachine;
import ab1.tests.utils.TestUtils.LeftMachine;
import ab1.tests.utils.TestUtils.MachineModule;
import ab1.tests.utils.TestUtils.RightMachine;
import ab1.tests.utils.TestUtils.ShiftLeftMachine;

public class TMTestAdvExp {

    private final TM machine;

    public TMTestAdvExp(TM tm) {
	this.machine = tm;
    }

    private Set<Character> expAlphabet;
    private CompositeMachine exp;

    public void setup() {
	expAlphabet = new HashSet<>(Arrays.asList('|', '#'));
	exp = new CompositeMachine();

	RightMachine right1 = new RightMachine();
	CharMachine char1 = new CharMachine('|');
	LeftMachine left1 = new LeftMachine('#');
	LeftMachine left2 = new LeftMachine();

	CharMachine char2 = new CharMachine('#');
	RightMachine right2 = new RightMachine('#');
	RightMachine right3 = new RightMachine('#');
	CopyMachine copy1 = new CopyMachine(expAlphabet);
	ShiftLeftMachine shift1 = new ShiftLeftMachine(expAlphabet);
	ShiftLeftMachine shift2 = new ShiftLeftMachine(expAlphabet);

	RightMachine right4 = new RightMachine('#');
	RightMachine right5 = new RightMachine('#');
	ShiftLeftMachine shift3 = new ShiftLeftMachine(expAlphabet);

	exp.setInitial(right1);
	exp.addTransition(right1, char1);
	exp.addTransition(char1, left1);
	exp.addTransition(left1, left2);
	exp.addTransition(left2, char2, '|');
	exp.addTransition(char2, right2);
	exp.addTransition(right2, right3);
	exp.addTransition(right3, copy1);
	exp.addTransition(copy1, shift1);
	exp.addTransition(shift1, shift2);
	exp.addTransition(shift2, left1);
	exp.addTransition(left2, right4, '#');
	exp.addTransition(right4, right5);
	exp.addTransition(right5, shift3);
    }

    private String doExp(String tape) {
	machine.reset();
	machine.setSymbols(expAlphabet);
	exp.addToMachine(machine);
	machine.setInitialState(exp.getInitialState());
	machine.addTransition(exp.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent(tape.toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	return new String(config.getLeftOfHead());
    }

    public void test1() {
	HashSet<Character> alphabet = new HashSet<>(Arrays.asList('|', '>', '<', '�', '(', '#'));
	MachineModule module = new ShiftLeftMachine(alphabet);
	MachineModule module2 = new ShiftLeftMachine(alphabet);

	machine.reset();
	machine.setSymbols(alphabet);

	module.addToMachine(machine);
	module2.addToMachine(machine);

	machine.setInitialState(module.getInitialState());
	machine.addTransition(module.getHaltState(), '#', module2.getInitialState(), '#', Movement.STAY);
	machine.addTransition(module2.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent("#|>#<(#(�>".toCharArray());

	while (!machine.isHalt())
	    machine.doNextStep();

	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#|><((�>", new String(config.getLeftOfHead()));
    }

    public void test2() {
	// 2^0 = 1
	Assert.assertEquals("#|", doExp("#"));
    }

    public void test3() {
	// 2^1 = 2
	Assert.assertEquals("#||", doExp("#|"));
    }

    public void test4() {
	// 2^2 = 4
	Assert.assertEquals("#||||", doExp("#||"));
    }

    public void test5() {
	// 2^3 = 8
	Assert.assertEquals("#||||||||", doExp("#|||"));
    }

    public void test6() {
	// 2^12
	Assert.assertEquals((1 << 12) + 1, doExp("#||||||||||||").length());
    }
}
