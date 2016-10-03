package com.dankideacentral.dic;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private static Properties props;
    public PropertiesReader(String fileName, AssetManager am) {
        props = new Properties();
        try {
            InputStream inputStream = am.open(fileName);
            props.load(inputStream);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.toString());
        }
    }
    public static String getProperty (String key) {
        return props.getProperty(key);
    }
}