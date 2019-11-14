package dawn.utils.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dawn.utils.common.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

/**
 * 二维码生成工具
 */
public class QrCodeUtils {

	private static final Logger logger = LoggerFactory.getLogger(QrCodeUtils.class);


	public static byte[] getQRCode(String str, String imageType, int width, int height, byte[] logo) {
		return getQRCode(str, imageType, width, height, 0, logo);
	}

	/**
	 * 根据传入的参数生成二维码图片返回字节数组
	 *
	 * @param str
	 * @return
	 */
	public static byte[] getQRCode(String str, String imageType, int width, int height, int margin, byte[] logo) {
		byte[] resultByte = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(changeEncodeType(str), BarcodeFormat.QR_CODE, width, height, getQREncodeHintType(margin));
			//先生成二维码
			BufferedImage image = bitToImage(bitMatrix);
			if (null != logo && logo.length > 0) {
				//再添加logo
				Image logoImage = ImageIO.read(new ByteArrayInputStream(logo));
				Graphics2D g = image.createGraphics();
				compressLogoImage(image, logoImage);
				// logo宽高
				int mWidth = logoImage.getWidth(null);
				int mHeight = logoImage.getHeight(null);
				// logo起始位置，此目的是为logo居中显示
				int x = (image.getWidth() - mWidth) / 2;
				int y = (image.getHeight() - mHeight) / 2;
				g.drawImage(logoImage, x, y, mWidth, mHeight, null);
				g.dispose();
			}
			boolean b = ImageIO.write(image, imageType, bos);
			if (!b) {
				logger.error("生成二维码出错");
				return null;
			}
		} catch (WriterException e) {
			logger.error("生成二维码出错", e);
		} catch (IOException e) {
			logger.error("生成二维码出错", e);
		}
		resultByte = bos.toByteArray();
		return resultByte;
	}

	/**
	 * 将bit转换成image
	 *
	 * @param bitMatrix
	 * @return
	 */
	private static BufferedImage bitToImage(BitMatrix bitMatrix) {
		if (bitMatrix == null) {
			return null;
		}
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//填充图片
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? Constants.COLOR_BLACK : Constants.COLOR_WHITE);
			}
		}
		return image;
	}

	/**
	 * 转码
	 *
	 * @param str
	 * @return
	 */
	private static String changeEncodeType(String str) {
		try {
			return new String(str.getBytes(Constants.CHARSET_UTF_8), Constants.CHARSET_ISO_8859_1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}

	/**
	 * 设置QR二维码参数
	 *
	 * @param margin 二维码边框 默认0
	 * @return
	 */
	private static Hashtable<EncodeHintType, Object> getQREncodeHintType(int margin) {
		// 用于设置QR二维码参数
		Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别——这里选择中等级别
		qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		// 设置二维码边框的距离
		if (margin <= 0) {
			margin = 0;
		}
		qrParam.put(EncodeHintType.MARGIN, margin);
		return qrParam;
	}


	/**
	 * 压缩logo
	 *
	 * @param image
	 * @param logoImage
	 */
	public static void compressLogoImage(BufferedImage image, Image logoImage) {
		int w = image.getWidth();
		int h = image.getHeight();

		int width = logoImage.getWidth(null);
		int height = logoImage.getHeight(null);
		if (width > w) {
			width = w;
		}
		if (height > h) {
			height = h;
		}
		Image mImage = logoImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = tag.getGraphics();
		g.drawImage(mImage, 0, 0, null); // 绘制缩小后的图
		g.dispose();
		logoImage = mImage;
	}


	public static void main(String[] args) throws Exception {
		//读取logo
		FileInputStream fis = new FileInputStream(new File("D:/logo.jpg"));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[fis.available()];
		//读取字节到buf
		fis.read(buf);
		//将buf写到bos
		bos.write(buf);
		fis.close();
		bos.close();

		String imagePath = "D:/target.jpg";
		byte[] byteArr = getQRCode("这里是测试：http://www.aladingbank.com", Constants.IMAGE_TYPE_JPG, 500, 500, bos.toByteArray());
		File file = new File(imagePath);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(byteArr);
		fos.flush();
		fos.close();
	}

}
