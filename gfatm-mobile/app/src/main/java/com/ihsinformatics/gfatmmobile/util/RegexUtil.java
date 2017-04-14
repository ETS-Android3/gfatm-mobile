/*
Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */
package com.ihsinformatics.gfatmmobile.util;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Regular Expression provider class to verify a valid expression (e.g. valid email address, name, etc.)
 * Characters:
 * <p>
 * /**
 *
 * @author owais.hussain@ihsinformatics.com
 */
public class RegexUtil {
    public static final String numericPattern = "^[0-9]+";
    public static final String alphaPattern = "^[A-Za-z. ]+";
    public static final String alphaNumPattern = "^[A-Za-z0-9]+";
    public static final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String contactNoPattern = "^[\\+|0][0-9\\s-]+";
    public static final String datePattern = "(0[1-9]|[1-9]|[12][0-9]|3[01])[-/](0[1-9]|1[012]|[1-9])[-/](19|20)\\d{2}";
    public static final String timePattern_am_pm = "(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";
    public static final String sqlDate = "^([0-9]{2,4})-([0-1][0-9])-([0-3][0-9])";
    public static final String sqlTime = "^([0-2][0-9]):([0-5][0-9]):([0-5][0-9])?$";
    public static final String sqlDateTime = "^([0-9]{2,4})-([0-1][0-9])-([0-3][0-9])(?:( [0-2][0-9]):([0-5][0-9]):([0-5][0-9]))?$";
    public static final String timePattern_24 = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    public static final String nicPattern = "^[0-9]{5,5}[-.]{0,1}[0-9]{7,7}[-.]{0,1}[0-9]";
    public static final String urlPattern = "^(((ht|f)tp(s?))\\:\\/\\/)?(localhost|([0-9]{1,2}|1[0-9]{2}|2[0-4][0-9]|25[0-5]|.){3}([0-9]{1,2}|1[0-9]{2}|2[0-4][0-9]|25[0-5])|(www.|[a-zA-Z].)[a-zA-Z0-9\\-\\.]+\\.(com|edu|gov|mil|net|org|biz|info|name|museum|us|ca|uk|pk|co|))(\\:[0-9]+)*(\\/($|[a-zA-Z0-9\\.\\,\\;\\?\\\\'\\\\\\\\\\\\+&amp;%\\\\$#\\\\=~_\\\\-]+))*$";
    public static final String smsPattern = "[A-Z0-9]{2,2}[0-9]{9,9} [0-3][0-9](JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)20[1-3][0-9] [YN]";
    public static final String ernsNumberPattern = "[0-9]{3,4}/[0-9]{2,2}";
    public static final String testIDPattern = "^{0,1}[0-9]{7,7}[-.]{0,1}[0-9]";
    public static final String mobileNumPattern = "[0][3][0-9]{9}";
    public static final String floatingPointPattern = "^[0-9]*.[0-9]{0,1}$";
    public static final String floatingPointPatternForThreeDecimalPlaces = "^[0-9]*.[0-9]{0,3}$";
    public static final String floatingPointPatternForTwoDecimalPlaces = "^[0-9]*.[0-9]{0,2}$";
    public static final String addressPattern ="^[-A-Za-z0-9.#&():;,'\" \\/]+";
    public static final String otherPattern ="^[-A-Za-z0-9.#&():;,'\"+%*=!| \\/]+";

    public static final int idLength = 7;
    public static final int mobileNumberLength = 11;
    public static final int defaultEditTextLength = 50;
    public static final int landlineNumberLength = 10;
    public static final InputFilter OTHER_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.equals("")) { // for backspace
                return source;
            }
            if (source.toString().matches(otherPattern)) {
                return source;
            }
            return "";

        }
    };






































































































































    public static final InputFilter FLOAT_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if(source.equals("")){ // for backspace
                return source;
            }
            if(source.toString().matches("^[.0-9]+")){
                return source;
            }
            return "";

        }
    };
    public static final InputFilter ALPHANUMERIC_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.equals("")) { // for backspace
                return source;
            }
            if (source.toString().matches(alphaNumPattern)) {
                return source;
            }
            return "";

        }
    };
    public static final InputFilter ADDRESS_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.equals("")) {
                return source;
            }
            if (source.toString().matches(addressPattern)) {
                return source;
            }
            return "";

        }
    };
    public static final InputFilter NIC_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals("")) { // for backspace
                return source;
            }
            if (source.toString().matches(nicPattern)) {
                return source;
            }
            return "";
        }
    };
    public static final InputFilter ALPHA_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.equals("")) { // for backspace
                return source;
            }
            if (source.toString().matches(alphaPattern)) {
                return source;
            }
            return "";

        }
    };
    public static final InputFilter NUMERIC_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.equals("")) { // for backspace
                return source;
            }
            if (source.toString().matches(numericPattern)) {
                return source;
            }
            return "";

        }
    };
    public static final InputFilter ID_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.equals("")) { // for backspace
                return source;
            }
            if (source.toString().matches("^[-A-Za-z0-9]+")) {
                return source;
            }
            return "";

        }
    };
    public static final InputFilter ERNS_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.equals("")) {
                return source;
            }
            if (source.toString().matches("^[/0-9]+")) {
                return source;
            }
            return "";

        }
    };
    private static final String ipAddressPattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    /**
     * Checks if given input is a valid number
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isNumeric(String string, boolean floating) {
        try {
            if (floating)
                return string.matches(floatingPointPattern);
            return string.matches(numericPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid word
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isWord(String string) {
        try {
            return string.matches(alphaPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidErnsNumber(String string) {
        try {
            return string.matches(ernsNumberPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid alphanumeric string
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isAlphaNumeric(String string) {
        try {
            return string.matches(alphaNumPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid IP
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isIpAddress(String string) {
        try {
            return string.matches(ipAddressPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid contact number (e.g. Mobile no, fax)
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isContactNumber(String string) {
        try {
            if (string.length() != mobileNumberLength)
                return false;
            return string.matches(contactNoPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid email address
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isEmailAddress(String string) {
        try {
            return string.matches(emailPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid date
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isValidDate(String string) {
        try {
            return string.matches(datePattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid time
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isValidTime(String string, boolean am_pm) {
        try {
            if (am_pm)
                return string.matches(timePattern_am_pm);
            return string.matches(timePattern_24);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid national ID number
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isValidNIC(String string) {
        try {
            return string.matches(nicPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid URL
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isValidURL(String string) {
        try {
            return string.matches(urlPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is valid according to acceptable SMS format
     *
     * @param string
     * @return true/false
     */
    public static boolean isValidSMS(String string) {
        try {
            return string.matches(smsPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is under acceptable length of Id
     *
     * @param id
     * @return true/false
     */
    public static boolean isValidId(String id) {
        boolean isValid = true;
        isValid = id.length() == idLength;
        id = id.replaceAll("\\W", "");
        // Validate Luhn check digit
        // TODO: Replace with isValidCheckDigit() method
        if (isValid) {
            String idWithoutCheckdigit = id.substring(0, id.length() - 1);
            char idCheckdigit = id.charAt(id.length() - 1);
            Boolean isDigit = (idCheckdigit >= '0' && idCheckdigit <= '9');
            if (!isDigit) return false;
            String validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVYWXZabcdefghijklmnopqrstuvwxyz_";
            idWithoutCheckdigit = idWithoutCheckdigit.trim();
            int sum = 0;
            for (int i = 0; i < idWithoutCheckdigit.length(); i++) {
                char ch = idWithoutCheckdigit.charAt(idWithoutCheckdigit
                        .length() - i - 1);
                if (validChars.indexOf(ch) == -1)
                    isValid = false;
                int digit = (int) ch - 48;
                int weight;
                if (i % 2 == 0) {
                    weight = (2 * digit) - (int) (digit / 5) * 9;
                } else {
                    weight = digit;
                }
                sum += weight;
            }
            sum = Math.abs(sum) + 10;
            int checkDigit = (10 - (sum % 10)) % 10;
            isValid = checkDigit == Integer.parseInt(String
                    .valueOf(idCheckdigit));

        }

        return isValid;
    }

    /**
     * Checks if given string (string + hyphen + check digit) has a valid Luhn
     * check digit
     *
     * @param str
     * @return true/false
     */
    public static boolean isValidCheckDigit(String str) {
        boolean isValid = true;
        str = str.replace("-", "");
        String idWithoutCheckdigit = str.substring(0, str.length() - 1);
        char idCheckdigit = str.charAt(str.length() - 1);
        String validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVYWXZ_";
        idWithoutCheckdigit = idWithoutCheckdigit.trim();
        int sum = 0;
        for (int i = 0; i < idWithoutCheckdigit.length(); i++) {
            char ch = idWithoutCheckdigit.charAt(idWithoutCheckdigit.length()
                    - i - 1);
            if (validChars.indexOf(ch) == -1)
                isValid = false;
            int digit = (int) ch - 48;
            int weight;
            if (i % 2 == 0) {
                weight = (2 * digit) - (int) (digit / 5) * 9;
            } else {
                weight = digit;
            }
            sum += weight;
        }
        sum = Math.abs(sum) + 10;
        int checkDigit = (10 - (sum % 10)) % 10;
        isValid = checkDigit == Integer.parseInt(String.valueOf(idCheckdigit));
        return isValid;
    }

    /**
     * Checks if given input is a valid number
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isNumericForTwoDecimalPlaces(String string, boolean floating) {
        try {
            if (floating)
                return string.matches(floatingPointPatternForTwoDecimalPlaces);
            return string.matches(numericPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid number
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isNumericForThreeDecimalPlaces(String string, boolean floating) {
        try {
            if (floating)
                return string.matches(floatingPointPatternForThreeDecimalPlaces);
            return string.matches(numericPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isLandlineNumber(String string) {
        try {
            if (!(string.length() == mobileNumberLength || string.length() == landlineNumberLength))
                return false;
            return string.matches(contactNoPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given input is a valid test id
     *
     * @param string input String
     * @return true/false
     */
    public static boolean isCorrectTestID(String string) {
        try {
            return string.matches(testIDPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isMobileNumber(String string) {
        try {
            if (string.length() != mobileNumberLength)
                return false;
            return string.matches(mobileNumPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}