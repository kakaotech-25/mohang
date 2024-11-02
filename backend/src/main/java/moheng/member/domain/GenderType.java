package moheng.member.domain;

public enum GenderType {
    MEN, WOMEN;

    public static boolean isMatches(GenderType input) {
        for(GenderType type : GenderType.values()) {
            if(type == input) return true;
        }
        return false;
    }
}
