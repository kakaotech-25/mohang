package moheng.member.domain;

import moheng.auth.exception.NoMatchingSocialTypeException;

import java.util.Arrays;
import java.util.Optional;

public enum SocialType {
    KAKAO, GOOGLE;

    public static boolean isMatches(SocialType input) {
        for(SocialType type : SocialType.values()) {
            if(type == input) return true;
        }
        return false;
    }

    public static SocialType findByName(String input) {
        return Arrays.stream(SocialType.values())
                .filter(type -> type.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(NoMatchingSocialTypeException::new);
    }
}
