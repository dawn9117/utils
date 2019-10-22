package dawn.utils.valid;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author HEBO
 * @created 2019-10-22 15:20
 */
public class ValidationResult {

	/**
	 * 是否有异常
	 */
	private boolean hasErrors;

	/**
	 * 异常消息记录
	 */
	private Map<String, String> errorMsg;

	/**
	 * 获取异常消息组装
	 *
	 * @return
	 */
	public String getMessage() {
		if (errorMsg == null || errorMsg.isEmpty()) {
			return StringUtils.EMPTY;
		}
		return JSON.toJSONString(errorMsg);
	}

	public boolean isHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	public Map<String, String> getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(Map<String, String> errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		return "ValidationResult{" +
				"hasErrors=" + hasErrors +
				", errorMsg=" + errorMsg +
				'}';
	}
}
