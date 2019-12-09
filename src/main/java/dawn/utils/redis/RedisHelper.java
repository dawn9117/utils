package dawn.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author HEBO
 * @created 2019-11-21 18:37
 */
@Component
public class RedisHelper {

	@Autowired
	private StringRedisTemplate redisTemplate;

	public void set() {
		redisTemplate.opsForValue().set("key1", "v1");
	}
}
