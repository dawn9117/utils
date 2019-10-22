package dawn.utils.http;

import com.alibaba.fastjson.JSON;
import dawn.utils.costant.ErrorCode;
import dawn.utils.exception.AppRTException;
import dawn.utils.exception.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyStore;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @author HEBO
 * @created 2019-09-24 18:22
 */
@Slf4j
public class HttpClientUtils {

	// 编码格式。发送编码格式统一用UTF-8
	private static final String ENCODING = "UTF-8";

	// 设置连接超时时间，单位毫秒。
	private static final int CONNECT_TIMEOUT = 30 * 1000;

	// 请求获取数据的超时时间(即响应时间)，单位毫秒。
	private static final int SOCKET_TIMEOUT = 30 * 1000;

	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();

//	添加证书
//	static{
//		try {
//			KeyStore keyStore = KeyStore.getInstance("PKCS12");
//			// pwd of .pfx
//			String pwd = "";
//			try (InputStream is = HttpClientUtils.class.getResourceAsStream("/cer.pfx")) {
//				if(is == null) {
//					log.error("[HttpClientUtils] load cer error");
//				}
//				keyStore.load(is, pwd.toCharArray());
//			}
//			// 相信自己的CA和所有自签名的证书.SSLContexts 4.3.x之后提供
//			//指定TLS版本
//			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, pwd.toCharArray()).build();
//			// 可选（"SSLv3", "TLSv1", "TLSv1.2"）
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
//					new DefaultHostnameVerifier());
//			httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//		} catch (Exception e) {
//			log.error("[HttpClientUtils] init error", e);
//		}
//	}

	/**
	 * 发送get请求；不带请求头和请求参数
	 *
	 * @param url 请求地址
	 * @return
	 * @throws AppRTException
	 */
	public static HttpClientResult get(String url) throws AppRTException {
		return get(url, null);
	}

	/**
	 * 发送get请求；带请求参数
	 *
	 * @param url    请求地址
	 * @param params 请求参数集合
	 * @return
	 * @throws AppRTException
	 */
	public static HttpClientResult get(String url, Map<String, String> params) throws AppRTException {
		return get(url, null, params);
	}

	/**
	 * 发送get请求；带请求头和请求参数
	 *
	 * @param url     请求地址
	 * @param headers 请求头集合
	 * @param params  请求参数集合
	 * @return
	 * @throws AppRTException
	 */
	public static HttpClientResult get(String url, Map<String, String> headers, Map<String, String> params) throws AppRTException {
		URI uri = buildURI(url, params);
		return send(new HttpGet(uri), headers, null);
	}

	/**
	 * 发送get请求；带请求头和请求参数
	 *
	 * @param url      请求地址
	 * @param headers  请求头集合
	 * @param params   请求参数集合
	 * @param function 回调函数, 定制化处理响应结果可用
	 * @return
	 * @throws AppRTException
	 */
	public static HttpClientResult getWithCallBack(String url, Map<String, String> headers, Map<String, String> params, Function<HttpResponse, String> function) throws AppRTException {
		URI uri = buildURI(url, params);
		return send(new HttpGet(uri), headers, null, function);
	}

	/**
	 * 发送post请求；不带请求头和请求参数
	 *
	 * @param url 请求地址
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult post(String url) throws Exception {
		return post(url, null, (String) null);
	}

	/**
	 * 发送post请求；带请求参数
	 *
	 * @param url    请求地址
	 * @param params 参数集合
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult post(String url, Map<String, String> params) throws AppRTException {
		return post(url, null, params);
	}

	/**
	 * 发送post请求；带请求参数
	 *
	 * @param url    请求地址
	 * @param params 参数集合
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult post(String url, String params) throws AppRTException {
		return post(url, null, params);
	}

	/**
	 * 发送post请求；带请求头和请求参数
	 *
	 * @param url     请求地址
	 * @param headers 请求头集合
	 * @param params  请求参数集合
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult post(String url, Map<String, String> headers, Map<String, String> params) throws AppRTException {
		// 执行请求并获得响应结果
		return post(url, headers, JSON.toJSONString(params));
	}

	/**
	 * 发送post请求；带请求头和请求参数
	 *
	 * @param url     请求地址
	 * @param headers 请求头集合
	 * @param params  请求参数集合
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult post(String url, Map<String, String> headers, String params) throws AppRTException {
		// 执行请求并获得响应结果
		return send(new HttpPost(url), headers, params);
	}

	/**
	 * 发送put请求；不带请求参数
	 *
	 * @param url 请求地址
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult put(String url) throws AppRTException {
		return put(url, null);
	}

	/**
	 * 发送put请求；带请求参数
	 *
	 * @param url    请求地址
	 * @param params 参数集合
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult put(String url, Map<String, String> params) throws AppRTException {
		return put(url, null, params);
	}

	/**
	 * 发送put请求；带请求参数
	 *
	 * @param url     请求地址
	 * @param headers 请求头
	 * @param params  参数集合
	 * @return HttpClientResult
	 * @throws Exception
	 */
	public static HttpClientResult put(String url, Map<String, String> headers, Map<String, String> params) throws AppRTException {
		URI uri = buildURI(url, params);
		return send(new HttpPut(uri), headers, null);
	}

	/**
	 * 发送delete请求；不带请求参数
	 *
	 * @param url 请求地址
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult delete(String url) throws AppRTException {
		return send(new HttpDelete(url), null, null);
	}

	/**
	 * 发送请求并获取响应结果
	 *
	 * @param httpMethod
	 * @return
	 * @throws Exception
	 */
	private static HttpClientResult send(HttpRequestBase httpMethod, Map<String, String> headers, String params) throws AppRTException {
		return send(httpMethod, headers, params, null);
	}

	/**
	 * 发送请求并获取响应结果
	 *
	 * @param httpMethod
	 * @return
	 * @throws Exception
	 */
	private static HttpClientResult send(HttpRequestBase httpMethod, Map<String, String> headers, String params, Function<HttpResponse, String> function) throws AppRTException {
		try {
			// 准备请求
			prepareRequest(httpMethod, headers, params);
			// 执行请求
			try (CloseableHttpResponse httpResponse = httpClient.execute(httpMethod)) {
				if (httpResponse.getStatusLine() == null) {
					log.info("[HttpClientUtils] send request uri[{}], response status line is null", httpMethod.getURI().toString());
					return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				}
				if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					log.info("[HttpClientUtils] send request uri[{}], response: [{}]", httpMethod.getURI().toString(), httpResponse.toString());
					throw ExceptionUtils.build(ErrorCode.HTTP_10002);
				}
				log.info("[HttpClientUtils] send request uri[{}], response success", httpMethod.getURI().toString());

				if (Objects.isNull(function)) {
					String content = Objects.isNull(httpResponse.getEntity()) ? StringUtils.EMPTY : EntityUtils.toString(httpResponse.getEntity(), ENCODING);
					return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
				}

				// 有回调函数, 特殊处理response
				return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), function.apply(httpResponse));
			}
		} catch (Exception e) {
			throw ExceptionUtils.build(ErrorCode.HTTP_10002, e);
		}
	}

	/**
	 * 准备请求
	 *
	 * @param httpMethod
	 * @param headers
	 * @param params
	 * @throws UnsupportedEncodingException
	 */
	private static void prepareRequest(HttpRequestBase httpMethod, Map<String, String> headers, String params) throws UnsupportedEncodingException {
		// 设置配置项
		httpMethod.setConfig(requestConfig);
		// 设置请求头
		setHeaders(headers, httpMethod);
		// 设置请求头
		setParams(params, httpMethod);
	}

	/**
	 * Description: 封装请求头
	 *
	 * @param params
	 * @param httpMethod
	 */
	private static void setHeaders(Map<String, String> params, HttpRequestBase httpMethod) {
		if (params != null) {
			Set<Map.Entry<String, String>> entrySet = params.entrySet();
			for (Map.Entry<String, String> entry : entrySet) {
				if (entry.getValue() == null) continue;
				httpMethod.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Description: 封装请求参数
	 *
	 * @param params
	 * @param httpMethod
	 * @throws UnsupportedEncodingException
	 */
	private static void setParams(String params, HttpRequestBase httpMethod) throws UnsupportedEncodingException {
		if (params != null && httpMethod instanceof HttpEntityEnclosingRequestBase) {
			HttpEntityEnclosingRequestBase method = (HttpEntityEnclosingRequestBase) httpMethod;
			method.setEntity(new StringEntity(params, ContentType.APPLICATION_JSON));
		}
	}

	private static URI buildURI(String url, Map<String, String> params) throws AppRTException {
		try {
			URIBuilder ub = new URIBuilder(url);
			if (params != null) {
				Set<Map.Entry<String, String>> entrySet = params.entrySet();
				for (Map.Entry<String, String> entry : entrySet) {
					if (entry.getValue() == null) continue;
					ub.setParameter(entry.getKey(), entry.getValue());
				}
			}
			return ub.build();
		} catch (Exception e) {
			throw ExceptionUtils.build(ErrorCode.HTTP_10004, e);
		}
	}

}
