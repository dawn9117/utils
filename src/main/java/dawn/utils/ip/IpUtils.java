package dawn.utils.ip;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * 客户端ip地址解析工具
 */
public class IpUtils {
	private static final String UNKNOWN = "unknown";

	public static String getIpAddress(ContainerRequestContext request, HttpServletRequest servletRequest) {
		String ip = request.getHeaderString("x-forwarded-for");
		if (ip != null && ip.indexOf(',') > 7)
			ip = ip.split(",")[0];

		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeaderString("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeaderString("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeaderString("WL-Proxy-Client-IP");
		}

		if (ip == null) {
			return servletRequest.getRemoteAddr();
		}

		return ip;
	}

	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.indexOf(',') > 7)
			ip = ip.split(",")[0];

		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null) {
			return request.getRemoteAddr();
		}
		return ip;
	}

}
