package cn.ac.iie.application;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

public class AppApplication extends Application {

	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();

	}

	public static Context getContext() {
		return mContext;
	}
	
	public static ContentResolver getAppContentResolver() {
        return mContext.getContentResolver();
    }

}
