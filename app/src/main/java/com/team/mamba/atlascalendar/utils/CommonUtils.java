package com.team.mamba.atlascalendar.utils;

import android.telephony.PhoneNumberUtils;
import android.util.Patterns;

public class CommonUtils {


    public static boolean isPhoneValid(String phoneNumber) {

        return phoneNumber.length() >= 6 && Patterns.PHONE.matcher(phoneNumber).matches()  && isPhoneValidLibPhone(phoneNumber);

    }

    public static boolean isEmailValid(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }


    private static boolean isPhoneValidLibPhone(String phone){

//        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//        boolean isValid = false;
//
//        try {
//
//            PhoneNumber phoneNumber = phoneUtil.parse(phone, "US");
//             isValid = phoneUtil.isValidNumber(phoneNumber);
//
//        } catch (NumberParseException e){
//
//            Logger.e(e.getLocalizedMessage());
//        }
//
//        return isValid;

        return PhoneNumberUtils.isGlobalPhoneNumber(phone);

    }

}
