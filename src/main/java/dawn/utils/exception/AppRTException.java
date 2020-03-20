package dawn.utils.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class AppRTException extends Exception {

	public static final long serialVersionUID = 1L;

	/**
	 * 应用异常码
	 */
	private String code;

	/**
	 * 文本消息
	 */
	private String textMessage;

	/**
	 * 文本信息的参数
	 */
	private Object[] messageParams;

	public AppRTException(Throwable cause) {
		super(cause);
	}

	public AppRTException(String code) {
		this(code, null);
	}

	public AppRTException(String code, String msg) {
		this(code, msg, false);
	}


	public AppRTException(String code, String msg, boolean useParamFormat, Object... messageParams) {
		super(msg);
		this.code = code;
		this.textMessage = msg;
		if (useParamFormat) {
			this.messageParams = messageParams;
		}
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

}