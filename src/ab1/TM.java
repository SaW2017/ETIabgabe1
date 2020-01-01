package ab1;

import java.util.Set;

/**
 * Interface zur Implementierung einer Turingmaschine
 * 
 * @author Raphael Wigoutschnigg
 * 
 */
public interface TM {
    /**
     * Setzt die Turingmaschine zurueck und loescht alle internen Daten. Dies
     * umfasst Ueberfuehrungen, Zustaende und Bandinhalt. Ausgangszustand ist ganz
     * links am leeren Band im Haltezustand.
     */
    public void reset();

    /**
     * Gibt die Menge der von der Turingmaschine verarbeiteten Symbole an. Es ist
     * sicherzustellen, dass das Zeichen '#' auf jeden Fall zu den Symbolen gehoert.
     * 
     * @param symbols
     *                    Menge der Symbole
     * @throws IllegalArgumentException
     *                                      wenn die Menge nicht das Leerzeichen '#'
     *                                      enthält.
     */
    public void setSymbols(Set<Character> symbols) throws IllegalArgumentException;

    /**
     * Liefert die Menge der verarbeiteten Symbole.
     * 
     * @return Menge der Symbole
     */
    public Set<Character> getSymbols();

    /**
     * Fuegt eine Ueberfuehrung hinzu. fromState und toStrate sind ganze Zahlen (0,
     * 1, ...). 0 steht hierbei fuer den Haltezustand. Wird eine Zustandsnummer
     * verwendet, die bisher nicht verwendet wurde, ist die Zustandsmenge intern
     * entsprechend zu erweitern. Die optionale Bewegung wird nach der
     * Schreiboperation ausgeführt.
     * 
     * @param fromState
     *                        Ausgangszustand
     * @param symbolRead
     *                        gelesenes Symbol
     * @param toState
     *                        Folgezustand
     * @param symbolWrite
     *                        geschriebenes Symbol, wenn null keines
     * @param movement
     *                        Bewegung des Schreib-/Lesekopfes, wenn null keine
     * 
     * @throws IllegalArgumentException
     *                                      Wird geworfen, wenn ausgehend vom
     *                                      Haltezustand ein Zeichen gelesen wird,
     *                                      wenn eine Transition nicht eindeutig ist
     *                                      (fromState, symbolRead) oder wenn ein
     *                                      Symbol nicht verarbeitet werden kann.
     */
    public void addTransition(int fromState, char symbolRead, int toState, char symbolWrite, Movement movement)
	    throws IllegalArgumentException;

    /**
     * Liefert die Menge der Zustaende, welche durch addTransition erzeugt wurden.
     * Der Haltezustand (0) ist in jedem Fall Teil dieser Menge.
     * 
     * @return
     */
    public Set<Integer> getStates();

    /**
     * Setzt den initialen Zustand der Maschine.
     * 
     * @param state
     *                  Startzutand
     */
    public void setInitialState(int state);

    /**
     * Setzt den initialen Inhalt des Bandes und setzt den Schreib-/Lesekopf hinter
     * das letzte Zeichen des Inhaltes. "#abc" liefert somit den Inhalt "#abc#...."
     * wobei der Schreib-/Lesekopf unter dem zweiten # steht.
     * 
     * @param content
     */
    public void setInitialTapeContent(char[] content);

    /**
     * Fuehrt einen Abarbeitungsschritt der Turingmaschine aus.
     * 
     * @throws IllegalStateException
     *                                   Wird geworfen, wenn die Maschine bereits im
     *                                   Haltezustand ist, keine passende Transition
     *                                   vorhanden ist oder die Maschine links in
     *                                   die "Wand" laeuft.
     */
    public void doNextStep() throws IllegalStateException;

    /**
     * Liefert true, wenn sich die Maschine im Haltezustand befindet.
     * 
     * @return
     */
    public boolean isHalt();

    /**
     * Liefert true, wenn die Maschine haengt (Bandende ueberschritten oder
     * unbekannter Zustand).
     */
    public boolean isCrashed();

    /**
     * Liefert die Konfiguration der Maschine. Ist isCrashed() == true, wird null
     * zurück geliefert.
     * 
     * @return Konfiguration der Maschine.
     */
    public TMConfig getTMConfig();
}
