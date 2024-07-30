package moheng.member.domain;

import static moheng.fixture.MemberFixtures.하온_이메일;
import static moheng.fixture.MemberFixtures.하온_닉네임;
import static moheng.fixture.MemberFixtures.하온_프로필_경로;
import static moheng.fixture.MemberFixtures.하온_소셜_타입_카카오;
import static moheng.fixture.MemberFixtures.하온_소셜_타입_구글;
import static moheng.fixture.MemberFixtures.하온_생년월일;
import static moheng.fixture.MemberFixtures.하온_성별;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.member.exception.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberTest {

    @DisplayName("카카오 소셜로그인 회원을 생성한다.")
    @Test
    public void 카카오_소셜로그인_회원을_생성한다() {
        // given, when, then
        assertDoesNotThrow(() ->
                new Member(하온_이메일, 하온_소셜_타입_카카오));
    }

    @DisplayName("구글 소셜로그인 회원을 생성한다.")
    @Test
    public void 구글_소셜로그인_회원을_생성한다() {
        // given, when, then
        assertDoesNotThrow(() ->
                new Member(하온_이메일, 하온_소셜_타입_구글));
    }

    @DisplayName("이메일 형식이 올바르지 않다면 예외가 발생한다.")
    @Test
    public void 이메일_형식이_올바르지_않다면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> new Member("invalid email", 하온_소셜_타입_구글))
                .isInstanceOf(InvalidEmailFormatException.class);
    }

    @DisplayName("닉네임 형식이 올바르지 않다면 예외가 발생한다.")
    @Test
    public void 닉네임_형식이_올바르지_않다면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> new Member(1L, 하온_이메일,
                "qweiqnweiqnweiqwieqniweiqweiqwneiqnweqwienwqeqieqnweiqwneiqwenqweiqnweiqweqweqweqweinqwneiqwei",
                하온_프로필_경로, 하온_소셜_타입_카카오, 하온_생년월일, 하온_성별))
                .isInstanceOf(InvalidNicknameFormatException.class);
    }

    @DisplayName("존재하지 않는 소셜 로그인 제공처라면 예외가 발생한다.")
    @Test
    public void 존재하지_않는_소셜_로그인_제공처라면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> new Member(1L, 하온_이메일,하온_닉네임,
                하온_프로필_경로, null, 하온_생년월일, 하온_성별))
                .isInstanceOf(NoExistSocialTypeException.class);
    }

    @DisplayName("성별 형식이 올바르지 않다면 예외가 발생한다.")
    @Test
    public void 성별_형식이_올바르지_않다면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> new Member(1L, 하온_이메일,
                하온_닉네임, 하온_프로필_경로,
                하온_소셜_타입_카카오, 하온_생년월일, null))
                .isInstanceOf(InvalidGenderFormatException.class);
    }

    @DisplayName("생년월일이 현재 날짜보다 이후라면 예외가 발생한다.")
    @Test
    public void 생년월일이_현재_날짜보다_이후라면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> new Member(1L, 하온_이메일,
                하온_닉네임, 하온_프로필_경로,
                하온_소셜_타입_카카오, LocalDate.of(2200, 1, 1), 하온_성별))
                .isInstanceOf(InvalidBirthdayException.class);
    }
}
