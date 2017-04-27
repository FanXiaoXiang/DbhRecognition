package com.bjfu.androidlib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by 11827 on 2017/3/3.
 */

public class SerializableUtils {
    public static final void saveObject(String path, Object saveObject) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        File f = new File(path);
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(saveObject);
        } catch (Exception e) {

        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null)
                    fos.close();
            } catch (IOException e) {

            }
        }
    }

    public static final Object restoreObject(String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object object = null;
        File f = new File(path);
        if (!f.exists()) return null;
        try {
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
            return object;
        } catch (Exception e) {

        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
}
