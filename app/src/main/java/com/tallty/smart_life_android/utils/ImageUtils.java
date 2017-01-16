package com.tallty.smart_life_android.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
     * 返回image file
     * 需要注意的地方有：1、判断是否有SD卡；2、判断SD卡存储空间是否够用。
     * @param fileName
     * @param bitmap
     * @throws IOException
     */
    public static File saveBitmapToFile(String fileName, Bitmap bitmap){
        FileOutputStream fos = null;
        File file = null;

        try {
            if(bitmap == null){
                return null;
            }
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File folderFile = new File(path);
            if(!folderFile.exists()){
                folderFile.mkdir();
            }
            file = new File(path + File.separator + fileName);
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
        }catch (IOException ignored){

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
        return file;
    }

    /**
     * 修改 inSampleSize
     * @param inSampleSize
     * @return
     */
    public static BitmapFactory.Options getBitmapOption(int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }
}
