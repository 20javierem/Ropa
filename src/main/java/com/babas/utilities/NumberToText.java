package com.babas.utilities;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class NumberToText {
    private static final DecimalFormat format = new DecimalFormat("##############.##");;
    private static final String[] UNITS = {
            "",
            "uno ",
            "dos ",
            "tres ",
            "cuatro ",
            "cinco ",
            "seis ",
            "siete ",
            "ocho ",
            "nueve "};
    private static final String[] TENS = {
            "diez ",
            "once ",
            "doce ",
            "trece ",
            "catorce ",
            "quince ",
            "dieciseis ",
            "diecisiete ",
            "dieciocho ",
            "diecinueve ",
            "veinti",
            "treinta ",
            "cuarenta ",
            "cincuenta ",
            "sesenta ",
            "setenta ",
            "ochenta ",
            "noventa "};
    private static final String[] HUNDREDS = {
            "",
            "ciento ",
            "doscientos ",
            "trecientos ",
            "cuatrocientos ",
            "quinientos ",
            "seiscientos ",
            "setecientos ",
            "ochocientos ",
            "novecientos "};

    public static String toText(Double number) {
        number = Double.valueOf(format.format(number));
        String literal;
        String parte_decimal;

        if (Pattern.matches("\\d{1,9}.\\d{1,2}", number.toString())) {
            String[] Num = number.toString().split("\\.");
            int dec= Integer.parseInt(Num[1])*10;
            parte_decimal = "con " + dec + "/100 Soles.";
            if (Integer.parseInt(Num[0]) == 0) {
                literal = "cero ";
            } else if (Integer.parseInt(Num[0]) > 999999) {
                literal = getMillions(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 999) {
                literal = getMiles(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 99) {
                literal = getHundreds(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 9) {
                literal = getTens(Num[0]);
            } else {
                literal = getUnits(Num[0]);
            }
            return (literal + parte_decimal);
        } else {
            return null;
        }
    }
    private static String getUnits(String numero) {
        String num = numero.substring(numero.length() - 1);
        int n = Integer.parseInt(num);
        return n==1 && numero.length()==1?"un ":UNITS[Integer.parseInt(num)];
    }
    private static String getTens(String number) {
        int n = Integer.parseInt(number);
        if (n > 19) {
            String u = getUnits(number);
            n = Integer.parseInt(number.substring(0, 1));
            if (u.isEmpty()) {
                return n==2?"veinte ": TENS[n + 8];
            } else {
                return n==2?TENS[n + 8] + u:TENS[n + 8] + "y " + u;
            }
        } else {
            return n<=9?n==1?"uno ":getUnits(number):TENS[n - 10];
        }
    }
    private static String getHundreds(String num) {
        if (Integer.parseInt(num) > 99) {
            if (Integer.parseInt(num) == 100) {
                return "cien ";
            } else {
                return HUNDREDS[Integer.parseInt(num.substring(0, 1))] + getTens(num.substring(1));
            }
        } else {
            return getTens(Integer.parseInt(num) + "");
        }
    }
    private static String getMiles(String number) {
        String c = number.substring(number.length() - 3);
        String m = number.substring(0, number.length() - 3);
        String n;
        if (Integer.parseInt(m) > 0) {
            n = getHundreds(m);
            return (Integer.parseInt(m)==1?"":n) + "mil " + getHundreds(c);
        } else {
            return getHundreds(c);
        }
    }
    private static String getMillions(String number) {
        String miles = number.substring(number.length() - 6);
        String millions = number.substring(0, number.length() - 6);
        return (millions.length()>1?getHundreds(millions) + "millones ":getUnits(millions) + "mill??n ") + getMiles(miles);
    }
}
