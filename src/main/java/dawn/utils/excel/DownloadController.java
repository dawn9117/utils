package dawn.utils.excel;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
public abstract class DownloadController {

	/**
	 * 通用下载
	 *
	 * @param response 响应对象
	 * @param fileName 下载的文件名
	 * @param in       文件输入流
	 */
	protected void download(HttpServletResponse response, String fileName, InputStream in) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
			IOUtils.copy(in, response.getOutputStream());
			in.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	/**
	 * 通过模板下载文件
	 *
	 * @param response 响应对象
	 * @param template 模板
	 */
	protected void download(HttpServletResponse response, DownloadTemplate template) {
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(template.getPath());
		download(response, template.getName() + "." + template.getSuffix(), stream);
	}


	/**
	 * Excel导出
	 *
	 * @param response 响应对象
	 */
	protected String exportExcel(List<?> list, String title, String sheetName, Class<?> clazz, String fileName, HttpServletResponse response) {
		ExportParams params = new ExportParams(title, sheetName, ExcelType.XSSF);
		params.setDictHandler(new DefaultDictHandler());
		try {
			ExcelUtils.exportExcel(list, clazz, fileName, params, response);
			return "导出成功";
		} catch (IOException e) {
			return "导出失败";
		}
	}


	public <M> List<M> importExcel(InputStream stream, Class<M> clazz) {
		return importExcel(stream, clazz, 1, 1);
	}

	public <M> List<M> importExcel(InputStream stream, Class<M> clazz, Integer titleRow, Integer headRow) {
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRow);
		params.setHeadRows(headRow);
		params.setNeedVerify(false);
		params.setDictHandler(new DefaultDictHandler());
		try {
			return ExcelUtils.importExcel(stream, params, clazz);
		} catch (IOException e) {
			log.error("导入失败:{}", e.getMessage());
			return null;
		}
	}


	class DefaultDictHandler extends DictHandler {

		@Override
		protected BiMap<String, String> load(String dict) {
//			List<DictModel> dictModels = dictService.queryDictItemsByCode(dict);
//			return dictModels.stream().collect(Collectors.toMap(DictModel::getValue, DictModel::getText, (oldValue, newValue) -> newValue, HashBiMap::create));
			return HashBiMap.create();
		}

	}


}
