package com.seroal.rssreader.utils;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by rouhalavi on 08/03/2017.
 */

public interface StorageManageable {

    public File getOutputMediaFile(String packname);
    public boolean storeImage(Bitmap image);
}
