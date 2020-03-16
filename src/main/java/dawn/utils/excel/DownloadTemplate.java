package dawn.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DownloadTemplate {

	IMPORT_USER("templates/excel/import_user.xlsx", "中国联通视频云用户", "xlsx"),
	IMPORT_DEPT("templates/excel/import_dept.xlsx", "中国联通视频云用户", "xlsx"),


	;


	private String path;

	private String name;

	private String suffix;


}
