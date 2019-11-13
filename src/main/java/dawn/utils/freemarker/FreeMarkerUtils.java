package dawn.utils.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class FreeMarkerUtils {

	/**
	 * 根据模版内容返回填充后的内容
	 *
	 * @param map         模版中的key-value
	 * @param templateTxt 模版内容
	 * @return
	 */
	public static String getFreeMarkerText(String templateTxt, Map<String, Object> map) throws Exception {
		String result;
		Configuration config = new Configuration();
		try {
			StringTemplateLoader strTemplate = new StringTemplateLoader();
			strTemplate.putTemplate("t", templateTxt);
			config.setTemplateLoader(strTemplate);
			config.setDefaultEncoding("UTF-8");
			Template template = config.getTemplate("t", "UTF-8");
			//输出流
			StringWriter out = new StringWriter();
			template.process(map, out);
			result = out.toString();
		} catch (IOException e) {
			throw new Exception("[FreeMarkerUtils] 获取freemark模版出错", e);
		} catch (TemplateException e) {
			throw new Exception("[FreeMarkerUtils] freemark模版处理异常", e);
		}
		return result;
	}

}
