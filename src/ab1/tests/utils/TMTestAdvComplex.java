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
import ab1.tests.utils.TestUtils.LeftMachine;
import ab1.tests.utils.TestUtils.RightMachine;
import ab1.tests.utils.TestUtils.ShiftLeftMachine;
import ab1.tests.utils.TestUtils.ShiftRightMachine;

public class TMTestAdvComplex {

    private final TM machine;

    public TMTestAdvComplex(TM tm) {
	this.machine = tm;
    }

    private Set<Character> alphabet;
    private CompositeMachine normalize, denormalize, increment, decrement, sub2, add, sub, search, full;

    public void setup() {
	alphabet = new HashSet<>(
		Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '-', '*', '/', ';', '#'));

	normalize = new CompositeMachine();
	LeftMachine left1 = new LeftMachine('#');
	RightMachine right1 = new RightMachine();
	CharMachine char1 = new CharMachine('#');
	RightMachine right2 = new RightMachine('#');
	ShiftLeftMachine shift1 = new ShiftLeftMachine(alphabet);
	RightMachine right3 = new RightMachine(';', '#');
	CharMachine char2 = new CharMachine('#');
	normalize.setInitial(left1);
	normalize.addTransition(left1, right1);
	normalize.addTransition(right1, char1, '0');
	normalize.addTransition(char1, right2);
	normalize.addTransition(right2, shift1);
	normalize.addTransition(shift1, left1);
	normalize.addTransition(right1, right1, ';');
	normalize.addTransition(right1, char2, '#');
	normalize.addTransition(right1, right3);
	normalize.addTransition(right3, right1, ';');

	denormalize = new CompositeMachine();
	left1 = new LeftMachine();
	LeftMachine left2 = new LeftMachine(';', '#');
	right1 = new RightMachine('#');
	CharMachine char11 = new CharMachine('#');
	RightMachine right11 = new RightMachine('#');
	ShiftRightMachine shift11 = new ShiftRightMachine(alphabet);
	LeftMachine left11 = new LeftMachine('#');
	CharMachine char12 = new CharMachine('0');
	LeftMachine left12 = new LeftMachine();
	CharMachine char13 = new CharMachine('#');
	CharMachine char21 = new CharMachine('#');
	RightMachine right21 = new RightMachine('#');
	ShiftRightMachine shift21 = new ShiftRightMachine(alphabet);
	LeftMachine left21 = new LeftMachine('#');
	CharMachine char22 = new CharMachine('0');
	LeftMachine left22 = new LeftMachine();
	CharMachine char23 = new CharMachine(';');
	denormalize.setInitial(left1);
	denormalize.addTransition(left1, char11, '#');
	denormalize.addTransition(left1, char21, ';');
	denormalize.addTransition(left1, left2);
	denormalize.addTransition(left2, left1, ';');
	denormalize.addTransition(left2, right1, '#');
	denormalize.addTransition(char11, right11);
	denormalize.addTransition(right11, shift11);
	denormalize.addTransition(shift11, left11);
	denormalize.addTransition(left11, char12);
	denormalize.addTransition(char12, left12);
	denormalize.addTransition(left12, char13);
	denormalize.addTransition(char13, right1);
	denormalize.addTransition(char21, right21);
	denormalize.addTransition(right21, shift21);
	denormalize.addTransition(shift21, left21);
	denormalize.addTransition(left21, char22);
	denormalize.addTransition(char22, left22);
	denormalize.addTransition(left22, char23);
	denormalize.addTransition(char23, left1);

	increment = new CompositeMachine();
	left1 = new LeftMachine();
	right1 = new RightMachine('#', ';');
	for (char c : "012345678".toCharArray()) {
	    CharMachine innerChar = new CharMachine((char) (c + 1));
	    increment.addTransition(left1, innerChar, c);
	    increment.addTransition(innerChar, right1);
	}
	char1 = new CharMachine('0');
	increment.addTransition(left1, char1, '9');
	increment.addTransition(char1, left1);
	for (char c : ";#".toCharArray()) {
	    CharMachine innerChar1 = new CharMachine('#');
	    RightMachine innerRight = new RightMachine('#');
	    ShiftRightMachine innerShift = new ShiftRightMachine(alphabet);
	    LeftMachine innerLeft1 = new LeftMachine('#');
	    CharMachine innerChar2 = new CharMachine('1');
	    LeftMachine innerLeft2 = new LeftMachine();
	    CharMachine innerChar3 = new CharMachine(c);
	    increment.addTransition(left1, innerChar1, c);
	    increment.addTransition(innerChar1, innerRight);
	    increment.addTransition(innerRight, innerShift);
	    increment.addTransition(innerShift, innerLeft1);
	    increment.addTransition(innerLeft1, innerChar2);
	    increment.addTransition(innerChar2, innerLeft2);
	    increment.addTransition(innerLeft2, innerChar3);
	    increment.addTransition(innerChar3, right1);
	}
	increment.setInitial(left1);

	decrement = new CompositeMachine();
	left1 = new LeftMachine();
	right1 = new RightMachine('#', ';');
	decrement.addTransition(left1, right1, '#');
	decrement.addTransition(left1, right1, ';');
	for (char c : "23456789".toCharArray()) {
	    CharMachine innerChar = new CharMachine((char) (c - 1));
	    decrement.addTransition(left1, innerChar, c);
	    decrement.addTransition(innerChar, right1);
	}
	char1 = new CharMachine('0');
	left2 = new LeftMachine('#', ';');
	decrement.addTransition(left1, char1, '1');
	decrement.addTransition(char1, left2);
	for (char c : ";#".toCharArray()) {
	    CharMachine innerChar1 = new CharMachine('#');
	    RightMachine innerRight = new RightMachine('#');
	    CompositeMachine innerNormalize = new CompositeMachine(normalize);
	    LeftMachine innerLeft1 = new LeftMachine('#');
	    CharMachine innerChar2 = new CharMachine(c);
	    decrement.addTransition(left2, innerChar1, c);
	    decrement.addTransition(innerChar1, innerRight);
	    decrement.addTransition(innerRight, innerNormalize);
	    decrement.addTransition(innerNormalize, innerLeft1);
	    decrement.addTransition(innerLeft1, innerChar2);
	    decrement.addTransition(innerChar2, right1);
	}
	char2 = new CharMachine('9');
	decrement.addTransition(left1, char2, '0');
	decrement.addTransition(char2, left1);
	decrement.setInitial(left1);

	sub2 = new CompositeMachine();
	left1 = new LeftMachine();
	char1 = new CharMachine('#');
	right1 = new RightMachine();
	char2 = new CharMachine('#');
	right2 = new RightMachine('#');
	shift1 = new ShiftLeftMachine(alphabet);
	left2 = new LeftMachine('#');
	CharMachine char3 = new CharMachine(';');
	sub2.addTransition(left1, char1, ';');
	sub2.addTransition(char1, right1);
	sub2.addTransition(right1, char2);
	sub2.addTransition(char2, right2);
	sub2.addTransition(right2, shift1);
	sub2.addTransition(shift1, left2);
	sub2.addTransition(left2, char3);
	right3 = new RightMachine();
	CompositeMachine dec1 = new CompositeMachine(decrement);
	LeftMachine left4 = new LeftMachine(';');
	LeftMachine left5 = new LeftMachine();
	RightMachine right4 = new RightMachine();
	RightMachine right5 = new RightMachine(';');
	LeftMachine left6 = new LeftMachine();
	sub2.addTransition(left1, right3);
	sub2.addTransition(right3, dec1);
	sub2.addTransition(dec1, left4);
	sub2.addTransition(left4, left5);
	sub2.addTransition(left5, right4, ';');
	sub2.addTransition(left5, right4, '#');
	sub2.addTransition(right4, right5);
	sub2.addTransition(right5, left6);
	RightMachine right6 = new RightMachine();
	CompositeMachine dec2 = new CompositeMachine(decrement);
	RightMachine right7 = new RightMachine(';');
	sub2.addTransition(left5, right6);
	sub2.addTransition(right6, dec2);
	sub2.addTransition(dec2, right7);
	sub2.addTransition(right7, left1);
	CharMachine char4 = new CharMachine('-');
	RightMachine right8 = new RightMachine();
	sub2.addTransition(left6, char4, ';');
	sub2.addTransition(char4, right8);
	RightMachine right9 = new RightMachine();
	CompositeMachine dec3 = new CompositeMachine(decrement);
	sub2.addTransition(left6, right9);
	sub2.addTransition(right9, dec3);
	sub2.addTransition(dec3, left6);
	sub2.setInitial(left1);

	add = new CompositeMachine();
	left1 = new LeftMachine();
	char1 = new CharMachine('#');
	right1 = new RightMachine();
	char2 = new CharMachine('#');
	right2 = new RightMachine('#');
	shift1 = new ShiftLeftMachine(alphabet);
	left2 = new LeftMachine('#');
	char3 = new CharMachine(';');
	add.addTransition(left1, char1, ';');
	add.addTransition(char1, right1);
	add.addTransition(right1, char2);
	add.addTransition(char2, right2);
	add.addTransition(right2, shift1);
	add.addTransition(shift1, left2);
	add.addTransition(left2, char3);
	right3 = new RightMachine();
	dec1 = new CompositeMachine(decrement);
	LeftMachine left3 = new LeftMachine(';');
	CompositeMachine inc1 = new CompositeMachine(increment);
	right4 = new RightMachine(';');
	add.addTransition(left1, right3);
	add.addTransition(right3, dec1);
	add.addTransition(dec1, left3);
	add.addTransition(left3, inc1);
	add.addTransition(inc1, right4);
	add.addTransition(right4, left1);
	add.setInitial(left1);

	sub = new CompositeMachine();
	CompositeMachine mon1 = new CompositeMachine(sub2);
	left1 = new LeftMachine();
	char1 = new CharMachine('#');
	right1 = new RightMachine();
	char2 = new CharMachine('#');
	right2 = new RightMachine('#');
	shift1 = new ShiftLeftMachine(alphabet);
	left2 = new LeftMachine('#');
	char3 = new CharMachine(';');
	sub.addTransition(mon1, left1);
	sub.addTransition(left1, char1, '-');
	sub.addTransition(char1, right1);
	sub.addTransition(right1, char2);
	sub.addTransition(char2, right2);
	sub.addTransition(right2, shift1);
	sub.addTransition(shift1, left2);
	sub.addTransition(left2, char3);
	right3 = new RightMachine();
	sub.addTransition(left1, right3);
	sub.setInitial(mon1);

	search = new CompositeMachine();
	left1 = new LeftMachine('#');
	right1 = new RightMachine('#', '+', '-', '*', '/');
	search.addTransition(left1, right1);
	search.setInitial(left1);

	full = new CompositeMachine();
	CompositeMachine norm1 = new CompositeMachine(normalize);
	CompositeMachine search1 = new CompositeMachine(search);
	CompositeMachine denorm1 = new CompositeMachine(denormalize);
	full.addTransition(norm1, search1);
	full.addTransition(search1, denorm1, '#');
	left1 = new LeftMachine();
	CompositeMachine add1 = new CompositeMachine(add);
	full.addTransition(search1, left1, '+');
	full.addTransition(left1, add1);
	left2 = new LeftMachine();
	CompositeMachine sub1 = new CompositeMachine(sub);
	full.addTransition(search1, left2, '-');
	full.addTransition(left2, sub1);
	char1 = new CharMachine('#');
	right1 = new RightMachine();
	char2 = new CharMachine('#');
	right2 = new RightMachine('#');
	shift1 = new ShiftLeftMachine(alphabet);
	ShiftLeftMachine shift2 = new ShiftLeftMachine(alphabet);
	full.addTransition(add1, char1);
	full.addTransition(sub1, char1);
	full.addTransition(char1, right1);
	full.addTransition(right1, char2);
	full.addTransition(char2, right2);
	full.addTransition(right2, shift1);
	full.addTransition(shift1, shift2);
	full.addTransition(shift2, search1);
	full.setInitial(norm1);
    }

    public void testNormalize() {
	machine.reset();
	machine.setSymbols(alphabet);
	normalize.addToMachine(machine);
	machine.setInitialState(normalize.getInitialState());
	machine.addTransition(normalize.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent("#0;5;0;+;-".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#;5;;+;-", new String(config.getLeftOfHead()));
    }

    public void testDenormalize() {
	machine.reset();
	machine.setSymbols(alphabet);
	denormalize.addToMachine(machine);
	machine.setInitialState(denormalize.getInitialState());
	machine.addTransition(denormalize.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent("#;5;;+;-".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#0;5;0;+;-", new String(config.getLeftOfHead()));
    }

    public void testIncrement1() {
	machine.reset();
	machine.setSymbols(alphabet);
	increment.addToMachine(machine);
	machine.setInitialState(increment.getInitialState());
	machine.addTransition(increment.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent("#;5".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#;6", new String(config.getLeftOfHead()));
    }

    public void testIncrement2() {
	machine.reset();
	machine.setSymbols(alphabet);
	increment.addToMachine(machine);
	machine.setInitialState(increment.getInitialState());
	machine.addTransition(increment.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent("#99".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#100", new String(config.getLeftOfHead()));
    }

    public void testDecrement1() {
	machine.reset();
	machine.setSymbols(alphabet);
	decrement.addToMachine(machine);
	machine.setInitialState(decrement.getInitialState());
	machine.addTransition(decrement.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent("#;5".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#;4", new String(config.getLeftOfHead()));
    }

    public void testDecrement2() {
	machine.reset();
	machine.setSymbols(alphabet);
	decrement.addToMachine(machine);
	machine.setInitialState(decrement.getInitialState());
	machine.addTransition(decrement.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent("#1".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#", new String(config.getLeftOfHead()));
    }

    public void testDecrement3() {
	machine.reset();
	machine.setSymbols(alphabet);
	decrement.addToMachine(machine);
	machine.setInitialState(decrement.getInitialState());
	machine.addTransition(decrement.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent("#;;".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#;;", new String(config.getLeftOfHead()));
    }

    public void testSub21() {
	machine.reset();
	machine.setSymbols(alphabet);
	LeftMachine lm = new LeftMachine();
	lm.addToMachine(machine);
	sub2.addToMachine(machine);
	machine.setInitialState(lm.getInitialState());
	machine.addTransition(lm.getHaltState(), ';', sub2.getInitialState(), ';', Movement.STAY);
	machine.addTransition(sub2.getHaltState(), ';', 0, ';', Movement.STAY);
	machine.setInitialTapeContent("#;5;3;".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#;2", new String(config.getLeftOfHead()));
    }

    public void testSub22() {
	machine.reset();
	machine.setSymbols(alphabet);
	LeftMachine lm = new LeftMachine();
	lm.addToMachine(machine);
	sub2.addToMachine(machine);
	machine.setInitialState(lm.getInitialState());
	machine.addTransition(lm.getHaltState(), ';', sub2.getInitialState(), ';', Movement.STAY);
	machine.addTransition(sub2.getHaltState(), ';', 0, ';', Movement.STAY);
	machine.setInitialTapeContent("#;;".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#", new String(config.getLeftOfHead()));
    }

    public void testSub23() {
	machine.reset();
	machine.setSymbols(alphabet);
	LeftMachine lm = new LeftMachine();
	lm.addToMachine(machine);
	sub2.addToMachine(machine);
	machine.setInitialState(lm.getInitialState());
	machine.addTransition(lm.getHaltState(), ';', sub2.getInitialState(), ';', Movement.STAY);
	machine.addTransition(sub2.getHaltState(), ';', 0, ';', Movement.STAY);
	machine.setInitialTapeContent("#9;10;".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#-", new String(config.getLeftOfHead()));
    }

    public void testAdd() {
	machine.reset();
	machine.setSymbols(alphabet);
	LeftMachine lm = new LeftMachine();
	lm.addToMachine(machine);
	add.addToMachine(machine);
	machine.setInitialState(lm.getInitialState());
	machine.addTransition(lm.getHaltState(), ';', add.getInitialState(), ';', Movement.STAY);
	machine.addTransition(add.getHaltState(), ';', 0, ';', Movement.STAY);
	machine.setInitialTapeContent("#;4;6;".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#;10", new String(config.getLeftOfHead()));
    }

    public void testSub1() {
	machine.reset();
	machine.setSymbols(alphabet);
	LeftMachine lm = new LeftMachine();
	lm.addToMachine(machine);
	sub.addToMachine(machine);
	machine.setInitialState(lm.getInitialState());
	machine.addTransition(lm.getHaltState(), ';', sub.getInitialState(), ';', Movement.STAY);
	machine.addTransition(sub.getHaltState(), ';', 0, ';', Movement.STAY);
	machine.setInitialTapeContent("#;4;6;".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#;", new String(config.getLeftOfHead()));
    }

    public void testSub2() {
	machine.reset();
	machine.setSymbols(alphabet);
	LeftMachine lm = new LeftMachine();
	lm.addToMachine(machine);
	sub.addToMachine(machine);
	machine.setInitialState(lm.getInitialState());
	machine.addTransition(lm.getHaltState(), ';', sub.getInitialState(), ';', Movement.STAY);
	machine.addTransition(sub.getHaltState(), ';', 0, ';', Movement.STAY);
	machine.setInitialTapeContent("#56;6;".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#50", new String(config.getLeftOfHead()));
    }

    public void testFull1() {
	machine.reset();
	machine.setSymbols(alphabet);
	full.addToMachine(machine);
	machine.setInitialState(full.getInitialState());
	machine.addTransition(full.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent("#56;6;+;17;-".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#45", new String(config.getLeftOfHead()));
    }

    public void testFull2() {
	machine.reset();
	machine.setSymbols(alphabet);
	full.addToMachine(machine);
	machine.setInitialState(full.getInitialState());
	machine.addTransition(full.getHaltState(), '#', 0, '#', Movement.STAY);
	machine.setInitialTapeContent("#4872;200;6739;+;3800;100;+;-;-;1832;-;5".toCharArray());
	while (!machine.isHalt())
	    machine.doNextStep();
	TMConfig config = machine.getTMConfig();
	Assert.assertEquals("#1;5", new String(config.getLeftOfHead()));
    }
}
