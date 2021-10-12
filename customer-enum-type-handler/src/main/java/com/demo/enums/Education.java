package com.demo.enums;

public enum Education implements IEnum<Integer> {
    PRIMARY_SCHOOL(0, "小学"),
    JUNIOR_MIDDLE_SCHOOL(1, "初中");

    private final Integer value;
    private final String description;

    Education(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
