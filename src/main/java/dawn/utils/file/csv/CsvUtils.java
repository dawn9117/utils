package dawn.utils.file.csv;

import com.alibaba.fastjson.JSON;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.StringJoiner;

/**
 * 参考: https://github.com/apache/commons-csv
 * {@link org.apache.commons.csv}
 * apache的csv工具已经很灵活, 不需要自己造轮子
 *
 * @author HEBO
 * @created 2019-11-14 16:26
 */
public class CsvUtils {

	public static void main(String[] args) throws IOException {
		try (InputStream is = CsvUtils.class.getClassLoader().getResourceAsStream("file/csvdata");
			 Reader reader = new InputStreamReader(is)) {
			// 构建parser
			CSVParser parse = CSVParser.parse(reader, CSVFormat.DEFAULT.withHeader().withTrim());
			// 文件头, 标题行
			List<String> headers = parse.getHeaderNames();
			System.out.println(JSON.toJSONString(headers));

			// 内容行
			parse.getRecords().forEach(record -> {
				String split = "           ";
				StringJoiner joiner = new StringJoiner(split).add(record.get("column1")).add(record.get("column2")).add(record.get("column3"));
				System.out.println(joiner.toString());
			});
		}
	}

}
