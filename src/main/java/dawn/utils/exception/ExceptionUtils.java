package dawn.utils.exception;

import dawn.utils.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常工具类
 *
 * @author HEBO
 * @created 2019-05-13 11:44
 */
@Slf4j
public abstract class ExceptionUtils {

	public static AppRTException build(String code, String msg) {
		return new AppRTException(code, msg);
	}

	public static AppRTException build(ErrorCode error) {
		return build(error.getCode(), error.getMsg());
	}

	public static AppRTException build(ErrorCode error, Throwable throwable) {
		log.error("服务器异常", throwable);
		return build(error.getCode(), error.getMsg());
	}

	public static AppRTException build(String msg) {
		return build(ErrorCode.BIZ_EXCEPTION.getCode(), msg);
	}

	public static AppRTException build(Throwable throwable) {
		return build(ErrorCode.BIZ_EXCEPTION.getCode(), throwable.toString());
	}
}
