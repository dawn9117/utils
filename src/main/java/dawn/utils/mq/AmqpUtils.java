package dawn.utils.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * @Description amqp 工具类
 */
public class AmqpUtils {


	/**
	 * 发送消息值默认exchange及roteKey
	 *
	 * @param amqpTemplate
	 * @param message
	 */
	public static void send(AmqpTemplate amqpTemplate, Object message) {
		amqpTemplate.convertAndSend(message);
	}

	/**
	 * 发送消息值默认exchange及指定roteey
	 *
	 * @param amqpTemplate
	 * @param roteKey
	 * @param message
	 */
	public static void send(AmqpTemplate amqpTemplate, String roteKey, Object message) {
		amqpTemplate.convertAndSend(roteKey, message);
	}


	/**
	 * 发送消息到amqp协议队列
	 *
	 * @param amqpTemplate
	 * @param exchange
	 * @param roteKey
	 * @param message
	 */
	public static void send(AmqpTemplate amqpTemplate, String exchange, String roteKey, Object message) {
		amqpTemplate.convertAndSend(exchange, roteKey, message);
	}

	/**
	 * 发送延迟消息到amqp协议队列
	 *
	 * @param amqpTemplate 消息模板
	 * @param exchange     交换机
	 * @param roteKey      路由key
	 * @param message      消息
	 * @param second       延迟（秒）
	 */
	public static void send(AmqpTemplate amqpTemplate, String exchange, String roteKey, Object message, int second) {
		if (second <= 0) {
			// 立马发送，不进死信队列
			send(amqpTemplate, exchange, roteKey, message);
		}
		MessagePostProcessor processor = msg -> {
			// rabbitmq的延迟时间毫秒
			msg.getMessageProperties().setExpiration(second * 1000 + "");
			return msg;
		};
		amqpTemplate.convertAndSend(exchange, roteKey, message, processor);
	}

}
