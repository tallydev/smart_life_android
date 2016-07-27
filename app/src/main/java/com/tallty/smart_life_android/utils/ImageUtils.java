package com.tallty.smart_life_android.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kang on 16/7/27.
 * 图片处理工具类
 */

public class ImageUtils {


    /**
     * 保存Image的方法。
     * 需要注意的地方有：1、判断是否有SD卡；2、判断SD卡存储空间是否够用。
     * @param fileName
     * @param bitmap
     * @throws IOException
     */
    public static String savaBitmap(String fileName, Bitmap bitmap){
        String imagePath = null;

        FileOutputStream fos = null;

        try {
            if(bitmap == null){
                return null;
            }
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//            String path = getStorageDirectory;
            File folderFile = new File(path);
            if(!folderFile.exists()){
                folderFile.mkdir();
            }
            File file = new File(path + File.separator + fileName);
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            imagePath =  file.getAbsolutePath();

        }catch (IOException E){

        }finally {
            if(fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imagePath;
    }
}
