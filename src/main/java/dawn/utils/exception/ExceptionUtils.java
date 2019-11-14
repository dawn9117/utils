package dawn.utils.exception;

import dawn.utils.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HEBO
 * @created 2019-05-13 11:44
 */
@Slf4j
public abstract class ExceptionUtils {

	public static AppRTException build(String code, String msg) {
		return new AppRTException(code, msg);
	}

	public static AppRTException build(ErrorCode error) {
		log.warn("[ExceptionUtils] build, code:{}, msg:{}", error.getCode(), error.getMsg());
		return build(error.getCode(), error.getMsg());
	}

	public static AppRTException build(String msg) {
		log.warn("[ExceptionUtils] build, code:{}, msg:{}", ErrorCode.BIZ_EXCEPTION.getCode(), msg);
		return build(ErrorCode.BIZ_EXCEPTION.getCode(), msg);
	}

	public static AppRTException build(Throwable throwable) {
		log.warn(ErrorCode.BIZ_EXCEPTION.getCode(), throwable);
		return build(ErrorCode.BIZ_EXCEPTION.getCode(), throwable.toString());
	}

	public static AppRTException build(ErrorCode error, Throwable throwable) {
		log.warn(error.getCode(), throwable);
		return build(error.getCode(), error.getMsg());
	}


}
