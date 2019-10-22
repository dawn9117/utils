package dawn.utils.dto.valid;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author HEBO
 * @created 2019-10-22 16:21
 */
@Data
public class ValidTestDto {

	@NotNull(message = "用户名不能为空")
	private String userName;

	@NotBlank(message = "密码不能为空")
	private String password;



}
