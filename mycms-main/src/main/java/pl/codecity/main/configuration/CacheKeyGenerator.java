package pl.codecity.main.configuration;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class CacheKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		return method.toString() + " [" + StringUtils.arrayToCommaDelimitedString(params) + "]";
	}
}
