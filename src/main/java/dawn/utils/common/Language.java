package dawn.utils.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
@AllArgsConstructor
public enum Language {

	ZH_CN("zh-CN", Locale.CHINA),
	EN_US("en-US", Locale.ENGLISH);

	private String language;
	private Locale locale;

	public static Language get(String language) {
		return Arrays.stream(values()).filter((item) -> item.language.equalsIgnoreCase(language)).findFirst().orElse(null);
	}
}
