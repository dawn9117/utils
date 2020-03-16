package dawn.utils.excel;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import com.google.common.collect.BiMap;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字典帮助类
 * 使用map缓存, 不会刷新缓存
 *
 * @author HEBO
 */
@Slf4j
public abstract class DictHandler implements IExcelDictHandler {

	/**
	 * 缓存所有的字典属性
	 */
	private Map<String, BiMap<String, String>> cache = new ConcurrentHashMap<>();

	/**
	 * 加载字典属性
	 *
	 * @param dict 字典code
	 * @return 字典code下的所有 value:name (K:value, V:name)
	 */
	protected abstract BiMap<String, String> load(String dict);

	/**
	 * 导出使用
	 *
	 * @param dict  字典code
	 * @param obj   实体对象
	 * @param name  字典子项name
	 * @param value 字典子项value
	 * @return
	 */
	@Override
	public String toName(String dict, Object obj, String name, Object value) {
		return getDictCache(dict).get(String.valueOf(value));
	}

	/**
	 * 导入使用
	 *
	 * @param dict  字典code
	 * @param obj   实体对象
	 * @param name  字典子项name
	 * @param value 字典子项value
	 * @return
	 */
	@Override
	public String toValue(String dict, Object obj, String name, Object value) {
		return getDictCache(dict).inverse().get(name);
	}

	private BiMap<String, String> getDictCache(String dict) {
		if (!cache.containsKey(dict)) {
			cache.put(dict, load(dict));
		}
		return cache.get(dict);
	}

}
