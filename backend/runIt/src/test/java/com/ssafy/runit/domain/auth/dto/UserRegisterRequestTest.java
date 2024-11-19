package com.ssafy.runit.domain.auth.dto;

import com.ssafy.runit.domain.auth.dto.request.UserRegisterRequest;
import com.ssafy.runit.domain.group.entity.Group;
import com.ssafy.runit.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRegisterRequestTest {

    @Test
    @DisplayName("Mapper 메서드가 올바르게 User 엔티티를 생성하는지 테스트")
    void mapper_CreatesUserEntity() {
        Group group = new Group();
        UserRegisterRequest request = UserRegisterRequest.builder()
                .userName("홍길동")
                .userImageUrl("http://example.com/image.jpg")
                .userNumber("1234567890")
                .build();
        User user = request.Mapper(group);
        assertThat(user.getUserName()).isEqualTo("홍길동");
        assertThat(user.getImageUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(user.getUserNumber()).isEqualTo("1234567890");
        assertThat(user.getUserGroup()).isEqualTo(group);
    }

    @Test
    @DisplayName("isValid 메서드가 필드의 null 여부를 올바르게 반환하는지 테스트")
    void isValid_ReturnsTrueWhenFieldsAreNotNull() {
        // 1. 테스트 입력 데이터 준비
        UserRegisterRequest request = UserRegisterRequest.builder()
                .userName("홍길동")
                .userImageUrl("http://example.com/image.jpg")
                .userNumber("1234567890")
                .build();
        boolean isValid = request.isValid();
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("isValid 메서드가 필드 중 하나라도 null이면 false를 반환하는지 테스트")
    void isValid_ReturnsFalseWhenAnyFieldIsNull() {
        UserRegisterRequest request1 = UserRegisterRequest.builder()
                .userName(null)
                .userImageUrl("http://example.com/image.jpg")
                .userNumber("1234567890")
                .build();
        assertThat(request1.isValid()).isFalse();
        UserRegisterRequest request2 = UserRegisterRequest.builder()
                .userName("홍길동")
                .userImageUrl(null)
                .userNumber("1234567890")
                .build();
        assertThat(request2.isValid()).isFalse();
        UserRegisterRequest request3 = UserRegisterRequest.builder()
                .userName("홍길동")
                .userImageUrl("http://example.com/image.jpg")
                .userNumber(null)
                .build();
        assertThat(request3.isValid()).isFalse();
    }
}
