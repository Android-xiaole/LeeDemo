package com.le.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.le.BuildConfig;
import com.le.base.BaseApplication;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtils {
	
	public final static int TYPE_FILE_IMAGE = 1;
	public final static int TYPE_FILE_VEDIO = 2;

	/**
	 * 判断是不是手机号码
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
//        String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
//		String regex = "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|7[06-8])\\d{8}$";
		String regex = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 检测版本是否需要更新
	 */
	public static boolean updateVersion(String version){
		String[] lastVersion = version.split("\\.");
		String[] myVersion = BaseApplication.mAppVersion.split("\\.");
		if (Integer.valueOf(myVersion[0])<Integer.valueOf(lastVersion[0])){
			return true;
		}else if (Integer.valueOf(myVersion[0])==Integer.valueOf(lastVersion[0])&&Integer.valueOf(myVersion[1])<Integer.valueOf(lastVersion[1])){
			return true;
		}else if (Integer.valueOf(myVersion[1])==Integer.valueOf(lastVersion[1])&&Integer.valueOf(myVersion[2])<Integer.valueOf(lastVersion[2])){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * @param bgAlpha
	 */
	public static void backgroundAlpha(Activity context, float bgAlpha)
	{
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		if (bgAlpha == 1) {
			context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
		} else {
			context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
		}
		context.getWindow().setAttributes(lp);
	}

	/**
	 * @param imagePath
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getMD5(String imagePath) throws NoSuchAlgorithmException, IOException {

		InputStream in = new FileInputStream(new File(imagePath));

		StringBuffer md5 = new StringBuffer();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] dataBytes = new byte[1024];

		int nread = 0;
		while ((nread = in.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}

		byte[] mdbytes = md.digest();

		// convert the byte to hex format
		for (int i = 0; i < mdbytes.length; i++) {
			md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return md5.toString().toLowerCase();
	}

	/**
	 * ----判断是那种类型的Uri---- true 为"content"类型，也就是系统图库里面的图片 false
	 * 为"File"类型，也就是SD里面的图片
	 */
	public static Bitmap getUriType(ImageView imageView, Uri imageUri,Context ctx){
		String scheme = imageUri.getScheme();
		if (scheme.equals("content")) {
			String[] proj = { MediaStore.Images.Media.DATA };
			android.database.Cursor cursor = ctx.getContentResolver().query(imageUri, proj, null, null, null);
			int columnindex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String img_path = cursor.getString(columnindex);
			Bitmap bitmap = setPicToImageView(imageView, new File(img_path));
			cursor.close();
			return bitmap;
		} else {
			Bitmap bitmap = setPicToImageView(imageView, new File(imageUri.getEncodedPath()));
			return bitmap;
		}
	}
	
	/**
	 * content Uri to path
	 * 此方法不适用于androidN以上的手机（拍照返回的uri回出现异常）
	 */
	public static String getUriType(Uri imageUri,Context ctx) {
		String scheme = imageUri.getScheme();
		if (scheme.equals("content")) {
			String[] proj = { MediaStore.Images.Media.DATA };
			android.database.Cursor cursor = ctx.getContentResolver().query(imageUri, proj, null, null, null);
			int columnindex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String img_path = cursor.getString(columnindex);
			cursor.close();
			return img_path;
		} else {
			return imageUri.getEncodedPath();
		}
	}

	/**
	 * path to content Uri
	 */
	public static Uri getImageContentUri(Context context, File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID },
				MediaStore.Images.Media.DATA + "=? ",
				new String[] { filePath }, null);

		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.MediaColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	/**
	 * -----------------------Android大图的处理方式---------------------------
	 * 
	 * @param imageView
	 * @param imageFile
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap setPicToImageView(ImageView imageView, File imageFile) {
		float imageViewWidth = imageView.getWidth();
		float imageViewHeight = imageView.getHeight();
		Options opts = new Options();

		// 设置这个，只得到Bitmap的属性信息放入opts，而不把Bitmap加载到内存中
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imageFile.getPath(), opts);

		float bitmapWidth = opts.outWidth;
		float bitmapHeight = opts.outHeight;

		int scale = (int) Math.max(bitmapWidth / imageViewWidth, bitmapHeight
				/ imageViewHeight);
		// 缩放的比例
		opts.inSampleSize = scale;
		// 内存不足时可被回收
		opts.inPurgeable = true;
		// 设置为false,表示不仅Bitmap的属性，也要加载bitmap
		opts.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), opts);
//		if (bitmap != null) {
//			imageView.setImageBitmap(bitmap);
//		}
		return bitmap;
	}
		
		@SuppressWarnings("deprecation")
		public static void setPicToImageView(ImageView imageView, Uri imageUri) {
			float imageViewWidth = imageView.getWidth();
			float imageViewHeight = imageView.getHeight();
			Options opts = new Options();
			
			File imageFile = new File(imageUri.getEncodedPath());

			// 设置这个，只得到Bitmap的属性信息放入opts，而不把Bitmap加载到内存中
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imageFile.getPath(), opts);

			float bitmapWidth = opts.outWidth;
			float bitmapHeight = opts.outHeight;

			int scale = (int) Math.max(bitmapWidth / imageViewWidth, bitmapHeight
					/ imageViewHeight);
			// 缩放的比例
			opts.inSampleSize = scale;
			// 内存不足时可被回收
			opts.inPurgeable = true;
			// 设置为false,表示不仅Bitmap的属性，也要加载bitmap
			opts.inJustDecodeBounds = false;

			Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), opts);
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
				bitmap = null;
			}
		}
		
		// -----------------------生成Uri---------------------------------------
				/**
				 * 得到输出文件的URI
				 * 
				 * @param fileType
				 * @return
				 */
				public static Uri getOutFileUri(int fileType) {
					if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){ /*7.0以上要通过FileProvider将File转化为Uri*/
						return FileProvider.getUriForFile(BaseApplication.appContext, BaseApplication.appContext.getPackageName()+".provider",getOutFile(fileType));
					}
					 /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
					return Uri.fromFile(getOutFile(fileType));
				}

				/**
				 * 生成输出文件
				 * 
				 * @param fileType
				 * @return
				 */
				private static File getOutFile(int fileType) {
					// 获取SD的状态
					String storageState = Environment.getExternalStorageState();
					if (Environment.MEDIA_REMOVED.equals(storageState)) {
						BaseApplication.showToast("SD卡不存在");
						return null;
					}
					File mediaStorageDir = null;
					if (Environment.MEDIA_MOUNTED.equals(storageState)) {
//						mediaStorageDir = new File(
//								Environment
//										.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//								"MyPictures");// 存放在手机内部存储Pictures下面的公共目录
						mediaStorageDir = new File(BaseApplication.mCachePath);
						if (!mediaStorageDir.exists()) {
							if (!mediaStorageDir.mkdirs()) {
								Log.i("MyPictures", "创建图片存储路径目录失败");
								Log.i("MyPictures",
										"mediaStorageDir : " + mediaStorageDir.getPath());
								return null;
							}
						}
					}
					File file = new File(getFilePath(mediaStorageDir, fileType));
					return file;
				}

				/**
				 * 生成输出文件路径，给拍摄的图片命名
				 * 
				 * @param mediaStorageDir
				 * @param fileType
				 * @return
				 */
				@SuppressLint("SimpleDateFormat")
				private static String getFilePath(File mediaStorageDir, int fileType) {
					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
							.format(new Date());
					String filePath = mediaStorageDir.getPath() + File.separator;
					if (fileType == TYPE_FILE_IMAGE) {
						filePath += ("IMG_" + timeStamp + ".jpg");
					} else if (fileType == TYPE_FILE_VEDIO) {
						filePath += ("VIDEO_" + timeStamp + ".mp4");
					} else {
						return null;
					}
					return filePath;
				}
		
		
		/**
		 * 将bitmap保存为文件
		 */
		public static String saveFile2Path(Bitmap bitmap) {
			File file0 = new File(BaseApplication.mCachePath);
			if (!file0.exists()) {
				file0.mkdirs();
			}
			File file = new File(file0.getPath() + "/picture.jpg");
			BufferedOutputStream bos;
			try {
				bos = new BufferedOutputStream(new FileOutputStream(file));
				if (bitmap != null) {
					bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
					bos.flush();
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file.getPath();
		}

	/**
	 * 将bitmap保存为文件
	 */
	public static File saveFile(Bitmap bitmap) {
		File file0 = new File(BaseApplication.mCachePath);
		if (!file0.exists()) {
			file0.mkdirs();
		}
		File file = new File(file0.getPath() + "/temp.jpg");
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			if (bitmap != null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
		
	/**
	 * 正确姿势回收Bitmap 
	 */
	public static void recycleBitmap(Bitmap bitmap){
		if (bitmap!=null&&!bitmap.isRecycled()) {
			bitmap.recycle();
			System.gc();
		}
	}
	
	/**
	 * 摄氏度 转 华氏度
	 * @param degree 需要转换的温度
	 * @param scale 保留的小数位 
	 * @return
	 */
	 public static double centigrade2Fahrenheit(double degree,int scale) {  
	        double d = 32 + degree * 1.8;  
	        return new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
	    }  
	 
	 /**
	  * 华氏度 转 摄氏度
	  * @param degree 需要转换的温度
	  * @param scale 保留的小数位 
	  * @return
	  */
	 public static double fahrenheit2Centigrade(double degree, int scale) {  
	        double d = (degree - 32) / 1.8;  
	        return new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
	    }  
	 
	 public static byte[] stringToASCBytes(String s) {
			if (s == null) {
				return new byte[0];
			}
			return s.getBytes();
		}
		
		public static String bytesToASCString(byte[] src){
			StringBuilder string = new StringBuilder("");
			if (src == null) return string.toString();
			for (int i = 0; i < src.length; i++) {
				string.append((char)src[i]);
			}
			return string.toString();
		}
		
		/**
		 * 编码带有字符串的Url
		 */
		private static String zhPattern = "[\u4e00-\u9fa5]+";

		public static String encode(String str, String charset) throws UnsupportedEncodingException {
			Pattern p = Pattern.compile(zhPattern);
			Matcher m = p.matcher(str);
			StringBuffer b = new StringBuffer();
			while (m.find()) {
				m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
			}
			m.appendTail(b);
			return b.toString();
		}
		
		/**
		 * 将imageView显示的图片转换成bitmap
		 */
		public static Bitmap getBitmap(ImageView iv){
			iv.setDrawingCacheEnabled(true);
			Bitmap bitmap = Bitmap.createBitmap(iv.getDrawingCache());
			iv.setDrawingCacheEnabled(false);
			return bitmap;
		}

	/***************************** 压   缩   图   片 ********************************************************/

	/**
	 * 质量压缩法：
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//		while ( baos.toByteArray().length / 1024>1024) {    //循环判断如果压缩后图片是否大于1M,大于继续压缩
//			baos.reset();//重置baos即清空baos
//			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩options%，把压缩后的数据存放到baos中
//		}
		int options = 100;
		while ( baos.toByteArray().length / 1024>500) {    //循环判断如果压缩后图片是否大于500kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			options -= 10;//每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 图片按比例大小压缩
	 * @param srcPath
	 * @return
	 */
	public Bitmap getimage(String srcPath) {
		Options newOpts = new Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}

	public Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Options newOpts = new Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
		
}
