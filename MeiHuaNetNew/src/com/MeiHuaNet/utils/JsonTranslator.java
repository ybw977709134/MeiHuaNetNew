package com.MeiHuaNet.utils;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import android.text.TextUtils;

/**
 * @description Json转换。用于Java Bean和Json之间的相互转换
 */
public class JsonTranslator {
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * @description Json反序列化
	 * @param srcBytes
	 * @param t
	 * @return
	 * @throws SerializeException
	 */
	public static <T> T deserialize(byte[] srcBytes, Class<T> t)
			throws SerializeException {
		if (srcBytes == null) {
			throw new IllegalArgumentException("srcBytes should not be null");
		}
		try {
			mapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,
					true);
			mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,
					true);
			mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			mapper.configure(
					org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			return mapper.readValue(srcBytes, 0, srcBytes.length, t);
		} catch (JsonParseException e) {
			throw new SerializeException(
					"Deserialize failed,cause by JsonParseException:{}", e);
		} catch (JsonMappingException e) {
			throw new SerializeException(
					"Deserialize failed,cause by JsonMappingException:{}", e);
		} catch (IOException e) {
			throw new SerializeException(
					"Deserialize failed,cause by IOException:{}", e);
		}
	}

	/**
	 * @description Json-->Java Bean
	 * @param srcValue
	 *            Json串
	 * @param t
	 *            Java Bean的范型
	 * @return
	 * @throws SerializeException
	 */
	public static <T> T deserialize(String srcValue, Class<T> t)
			throws SerializeException {
		if (TextUtils.isEmpty(srcValue)) {
			return null;
			// throw new
			// IllegalArgumentException("srcBytes should not be null");
		}
		try {
			mapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,
					true);
			mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,
					true);
			mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			mapper.configure(
					org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			mapper.configure(
					org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES,
					false);
			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
			mapper.configure(Feature.ALLOW_COMMENTS, true);
			return mapper.readValue(srcValue, t);
		} catch (JsonParseException e) {
			throw new SerializeException(
					"Deserialize failed,cause by JsonParseException:{}", e);
		} catch (JsonMappingException e) {
			throw new SerializeException(
					"Deserialize failed,cause by JsonMappingException:{}", e);
		} catch (IOException e) {
			throw new SerializeException(
					"Deserialize failed,cause by IOException:{}", e);
		}
	}

	/**
	 * @param <T>
	 * @description Json-->Java Bean
	 * @param srcValue
	 *            Json串
	 * @param t
	 *            Java Bean的范型
	 * @return 数组对象
	 * @throws SerializeException
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static <T> ArrayList<T> deserializeArray(String srcValue, Class<T> t)
			throws SerializeException {
		if (TextUtils.isEmpty(srcValue)) {
			return null;
			// throw new
			// IllegalArgumentException("srcBytes should not be null");
		}
		try {
			mapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,
					true);
			mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,
					true);
			mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			mapper.configure(
					org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			mapper.configure(
					org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES,
					false);
			mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
			mapper.configure(Feature.ALLOW_COMMENTS, true);
			return (ArrayList<T>) mapper.readValue(srcValue,
					TypeFactory.collectionType(ArrayList.class, t));
		} catch (JsonParseException e) {
			throw new SerializeException(
					"Deserialize failed,cause by JsonParseException:{}", e);
		} catch (JsonMappingException e) {
			throw new SerializeException(
					"Deserialize failed,cause by JsonMappingException:{}", e);
		} catch (IOException e) {
			throw new SerializeException(
					"Deserialize failed,cause by IOException:{}", e);
		}
	}

	/**
	 * @description JavaBean --> Json字节
	 * @param obj
	 * @return
	 * @throws SerializeException
	 */
	public static byte[] serialize(Object obj) throws SerializeException {
		try {
			return mapper.writeValueAsBytes(obj);
		} catch (JsonGenerationException e) {
			throw new SerializeException(
					"Serialize failed,cause by JsonGenerationException:{}", e);
		} catch (JsonMappingException e) {
			throw new SerializeException(
					"Serialize failed,cause by JsonMappingException:{}", e);
		} catch (IOException e) {
			throw new SerializeException(
					"Serialize failed,cause by IOException:{}", e);
		}
	}

	/**
	 * @description JavaBean --> Json串
	 * @param obj
	 * @return
	 * @throws SerializeException
	 */
	public static String serialize2String(Object obj) throws SerializeException {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			throw new SerializeException(
					"Serialize failed,cause by JsonGenerationException:{}", e);
		} catch (JsonMappingException e) {
			throw new SerializeException(
					"Serialize failed,cause by JsonMappingException:{}", e);
		} catch (IOException e) {
			throw new SerializeException(
					"Serialize failed,cause by IOException:{}", e);
		}
	}
}
