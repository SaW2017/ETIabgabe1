package ab1.impl.FreislichWachter;

import ab1.RegEx;

public class RegExImpl implements RegEx {
    // https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html

    @Override
    public String getRegexGeradeLaenge() {
        // beliebig oft Gruppe von zwei beliebigen Chars
        return "(..)*";
    }

    @Override
    public String getRegexGanzeZahlen() {
        // +0/-0 unerwünscht
        // +/- o bis 1-Mal
        // 0 oder Zahlen 1-9 konkateniert mit 0-9 beliebig oft
        return "(\\+(?!0)|\\-(?!0)){0,1}(0|[1-9][0-9]*)";
    }

    @Override
    public String getRegexTelnummer() {
        // + oder 00
        // Zahl 1-9 konkat 0-9 (Länge: 0 bis 2)
        // Leerzeichen (0 oder 1-Mal)
        // Zahl 1-9 konkat 0-9 (Länge:2)
        // Leerzeichen (0 oder 1-Mal)
        // Zahlen 0-9 (Länge:6-8)
        return "(\\+|00)[1-9][0-9]{0,2}(\\s{0,1})[1-9][0-9]{2}(\\s{0,1})[0-9]{6,8}";
    }

    @Override
    public String getRegexDatum() {
        // 31 Tage:
        // 0 (0 oder 1-Mal) konkat Zahl 1-9
        // oder Zahl 1,2 konkat Zahl 0-9
        // oder 30 / 31
        // .
        // 12 Monate:
        // 0 (0 oder 1-Mal) konkat Zahl 1-9
        // oder 10, 11, 12
        // .
        // Jahre: 1000 - 9999
        // Zahl 1-9 konkat Zahl 0-9 (Länge 3)
        return "(0?[1-9]|[1,2]\\d|3[0,1])\\.(0?[1-9]|1[0-2])\\.[1-9][0-9]{3}";
    }

    @Override
    public String getRegexDomainName() {
        // Kleinbuchstaben a-z (Länge mind. 1). das ganze mind. 1x
        // tld: a-z (länge 2 oder 3)
        return "([a-z]+\\.)+[a-z]{2,3}";
    }

    @Override
    public String getRegexEmail() {
        // (?i ) = Case-insensitive
        // ein Buchstabe a-z
        // a-z, ., -, 0-9 erlaubt * beliebig oft
        // domainteil- siehe oben
        return "(?i)[a-z]([a-z.-0-9])*@([a-z]+\\.)+[a-z]{2,3}";
    }
//TODO noch zu machen
    @Override
    public String getRegexURL() {
        // Beginnt mit http://, https://, ftp://
        // Buchstabe/Wort mit darauffolgendem Punkt
        // Top-Level-Domain
        // Port(doppelpunkt & nur Zahlen)
        // Schrägstrich mit Pfad
        // Abschliessender Schrägstrich
        return "(https?|ftp)://([a-z]+.)+[a-z]{2,3}(:[0-9&&[^a-z]]+)?(/[-.a-z]*)*/*";
    }

    @Override
    public String getRegexVereinfachen1() {
        //return "[aabcd]{1}[a-d]{8}[abcdd]{1}";
        // Zeichenfolge aus Buchstaben a-d mit Länge 10
        return "[a-d]{10}";
    }

    @Override
    public String getRegexVereinfachen2() {
        // a oder b (Länge mind. 2 . max. 4)
        return "[ab]{2,4}";
    }
}
