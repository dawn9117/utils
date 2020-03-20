package dawn.utils.serial;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

/**
 * JDK序列化对象后, 对象反序列化失败, 重写方法解决问题
 */
@Slf4j
@NoArgsConstructor
public class JdkObjectInputStream extends ObjectInputStream {

	public JdkObjectInputStream(InputStream arg0) throws IOException {
		super(arg0);
	}

	/**
	 * 重写resolveClass, 替换包名, 解决反序列化对象 包路径不对的问题
	 *
	 * @param desc
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		String name = desc.getName();
		try {
			// deserialization package为序列化对象的包名, target package为本地类的包名
			if (name.startsWith("deserialization package")) {
				name = name.replace("deserialization package", "target package");
			}
			return Class.forName(name);
		} catch (ClassNotFoundException ex) {
			log.error(this.getClass().getName() + " resolveClass error", ex);
		}
		return super.resolveClass(desc);
	}

	/**
	 * 解决serialVersionUID不一致反序列化失败的问题
	 * 重置序列化对象的serialVersionUID为1, 本地定义类serialVersionUID = 1
	 *
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
		ObjectStreamClass desc = super.readClassDescriptor();
		// serialVersionUID=1, 不需要处理
		if (desc.getSerialVersionUID() == 1)
			return desc;

		try {
			// 目标类1, target Object name为类名
			if (StringUtils.contains(desc.getName(), "target Object name")) {
				Field field = desc.getClass().getDeclaredField("suid");
				field.setAccessible(true);
				field.set(desc, 1L);
			}

			// 目标类2...


		} catch (Exception e) {
			log.error(this.getClass().getName() + " readClassDescriptor error", e);
		}
		return desc;
	}
}
