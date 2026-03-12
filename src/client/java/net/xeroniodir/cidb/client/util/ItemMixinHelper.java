package net.xeroniodir.cidb.client.util;

import org.apache.commons.lang3.math.Fraction;

public class ItemMixinHelper {
    public static String TextDurabilityText(int maxDamage,int damage){
        String text;
        int trueDurability = maxDamage - damage;
        text = Integer.toString(trueDurability);
        if(text.length()>=10){
            if(text.length()==10 && text.charAt(1) != 0 ){
                text = text.charAt(0) + "." + text.charAt(1) + "B";
            }
            else if(text.length()==10 && text.charAt(1) == 0 ){
                text = text.charAt(0) + "B";
            }
            else{
                text = trueDurability / 1000000000 + "B";
            }
        }
        else if (text.length()>=7) {
            if(text.length()==7 && text.charAt(1) != 0){
                text = text.charAt(0) + "." + text.charAt(1) + "m";
            }
            else if(text.length()==7 && text.charAt(1) == 0 ){
                text = text.charAt(0) + "m";
            }
            else{
                text = trueDurability / 1000000 + "m";
            }
        }
        else if (text.length()>=4) {
            if(text.length()==4 && text.charAt(1) != 0){
                text = text.charAt(0) + "." + text.charAt(1) + "k";
            }
            else if(text.length()==4 && text.charAt(1) == 0){
                text = text.charAt(0) + "k";
            }
            else{
                text = trueDurability / 1000 + "k";
            }
        }
        return text;
    }

    public static int getNumeratorFromFraction(Fraction ihateyou, int max){ /// Gets
        int numerator = ihateyou.getNumerator();
        int denominator = ihateyou.getDenominator();
        int moh = max / denominator;
        if(denominator != max){
            return numerator * moh;
        }
        else return numerator;
    }
}
