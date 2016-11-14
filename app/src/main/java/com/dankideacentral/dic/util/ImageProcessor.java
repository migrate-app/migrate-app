package com.dankideacentral.dic.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by srowhani on 11/13/16.
 */

public class ImageProcessor {
    /**
     * Produces a mutable Bitmap fro a given request url.
     * @param imageUrl
     * @return
     */
    public static Bitmap bitmapFromUrl (String imageUrl) {
        URL url;
        try {
            url = new URL(imageUrl);
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                // returns a mutable copy of the bitmap
                Bitmap image = BitmapFactory.decodeStream(is).copy(Bitmap.Config.ARGB_8888, true);
                return image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
