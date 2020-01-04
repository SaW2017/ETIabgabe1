package ab1;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TMStateConfig {

    Boolean crashed = false;
    Boolean halt = false;

    // TODO nicht vergessen am Ende in alte Config umzuwandeln
    private LinkedList<Character> leftOfHead;
    private char belowHead;
    // TODO verkehrt herum geordnet
    private LinkedList<Character> rightOfHead;

    public LinkedList<Character> getLeftOfHead() {
        return leftOfHead;
    }

    public void setLeftOfHead(LinkedList<Character> leftOfHead) {
        this.leftOfHead = leftOfHead;
    }

    public char getBelowHead() {
        return belowHead;
    }

    public void setBelowHead(char belowHead) {
        this.belowHead = belowHead;
    }

    public LinkedList<Character> getRightOfHead() {
        return rightOfHead;
    }

    public void setRightOfHead(LinkedList<Character> rightOfHead) {
        this.rightOfHead = rightOfHead;
    }

    public Boolean getCrashed() {
        return crashed;
    }

    public void setCrashed(Boolean crashed) {
        this.crashed = crashed;
    }

    public Boolean getHalt() {
        return halt;
    }

    public void setHalt(Boolean halt) {
        this.halt = halt;
    }

    public TMStateConfig() {
        super();
        this.leftOfHead = new LinkedList<Character>();
        this.belowHead = '#';
        this.rightOfHead = new LinkedList<Character>();
    }

    public TMStateConfig(LinkedList<Character> leftOfHead, char belowHead, LinkedList<Character> rightOfHead) {
        super();
        this.leftOfHead = leftOfHead;
        this.belowHead = belowHead;
        this.rightOfHead = rightOfHead;
    }

    public void performMovement(Movement movement, Character writeSymbol) throws IllegalStateException {
        try {
            switch (movement) {
                case LEFT:
                    if (leftOfHead.size() == 0) {
                        this.crashed = true;
                        this.halt = false;
                        throw new IllegalStateException("You can't go over left border!");
                    } else {
                        this.crashed = false;
                        this.halt = false;
                        belowHead = writeSymbol;
                        this.rightOfHead.add(belowHead);
                        if (getLeftOfHead().size() > 0) {
                            belowHead = this.leftOfHead.remove(leftOfHead.size() - 1);
                        } else {
                            belowHead = '#';
                        }
                    }
                    break;

                case RIGHT:
                    belowHead = writeSymbol;
                    this.leftOfHead.add(belowHead);
                    if (getRightOfHead().size() > 0) {
                        belowHead = this.rightOfHead.remove(rightOfHead.size() - 1);
                    } else {
                        belowHead = '#';
                    }
                    this.crashed = false;
                    this.halt = false;
                    break;

                case STAY:
                    belowHead = writeSymbol;
                    this.halt = false;
                    this.crashed = false;
                    break;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public TMConfig changeConfig(TMStateConfig currentState) {
        if (currentState.getCrashed()) {
            return null;
        } else {
            LinkedList<Character> leftSide = currentState.getLeftOfHead();
            LinkedList<Character> rightSide = currentState.getRightOfHead();
            char[] leftOfHeadNew = new char[leftSide.size()];
            char[] rightOfHeadNew = new char[rightSide.size()];

            for (int i = 0; i < leftOfHeadNew.length; i++) {
                leftOfHeadNew[i] = leftSide.get(i);
            }

            for (int i = 0; i < rightOfHeadNew.length; i++) {
                rightOfHeadNew[i] = rightSide.get(i);
            }

            Set<Character> duplicates = findDuplicates(rightSide);
            // wenn rightSide nur # enthält --> neues char-Array
            if (duplicates.contains('#') && duplicates.size() == 1) {
                rightOfHeadNew = new char[0];
            }

            return new TMConfig(leftOfHeadNew, currentState.getBelowHead(), rightOfHeadNew);
        }
    }

    private Set<Character> findDuplicates(LinkedList<Character> listContainingDuplicates) {

        Set<Character> setToReturn = new HashSet<>();
        Set<Character> set1 = new HashSet<>();

        for (Character actElement : listContainingDuplicates) {
            if (!set1.add(actElement)) {
                setToReturn.add(actElement);
            }
        }
        return setToReturn;
    }
}
