package com.example.forlempopoli.Utilities;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternClass {


    // Function to remove non-alphanumeric
    // characters from string
    public static String removeNonAlphanumeric(String str)
    {
        // replace the given string
        // with empty string
        // except the pattern "[^a-zA-Z0-9]"
        str = str.replaceAll(
                "[^a-zA-Z0-9\\s]", "");

        // return string
        return str;
    }

    public static boolean isValidEmail(String email) {
        //Regular Expression
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String regex = "^(.+)@(.+)$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        Log.d("emaildPattern 1", String.valueOf(matcher.matches()));
        Log.d("emaildPattern 2", String.valueOf(email.matches(checkEmail)));
//        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
//        return matcher.matches();
        return email.matches(checkEmail);
    }

    public static boolean isValidPhone(String number) {
        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        String chechNumber = "(0|91)?[6-9][0-9]{9}";
        return number.matches(chechNumber);
    }

    public static boolean isValidPassword(String password) {
        String checkPassword = "^" +
                //"(?=.*[0-9])" + // at least 1 digit
                //"(?=.*[a-z])" + // at least 1 lower case character
                //"(?=.*[A-Z])" + //  at least 1 Upper Case Character
//                "(?=.*[a-zA-Z])" + // Any letter
                //"(?=.*[@#$%^&+=])" + //at least 1 speacial character
                "(?=\\S+$)" + //no white space
                ".{4,}" +  // at least 4 characters
                "$";

        return password.matches(checkPassword);
    }

    public static boolean isValidPan(String pan){
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        Matcher matcher = pattern.matcher(pan);
        // Check if pattern matches
        if (matcher.matches()) {
            Log.i("Matching","Yes");

        }
        return matcher.matches();
    }

    public static float discountPercentage(float offerPrice,float sellingPrice){
        // Calculating discount
        float discount = offerPrice - sellingPrice;

        // Calculating discount percentage
        float disPercent = (discount / offerPrice) * 100;

//        float x = Math.abs(disPercent); // For removing -(negative) before percantage

        return Math.abs(disPercent);
    }

    public static float savingAmount(float offerPrice,float sellingPrice){
        // Calculating discount
        float discount =sellingPrice - offerPrice;

        // Calculating discount percentage
    //    float disPercent = (discount / offerPrice) * 100;

//        float x = Math.abs(disPercent); // For removing -(negative) before percantage

        return Math.abs(discount);
    }
}
