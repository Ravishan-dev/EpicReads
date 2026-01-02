package com.ravishandev.epicreads.validation;

public class Validator {

    // Email validation regex - RFC 5322 simplified
    public static final String EMAIL_VALIDATION = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    // Password validation: min 8 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special char
    public static final String PASSWORD_VALIDATION = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String MOBILE_VALIDATION = "^(0{1})(7{1})([0|1|2|4|5|6|7|8]{1})([0-9]{7})";
    public static final String VERIFICATION_CODE_VALIDATION = "\\d{6}"; // ^[0-9]{6}$
    public static final String POSTAL_CODE_VALIDATION = "\\d{5}"; // ^[0-9]{6}$
    public static final String IS_INTEGER = "^\\d+$";
    public static final String NON_DIGIT_PATTERN = "\\D+";

}
