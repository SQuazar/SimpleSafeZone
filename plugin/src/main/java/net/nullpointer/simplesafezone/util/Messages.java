package net.nullpointer.simplesafezone.util;

public enum Messages {
    NO_PERMISSION("no-permission"),
    UNKNOWN_COMMAND("unknown-command"),
    ONLY_PLAYERS("only-players"),
    ALREADY_EXISTS("already-exists"),
    INVALID_RADIUS("invalid-radius"),
    TYPE_NOT_FOUND("type-not-found"),
    MIN_RADIUS_RESTRICTION("min-radius-restriction"),
    MAX_RADIUS_RESTRICTION("max-radius-restriction"),
    SAFE_ZONE_OVERLAPS("safe-zone-overlaps"),
    ENTER_OPTION("enter-option"),
    OPTION_NOT_FOUND("option-not-found"),
    ENTER_RULE("enter-rule"),
    RULE_NOT_FOUND("rule-not-found"),
    ENTER_BOOLEAN("enter-boolean"),
    INVALID_BOOLEAN("invalid-boolean"),
    ENTER_ACTION("enter-action"),

    SAFE_ZONE_CREATED("safe-zone-created"),
    SAFE_ZONE_EXPIRED("safe-zone-expired"),
    SAFE_ZONE_DELETED("safe-zone-deleted"),
    SAFE_ZONE_NOT_FOUND("safe-zone-not-found"),
    SAFE_ZONE_SELECTED("safe-zone-selected"),
    SAFE_ZONE_DESELECTED("safe-zone-deselected"),

    ENTITIES_REMOVED("entities-removed"),
    ENTITIES_TP("entities-tp"),

    RULE_STATE_CHANGED("rule-state-changed"),

    RULE_BREAK_BLOCK_DISABLE("rule.break-block-disable"),
    RULE_BREAK_BLOCK_ENABLE("rule.break-block-enable"),
    RULE_PLACE_BLOCK_DISABLE("rule.place-block-disable"),
    RULE_PLACE_BLOCK_ENABLE("rule.place-block-enable"),
    RULE_PICKUP_DISABLE("rule.pickup-disable"),
    RULE_PICKUP_ENABLE("rule.pickup-enable");

    private final String key;

    Messages(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
