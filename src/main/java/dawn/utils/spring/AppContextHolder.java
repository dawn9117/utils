package dawn.utils.spring;

import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AppContextHolder implements ApplicationContextAware {

	private static ApplicationContext context;

	/**
	 * 此方法可以把ApplicationContext对象inject到当前类中作为一个静态成员变量。
	 *
	 * @param applicationContext ApplicationContext 对象.
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	/**
	 * 获取ApplicationContext
	 *
	 * @return
	 */
	public ApplicationContext getApplicationContext() {
		return context;
	}

	/**
	 * @param beanName bean的名字
	 * @return 返回一个bean对象
	 */
	public Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	/**
	 * @param clazz bean类型
	 * @return 返回该类型的所有bean对象
	 */
	public <T> Map<String, T> getBeanMap(Class<T> clazz) {
		return context.getBeansOfType(clazz);
	}

	/**
	 * @param clazz bean类型
	 * @return 返回该类型的所有bean对象
	 */
	public <T> List<T> getBeanList(Class<T> clazz) {
		return Lists.newArrayList(context.getBeansOfType(clazz).values());
	}
}