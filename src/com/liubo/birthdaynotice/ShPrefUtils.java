package com.liubo.birthdaynotice;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ShPrefUtils {
	private static final String PREF_FILE_USERINFO = "pref_user_info";
	private SharedPreferences shprefUserInfo = null;
	private static ShPrefUtils instance;
	
	public static ShPrefUtils getInstance(Context context){
		if(instance == null)
			instance = new ShPrefUtils(context);
		return instance;
	}
	
	private ShPrefUtils(Context context) {
		shprefUserInfo = context.getSharedPreferences(PREF_FILE_USERINFO, Activity.MODE_PRIVATE);
	}
	
	public Map<String,?> getAll(){
		return shprefUserInfo.getAll();
	}
	
	public String get(String key, String defValue){
		return shprefUserInfo.getString(key, defValue);
	}
	
	public void put(String key, String value){
		SharedPreferences.Editor edit = shprefUserInfo.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public void delete(String key){
		SharedPreferences.Editor edit = shprefUserInfo.edit();
		edit.remove(key);
		edit.commit();
	}
	
	public void clear(){
		SharedPreferences.Editor edit = shprefUserInfo.edit();
		edit.clear();
		edit.commit();
	}
}
