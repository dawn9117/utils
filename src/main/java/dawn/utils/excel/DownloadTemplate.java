package dawn.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DownloadTemplate {

	IMPORT_USER("templates/excel/import_user.xlsx", "XXXX", "xlsx"),
	IMPORT_DEPT("templates/excel/import_dept.xlsx", "XXXX", "xlsx"),


	;


	private String path;

	private String name;

	private String suffix;


}
