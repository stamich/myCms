package pl.codecity.main.utilities;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Locale;

public class StringFormatter implements Formatter<String> {

	@Override
	public String print(String object, Locale locale) {
		return (!object.equals("") ? object : null);
	}

	@Override
	public String parse(String text, Locale locale) throws ParseException {
		String value = StringUtils.trimWhitespace(text);
		//value = Normalizer.normalize(value, Normalizer.Form.NFKC);
		return value;
	}
}
