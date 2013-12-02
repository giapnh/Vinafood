/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.hgbk.vtio.vinafood.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

/**
 * 
 * @author Nguyen Huu Giap
 */
public class FileManager {

	public static final String FILE_DIR = "E://Dropbox//GR//Design";
	public static final String FILE_NAME = "data.dat";

	private static synchronized File openFile(int url) {
		String filename = String.valueOf(url);
		File f = new File(FILE_DIR, filename);
		return f;
	}

	public static byte[] loadFile(int url) {
		FileInputStream input = null;
		ByteArrayOutputStream bos = null;
		try {
			input = new FileInputStream(openFile(url));
			int len;
			bos = new ByteArrayOutputStream();
			byte[] arr = new byte[1024];
			while ((len = input.read(arr, 0, 1024)) != -1) {
				bos.write(arr, 0, len);
			}
			arr = bos.toByteArray();
			bos.close();
			input.close();
			return arr;
		} catch (Exception e) {
			return null;
		} finally {
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
				}
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
				}
		}
	}

	public static void save(int url, long version, byte[] data) {
		OutputStream os = null;
		try {
			File f = openFile(url);
			os = new FileOutputStream(f);
			os.write(data, 0, data.length);
		} catch (Throwable e) {
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
				}
		}
	}

	public static byte[] loadFromRaw(int id, Context context) {
		InputStream input = null;
		ByteArrayOutputStream bos = null;
		try {
			input = context.getResources().openRawResource(id);
			int len;
			bos = new ByteArrayOutputStream();
			byte[] arr = new byte[1024];
			while ((len = input.read(arr, 0, 1024)) != -1) {
				bos.write(arr, 0, len);
			}
			arr = bos.toByteArray();
			bos.close();
			input.close();
			return arr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
