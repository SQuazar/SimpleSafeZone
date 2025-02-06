package net.nullpointer.simplesafezone.util;

public enum Config {
    AUTO_CUI("auto-cui"),
    LIFE_TIME("life-time"),
    STORE_ZONES("store-zones"),
    SAFE_ZONE_DEFAULT_RADIUS("safezone.default-radius"),
    SAFE_ZONE_MAX_RADIUS("safezone.max-radius"),
    SAFE_ZONE_MIN_RADIUS("safezone.min-radius"),
    SAFE_ZONE_DEFAULT_TYPE("safezone.default-type"),;

    private final String key;

    Config(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
