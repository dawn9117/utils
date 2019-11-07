package dawn.utils.http;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpClientResult {
	/**
	 * 响应状态码
	 */
	private int code;

	/**
	 * 响应数据
	 */
	private String content;

	public HttpClientResult(int code) {
		this.code = code;
	}

	public Boolean isSuccess() {
		return code == 200;
	}

	public Boolean hasContent() {
		return content != null;
	}

	public <T> T getContent(Class<T> clazz) {
		return JSON.parseObject(content, clazz);
	}

	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
