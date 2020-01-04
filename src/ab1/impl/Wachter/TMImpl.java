package ab1.impl.Wachter;

import ab1.Movement;
import ab1.TM;
import ab1.TMConfig;
import ab1.TMStateConfig;

import java.util.*;

public class TMImpl implements TM {

    // alle Zeichen die auf dem Band stehen können
    Set<Character> symbols;
    Set<Integer> states;
    TMStateConfig currentState = new TMStateConfig();

    // (State)Transitions:  Map<fromState, Map<SymbolRead, ToState>>
    private Map<Integer, Map<Character, Integer>> toStates;
    private Map<Integer, Map<Character, Character>> writeSymbols;
    private Map<Integer, Map<Character, Movement>> movements;
    private Integer state;

    @Override
    public void reset() {
        this.currentState = new TMStateConfig();
        this.toStates = new HashMap<>();
        this.writeSymbols = new HashMap<>();
        this.movements = new HashMap<>();
        //this.currentState.setHalt(true);
        this.symbols = null;
        this.states = null;
    }

    @Override
    public void setSymbols(Set<Character> symbols) throws IllegalArgumentException {
        if (!symbols.contains('#')) {
            throw new IllegalArgumentException();
        } else {
            this.symbols = symbols;
        }
    }

    @Override
    public Set<Character> getSymbols() {
        return this.symbols;
    }

    @Override
    public void addTransition(int fromState, char symbolRead, int toState, char symbolWrite, Movement movement) throws IllegalArgumentException {
        if (this.currentState.getHalt() || fromState == 0) {
            throw new IllegalArgumentException("Transition not possible! TM is already in \'Halt\'!");
        } else if (!symbols.contains(symbolRead) || !symbols.contains(symbolWrite)) {
            throw new IllegalArgumentException("Symbol isn`t an allowed Symbol!");
        }

        // else if (toState < 0 || fromState < 0) {   Angabe states 0 - ....
        //    throw new IllegalArgumentException("Transitions can only be positive Numbers!");
        // }
        else {
            if (!toStates.containsKey(fromState)) {
                toStates.put(fromState, new HashMap<>());
                toStates.get(fromState).put(symbolRead, toState);
                writeSymbols.put(fromState, new HashMap<>());
                writeSymbols.get(fromState).put(symbolRead, symbolWrite);
                movements.put(fromState, new HashMap<>());
                movements.get(fromState).put(symbolRead, movement);
            } else {
                // Key existiert --> innere Map erweitern
                Map<Character, Integer> innerToStates = toStates.get(fromState);
                if (!innerToStates.containsKey(symbolRead)) {
                    toStates.get(fromState).put(symbolRead, toState);
                    writeSymbols.get(fromState).put(symbolRead, symbolWrite);
                    movements.get(fromState).put(symbolRead, movement);
                } else {
                    throw new IllegalArgumentException("Transition is not distinct!");
                }
            }

            // fill Set with all States
            for (Integer actualFromState : toStates.keySet()) {
                if (this.states != null) {
                    if (!this.states.contains(actualFromState)) {
                        this.states.add(actualFromState);
                    }
                    // falls es Zustände gibt aus die man nicht mehr raus kommt, müssen die auch in das Set
                    for (Integer actualToState : toStates.get(actualFromState).values()) {
                        if (!this.states.contains(actualToState)) {
                            this.states.add(actualToState);
                        }
                    }
                } else {
                    this.states = new HashSet<Integer>();
                    this.states.add(actualFromState);
                }
            }
        }
    }

    @Override
    public Set<Integer> getStates() {
        if (!this.states.contains(0)) {
            this.states.add(0);
        }
        return this.states;
    }

    @Override
    public void setInitialState(int state) {
        this.state = state;
    }

    @Override
    public void setInitialTapeContent(char[] content) {
        LinkedList<Character> leftOfHead = new LinkedList<>();
        for (int i = 0; i < content.length; i++) {
            // Nichterlaubtes Symbol
            if (!symbols.contains(content[i])) {
                currentState.setCrashed(true);
            } else {
                leftOfHead.add(i, content[i]);
            }
        }
        char belowHead = '#';
        LinkedList<Character> rightOfHead = new LinkedList<>();
        this.currentState = new TMStateConfig(leftOfHead, belowHead, rightOfHead);
    }

    @Override
    public void doNextStep() throws IllegalStateException {
//todo movement = null exceptioN!
        if (toStates.size() == 0) {
            currentState.setCrashed(true);
            throw new IllegalStateException("There exists no Transition");
        } else {
            if (currentState.getHalt() || this.state == 0) {
                currentState.setCrashed(true);
                throw new IllegalStateException("Tm is already stopped!");
            } else {
                if (toStates.get(state).get(currentState.getBelowHead()) == null) {
                    currentState.setCrashed(true);
                    throw new IllegalStateException("There exists no Transition");
                } else {
                    int nextState = toStates.get(state).get(currentState.getBelowHead());
                    char writeSymbol = writeSymbols.get(state).get(currentState.getBelowHead());
                    Movement movement = movements.get(state).get(currentState.getBelowHead());
                    try {
                        // perform movement & write Symbol
                        currentState.performMovement(movement, writeSymbol);
                        this.state = nextState;
                        if (nextState == 0) {
                            currentState.setHalt(true);
                        }
                    } catch (IllegalStateException e) {
                        currentState.setCrashed(true);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public boolean isHalt() {
        return this.currentState.getHalt();
    }

    @Override
    public boolean isCrashed() {
        return this.currentState.getCrashed();
    }

    @Override
    public TMConfig getTMConfig() {
        return currentState.changeConfig(currentState);
    }
}
