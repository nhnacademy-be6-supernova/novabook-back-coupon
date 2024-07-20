package store.novabook.coupon.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 타입 변환 유틸리티 클래스입니다.
 *
 * <p>이 클래스는 객체를 지정된 타입의 맵으로 캐스팅하는 메서드를 제공합니다.</p>
 */
public class TypeUtil {

	// 인스턴스화 방지
	private TypeUtil() {
	}

	/**
	 * 객체를 지정된 키와 값 타입의 맵으로 캐스팅합니다.
	 *
	 * @param <K>        맵의 키 타입
	 * @param <V>        맵의 값 타입
	 * @param obj        캐스팅할 객체
	 * @param keyClass   키 타입 클래스
	 * @param valueClass 값 타입 클래스
	 * @return 지정된 키와 값 타입의 맵
	 * @throws ClassCastException 객체가 맵이 아니거나 맵 엔트리를 지정된 타입으로 캐스팅할 수 없는 경우 예외 발생
	 */
	public static <K, V> Map<K, V> castMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
		if (obj instanceof Map<?, ?> rawMap) {
			Map<K, V> resultMap = new HashMap<>();
			for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
				Object rawKey = entry.getKey();
				Object rawValue = entry.getValue();
				if (keyClass.isInstance(rawKey) && valueClass.isInstance(rawValue)) {
					resultMap.put(keyClass.cast(rawKey), valueClass.cast(rawValue));
				} else {
					throw new ClassCastException("Cannot cast map entries to the specified types.");
				}
			}
			return resultMap;
		} else {
			throw new ClassCastException("Object is not a map.");
		}
	}
}
