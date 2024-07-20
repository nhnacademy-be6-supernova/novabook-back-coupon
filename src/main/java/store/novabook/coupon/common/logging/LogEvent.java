package store.novabook.coupon.common.logging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그 이벤트 정보를 담는 클래스입니다.
 */
@AllArgsConstructor
@Getter
@Setter
public class LogEvent {
	private String projectName;
	private String projectVersion;
	private String logVersion;
	private String body;
	private String logSource;
	private String logType;
	private String host;
	private String secretKey;
	private String logLevel;
	private String platform;
}
