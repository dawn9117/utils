package dawn.utils.math;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author HEBO
 * @created 2019-10-30 9:59
 */
public final class BigDecimalUtils {


	public static BigDecimal multiply(int scale, int round, BigDecimal... bds) {
		if (ArrayUtils.isEmpty(bds)) {
			return null;
		}
		BigDecimal m = null;
		for (BigDecimal decimal : bds) {
			if (Objects.isNull(decimal)) {
				return null;
			}
			if (Objects.isNull(m)) {
				m = decimal;
				continue;
			}
			m = m.multiply(decimal);
		}
		return m.setScale(scale, round);
	}

	public static boolean between(BigDecimal origin, BigDecimal min, BigDecimal max) {
		return origin.compareTo(min) >= 0 && origin.compareTo(max) <= 0;
	}

	public static void main(String[] args) {
		BigDecimal multiply = multiply(0, 0, new BigDecimal(1), null);
		System.out.println(multiply);
		System.out.println(between(new BigDecimal("3.00001"), new BigDecimal("2"), new BigDecimal("3")));
	}

}
