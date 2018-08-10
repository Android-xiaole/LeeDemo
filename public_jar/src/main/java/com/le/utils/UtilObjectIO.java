package com.le.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by sahara on 2016/4/12.
 */
public class UtilObjectIO {

    /**
     * 将泛型参数对象写成文件，用于存储
     * @param t
     * @param strs(可变参数，字符串数组)
     * @param <T>
     * @return true|false
     */
    public static <T extends Serializable> boolean writeObject(T t, String path, String... strs){
        String str = "";
        for (String s:strs) {
            str = str + "/" + s;
            path = path+"/"+s;
            if (!UtilFile.isHaveFile(path)){//如果创建文件夹都失败了 那就直接跳过,失败的原因可能是权限问题
                return false;
            }
        }
        File file = new File(path+"/"+t.getClass().getSimpleName()+".txt");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(t);
            oos.close();
            MLog.e("UtilObjectIO:写入" + str +"/"+ t.getClass().getSimpleName() + "成功！");
            return true;
        } catch (IOException e) {
            MLog.e("UtilObjectIO error:写入异常"+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据类名读取相应的缓存文件，
     * @param clss
     * @param strs（可变参数，字符串数组）
     * @param <T>
     * @return
     */
    public static <T extends Serializable> T readObject(Class<T> clss, String path , String... strs){
        String str = "";
        for (String s:strs) {
            str = str+"/"+s;
            path = path+"/"+s;
            if (!UtilFile.isHaveFile(path)){//如果创建文件夹失败了 直接跳过
                return null;
            }
        }
        File file = new File(path+"/"+clss.getSimpleName()+".txt");
        T t = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            try {
                t = (T) ois.readObject();
                MLog.e("UtilObjectIO:读取"+str+"/"+clss.getSimpleName()+"成功！");
                ois.close();
            } catch (ClassNotFoundException e) {
                MLog.e("UtilObjectIO error:"+str+"没有读到缓存文件"+e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            MLog.e("UtilObjectIO error:读取异常"+e.getMessage());
            e.printStackTrace();
        }
        return  t;
    }

}
