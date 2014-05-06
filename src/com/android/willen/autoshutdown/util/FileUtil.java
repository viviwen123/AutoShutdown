package com.android.willen.autoshutdown.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileUtil {
	static String prePath = "/data/data/com.android.willen.autoshutdown/file/";
	//读取文件数据
	public static String read(String key) {
		String value = "";
		try {
			value = ReadFile(key);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return value;
	}
	
	// 向文件写入数据
	public static void write(String strKey, String strKeyValue) {
		try {
			WriteFile(strKey,strKeyValue);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 读取文件数据
	public static String ReadFile(String path) throws IOException {
		path = prePath + path+".dat";
		if (FileExist(path)) {
			StringBuffer buffer = new StringBuffer();
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");// 文件编码Unicode,UTF-8,ASCII,GB2312,Big5
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char) ch);
			}
			in.close();
			return buffer.toString();
		}
		return "";
	}

	// 向文件写入数据
	public static boolean WriteFile(String path, String str) throws IOException {
		path = prePath + path+".dat";
		if (FileExist(path)) {
			FileWriter fw = null;
			BufferedWriter bw = null;
			fw = new FileWriter(path, false);// FALSE为不在文件后面添加
			bw = new BufferedWriter(fw); // 将缓冲对文件的输出
			bw.write(str); // 写入文件
			//bw.newLine();
			bw.flush(); // 刷新该流的缓冲
			bw.close();
			fw.close();
			return true;
		}
		return false;
	}

	// 判断文件是否存在， 不存在则创建
	public static boolean FileExist(String path) throws IOException {
		String[] fileName = path.split("/");
		String root = path.substring(0, path.length()
				- (fileName[fileName.length - 1]).length() - 1);
		File file = new File(root);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		File dir = new File(path);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}
		}
		return true;
	}
}
