package dawn.utils.http;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @author HEBO
 * @created 2019-09-24 18:22
 */
public class HttpClientUtils {

	// 设置连接超时时间，单位毫秒。
	private static final int CONNECT_TIMEOUT = 30 * 1000;

	// 请求获取数据的超时时间(即响应时间)，单位毫秒。
	private static final int SOCKET_TIMEOUT = 30 * 1000;

	/**
	 * 编码格式。发送编码格式统一用UTF-8
	 */
	private static final String CHARSET = "charset";
	private static final String ENCODING = "UTF-8";

	/**
	 * 表单提交user-agent
	 */
	private static final String FORM_USER_AGENT = "Mozilla/5.0(Windows NT 6.1;Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0";

	/**
	 * 表单提交content-type
	 */
	private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";


	/**
	 * httpClient
	 */
	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	/**
	 * requestConfig
	 */
	private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();

//	添加证书 TODO, 需要证书时添加此处
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
	 * @throws Exception
	 */
	public static HttpClientResult get(String url) throws Exception {
		return get(url, null);
	}

	/**
	 * 发送get请求；带请求参数
	 *
	 * @param url    请求地址
	 * @param params 请求参数集合
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult get(String url, Map<String, String> params) throws Exception {
		return get(url, null, params);
	}

	/**
	 * 发送get请求；带请求头和请求参数
	 *
	 * @param url     请求地址
	 * @param headers 请求头集合
	 * @param params  请求参数集合
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult get(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
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
	 */
	public static HttpClientResult getWithCallBack(String url, Map<String, String> headers, Map<String, String> params, Function<HttpResponse, String> function) throws Exception {
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
	public static HttpClientResult post(String url, Map<String, String> params) throws Exception {
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
	public static HttpClientResult post(String url, String params) throws Exception {
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
	public static HttpClientResult post(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
		// 执行请求并获得响应结果
		return post(url, headers, JSON.toJSONString(params));
	}

	/**
	 * 发送post请求；带请求头和请求参数
	 *
	 * @param url    请求地址
	 * @param params 请求参数集合
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult postForm(String url, Map<String, String> params) throws Exception {
		// 执行请求并获得响应结果
		return postForm(url, null, params, null);
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
	public static HttpClientResult postForm(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
		// 执行请求并获得响应结果
		return sendForm(new HttpPost(url), headers, params, null);
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
	public static HttpClientResult postForm(String url, Map<String, String> headers, Map<String, String> params, Function<HttpResponse, String> function) throws Exception {
		// 执行请求并获得响应结果
		return sendForm(new HttpPost(url), headers, params, function);
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
	public static HttpClientResult post(String url, Map<String, String> headers, String params) throws Exception {
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
	public static HttpClientResult put(String url) throws Exception {
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
	public static HttpClientResult put(String url, Map<String, String> params) throws Exception {
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
	public static HttpClientResult put(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
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
	public static HttpClientResult delete(String url) throws Exception {
		return delete(url, null, null);
	}

	/**
	 * 发送delete请求；不带请求参数
	 *
	 * @param url 请求地址
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult delete(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
		URI uri = buildURI(url, params);
		return send(new HttpDelete(uri), headers, null);
	}

	/**
	 * 发送请求并获取响应结果
	 *
	 * @param httpMethod
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult send(HttpRequestBase httpMethod, Map<String, String> headers, String params) throws Exception {
		return send(httpMethod, headers, params, null);
	}

	/**
	 * 发送请求并获取响应结果
	 *
	 * @param httpMethod
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public static HttpClientResult send(HttpRequestBase httpMethod, Map<String, String> headers, String params, Function<HttpResponse, String> function) throws Exception {
		try {
			prepareRequest(httpMethod, headers, params);

			// 执行请求
			return send(httpMethod, function);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * 发送请求并获取响应结果
	 *
	 * @param httpMethod
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult sendForm(HttpRequestBase httpMethod, Map<String, String> headers, Map<String, String> params, Function<HttpResponse, String> function) throws Exception {
		try {
			// 准备请求
			prepareFormRequest(headers, params, httpMethod);
			return send(httpMethod, function);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public static HttpClientResult send(HttpRequestBase httpMethod, Function<HttpResponse, String> function) throws IOException, Exception {
		// 执行请求
		try (CloseableHttpResponse httpResponse = httpClient.execute(httpMethod)) {
			if (httpResponse.getStatusLine() == null) {
				return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}

			if (Objects.isNull(function)) {
				String content = Objects.isNull(httpResponse.getEntity()) ? StringUtils.EMPTY : EntityUtils.toString(httpResponse.getEntity(), ENCODING);
				return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
			}

			// 有回调函数, 特殊处理response
			return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), function.apply(httpResponse));
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
		if (headers != null) {
			Set<Map.Entry<String, String>> entrySet = headers.entrySet();
			for (Map.Entry<String, String> entry : entrySet) {
				if (entry.getValue() == null) continue;
				httpMethod.addHeader(entry.getKey(), entry.getValue());
			}
		}

		// 设置请求体
		if (params != null && httpMethod instanceof HttpEntityEnclosingRequestBase) {
			HttpEntityEnclosingRequestBase method = (HttpEntityEnclosingRequestBase) httpMethod;
			method.setEntity(new StringEntity(params, ContentType.APPLICATION_JSON));
		}
	}

	/**
	 * Description: 封装请求参数
	 *
	 * @param params
	 * @param httpMethod
	 * @throws UnsupportedEncodingException
	 */
	private static void prepareFormRequest(Map<String, String> headers, Map<String, String> params, HttpRequestBase httpMethod) throws Exception {
		// 设置请求头
		httpMethod.setHeader(HTTP.CONTENT_TYPE, FORM_CONTENT_TYPE);
		httpMethod.setHeader(HTTP.USER_AGENT, FORM_USER_AGENT);
		httpMethod.setHeader(CHARSET, ENCODING);
		httpMethod.setConfig(requestConfig);
		if (MapUtils.isNotEmpty(headers)) {
			headers.forEach(httpMethod::setHeader);
		}
		if (MapUtils.isEmpty(params) || !(httpMethod instanceof HttpEntityEnclosingRequestBase)) {
			return;
		}

		// 设置表单参数
		List<NameValuePair> paramList = Lists.newArrayList();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		HttpEntityEnclosingRequestBase method = (HttpEntityEnclosingRequestBase) httpMethod;
		try {
			method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e);
		}
	}

	public static URI buildURI(String url, Map<String, String> params) throws Exception {
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
			throw new Exception(e);
		}
	}

	public static URI buildURI(String url) throws Exception {
		return buildURI(url, null);
	}

}
