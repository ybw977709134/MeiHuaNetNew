package com.MeiHuaNet.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility methods for packing/unpacking primitive values in/out of byte arrays using big-endian byte ordering.
 */
public final class Bits {

	/*
	 * Methods for unpacking primitive values from byte arrays starting at given offsets.
	 */

	public static boolean getBoolean(byte[] b, int off) {
		return b[off] != 0;
	}

	public static char getChar(byte[] b, int off) {
		return (char) (((b[off + 1] & 0xFF)) | ((b[off]) << 8));
	}

	public static short getShort(byte[] b, int off) {
		return (short) (((b[off + 1] & 0xFF)) | ((b[off]) << 8));
	}

	public static int getInt(byte[] b, int off) {
		return ((b[off + 3] & 0xFF)) | ((b[off + 2] & 0xFF) << 8) | ((b[off + 1] & 0xFF) << 16) | ((b[off]) << 24);
	}

	public static long getLong(byte[] b, int off) {
		return ((b[off + 7] & 0xFFL)) | ((b[off + 6] & 0xFFL) << 8) | ((b[off + 5] & 0xFFL) << 16) | ((b[off + 4] & 0xFFL) << 24) | ((b[off + 3] & 0xFFL) << 32) | ((b[off + 2] & 0xFFL) << 40) | ((b[off + 1] & 0xFFL) << 48) | ((b[off] & 0xFFL) << 56);
	}

	public static int getRGB(byte[] b, int off) {
		return (b[off + 2] & 0xff) | ((b[off + 1] & 0xff) << 8) | ((b[off] & 0xff) << 16);
	}

	/*
	 * Methods for packing primitive values into byte arrays starting at given offsets.
	 */

	public static void putBoolean(byte[] b, int off, boolean val) {
		b[off] = (byte) (val ? 1 : 0);
	}

	public static void putChar(byte[] b, int off, char val) {
		b[off + 1] = (byte) (val);
		b[off] = (byte) (val >>> 8);
	}

	public static void putShort(byte[] b, int off, short val) {
		b[off + 1] = (byte) (val);
		b[off] = (byte) (val >>> 8);
	}

	public static void putInt(byte[] b, int off, int val) {
		b[off + 3] = (byte) (val);
		b[off + 2] = (byte) (val >>> 8);
		b[off + 1] = (byte) (val >>> 16);
		b[off] = (byte) (val >>> 24);
	}

	public static void putLong(byte[] b, int off, long val) {
		b[off + 7] = (byte) (val);
		b[off + 6] = (byte) (val >>> 8);
		b[off + 5] = (byte) (val >>> 16);
		b[off + 4] = (byte) (val >>> 24);
		b[off + 3] = (byte) (val >>> 32);
		b[off + 2] = (byte) (val >>> 40);
		b[off + 1] = (byte) (val >>> 48);
		b[off] = (byte) (val >>> 56);
	}

	public static void putRGB(byte[] b, int off, int rgb) {
		b[off + 2] = (byte) rgb;
		b[off + 1] = (byte) (rgb >> 8);
		b[off] = (byte) (rgb >> 16);
	}

	/**
	 * ���ָ����ֵ��2���Ʊ�ʾ��ĳһλ�Ƿ�Ϊ1
	 * @param value ָ����ֵ
	 * @param bit ָ��λ��
	 * @return
	 */
	public static boolean checkBit(long value, int bit) {
		return (value & (1 << bit)) != 0;
	}

	/**
	 * 从src中index的位置开始索引sub
	 * @param src 总字节数组
	 * @param sub 要查找的字节数组
	 * @param index 从总数组中开始查找的位置
	 * @return src包含sub,return sub在src中的开始位置的索引，不包含return -1
	 */
	public static int indexOf(byte[] src, byte[] sub, int index) {
		if (index < 0 || index > src.length)
			throw new IndexOutOfBoundsException();
		if (index + sub.length > src.length)
			return -1;
		for (; index <= src.length - sub.length; index++) {
			boolean found = true;
			for (int j = 0; j < sub.length && found; j++){
				if (sub[j] != src[index + j])
					found = false;
			}
			if (found)
				return index;
		}
		return -1;
	}

	public static byte[] read(InputStream in, int length) throws IOException {
		if (length == 0)
			return new byte[0];
		byte[] buf;
		int read = 0;
		if (length < 0) {
			ByteArrayDataOutputStream dos = new ByteArrayDataOutputStream(2048);
			buf = new byte[2048];
			while ((read = in.read(buf)) > 0)
				dos.write(buf, 0, read);
			buf = dos.toByteArray();
			dos.close();
			dos = null;
		} else {
			buf = new byte[length];
			while (read < length)
				read += in.read(buf, read, length - read);
		}
		return buf;
	}
}