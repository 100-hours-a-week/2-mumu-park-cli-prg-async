package dto;

public record GameProgressInfo(
        boolean isPlayer,
        int damage,
        int hp
) {
}
