package com.journear.app.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class IsValid {
    public static final String VALID_USERNAME_REGEX = "$\\w+[\\w\\d]{3,}";
    // password requirements - a digit, minimum 8 characters, either an uppercase or a special character
    public static final String VALID_PASSWORD_REGEX = "^(?=.*([A-Z]|[\\$%#@!\\^&\\*~`\\-\\+_\\=\\?<>]))(?=.*\\d)[A-Za-z\\d\\$%#@!\\^&\\*~`\\-\\+_\\=\\?<>]{8,}$";
    // below regex comes from here - https://stackoverflow.com/a/21456918/1750620
//            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    // 1) Begins with 0
    // 2) Then contains 8 and any number between 0-9
    // 3) Then contains 8 digits
    public static final String VALID_MOBILE_REGEX = "[0][7-9][0-9]{8}";

    public static boolean userName(String s) {
        if (StringUtils.isEmpty(s) || s.length() < 4 || !StringUtils.isEmpty(s.replaceAll(VALID_USERNAME_REGEX, "")))
            return false;
        return true;
    }

    public static boolean password(String s) {
        if (StringUtils.isEmpty(s) || s.length() < 8 || !s.matches(VALID_PASSWORD_REGEX))
            return false;
        return true;
    }

    public static boolean email(String s) {
        return EmailValidator.getInstance().isValid(s);
    }

    public static boolean Mobile(String s) {

        if (StringUtils.isEmpty(s) || s.length() < 10 || !StringUtils.isEmpty(s.replaceAll(VALID_MOBILE_REGEX, "")))
            return false;
        return true;
    }

    final static String DATE_FORMAT = "dd-MM-yyyy";

    public static boolean DoB(String date)
    {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
