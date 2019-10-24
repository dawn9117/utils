package dawn.utils.valid;

import dawn.utils.costant.ErrorCode;
import dawn.utils.exception.AppRTException;
import dawn.utils.exception.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * @author HEBO
 * @created 2019-05-20 13:57
 */
@Slf4j
public abstract class Asserts {

	public static void notNull(Object obj, String msg) throws AppRTException {
		if (obj == null) {
			log.info(msg);
			throw ExceptionUtils.build(msg);
		}
	}

	public static void notNull(Object obj, ErrorCode errorCode) throws AppRTException {
		if (obj == null) {
			log.info(errorCode.getMsg());
			throw ExceptionUtils.build(errorCode);
		}
	}

	public static void notEmpty(Collection<?> coll, String msg) throws AppRTException {
		if (coll == null || coll.size() == 0) {
			log.info(msg);
			throw ExceptionUtils.build(msg);
		}
	}


	public static void notEmpty(Collection<?> coll, ErrorCode errorCode) throws AppRTException {
		if (coll == null || coll.size() == 0) {
			log.info(errorCode.getMsg());
			throw ExceptionUtils.build(errorCode);
		}
	}

	public static void instanceOf(Object obj, Class<?> clazz, String msg) throws AppRTException {
		if (!clazz.isAssignableFrom(obj.getClass())) {
			log.info(msg);
			throw ExceptionUtils.build(msg);
		}
	}

	public static void instanceOf(Object obj, Class<?> clazz, ErrorCode errorCode) throws AppRTException {
		if (!clazz.isAssignableFrom(obj.getClass())) {
			log.info(errorCode.getMsg());
			throw ExceptionUtils.build(errorCode);
		}
	}

	public static void bigThen(BigDecimal var0, BigDecimal var1, String msg) throws AppRTException {
		if (var0 == null || var0.compareTo(var1) <= 0) {
			log.info(msg);
			throw ExceptionUtils.build(msg);
		}
	}

	public static void bigThen(BigDecimal var0, BigDecimal var1, ErrorCode errorCode) throws AppRTException {
		if (var0 == null || var0.compareTo(var1) <= 0) {
			log.info(errorCode.getMsg());
			throw ExceptionUtils.build(errorCode);
		}
	}

	public static void bigThenOrEquals(BigDecimal var0, BigDecimal var1, String msg) throws AppRTException {
		if (var0 == null || var0.compareTo(var1) < 0) {
			throw ExceptionUtils.build(msg);
		}
	}

}
