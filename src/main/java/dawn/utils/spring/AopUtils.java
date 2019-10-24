package dawn.utils.spring;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;

import java.lang.reflect.Field;

/**
 * @author HEBO
 * @created 2019-10-23 18:34
 */
public class AopUtils {

	/**
	 * 获取 目标对象
	 * @param proxy 代理对象
	 * @return
	 * @throws Exception
	 */
	public static Object getTarget(Object proxy) throws Exception {
		if(!org.springframework.aop.support.AopUtils.isAopProxy(proxy)) {
			return proxy;//不是代理对象
		}

		if(org.springframework.aop.support.AopUtils.isJdkDynamicProxy(proxy)) {
			return getJdkDynamicProxyTargetObject(proxy);
		} else { //cglib
			return getCglibProxyTargetObject(proxy);
		}
	}


	private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
		h.setAccessible(true);

		Object dynamicAdvisedInterceptor = h.get(proxy);
		Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
		advised.setAccessible(true);

		return ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
	}


	private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
		h.setAccessible(true);

		AopProxy aopProxy = (AopProxy) h.get(proxy);
		Field advised = aopProxy.getClass().getDeclaredField("advised");
		advised.setAccessible(true);

		return ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
	}
}
