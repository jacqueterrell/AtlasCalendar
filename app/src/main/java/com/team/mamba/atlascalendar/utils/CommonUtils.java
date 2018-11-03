package com.team.mamba.atlascalendar.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import java.io.IOException;
import java.io.InputStream;

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

    public static String loadJSONFromAsset(Context context, String jsonFileName) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }


}
