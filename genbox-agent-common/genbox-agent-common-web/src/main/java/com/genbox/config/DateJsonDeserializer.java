package com.genbox.config;

import com.genbox.util.DateUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 配置类。
 */
public class DateJsonDeserializer extends JsonDeserializer<Date> {

	private static final Pattern P = Pattern.compile("^[0-9]*");
	private static final List<String> FORMAT = new ArrayList<>(4);

	static {
		FORMAT.add("yyyy-MM");
		FORMAT.add("yyyy-MM-dd");
		FORMAT.add("yyyy-MM-dd HH:mm");
		FORMAT.add("yyyy-MM-dd HH:mm:ss");
		FORMAT.add("yyyy/MM/dd HH:mm:ss");
	}

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return parseDate(p.getText());
	}

	public static Date parseDate(String str) {
		Date convertDate = null;

		if (str == null || "".equals(str)) {
			return null;
		}

		if (isNum(str)) {
			convertDate = DateUtils.parse(Long.valueOf(str));
		}
		else {
			if (str.matches("^\\d{4}-\\d{1,2}$")) {
				return DateUtils.parse(str, FORMAT.get(0));
			}
			else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
				return DateUtils.parse(str, FORMAT.get(1));
			}
			else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
				return DateUtils.parse(str, FORMAT.get(2));
			}
			else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
				return DateUtils.parse(str, FORMAT.get(3));
			}
			else if (str.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
				return DateUtils.parse(str, FORMAT.get(4));
			}
			else {
				throw new IllegalArgumentException("Invalid boolean value '" + str + "'");
			}
		}

		return convertDate;
	}

	public static boolean isNum(String number) {
		Matcher m = P.matcher(number);
		return m.matches();
	}
}
