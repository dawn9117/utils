package dawn.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictModel implements Serializable {

	/**
	 * 字典value
	 */
	private String value;

	/**
	 * 字典名称
	 */
	private String name;


}
