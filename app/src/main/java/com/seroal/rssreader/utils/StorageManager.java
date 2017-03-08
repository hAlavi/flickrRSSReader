package com.seroal.rssreader.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rouhalavi on 06/03/2017.
 */

public class StorageManager implements StorageManageable{
    private static StorageManager smInstance = new StorageManager();

    private StorageManager(){
    }

    public static StorageManager getInstance(){
        return smInstance;
    }

    public File getOutputMediaFile(String packName){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + packName
                + "/Files");


        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


    public boolean storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile("utils");
        if (pictureFile == null) {

            return false;
        }
        try {

            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;

        } catch (IOException e) {

            return false;
        }
    }

}
