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
    private Integer state = 0;

    @Override
    public void reset() {
        this.currentState = new TMStateConfig();
        this.toStates = new HashMap<>();
        this.writeSymbols = new HashMap<>();
        this.movements = new HashMap<>();
        //this.currentState.setHalt(true);
        // TODO notwendig?
        this.symbols = null;
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
        // Todo Abprüfen auf fehler muss anders gehen?!
        //todo wann ist man im HALTE-Zustand
        if (this.currentState.getHalt()) {
            throw new IllegalArgumentException("Transition not possible!");

            //TODO fehlt noch: uneindeutige Transition, Symbol kann nicht verarbeitet werden
        } else {
            if (!toStates.containsKey(fromState)) {
                toStates.put(fromState, new HashMap<>());
                toStates.get(fromState).put(symbolRead, toState);
                writeSymbols.put(fromState, new HashMap<>());
                writeSymbols.get(fromState).put(symbolRead, symbolWrite);
                movements.put(fromState, new HashMap<>());
                movements.get(fromState).put(symbolRead, movement);
            } else {
                //todo Key existiert aber innere Map erweitern
                Map<Character, Integer> innerToStates = toStates.get(fromState);
                if (!innerToStates.containsKey(symbolRead)) {
                    toStates.get(fromState).put(symbolRead, toState);
                    writeSymbols.get(fromState).put(symbolRead, symbolWrite);
                    movements.get(fromState).put(symbolRead, movement);
                }
            }
            if (fromState == 0) {
                //this.currentState.setHalt(true);
            } else {
                //    this.currentState.setHalt(false);
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
            leftOfHead.add(i, content[i]);
        }
        char belowHead = '#';
        LinkedList<Character> rightOfHead = new LinkedList<>();
        this.currentState = new TMStateConfig(leftOfHead, belowHead, rightOfHead);
    }

    @Override
    public void doNextStep() throws IllegalStateException {

        if (toStates.size() == 0) {
            throw new IllegalStateException("There exists no Transition");
        } else {
            if (currentState.getHalt()) {
                throw new IllegalStateException("Tm is already stopped!");
            } else {
                int nextState = toStates.get(state).get(currentState.getBelowHead());
                char writeSymbol = writeSymbols.get(state).get(currentState.getBelowHead());
                Movement movement = movements.get(state).get(currentState.getBelowHead());
                try {
                    currentState.performMovement(movement);

                    if (writeSymbol != '\0') {
                        // todo erst schreiben, dann bewegen ?? was machen wir hier?
                        switch (movement) {
                            case LEFT:
                                currentState.getRightOfHead().remove(currentState.getRightOfHead().size() - 1);
                                currentState.getRightOfHead().add(writeSymbol);
                                break;

                            case RIGHT:
                                currentState.getLeftOfHead().remove(currentState.getLeftOfHead().size() - 1);
                                currentState.getLeftOfHead().add(writeSymbol);
                                break;

                            case STAY:
                                currentState.setBelowHead(writeSymbol);
                                break;
                        }
                    }
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
        /*
         * internt aktuellen State festhalten = state
         * und den updaten mit den Maps
         * nextState = toStates Get (Akt. Schlüssel fromState, zweite Schlüssel = akt gelesene Zeichen Current State = below Head
         * --> dann weiss ich welcher der Folgezustand, Movement, Writesymbol ist alles auf dieselbe Weise
         * movement machen (currentState -> performMovement), setter BelowHead, performMoevemtn mit try-catch
         * wenn boolean crashed = true
         * */
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
