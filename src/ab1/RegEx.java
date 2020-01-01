package ab1;

/**
 * 
 * @author Raphael Wigoutschnigg
 * 
 */
public interface RegEx {

    /**
     * Alle Zeichenketten gerader Länge sollen akzeptiert werden
     */
    public String getRegexGeradeLaenge();

    /**
     * Ganze Zahlen ohne führende 0-en und optionalem Vorzeichen (+ oder -)
     */
    public String getRegexGanzeZahlen();

    /**
     * (vereinfachte) Telefonnummer der Form +Längervorwahl Vorwahl Nummer (+ kann
     * auch durch 00 ersetzt werden=. Die Nummer ist maximal 6-8 Stellen lang. Die
     * Vorwahl ist exakt 3 Stelle lang (ohne führende Nullen). Die Ländervorwahl ist
     * 1-3 Stellen lang (ohne führende 0). Zwischen Längervorwahl und Vorwahl sowie
     * Vorwahl und Nummer darf ein Leerzeichen vorhanden sein.
     */
    public String getRegexTelnummer();

    /**
     * Jahresangaben der Form TT.MM.JJJJ (T ... Tag, M ... Monat, J ... Jahr) Tag
     * und Monat können auch nur aus einer Zahl (z.B. 1.1.2012) oder aus zwei Zahlen
     * (zB. 01.01.2012) bestehen. Die Jahrzahl besteht immer aus vier Ziffern (ohne
     * führende Nullen). Es ist bei diesem Beispiel egal, ob Daten akzeptiert
     * werden, die es nicht gibt (z.B. 31.02.2020). Es geht nur um die strukturelle
     * Korrektheit.
     */
    public String getRegexDatum();

    /**
     * Domain-Name ohne Umlaute und Sonderzeichen (vereinfachte Form). Nur
     * Kleinbuchstaben. TLD mit 2 oder 3 Zeichen.
     */
    public String getRegexDomainName();

    /**
     * Email-Adresse (1) Benutzerteil ohne Umlaute beginnend mit einem Buchstaben.
     * Punkte und Bindestriche sowie Buchstaben und Ziffern sind erlaubt (2)
     * Domainteil wie im Bsp getRegexDomainName()
     */
    public String getRegexEmail();

    /**
     * Ein URL, welches die Regeln 1-4 erfüllt. (1) Muss mit http, https oder ftp
     * starten und danach ein :// enthalten. (2) Muss eine gülte Domain besitzen (3)
     * Kann eine Portnummer enthalten (4) Kann Ziffern, Buchstaben, Punkte,
     * Bindestriche und Schrägstriche enthalten
     */
    public String getRegexURL();

    /**
     * Vereinfachen Sie folgende Regex auf maximal 20 Zeichen:
     * [aabcd][abcd][abcd][abcd][abcd][abcd][abcd][abcd][abcd][abcdd]
     */
    public String getRegexVereinfachen1();

    /**
     * Vereinfachen Sie folgende Regex auf maximal 10 Zeichen: [ab][ab][ab]?[ab]?
     */
    public String getRegexVereinfachen2();
}
