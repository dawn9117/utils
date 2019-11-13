package dawn.utils.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;

/**
 * PDF工具类
 */
public class PDFUtils {

	private static final String FONT_NAME = "STSongStd-Light";

	private static final String FONT_CODE = "UniGB-UCS2-H";

	/**
	 * 把内容以pdf的形式写入到file中
	 *
	 * @param content 写入的内容
	 * @param file    要写入的文件
	 * @throws Exception
	 */
	public static void createPdfByStr(String content, File file) throws Exception {
		/* 使用中文字体 */
		BaseFont bfChinese = BaseFont.createFont(FONT_NAME, FONT_CODE, BaseFont.NOT_EMBEDDED); // 中文处理  
		Font chinese = new Font(bfChinese);
		// 输出到本地，方便本地测试一下效果
		Document document = new Document(PageSize.A4, 80, 80, 20, 45);
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.add(new Paragraph(content, chinese));
		document.close();
	}

	/**
	 * 把html网页转换成pdf文件
	 *
	 * @param html
	 * @param file
	 * @throws Exception
	 */
	public static void createPdfByHtml(String html, File file) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);
		StringReader sr = new StringReader(html);
		PD4ML pd4ml = new PD4ML();
		pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
		pd4ml.setHtmlWidth(950);
		pd4ml.setPageSize(pd4ml.changePageOrientation(PD4Constants.A4));
		// 找到字体，才能解决中文问题
		pd4ml.useTTF("java:fonts", true);
		pd4ml.setDefaultTTFs("KaiTi_GB2312", "KaiTi_GB2312", "KaiTi_GB2312");
		pd4ml.enableDebugInfo();
		pd4ml.render(sr, fos);
	}

	public static void main(String[] args) throws Exception {
		String html = "<html>\n" +
				"<head>\n" +
				"<title>\n" +
				"head头部\n" +
				"</title>\n" +
				"</head>\n" +
				"<body>\n" +
				" <h1>Test模板头部</h1>\n" +
				"</body>\n" +
				"</html>";
		createPdfByHtml("你好，中国!", new File("D:\\a.pdf"));
	}
}
