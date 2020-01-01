package ab1.tests.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ab1.Movement;
import ab1.TM;

public class TestUtils {

    private static class Transition {
	private int fromState;
	private char readChar;
	private int toState;
	private char writeChar;
	private Movement movement;
	private boolean wholeAlphabet = false;

	private Transition(int fromState, char readChar, int toState, char writeChar, Movement movement,
		boolean wholeAlphabet) {
	    this.fromState = fromState;
	    this.readChar = readChar;
	    this.toState = toState;
	    this.writeChar = writeChar;
	    this.movement = movement;
	    this.wholeAlphabet = wholeAlphabet;
	}

	public String toString() {
	    return "(" + fromState + "," + readChar + ") -> (" + toState + "," + writeChar + "," + movement.toString()
		    + ")";
	}
    }

    public static abstract class MachineModule {
	protected List<Transition> transitions = new LinkedList<>();
	protected int initialState;

	private int haltState;

	protected abstract void initialize();

	private boolean initialized = false;

	protected void init() {
	    if (!initialized) {
		initialize();
		initialized = true;
	    }
	}

	public void addToMachine(TM machine) {
	    init();

	    int maxState = 0;
	    for (int i : machine.getStates())
		if (i > maxState)
		    maxState = i;
	    haltState = maxState + 1;

	    Set<Character> alphabet = machine.getSymbols();

	    for (Transition t : transitions) {
		if (!t.wholeAlphabet) {
		    machine.addTransition(t.fromState + haltState, t.readChar, t.toState + haltState, t.writeChar,
			    t.movement);
		} else {
		    for (char c : alphabet) {
			try {
			    machine.addTransition(t.fromState + haltState, c, t.toState + haltState,
				    t.readChar == t.writeChar ? c : t.writeChar, t.movement);
			} catch (IllegalArgumentException ex) {
			}
		    }
		}
	    }
	}

	public int getInitialState() {
	    init();
	    return initialState + haltState;
	}

	public int getHaltState() {
	    init();
	    return haltState;
	}
    }

    public static class CompositeMachine extends MachineModule {
	private List<MachineTransition> machineTransitions = new LinkedList<>();
	private MachineModule initialModule;

	public CompositeMachine() {
	}

	public CompositeMachine(CompositeMachine copy) {
	    this.machineTransitions.addAll(copy.machineTransitions);
	    this.initialModule = copy.initialModule;
	}

	public void addTransition(MachineModule a, MachineModule b, char characterRead) {
	    MachineTransition mt = new MachineTransition();
	    mt.a = a;
	    mt.b = b;
	    mt.read = characterRead;
	    machineTransitions.add(mt);
	}

	public void addTransition(MachineModule a, MachineModule b) {
	    MachineTransition mt = new MachineTransition();
	    mt.a = a;
	    mt.b = b;
	    mt.all = true;
	    machineTransitions.add(mt);
	}

	public void setInitial(MachineModule initial) {
	    this.initialModule = initial;
	}

	@Override
	protected void initialize() {
	    HashSet<MachineModule> modules = new HashSet<>();
	    HashMap<MachineModule, List<MachineTransition>> moduleTransitions = new HashMap<>();
	    for (MachineTransition mt : machineTransitions) {
		modules.add(mt.a);
		modules.add(mt.b);

		List<MachineTransition> mmt = moduleTransitions.get(mt.a);
		if (mmt == null) {
		    mmt = new LinkedList<>();
		    moduleTransitions.put(mt.a, mmt);
		}
		mmt.add(mt);
	    }

	    int offset = 1;
	    HashMap<MachineModule, Integer> offsets = new HashMap<>();
	    for (MachineModule mm : modules) {
		offsets.put(mm, offset);
		mm.init();

		int maxState = 0;
		for (Transition t : mm.transitions) {
		    if (t.fromState > maxState)
			maxState = t.fromState;
		    if (t.toState > maxState)
			maxState = t.toState;
		}
		offset += maxState + 1;
	    }

	    for (MachineModule mm : modules) {
		int off = offsets.get(mm);

		for (Transition t : mm.transitions)
		    transitions.add(new Transition(t.fromState + off, t.readChar, t.toState + off, t.writeChar,
			    t.movement, t.wholeAlphabet));

		List<MachineTransition> mts = moduleTransitions.get(mm);
		if (mts != null) {
		    for (MachineTransition mt : mts)
			transitions.add(new Transition(off, mt.read, offsets.get(mt.b) + mt.b.initialState, mt.read,
				Movement.STAY, mt.all));
		}

		transitions.add(new Transition(off, '#', 0, '#', Movement.STAY, true));
	    }

	    initialState = initialModule.initialState + offsets.get(initialModule);
	}

	private static class MachineTransition {
	    private MachineModule a, b;
	    private char read;
	    private boolean all;
	}
    }

    public static abstract class MoveMachine extends MachineModule {
	private Character[] repeatUntil = null;

	public MoveMachine() {
	}

	public MoveMachine(char repeatUntil) {
	    this.repeatUntil = new Character[] { repeatUntil };
	}

	public MoveMachine(Character... repeatUntil) {
	    this.repeatUntil = repeatUntil;
	}

	protected abstract Movement getDirection();

	@Override
	protected void initialize() {
	    if (repeatUntil != null) {
		initialState = 2;
		transitions.add(new Transition(2, '#', 1, '#', getDirection(), true));
		for (char c : repeatUntil)
		    transitions.add(new Transition(1, c, 0, c, Movement.STAY, false));
		transitions.add(new Transition(1, '#', 1, '#', getDirection(), true));
	    } else {
		initialState = 1;
		transitions.add(new Transition(1, '#', 0, '#', getDirection(), true));
	    }
	}
    }

    public static class LeftMachine extends MoveMachine {
	public LeftMachine() {
	}

	public LeftMachine(char repeatUntil) {
	    super(repeatUntil);
	}

	public LeftMachine(Character... repeatUntil) {
	    super(repeatUntil);
	}

	@Override
	protected Movement getDirection() {
	    return Movement.LEFT;
	}
    }

    public static class RightMachine extends MoveMachine {
	public RightMachine() {
	}

	public RightMachine(char repeatUntil) {
	    super(repeatUntil);
	}

	public RightMachine(Character... repeatUntil) {
	    super(repeatUntil);
	}

	@Override
	protected Movement getDirection() {
	    return Movement.RIGHT;
	}
    }

    public static class CharMachine extends MachineModule {
	private char c;

	public CharMachine(char c) {
	    this.c = c;
	}

	@Override
	protected void initialize() {
	    initialState = 1;
	    transitions.add(new Transition(1, '\0', 0, c, Movement.STAY, true));
	}
    }

    public static class ShiftLeftMachine extends CompositeMachine {
	public ShiftLeftMachine(Set<Character> alphabet) {
	    LeftMachine left1 = new LeftMachine('#');
	    LeftMachine left2 = new LeftMachine();
	    RightMachine right1 = new RightMachine();
	    RightMachine right2 = new RightMachine();

	    addTransition(left1, left2);
	    addTransition(left2, right1);
	    addTransition(right1, right2);

	    for (char c : alphabet) {
		if (c == '#')
		    continue;
		LeftMachine leftInner = new LeftMachine();
		CharMachine charInner = new CharMachine(c);
		addTransition(right2, leftInner, c);
		addTransition(leftInner, charInner);
		addTransition(charInner, right1);
	    }

	    LeftMachine left3 = new LeftMachine();
	    CharMachine char1 = new CharMachine('#');

	    addTransition(right2, left3, '#');
	    addTransition(left3, char1);

	    setInitial(left1);
	}
    }

    public static class ShiftRightMachine extends CompositeMachine {
	public ShiftRightMachine(Set<Character> alphabet) {
	    RightMachine right1 = new RightMachine();
	    LeftMachine left1 = new LeftMachine();
	    LeftMachine left2 = new LeftMachine();

	    addTransition(right1, left1);
	    addTransition(left1, left2);

	    for (char c : alphabet) {
		if (c == '#')
		    continue;
		RightMachine rightInner = new RightMachine();
		CharMachine charInner = new CharMachine(c);
		addTransition(left2, rightInner, c);
		addTransition(rightInner, charInner);
		addTransition(charInner, left1);
	    }

	    RightMachine right2 = new RightMachine();
	    CharMachine char1 = new CharMachine('#');
	    RightMachine right3 = new RightMachine('#');

	    addTransition(left2, right2, '#');
	    addTransition(right2, char1);
	    addTransition(char1, right3);

	    setInitial(right1);
	}
    }

    public static class CopyMachine extends CompositeMachine {
	public CopyMachine(Set<Character> alphabet) {
	    LeftMachine left1 = new LeftMachine('#');
	    RightMachine right1 = new RightMachine();
	    RightMachine right2 = new RightMachine('#');

	    addTransition(left1, right1);
	    addTransition(right1, right2, '#');

	    for (char c : alphabet) {
		if (c == '#')
		    continue;
		CharMachine innerChar1 = new CharMachine('#');
		RightMachine innerRight1 = new RightMachine('#');
		RightMachine innerRight2 = new RightMachine('#');
		CharMachine innerChar2 = new CharMachine(c);
		LeftMachine innerLeft1 = new LeftMachine('#');
		LeftMachine innerLeft2 = new LeftMachine('#');
		CharMachine innerChar3 = new CharMachine(c);

		addTransition(right1, innerChar1, c);
		addTransition(innerChar1, innerRight1);
		addTransition(innerRight1, innerRight2);
		addTransition(innerRight2, innerChar2);
		addTransition(innerChar2, innerLeft1);
		addTransition(innerLeft1, innerLeft2);
		addTransition(innerLeft2, innerChar3);
		addTransition(innerChar3, right1);
	    }

	    setInitial(left1);
	}
    }
}
