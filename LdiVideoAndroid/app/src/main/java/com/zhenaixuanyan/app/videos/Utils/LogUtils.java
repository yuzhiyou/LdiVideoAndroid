package com.zhenaixuanyan.app.videos.Utils;

import android.util.Log;

public class LogUtils {
	private static final Boolean DEBUG = true;
	public static final String TAG = "LIDVIDEO";

	public static void putLog(String logs) {
		if (DEBUG) {
			System.out.println(logs);
		}
	}

	public static void putLog(Object logs) {
		if (DEBUG) {
			System.out.println(logs);
		}
	}

	public static void i(String msg) {
		if (DEBUG) {
			Log.i(TAG, msg);
		}
	}

	public static void v(String msg) {
		if (DEBUG) {
			Log.v(TAG, msg);
		}
	}

	public static void d(String msg) {
		if (DEBUG) {
			Log.d(TAG, msg);
		}
	}

	public static void w(String msg) {
		if (DEBUG) {
			Log.w(TAG, msg);
		}
	}

	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, msg);
		}
	}
}
