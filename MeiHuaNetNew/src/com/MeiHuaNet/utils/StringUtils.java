package com.MeiHuaNet.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class StringUtils {

	/**
	 * 将json编码后的字符转换成html字符
	 * 
	 * @param str
	 * @return
	 */
	public static String getSummaryStr(String str) {
		String result = str;
		if (result != null && result.length() > 0) {
			result = handleStr(str);
			result = result.replaceAll("&ldquo;", "“");
			result = result.replaceAll("&rdquo;", "”");
			result = result.replaceAll("&quot;", "\"");
			result = result.replaceAll("&nbsp;", "");
			result = result.replaceAll("&bull;", "•");
			result = result.replaceAll("&mdash;", "-");
			result = result.replaceAll("\\s*|\t|\r|\n", "");
			result = result.trim();
		}
		return result;
	}

	/**
	 * 将String转换成int
	 * 
	 * @param countStr
	 * @return
	 */
	public static int stringToInteger(String countStr) {
		int count = 0;
		try {
			count = Integer.valueOf(countStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 去掉字符串中的html标签
	 * 
	 * @param detailStr
	 * @return
	 */
	public static String getDetailStr(String detailStr) {
		if (!TextUtils.isEmpty(detailStr)) {
			detailStr = detailStr.replaceAll("<p>", "");
			detailStr = detailStr.replaceAll("</p>", "");
			detailStr = detailStr.replaceAll("<br/>", "");
			detailStr = detailStr.replaceAll("<br />", "");
			return detailStr;
		}
		return detailStr;
	}

	/**
	 * 处理活动详情返回的内容
	 * 
	 * @param content
	 * @param isStrong
	 *            是否删除内容中的strong标签
	 * @return
	 */
	public static String GetEventDetailStr(String content, boolean isStrong) {
		content = "<body style=\"color:#333333 ; line-height: 150%\">"
				+ content + "</body>";
		String result = content;
		try {
			Pattern pattern;
			Matcher m = null;
			pattern = Pattern.compile("<p.*?>", Pattern.CASE_INSENSITIVE
					| Pattern.DOTALL);
			m = pattern.matcher(result);
			int step = 0;
			while (m.find(step)) {
				String s = m.group();
				result = result.replace(s, "<p>");
				step = m.start() + "<p>".length();
				m.reset(result);
			}
			pattern = Pattern.compile("<span.*?>", Pattern.CASE_INSENSITIVE
					| Pattern.DOTALL);
			m = pattern.matcher(result);
			int step1 = 0;
			while (m.find(step1)) {
				String s = m.group();
				result = result.replaceFirst(s, "<span>");
				step1 = m.start() + "<span>".length();
				m.reset(result);
			}
			pattern = Pattern.compile("<strong.*?>", Pattern.CASE_INSENSITIVE
					| Pattern.DOTALL);
			m = pattern.matcher(result);
			int step2 = 0;
			while (m.find(step2)) {
				String s = m.group();
				if (isStrong) {
					result = result.replaceFirst(s, "<strong>");
					step2 = m.start() + "<strong>".length();
				} else {
					result = result.replace(s, "");
					step2 = m.start();
				}
				m.reset(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 检查所给的字符串是否是正确的电话号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[^1^4,\\D]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		Pattern emailer = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		email = email.toLowerCase();
		if (email.endsWith(".con"))
			return false;
		if (email.endsWith(".cm"))
			return false;
		if (email.endsWith("@gmial.com"))
			return false;
		if (email.endsWith("@gamil.com"))
			return false;
		if (email.endsWith("@gmai.com"))
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 判断所给的字符串是否全部由英文字母或数字组成(长度6-16)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumericAndLetters(String str) {

		Pattern pattern = Pattern.compile("[a-zA-Z\\d]{6,16}");
		return pattern.matcher(str).matches();

	}

	/***
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		if (TextUtils.isEmpty(input)) {
			return "";
		}
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * * 去除特殊字符或将所有中文标号替换为英文标号
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		str = str.replaceAll("【", "[").replaceAll("】", "]")
				.replaceAll("！", "!").replaceAll("：", " : ")
				.replaceAll("（", "(").replaceAll("）", ")");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 处理文字排版不正常的问题
	 * 
	 * @param str
	 * @return
	 */
	public static String handleStr(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		String s = stringFilter(str);
		s = ToDBC(s);
		return s;
	}

	/**
	 * 处理分享到各个平台的内容（将内容中的<br />
	 * <p>
	 * 等标签去掉）
	 * 
	 * @param content
	 * @return
	 */
	public static String handleContent(String content) {
		if (content == null) {
			return "";
		}
		String result = content.replaceAll("<br />", "  ");
		result = result.replaceAll("<p>", " ");
		result = result.replaceAll("</p>", "");
		result = result.replaceAll("&middot;", ".");
		result = result.replaceAll("&ldquo;", "“");
		result = result.replaceAll("&rdquo;", "”");
		result = result.replaceAll("&nbsp;", "");
		result = result.replaceAll("&mdash;", "-");
		result = result.replaceAll("<strong>", "");// 这个标签有时会导致分享失败（分享到印象笔记时）
		result = result.replaceAll("</strong>", "");
		result = result.replaceAll("&", "&amp;");// &在xml中有特殊的含义，不替换的话分享到印象笔记会导致分享失败
		result = getNoPicStr(result);
		// 印象笔记分享的时候，组织的text内容是一个xml文件。所以分享的内容中如果出现了<>标签时容易报错，分享失败
		result = getNoBracketStr(result);
		return result;
	}

	/**
	 * 去掉字符串开头的img或embed标签，避免分享的内容以html文本出现
	 * 
	 * @param detail
	 * @return
	 */
	public static String getNoPicStr(String detail) {
		String result = detail;
		Pattern pattern = Pattern.compile("<img.*?>");
		Matcher m = pattern.matcher(detail);
		while (m.find()) {
			String s = m.group();
			result = result.replace(s, "");
		}
		Pattern p = Pattern.compile("<a.*?</a>");
		Matcher m1 = p.matcher(result);
		while (m1.find()) {
			String ss = m1.group();
			result = result.replace(ss, "");
		}
		Pattern p1 = Pattern.compile("<p.*?>");
		Matcher m2 = p1.matcher(result);
		while (m2.find()) {
			String s2 = m2.group();
			result = result.replace(s2, "");
		}
		return result;
	}

	/**
	 * 获取没有中括号标签的字符串
	 * 
	 * @param detail
	 * @return 没有<>标签的字符串
	 */
	public static String getNoBracketStr(String detail) {
		String result = detail;
		Pattern pattern = Pattern.compile("<.*?>");
		Matcher m = pattern.matcher(detail);
		while (m.find()) {
			String s = m.group();
			result = result.replace(s, "");
		}
		Pattern p = Pattern.compile("</.*?>");
		Matcher m1 = p.matcher(result);
		while (m1.find()) {
			String ss = m1.group();
			result = result.replace(ss, "");
		}
		return result;
	}

	/**
	 * 检查邮编是否 合法
	 * 
	 * @param value
	 * @return
	 */
	public static boolean checkPostCode(String value) {
		return value.matches("[1-9]\\d{5}(?!\\d)");
	}
	
	public static String handleEmbd(String content){
		Pattern pattern = Pattern.compile("<embed.*?>");
		Matcher matcher = pattern.matcher(content);
		int step = 0;
		String result = content;
		while(matcher.find(step)){
			String str = matcher.group();
			String strmodify = "";
			strmodify = str.replace("<embed", "<embed wmode=\"transparent\" style=\"z-index:-1\"");
			int end = matcher.end();
			step= end+strmodify.length()-"<embed".length();
			result = result.replace(str, strmodify);
			matcher.reset(result);
		}
		return result;
	}
}
