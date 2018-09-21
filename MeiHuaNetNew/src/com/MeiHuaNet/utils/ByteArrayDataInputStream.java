package com.MeiHuaNet.utils;

import java.io.InputStream;

/**
 * 非同步,无IO异常的字节数组输入流
 */
public class ByteArrayDataInputStream extends InputStream {

	/*
	 * 字节数组缓冲
	 */
	protected byte[] buf;
	/*
	 * 当前位置
	 */
	protected int pos;
	/*
	 * 标记位置
	 */
	protected int markPos;
	/*
	 * 数量
	 */
	protected int count;

	/**
	 * 构造函数
	 * @param buf
	 */
	public ByteArrayDataInputStream(byte[] buf) {
		this(buf, 0, buf.length);
	}

	public ByteArrayDataInputStream(byte[] buf, int off, int len) {
		this.buf = buf;
		this.markPos = this.pos = off;
		this.count = off + len;
	}

	/**
	 * @see java.io.InputStream#read()
	 */
	public int read() {
		if (pos == count)
			return -1;
		return buf[pos++] & 0xff;
	}

	/**
	 * @see java.io.DataInput#readBoolean()
	 */
	public boolean readBoolean() {
		checkEOF(1);
		return read() != 0;
	}

	/**
	 * @see java.io.DataInput#readByte()
	 */
	public byte readByte() {
		checkEOF(1);
		return buf[pos++];
	}

	/**
	 * @see java.io.DataInput#readChar()
	 */
	public char readChar() {
		checkEOF(2);
		char c = Bits.getChar(buf, pos);
		pos += 2;
		return c;
	}

	/**
	 * @see java.io.InputStream#read(byte[])
	 */
	public int read(byte[] b) {
		return read(b, 0, b.length);
	}

	/**
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	public int read(byte[] b, int off, int len) {
		int bytes = available();
		if (bytes == 0)
			return -1;
		if (len <= 0)
			return 0;
		if (len > bytes)
			len = bytes;
		readFully(b, off, len);
		return len;
	}

	/**
	 * @see java.io.DataInput#readFully(byte[])
	 */
	public void readFully(byte[] b) {
		readFully(b, 0, b.length);
	}

	/**
	 * @see java.io.DataInput#readFully(byte[], int, int)
	 */
	public void readFully(byte[] b, int off, int len) {
		if (len <= 0)
			return;
		checkEOF(len);
		System.arraycopy(buf, pos, b, off, len);
		pos += len;
	}
	
	/**
	 * @see java.io.DataInput#readInt()
	 */
	public int readInt() {
		checkEOF(4);
		int value = Bits.getInt(buf, pos);
		pos += 4;
		return value;
	}

	/**
	 * @see java.io.DataInput#readLong()
	 */
	public long readLong() {
		checkEOF(8);
		long value = Bits.getLong(buf, pos);
		pos += 8;
		return value;
	}

	/**
	 * @see java.io.DataInput#readShort()
	 */
	public short readShort() {
		checkEOF(2);
		short value = Bits.getShort(buf, pos);
		pos += 2;
		return value;
	}

	/**
	 * 读取Unicode编码的字符串
	 * @return
	 *//*
	public String readUnicode() {
		int unicodelen = readUnsignedShort();
		if (unicodelen == 0)
			return "";
		checkEOF(unicodelen);
		char[] chararr = new char[unicodelen >> 1];
		for (int i = 0; i < chararr.length; i++)
			chararr[i] = readChar();
		return new String(chararr);
	}
*/
	/**
	 * @see java.io.DataInput#readUTF()
	 */
	public String readUTF() {
		int utflen = readUnsignedShort();
		if (utflen == 0)
			return "";
		checkEOF(utflen);
		byte[] bytearr = new byte[utflen];
		readFully(bytearr);
		char[] chararr = new char[utflen];

		int c, char2, char3;
		int count = 0;
		int chararr_count = 0;

		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			if (c > 127)
				break;
			count++;
			chararr[chararr_count++] = (char) c;
		}

		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				/* 0xxxxxxx */
				count++;
				chararr[chararr_count++] = (char) c;
				break;
			case 12:
			case 13:
				/* 110x xxxx 10xx xxxx */
				count += 2;
				if (count > utflen)
					throw new RuntimeException("UTFDataFormatException: malformed input: partial character at end");
				char2 = (int) bytearr[count - 1];
				if ((char2 & 0xC0) != 0x80)
					throw new RuntimeException("UTFDataFormatException: malformed input around byte " + count);
				chararr[chararr_count++] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
				break;
			case 14:
				/* 1110 xxxx 10xx xxxx 10xx xxxx */
				count += 3;
				if (count > utflen)
					throw new RuntimeException("UTFDataFormatException: malformed input: partial character at end");
				char2 = (int) bytearr[count - 2];
				char3 = (int) bytearr[count - 1];
				if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
					throw new RuntimeException("UTFDataFormatException: malformed input around byte " + (count - 1));
				chararr[chararr_count++] = (char) (((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
				break;
			default:
				/* 10xx xxxx, 1111 xxxx */
				throw new RuntimeException("UTFDataFormatException: malformed input around byte " + count);
			}
		}
		return new String(chararr, 0, chararr_count);
	}

	/**
	 * @see java.io.DataInput#readUnsignedByte()
	 */
	public int readUnsignedByte() {
		checkEOF(1);
		return buf[pos++] & 0xFF;
	}

	/**
	 * @see java.io.DataInput#readUnsignedShort()
	 */
	public int readUnsignedShort() {
		return readShort() & 0xFFFF;
	}

	/**
	 * 跳到下一行
	 * @return 跳过的字节数(不包含换行)
	 *//*
	public int skip2NextLine() {
		if (pos == count)
			return -1;
		int len = 0;
		for (; pos < count; len++, pos++) {
			if (buf[pos] == '\n') {
				pos++;
				break;
			} else if (buf[pos] == '\r') {
				pos++;
				if (pos < count && buf[pos] == '\n')
					pos++;
				break;
			}
		}
		return len;
	}*/

/*	*//**
	 * 读取一个文本行。通过下列字符之一即可认为某行已终止：换行 ('\n')、回车 ('\r') 或回车后直接跟着换行。
	 * @return 包含该行内容的字符串，不包含任何行终止符，如果已到达流末尾，则返回 null
	 *//*
	public String readLine() {
		if (pos == count)
			return null;
		return new String(buf, pos, skip2NextLine());
	}

	*//**
	 * 读取一个文本行。通过下列字符之一即可认为某行已终止：换行 ('\n')、回车 ('\r') 或回车后直接跟着换行。
	 * @param enc 编码格式
	 * @return 包含该行内容的字符串，不包含任何行终止符，如果已到达流末尾，则返回 null
	 * @throws UnsupportedEncodingException 如果系统不支持指定的编码格式则抛出异常
	 *//*
	public String readLine(String enc) throws UnsupportedEncodingException {
		if (pos == count)
			return null;
		return new String(buf, pos, skip2NextLine(), enc);
	}
*/
	/**
	 * @see java.io.DataInput#skipBytes(int)
	 */
	public int skipBytes(int n) {
		int newPos = pos + n;
		if (newPos < 0)
			newPos = 0;
		else if (newPos > count)
			newPos = count;
		int skip = newPos - pos;
		pos = newPos;
		return skip;
	}

	/**
	 * @see java.io.InputStream#available()
	 */
	public int available() {
		return count - pos;
	}

	/**
	 * @see java.io.InputStream#mark(int)
	 */
	public void mark(int readlimit) {
		markPos = pos;
	}

	/**
	 * @see java.io.InputStream#markSupported()
	 */
	public boolean markSupported() {
		return true;
	}

	/**
	 * @see java.io.InputStream#reset()
	 */
	public void reset() {
		pos = markPos;
	}

	/**
	 * @see java.io.InputStream#skip(long)
	 */
	public long skip(long n) {
		return skipBytes((int) n);
	}

	/**
	 * @see java.io.InputStream#close()
	 */
	public void close() {
	}

	protected void checkEOF(int bytes) {
		if (pos + bytes > count)
			throw new RuntimeException("EOF Exception");
	}
}