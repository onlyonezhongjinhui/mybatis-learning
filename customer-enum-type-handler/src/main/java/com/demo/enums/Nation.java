package com.demo.enums;

public enum Nation {
    H("h", "汉族"),
    Z("z", "壮族");

    @EnumValue
    private final String value;
    private final String description;

    Nation(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

}
