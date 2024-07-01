package store.novabook.coupon.coupon.dto.message;

/**
 * {@code MemberRegistrationMessage} 레코드는 회원 등록 메시지를 나타냅니다.
 * 이 메시지는 회원 ID를 포함합니다.
 *
 * @param memberId 등록된 회원의 ID
 */
public record MemberRegistrationMessage(Long memberId) {
}
