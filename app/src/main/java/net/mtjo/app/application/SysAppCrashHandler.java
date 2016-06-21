package net.mtjo.app.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

/**
 * 自定义的 异常处理类 , 实现了 UncaughtExceptionHandler接口
 * @author Administrator
 *
 */
public class SysAppCrashHandler implements UncaughtExceptionHandler {
	private static SysAppCrashHandler myCrashHandler ;
	private Context context;
	private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//1.私有化构造方法
	private SysAppCrashHandler(){

	}

	public static synchronized SysAppCrashHandler getInstance(){
		if(myCrashHandler!=null){
			return myCrashHandler;
		}else {
			myCrashHandler  = new SysAppCrashHandler();
			return myCrashHandler;
		}
	}
	public void init(Context context) {
		this.context = context;
	}


	public void uncaughtException(Thread arg0, Throwable arg1) {
		try {
			String errorinfo = getErrorInfo(arg1);
			saveLog(dataFormat.format(new Date()) + "\n" + errorinfo);	
		} catch (Exception e) {
		}

		//干掉当前的程序
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public void saveLog(String content)throws Exception{
		saveToSDCard("LSCN.log", content);
	}
	
	public void saveToSDCard(String filename, String content)throws Exception{
		
		File file = new File(Environment.getExternalStorageDirectory()+"/"+filename);
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream outStream = new FileOutputStream(file,true);
		byte[] bytes = content.getBytes();
		outStream.write(bytes);
		outStream.close();
	}
	
	/**
	 * 获取错误的信息
	 * @param arg1
	 * @return
	 */
	private String getErrorInfo(Throwable arg1) {
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error= writer.toString();
		return error;
	}

	/**
	 * 获取手机的硬件信息
	 * @return
	 */
	private String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		//通过反射获取系统的硬件信息
		try {

			Field[] fields = Build.class.getDeclaredFields();
			for(Field field: fields){
				//暴力反射 ,获取私有的信息
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name+"="+value);
				sb.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取手机的版本信息
	 * @return
	 */
	private String getVersionInfo(){
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info =pm.getPackageInfo(context.getPackageName(), 0);
			return  info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}
}