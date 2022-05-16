package com.fashion.forlempopoli.Utilities;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternClass {


    // Function to remove non-alphanumeric
    // characters from string
    public static String removeNonAlphanumeric(String str) {
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

    public static boolean isValidPan(String pan) {
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        Matcher matcher = pattern.matcher(pan);
        // Check if pattern matches
        if (matcher.matches()) {
            Log.i("Matching", "Yes");

        }
        return matcher.matches();
    }

    public static float discountPercentage(float offerPrice, float sellingPrice) {
        // Calculating discount
        float discount = offerPrice - sellingPrice;

        // Calculating discount percentage
        float disPercent = (discount / offerPrice) * 100;

//        float x = Math.abs(disPercent); // For removing -(negative) before percantage

        return Math.abs(disPercent);
    }

    public static float savingAmount(float offerPrice, float sellingPrice) {
        // Calculating discount
        float discount = sellingPrice - offerPrice;

        // Calculating discount percentage
        //    float disPercent = (discount / offerPrice) * 100;

//        float x = Math.abs(disPercent); // For removing -(negative) before percantage

        return Math.abs(discount);
    }

    public static class DecimalDigitsInputFilter implements InputFilter {
        private final Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    public static class DecimalDigitsInputFilter2 implements InputFilter {

        int decimalDigits;

        /**
         * Constructor.
         *
         * @param decimalDigits maximum decimal digits
         */
        public DecimalDigitsInputFilter2(int decimalDigits) {
            this.decimalDigits = decimalDigits;
        }

        @Override
        public CharSequence filter(CharSequence source,
                                   int start,
                                   int end,
                                   Spanned dest,
                                   int dstart,
                                   int dend) {


            int dotPos = -1;
            int len = dest.length();
            for (int i = 0; i < len; i++) {
                char c = dest.charAt(i);
                if (c == '.' || c == ',') {
                    dotPos = i;
                    break;
                }
            }
            if (dotPos >= 0) {

                // protects against many dots
                if (source.equals(".") || source.equals(","))
                {
                    return "";
                }
                // if the text is entered before the dot
                if (dend <= dotPos) {
                    return null;
                }
                if (len - dotPos > decimalDigits) {
                    return "";
                }
            }

            return null;
        }

    }

}
