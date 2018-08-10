package com.le.net;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by peng on 2015/8/27.
 *
 */
public class HttpResponse {
	HttpResponse(int sc, Map<String, List<String>> fs, String strRes) {
		statusCode = sc;
		fields = fs;
//		inputStream = is;
		this.strRes = strRes;
	}

	public int statusCode;
	private Map<String, List<String>> fields;
//	public InputStream inputStream;
	public String strRes;

	public List<String> getHeaders(String key) {
		for (Map.Entry<String, List<String>> entry : fields.entrySet()) {
			if (entry.getKey() == null) {
				continue;
			}
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return new ArrayList<>();
	}

	private String str;

	public String getString() throws IOException {
		return strRes;
//		if (str == null) {
//			str = "";
//			BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
//			while(true) {
//				String line = rd.readLine();
//				if(line != null) {
//					str += line + "\n";
//				} else {
//					break;
//				}
//			}
//		}
//		return this.str;
	}

	/**
	 * If the return value is a JSONObject, will be transformed into a class
	 */
	public <T> T parseJson(Class<T> t) throws IOException {
//		return mapper.readValue(this.getString(), t);
		return JSON.parseObject(this.getString(), t);
	}

	/**
	 * If the return value is a JsonArray, will be converted to a list < Class >
	 */
	public <T> List<T> parseJsonArray(Class<T> t) throws IOException {
//		return mapper.readValue(this.getString(), mapper.getTypeFactory().constructCollectionType(List.class, t));
		return JSON.parseArray(this.getString(), t);
	}
}
