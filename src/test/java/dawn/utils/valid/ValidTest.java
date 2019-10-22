package dawn.utils.valid;

import dawn.utils.dto.valid.ValidTestDto;

/**
 * @author HEBO
 * @created 2019-10-22 16:24
 */
public class ValidTest {

	public static void main(String[] args) {
		ValidationResult valid = ValidateUtils.valid(new ValidTestDto());

		if(valid.isHasErrors()) {
			System.out.println(valid.getMessage());
		}

	}
}
