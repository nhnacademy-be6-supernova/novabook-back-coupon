package store.novabook.coupon.common.adapter.dto;

import java.util.List;

/**
 * {@code GetMemberIdAllResponse} 클래스는 모든 회원 ID 응답을 담는 DTO입니다.
 *
 * @param memberIds 회원 ID 목록
 */
public record GetMemberIdAllResponse(List<Long> memberIds) {
}
