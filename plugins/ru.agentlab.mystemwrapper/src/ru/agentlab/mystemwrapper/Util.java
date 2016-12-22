package ru.agentlab.mystemwrapper;

import java.io.UnsupportedEncodingException;

/**
 * Created by Evgeny on 25.12.2014.
 */
public class Util
{
    public static String convertEncoding(String str)
    {
        if(null == str)
            return null;

        try {
            return new String(str.getBytes("cp1251"), "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String revertEncoding(String str)
    {
        if(null == str)
            return null;

        try {
            return new String(str.getBytes("utf8"), "cp1251");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
