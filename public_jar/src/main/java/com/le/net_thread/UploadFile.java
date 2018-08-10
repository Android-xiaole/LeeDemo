package com.le.net_thread;

import com.le.utils.MLog;
import com.le.utils.UtilString;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.name;

/**
 * Created by sahara on 2016/4/27.
 */
public class UploadFile {
    // 定义数据分割线
    private static String BOUNDARY = "------------------------7dc2fd5c0894";
    // 定义最后数据分割线
    private static byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();

    public void makeRequest(File file, final String url, MyCallBack callBack){
        HttpURLConnection conn = null;
        String result = "";
        try {
            URL url1 = new URL(url);
            MLog.e("当前上传图片接口："+url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
//            conn.setRequestProperty("Cookie", BaseConfig.COOKIE);
            BufferedOutputStream bos = new BufferedOutputStream(conn.getOutputStream());

            StringBuilder sb = new StringBuilder();
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\""+name+"\";filename=\"" + file.getName() + "\"\r\n");
            // 這裏不能漏掉，根據文件類型來來做處理，由於上傳的是圖片，所以這裏可以寫成image/pjpeg
            String type = "";
            switch (UtilString.getLast(file.getName(), ".")+"") {
                case "png":
                    type = "Content-Type: image/png\r\n\r\n";
                    break;
                case "jpg":
                    type = "Content-Type: image/jpeg\r\n\r\n";
                    break;
                case "txt":
                    type = "Content-Type: text/plain\r\n\r\n";
                    break;
                default:
                    type = "Content-Type: application/octet-stream\r\n\r\n";
                    break;
            }
            sb.append(type);//text/plain
            //application/octet-stream
            //image/png
            //image/jpeg
            bos.write(sb.toString().getBytes());
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] byteArray=new byte[1024];
            int tmp=0;
            int current = 0;
            int total = bis.available();
            while ((tmp = bis.read(byteArray))!=-1) {
                current = current+tmp;
                bos.write(byteArray, 0, tmp);
                callBack.inProgress(current,total,file.getName());
            }
//            bos.write("\r\n".getBytes());
            bos.write(end_data);
            bis.close();
            bos.flush();
            bos.close();

//            conn.connect();
            //上传完成后，开始读取返回内容
            if (conn.getResponseCode()>=300){
                InputStream errorStream = conn.getErrorStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(errorStream, "UTF8"));
                String errorStr = "";
                while(true) {
                    String line = rd.readLine();
                    if(line != null) {
                        errorStr += line + "\n";
                    } else {
                        callBack.error(new Exception(errorStr),file.getName());
                        break;
                    }
                }
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = br.readLine())!=null) {
                result = result+line+"\n";
            }
            callBack.finish(result);
        } catch (Exception e) {
            callBack.error(e,file.getName());
        }
    }

}
