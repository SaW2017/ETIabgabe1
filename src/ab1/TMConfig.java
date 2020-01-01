package ab1;

/**
 * @author Raphael Wigoutschnigg
 */
public class TMConfig {
    /**
     * Alle Zeichen auf dem Band vom Beginn des Bandes bis zur Stelle links vom Kopf
     * (darf leer sein).
     */
    private char[] leftOfHead;
    /**
     * Das Zeichen, das sich aktuell unter dem Kopf befindet.
     */
    private char belowHead;
    /**
     * Alle Zeichen von der Stelle rechts vom Kopf bis zum letzten von '#'
     * verschiedenen Zeichen (darf leer sein).
     */
    private char[] rightOfHead;

    public char[] getLeftOfHead() {
        return leftOfHead;
    }

    public char getBelowHead() {
        return belowHead;
    }

    public char[] getRightOfHead() {
        return rightOfHead;
    }

    public TMConfig(char[] leftOfHead, char belowHead, char[] rightOfHead) {
        super();
        this.leftOfHead = leftOfHead;
        this.belowHead = belowHead;
        this.rightOfHead = rightOfHead;
    }

}
