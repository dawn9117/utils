package dawn.utils.costant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author HEBO
 * @created 2019-05-13 11:40
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {
	BIZ_EXCEPTION("-0001", "业务异常"),

	HTTP_10001("-10001", "http request header size error"),
	HTTP_10002("-10002", "http request execute error"),
	HTTP_10003("-10003", "http request execute failed"),
	HTTP_10004("-10004", "http request build uri failed"),
	HTTP_10005("-10005", "http request build form entity error"),

	;

	private String code;

	private String msg;

}
