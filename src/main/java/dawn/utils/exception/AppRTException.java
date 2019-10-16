package dawn.utils.exception;

public class AppRTException extends Exception {

	public static final long serialVersionUID = 0;

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

	public AppRTException(String code, String msg) {
		super(code + " : " + msg);
		this.code = code;
		this.textMessage = msg;
	}

	public AppRTException(String code) {
		super(code);
		this.code = code;
	}

	public AppRTException(String code, boolean useParamFormat, Object... messageParams) {
		super(code);
		this.code = code;
		if (useParamFormat) {
			this.messageParams = messageParams;
		}
	}

	public AppRTException(String code, String msg, Throwable cause) {
		super(code + ": " + msg, cause);
		this.code = code;
	}

	public AppRTException(Throwable cause) {
		super(cause);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String string) {
		code = string;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public Object[] getMessageParams() {
		return messageParams;
	}

	public void setMessageParams(Object[] messageParams) {
		this.messageParams = messageParams;
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

}