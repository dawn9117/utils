package dawn.utils.controller;

import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HEBO
 */
@RequestMapping("/csv")
@RestController
public class CsvController {

	@GetMapping("/export")
	public void write(HttpServletResponse res)  {
		String fileName = "test.csv";
		res.setHeader("content-type", "application/octet-stream");
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		List<String[]> users = getCsvUsers();
		try {
			CSVPrinter printer = new CSVPrinter(new StringBuffer(), CSVFormat.INFORMIX_UNLOAD_CSV);
			printer.printRecords(users);
//			printer.flush();
			res.getOutputStream().write(printer.getOut().toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			// TODO
		}
	}

	private List<String[]> getCsvUsers() {
		List<CsvUser> users = Lists.newArrayList();
		for (int i = 0; i < 100; i++) {
			CsvUser user = new CsvUser();
			user.setAge(i);
			user.setUsername("用户名" + i);
			user.setAddress("地址" + i);
			users.add(user);
		}

		List<String[]> records = users.stream().map(user -> {
			List<String> list = Lists.newArrayList();
			list.add(String.valueOf(user.getAge()));
			list.add(user.getUsername());
			list.add(user.getAddress());
			return list.toArray(new String[0]);
		}).collect(Collectors.toList());
		return records;
	}

}
