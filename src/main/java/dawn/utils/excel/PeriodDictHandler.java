package dawn.utils.excel;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.BiMap;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 字典帮助类
 * 使用guava缓存的版本, 会定时刷新缓存
 *
 * @author HEBO
 */
@Slf4j
public abstract class PeriodDictHandler implements IExcelDictHandler {

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
		try {
			return cache.get(dict);
		} catch (ExecutionException e) {
			log.error("查询字典失败" + ": " + e.getMessage());
			throw new RuntimeException("查询字典失败");
		}
	}


	////////////////////////////////////////////////////
	/////////////////  guava 缓存   /////////////////////
	////////////////////////////////////////////////////
	// guava线程池
	ListeningExecutorService refreshPools = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(2));

	LoadingCache<String, BiMap<String, String>> cache = CacheBuilder.newBuilder()
			.refreshAfterWrite(30, TimeUnit.MINUTES)
			.expireAfterAccess(1, TimeUnit.HOURS)
			.maximumSize(50)
			.build(new CacheLoader<String, BiMap<String, String>>() {
				@Override
				// 当本地缓存命没有中时，调用load方法获取结果并将结果缓存
				public BiMap<String, String> load(String key) {
					return load(key);
				}

				// 刷新
				@Override
				public ListenableFuture<BiMap<String, String>> reload(String key, BiMap<String, String> oldValue) throws Exception {
					log.info("刷新远程读取缓存到本地");
					return refreshPools.submit(() -> PeriodDictHandler.this.load(key));
				}
			});
}
