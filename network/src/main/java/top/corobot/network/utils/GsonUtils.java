package top.corobot.network.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * GSON工具类
 * 
 */
public class GsonUtils {
	public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z").create();

	/*
	 * Parse the object data into json
	 */
	public static String toJson(final Object object) {
		// TODO Auto-generated method stub
		return toString(object);
	}

	public static String toJson(final Object object, Type typeOfT) {
		// TODO Auto-generated method stub
		return toString(object, typeOfT);
	}

	public static String toString(Object object, Type typeOfT) {
		String result = "";
		try {
			if (typeOfT == null)
				result = GSON.toJson(object);
			else
				result = GSON.toJson(object, typeOfT);
		} catch (Exception e) {
			result = "";
		}catch (Error error){
			error.printStackTrace();
		}

		return result;
	}

	public static String toString(final Object object) {
		return toString(object, null);
	}

	/**
	 * 从JSON转换为对象
	 * 
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T> T fromJson(final String json, Class<T> classOfT) {
		// TODO Auto-generated method stub
		T result = null;
		try {
			result = GSON.fromJson(json, classOfT);
		} catch (Exception e) {
			result = null;
		}catch (Error error){
			error.printStackTrace();
		}

		return result;
	}

	/**
	 * java.lang.reflect.Type type = new TypeToken<T>() { }.getType();
	 * 
	 * @param json
	 * @param typeOfT
	 * @return
	 */

	public static <T> T fromJson(final String json, Type typeOfT) {
		// TODO Auto-generated method stub
		T result = null;
		try {
			result = GSON.fromJson(json, typeOfT);
		} catch (Exception e) {
			result = null;
		}catch (Error error){
			error.printStackTrace();
		}

		return result;
	}
}
