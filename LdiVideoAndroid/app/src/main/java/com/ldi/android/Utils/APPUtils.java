package com.ldi.android.Utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class APPUtils {
	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
		}
		return verName;

	}
	
	public static void showSoftInput(final EditText edittext) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager =

				(InputMethodManager) edittext.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edittext, 0);
			}
		}, 400);
	}

	public static void showSoftInputNow(final EditText edittext) {

		InputMethodManager inputManager =

		(InputMethodManager) edittext.getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(edittext, 0);

	}

	public static void hideSoftInput(final EditText edittext) {
		InputMethodManager manager = (InputMethodManager) edittext.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromWindow(edittext.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
