package com.dankideacentral.dic;

import android.content.Context;
import android.content.res.AssetManager;

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
        } catch (IOException e) {}
    }
    public static String getProperty (String key) {
        return props.getProperty(key);
    }
}