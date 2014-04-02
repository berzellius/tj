package com.tajinsurance.utils;

/**
 * Created by berz on 27.03.14.
 */
public class CodeUtilsImpl implements CodeUtils {

    @Override
    public String getDigitCode(int c) {
        String code = "";
        for(int i = 0; i < c; i++){
            int d = (int) (Math.random()*10);
            code += Integer.valueOf(d).toString();
        }
        return code;
    }
}
