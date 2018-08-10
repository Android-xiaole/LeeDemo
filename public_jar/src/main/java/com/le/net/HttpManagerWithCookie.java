package com.le.net;

import com.blankj.utilcode.util.ToastUtils;
import com.le.utils.MLog;
import com.le.base.BaseConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by peng on 2015/8/27.
 *
 */
public class HttpManagerWithCookie extends BaseHttpManager {
	public List<String> cookies = new ArrayList<>();

	@Override
	public boolean requestFilter(HttpRequest httpRequest) throws Exception {
		synchronized (this.cookies) {
			String cookiestr = "";
			for (String cookie : this.cookies) {
				String prefix = "";
				if (!cookiestr.equals("")) {
					prefix = "; ";
				}
				cookiestr += prefix + cookie;
				BaseConfig.COOKIE = cookiestr;
			}
			MLog.e("test", "cookie_value:" + BaseConfig.COOKIE);
			if (cookiestr != null) {
				httpRequest.headers.put("Cookie", cookiestr);
			}
		}
		return true;
	}

	@Override
	public boolean responseFilter(HttpResponse httpResponse) throws Exception {
		if (httpResponse.statusCode >= 300) {
			System.out.println(httpResponse.getString());
			return false;
		} else {
			String[] cookieKeys = new String[] { "Set-Cookie", "Set-cookie",
					"set-Cookie", "set-cookie" };
			synchronized (this.cookies) {
				this.cookies.clear();
				for (String cookieKey : cookieKeys) {
					List<String> cookies = httpResponse.getHeaders(cookieKey);

					for (String cookie : cookies) {
						this.cookies.add(cookie);
					}
				}
			}
			return true;
		}
	}

	@Override
	public void error(Exception e) {
		e.printStackTrace();
		MLog.e(this.getClass().getSimpleName()+"未处理的异常:"+e);
		ToastUtils.showShort(e+"");
	}
	
}
