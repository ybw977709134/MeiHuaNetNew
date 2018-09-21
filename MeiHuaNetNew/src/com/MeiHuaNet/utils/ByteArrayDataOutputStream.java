package com.MeiHuaNet.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 非同步，无IO异常的字节数组输出流
 */
public class ByteArrayDataOutputStream extends OutputStream {
	/*
	 * 字节数组缓冲
	 */
	protected byte[] buf;
	/*
	 * 写入字节数
	 */
	protected int count;

	private boolean append = true;

	/**
	 * 构造函数
	 */
	public ByteArrayDataOutputStream() {
		this(16);
	}

	/**
	 * 构造函数，指定初始化缓冲数组尺寸
	 */
	public ByteArrayDataOutputStream(int size) {
		if (size < 0)
			throw new IllegalArgumentException(size + " < 0");
		buf = new byte[size];
	}

	public ByteArrayDataOutputStream(byte[] buf) {
		this.buf = buf;
		this.append = false;
	}

	/**
	 * @see java.io.OutputStream#close()
	 */
	public void close() {
	}

	/**
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write(byte[] b, int off, int len) {
		if (len <= 0)
			return;
		ensureCapacity(len);
		System.arraycopy(b, off, buf, count, len);
		count += len;
	}

	/**
	 * @see java.io.OutputStream#write(byte[])
	 */
	public void write(byte[] b) {
		write(b, 0, b.length);
	}

	/**
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int value) {
		ensureCapacity(1);
		buf[count++] = (byte) value;
	}

	/**
	 * @see java.io.DataOutput#writeBoolean(boolean)
	 */
	public void writeBoolean(boolean value) {
		write(value ? 1 : 0);
	}

	/**
	 * @see java.io.DataOutput#writeByte(int)
	 */
	public void writeByte(int value) {
		write(value);
	}

	/**
	 * @see java.io.DataOutput#writeChar(int)
	 */
	public void writeChar(int value) {
		ensureCapacity(2);
		Bits.putChar(buf, count, (char) value);
		count += 2;
	}

	/**
	 * @see java.io.DataOutput#writeChars(String)
	 */
	public void writeChars(String string) {
		ensureCapacity(string.length() * 2);
		for (int i = 0; i < string.length(); i++) {
			Bits.putChar(buf, count, string.charAt(i));
			count += 2;
		}
	}

	/**
	 * @see java.io.DataOutput#writeInt(int)
	 */
	public void writeInt(int value) {
		ensureCapacity(4);
		Bits.putInt(buf, count, value);
		count += 4;
	}

	/**
	 * @see java.io.DataOutput#writeLong(long)
	 */
	public void writeLong(long value) {
		ensureCapacity(8);
		Bits.putLong(buf, count, value);
		count += 8;
	}

	/**
	 * @see java.io.DataOutput#writeShort(int)
	 */
	public void writeShort(int value) {
		ensureCapacity(2);
		Bits.putShort(buf, count, (short) value);
		count += 2;
	}

	/**
	 * 按Unicode编码格式写入字符串
	 * @param str
	 *//*
	public void writeUnicode(String str) {
		writeShort(str.length() * 2);
		for (int i = 0; i < str.length(); i++)
			writeChar(str.charAt(i));
	}
*/
	/**
	 * @see java.io.DataOutput#writeUTF(String)
	 */
	public void writeUTF(String str) {
		int strlen = str.length();
		int utflen = 0;
		int c, count = 0;

		/* use charAt instead of copying String to char array */
		for (int i = 0; i < strlen; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				utflen++;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}

		if (utflen > 0xFFFF)
			throw new RuntimeException("UTFDataFormatException: encoded string too long: " + utflen + " bytes");

		byte[] bytearr = new byte[utflen + 2];
		bytearr[count++] = (byte) (utflen >>> 8);
		bytearr[count++] = (byte) (utflen >>> 0);

		int i = 0;
		for (i = 0; i < strlen; i++) {
			c = str.charAt(i);
			if (!((c >= 0x0001) && (c <= 0x007F)))
				break;
			bytearr[count++] = (byte) c;
		}

		for (; i < strlen; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				bytearr[count++] = (byte) c;
			} else if (c > 0x07FF) {
				bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
				bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
				bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
			} else {
				bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
				bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
			}
		}
		this.ensureCapacity(utflen + 2);
		System.arraycopy(bytearr, 0, buf, this.count, utflen + 2);
		this.count += utflen + 2;
	}

	/**
	 * 将此缓冲输出流里的内容写入到另外一个输出流里
	 * @param out 输出流
	 * @throws IOException
	 */
	public void writeTo(OutputStream out) throws IOException {
		out.write(buf, 0, count);
	}

	/**
	 * @see java.io.ByteArrayOutputStream#reset()
	 */
	public void reset() {
		count = 0;
	}

	/**
	 * @see java.io.ByteArrayOutputStream#size()
	 */
	public int size() {
		return count;
	}

	/**
	 * @see java.io.ByteArrayOutputStream#toByteArray()
	 */
	public byte[] toByteArray() {
		byte[] bytearray = new byte[count];
		System.arraycopy(buf, 0, bytearray, 0, count);
		return bytearray;
	}

	/**
	 * 
	 * @param size
	 */
	private void ensureCapacity(int size) {
		int minCapacity = count + size;
		if (buf.length >= minCapacity)
			return;
		if (!append)
			throw new RuntimeException("OverFlow");
		int newCapacity = buf.length << 1;
		if (newCapacity < minCapacity)
			newCapacity = minCapacity;
		byte[] newBuf = new byte[newCapacity];
		System.arraycopy(buf, 0, newBuf, 0, count);
		buf = newBuf;
	}
}
