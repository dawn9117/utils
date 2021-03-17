package dawn.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.extra.compress.CompressUtil;
import cn.hutool.extra.compress.extractor.Extractor;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dawn.utils.http.HttpClientResult;
import dawn.utils.jackson.CustomizeLocalDateTimeDeserializer;
import lombok.Data;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author HEBO
 */
public class MainTest {


	int max = 900000000;

	/**
	 * 根据id值算出邀请码
	 *
	 * @param num
	 * @return
	 */
	public int getCode(int num) {
		num = max - (num * 88) ^ 123;
		return num;
	}

	/**
	 * 根据邀请码逆运算id值
	 *
	 * @param code
	 * @return
	 */
	public int getNum(int code) {
		code = ((max - code) ^ 123) / 88 + 1;
		return code;
	}

	@Test
	public void test() {
		MainTest test = new MainTest();
		int code = test.getCode(1051112);
		int num = test.getNum(code);
		System.out.println(code);
		System.out.println(num);
	}

	@Test
	public void subString() {
		String str = "/home/v2.0.0/product/2/2/source";
		System.out.println(getBaseDir("/product/123/123/source", str));

	}

	@Test
	public void runtimeTest() {
		System.out.println(RuntimeUtil.execForStr("/bin/bash", "-c", "source /Users/jiabei/dev/test.sh"));
		System.out.println("jieshu");
	}

	@Test
	public void fileTest() throws FileNotFoundException {
		File file = FileUtil.file("/Users/jiabei/dev/asd/asd/asdsa/log.sql");
		FileUtil.appendUtf8Lines(Lists.newArrayList("2312312"), file);
		FileUtil.appendUtf8Lines(Lists.newArrayList("2312312"), file);
		FileUtil.appendUtf8Lines(Lists.newArrayList("2312312"), file);
		FileUtil.appendUtf8Lines(Lists.newArrayList("2312312"), file);
		FileUtil.appendUtf8Lines(Lists.newArrayList("2312312"), file);
		FileUtil.appendUtf8Lines(Lists.newArrayList("2312312"), file);
		FileUtil.appendUtf8Lines(Lists.newArrayList("2312312"), file);
		System.out.println();
	}

	@Test
	public void reduce() {
		ArrayList<String> strings = Lists.newArrayList("a", "b", "c");
		String reduce = strings.stream().reduce(null, (a, b) -> {
			System.out.println(a);
			System.out.println(b);
			System.out.println("======");
			return a + b;
		});
		System.out.println(reduce);
	}

	@Test
	public void flatMap() {
		Map<String, List<String>> map = Maps.newHashMap();
		map.put("1", Lists.newArrayList("1", "2", "3"));
		map.put("2", Lists.newArrayList("4", "5", "6"));
		map.put("3", Lists.newArrayList("7", "8", "9"));
		List<String> strings = map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
		System.out.println(strings);
	}

	@Test
	public void finallyTest() {
		ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		for (Integer integer : integers) {
			try {
				if (integer == 3) {
					System.out.println(3 / 0);
				}
			} catch (Exception e) {
				break;
			} finally {
				System.out.println(integer);
			}
		}
	}

	@Test
	public void tryFinallyTest() {
		ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		for (Integer integer : integers) {
			try {
				if (integer == 3) {
					System.out.println(3 / 0);
				}
			} finally {
				System.out.println(integer);
			}
		}
	}

	@Test
	public void testSh() throws InterruptedException {
		String dir = "/Users/jiabei/Downloads";
		String file = "test.sh";
		StringJoiner cmd = new StringJoiner(";");
		cmd.add("cd " + dir);
		cmd.add(String.format("source %s", file));
		cmd.add("cd " + dir + ";");
		Process process = RuntimeUtil.exec("/bin/bash", "-c", cmd.toString());
		if (process.waitFor(3, TimeUnit.MINUTES) && process.exitValue() != 0) {
			throw new RuntimeException(String.format("执行脚本失败:%s, exitValue:%s, 异常信息:%s", cmd.toString(), process.exitValue(), IoUtil.readUtf8(process.getInputStream())));
		}
		System.out.println(IoUtil.readUtf8(process.getInputStream()));
	}

	@Test
	public void testAES() throws Exception {
		String uuid = RandomUtil.randomString(16).toUpperCase();
		System.out.println(uuid);
		String base64 = new AES(uuid.getBytes()).encryptBase64("v1.0.0");
		System.out.println(base64);
		String str = new AES(uuid.getBytes()).decryptStr(base64);
		System.out.println(str);
	}

	@Test
	public void testDay() {
		DateTime begin = DateUtil.beginOfDay(new Date());

		DateTime offsetDay = DateUtil.offsetDay(begin, -1);
		DateTime end = DateUtil.endOfDay(new Date());
		System.out.println(DateUtil.format(begin, DatePattern.NORM_DATETIME_FORMAT));
		System.out.println(DateUtil.format(end, DatePattern.NORM_DATETIME_FORMAT));
		System.out.println(DateUtil.format(offsetDay, DatePattern.NORM_DATETIME_FORMAT));
	}

	@Test
	public void testCompress() {
//		CompressorOutputStream outputStream = new CompressUtil().getOut(CompressorStreamFactory.GZIP, FileUtil.getOutputStream("/Users/jiabei/Downloads/test.gz"));
//		IoUtil.copy(FileUtil.getInputStream("/Users/jiabei/Downloads/2.1.0.zip"), outputStream);
		CompressorInputStream in = new CompressUtil().getIn(CompressorStreamFactory.GZIP, FileUtil.getInputStream("/Users/jiabei/Downloads/test.gz"));
		Extractor extractor = CompressUtil.createExtractor(Charset.defaultCharset(), in);
		extractor.extract(FileUtil.file("/Users/jiabei/Downloads/111"));
		extractor.close();

	}

	@Test
	public void stock() {
		//初始资金
		final BigDecimal init = new BigDecimal("100000");
		//每次拿总资金的多少冒险
		final BigDecimal rate = new BigDecimal("0.2");
		//平均盈利幅度
		final BigDecimal win = new BigDecimal("0.20");
		//止损点
		final BigDecimal lose = new BigDecimal("-0.05");
		//周期内交易次数
		final BigDecimal trade_time = new BigDecimal("200");
		//正确进场概率
		final BigDecimal right_chosen = new BigDecimal("0.33");
		//最低最终收益
		BigDecimal top_rate = init;
		//最高最终收益
		BigDecimal low_rate = init;
		//测试的次数
		final int test_time = 10000;
		for (int i = 0; i < test_time; i++) {
			final AtomicInteger win_count = new AtomicInteger(0);
			final AtomicInteger lose_count = new AtomicInteger(0);
			final AtomicReference<BigDecimal> base = new AtomicReference<>(init);
			IntStream.range(0, trade_time.intValue()).forEach(
					c -> {
						double v = Math.random() * 1;
						BigDecimal se;
						if (v < right_chosen.doubleValue()) {
							se = win;
							win_count.incrementAndGet();
						} else {
							se = lose;
							lose_count.incrementAndGet();
						}
						BigDecimal finalSe = se;
						base.getAndUpdate(operand -> (operand.multiply((BigDecimal.ONE.subtract(rate))).add(operand.multiply(rate).multiply(BigDecimal.ONE.add(finalSe)))));
					}
			);
			final BigDecimal l = base.get();
			top_rate = top_rate.max(l);
			low_rate = low_rate.min(l);
		}
		System.out.println("highest earn rate: " + top_rate.divide(init).toPlainString());
		System.out.println("lowest  earn rate: " + low_rate.divide(init).toPlainString());
	}

	@Test
	public void json() {
		String str = "{32:{\"createTime\":1614057965000,\"delFlag\":\"0\",\"enableFlag\":\"1\",\"id\":40,\"params\":{},\"productId\":32,\"route\":\"/data/insight/product/upload/product/BASE_SERVICE_CloudBeaver_20.0.3/cloudbeaver.tar\",\"scriptInstall\":\"/data/insight/product/upload/product/BASE_SERVICE_CloudBeaver_20.0.3/cloudbeaver.sh\",\"sort\":1,\"version\":\"20.0.3\"},1:{\"createTime\":1608687011000,\"delFlag\":\"0\",\"enableFlag\":\"1\",\"id\":1,\"params\":{},\"productId\":1,\"route\":\"/data/insight/product/upload/product/BASE_ENV_Docker-CE_v19/docker.tar.gz\",\"scriptInstall\":\"/data/insight/product/upload/product/BASE_ENV_Docker-CE_v19/script.sh\",\"scriptUninstall\":\"/data/insight/product/upload/product/BASE_ENV_Docker-CE_v19/docker-uninstall.sh\",\"sort\":1,\"version\":\"v19\"},4:{\"createTime\":1608713256000,\"delFlag\":\"0\",\"enableFlag\":\"1\",\"id\":4,\"params\":{},\"productId\":4,\"route\":\"/data/insight/product/upload/product/BASE_ENV_OpenJDK_jdk1.8/jdk-8u251-linux-x64.tar.gz\",\"scriptInstall\":\"/data/insight/product/upload/product/BASE_ENV_OpenJDK_jdk1.8/script.sh\",\"scriptUninstall\":\"/data/insight/product/upload/product/BASE_ENV_OpenJDK_jdk1.8/jdk-uninstall.sh\",\"sort\":1,\"version\":\"jdk1.8\"},5:{\"createTime\":1608713390000,\"delFlag\":\"0\",\"enableFlag\":\"1\",\"id\":5,\"params\":{},\"productId\":5,\"route\":\"/data/insight/product/upload/product/BASE_ENV_insight-install_v1.0.0/insight-install.jar\",\"scriptInstall\":\"/data/insight/product/upload/product/BASE_ENV_insight-install_v1.0.0/script.sh\",\"scriptUninstall\":\"\",\"scriptUpgrade\":\"/data/insight/product/upload/product/BASE_ENV_insight-install_v1.0.0/insight-upgrade.sh\",\"sort\":1,\"version\":\"v1.0.0\"},12:{\"createTime\":1611713134000,\"delFlag\":\"0\",\"enableFlag\":\"1\",\"id\":27,\"params\":{},\"productId\":12,\"route\":\"/data/insight/product/upload/product/DATABASE_mysql_5.7.28/mysql-5.7.28.tar\",\"scriptInstall\":\"/data/insight/product/upload/product/DATABASE_mysql_5.7.28/script.sh\",\"scriptUninstall\":\"/data/insight/product/upload/product/DATABASE_mysql_5.7.28/uninstall.sh\",\"sort\":1,\"version\":\"5.7.28\"},28:{\"createTime\":1612013184000,\"delFlag\":\"0\",\"enableFlag\":\"1\",\"id\":36,\"params\":{},\"productId\":28,\"route\":\"/data/insight/product/upload/product/SERVICE_VISION_vision-system-register_2.1.0/vision-system-register.jar\",\"scriptInstall\":\"/data/insight/product/upload/product/SERVICE_VISION_vision-system-register_2.1.0/script-register.sh\",\"sort\":1,\"version\":\"2.1.0\"},13:{\"createTime\":1611823691000,\"delFlag\":\"0\",\"enableFlag\":\"1\",\"id\":28,\"params\":{},\"productId\":13,\"route\":\"/data/insight/product/upload/product/DATABASE_redis_5.0.6/redis.tar\",\"scriptInstall\":\"/data/insight/product/upload/product/DATABASE_redis_5.0.6/script.sh\",\"scriptUninstall\":\"/data/insight/product/upload/product/DATABASE_redis_null/uninstall.sh\",\"sort\":1,\"version\":\"5.0.6\"},29:{\"createTime\":1612014538000,\"delFlag\":\"0\",\"enableFlag\":\"1\",\"id\":37,\"params\":{},\"productId\":29,\"route\":\"/data/insight/product/upload/product/SERVICE_VISION_vision-system-gateway_null/vision-system-gateway.jar\",\"scriptInstall\":\"/data/insight/product/upload/product/SERVICE_VISION_vision-system-gateway_2.1.0/script-gateway.sh\",\"sort\":1,\"version\":\"2.1.0\"},14:{\"createTime\":1611883634000,\"delFlag\":\"0\",\"enableFlag\":\"1\",\"id\":29,\"params\":{},\"productId\":14,\"route\":\"/data/insight/product/upload/product/SERVICE_VISION_pre-deploy_2.1.0/sql.tar\",\"scriptInstall\":\"/data/insight/product/upload/product/SERVICE_VISION_pre-deploy_2.1.0/script.sh\",\"scriptUninstall\":\"/data/insight/product/upload/product/SERVICE_VISION_pre-deploy_2.1.0/pre-deploy-uninstall.sh\",\"sort\":1,\"version\":\"2.1.0\"},30:{\"createTime\":1612014587000,\"delFlag\":\"0\",\"enableFlag\":\"1\",\"id\":38,\"params\":{},\"productId\":30,\"route\":\"/data/insight/product/upload/product/SERVICE_VISION_vision-system-auth_null/vision-system-auth.jar\",\"scriptInstall\":\"/data/insight/product/upload/product/SERVICE_VISION_vision-system-auth_2.1.0/script-auth.sh\",\"sort\":1,\"version\":\"2.1.0\"}}";
		System.out.println(JSON.toJSONString(JSON.parse(str)));
	}

	@Test
	public void testInteger() {
		System.out.println(Objects.equals(null, 1));
	}

	@Test
	public void testCopy() {
		HttpClientResult result = new HttpClientResult();
		result.setCode(1);
		HttpClientResult result2 = new HttpClientResult();
		BeanUtils.copyProperties(result, result2);
		result.setContent("haha");
		BeanUtils.copyProperties(result2, result);
		System.out.println(result.getContent());
	}

	@Test
	public void testLocalDatetime() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		LocalTimeTest test = new LocalTimeTest();
		test.setTime(LocalDateTime.now());
		String string = mapper.writeValueAsString(test);
		System.out.println(string);
		LocalTimeTest value = mapper.readValue(string, LocalTimeTest.class);
		System.out.println(value);
	}

	private String getBaseDir(String sourcePath, String fullPath) {
		while (sourcePath.contains("/")) {
			sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf("/"));
			fullPath = fullPath.substring(0, fullPath.lastIndexOf("/"));
		}
		return fullPath;
	}

	@Data
	public static class LocalTimeTest {

		@JsonDeserialize(using = CustomizeLocalDateTimeDeserializer.class)
		private LocalDateTime time;

	}

}
