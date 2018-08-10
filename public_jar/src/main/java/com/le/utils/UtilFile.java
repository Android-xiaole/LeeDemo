package com.le.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


public class UtilFile {
	/**
	 * 判断SD是否可用 如果可用返回根路径
	 * @return SD根路径/null
	 */
	public static String isSdcardExist() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} 
		return null;
	}

	/**
	 * path为文件夹 那就生成一个文件夹 但是可能生成失败 比如权限问题
	 * path为文件 保证父文件夹存在
	 * @param path
	 * @return: boolean 是否存在此文件(如里是文件夹则为true)
	 */
	public static boolean isHaveFile(String path) {
		MLog.i("检查文件路径是否存在");
		if (path == null) {
			return false;
		}
		File file = new File(path);
		if (file.isFile()) {// 是文件
			if (file.exists()) {
				return true;// 存在此文件
			}
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				if (parentFile.mkdirs()){
					MLog.e("生成文件路径:"+parentFile.getAbsolutePath()+"成功！");
				}else{
					MLog.e("文件路径:"+parentFile.getAbsolutePath()+"生成失败！");
				}
			}
			return false;
		} else {// 是文件夹
			if (!file.exists()) {
				if (file.mkdirs()){
					MLog.e("生成文件路径:"+path+"成功！");
				}else{
					MLog.e("文件路径:"+path+"生成失败！");
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * 复制文件
	 * 
	 * @param from
	 * @param to
	 */
	public static void copyFile(File from, File to) {
		if (null == from || !from.exists()) {
			return;
		}
		if (null == to) {
			return;
		}
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			is = new FileInputStream(from);
			if (!to.exists()) {
				to.createNewFile();
			}
			os = new FileOutputStream(to);
			copyFileFast(is, os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			is = null;
			os = null;
		}
	}

	/**
	 * 快速复制文件（采用nio操作）
	 * 
	 * @param is
	 *            数据来源
	 * @param os
	 *            数据目标
	 * @throws IOException
	 */
	public static void copyFileFast(FileInputStream is, FileOutputStream os) throws IOException {
		FileChannel in = is.getChannel();
		FileChannel out = os.getChannel();
		in.transferTo(0, in.size(), out);
	}
	/**
	 * 得到path下的所有文件
	 * @param path
	 * @return
	 * @return: File[]
	 */
	public static List<String> getFiles(String path){
		File file = new File(path);
		List<String> retFiles = new ArrayList<>();
		File[] listFiles = file.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].isDirectory()) {
				List<String> files = getFiles(listFiles[i].getAbsolutePath());
				for (int j = 0; j < files.size(); j++) {
					retFiles.add(files.get(j));
				}
			}else {
				retFiles.add(listFiles[i].getAbsolutePath());
			}
		}
		return retFiles;
	}
	
	/**未知文件*/
	public static final int FILE = -1;
	/**文件夹*/
	public static final int FOLDER = 0;
	public static final int MUSIC = 1;
	public static final int VIDEO = 2;
	public static final int ZIP = 3;
	public static final int PIC = 4;
	/**
	 * 文件类型
	 */
	public static int getType(String str){
		if (str==null) {
			return -1;
		}
		
		String music = ".mp3.wav.mid.midi.wma.amr.ogg.m4a";
		String video = ".3gp.mp4.flv.avi.mov.3gp.m4v.wmv.rm.rmvb.mkv.ts.webm";
		String zip = ".zip";
		String pic = ".jpg.png.jpeg.bmp.gif";
		int lastIndexOf = str.lastIndexOf('.');
		str = str.substring(lastIndexOf, str.length());
		if (music.contains(str)) {
			return MUSIC;
		}
		if (video.contains(str)) {
			return VIDEO;
		}
		if (zip.contains(str)) {
			return ZIP;
		}
		if (pic.contains(str)) {
			return PIC;
		}
		return FILE;
	}
}
