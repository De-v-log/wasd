package com.wasd.user.enums;

public enum MbtiEnum {

    ISTJ("Introverted", "Sensing", "Thinking", "Judging"),
    ISFJ("Introverted", "Sensing", "Feeling", "Judging"),
    INFJ("Introverted", "Intuitive", "Feeling", "Judging"),
    INTJ("Introverted", "Intuitive", "Thinking", "Judging"),
    ISTP("Introverted", "Sensing", "Thinking", "Perceiving"),
    ISFP("Introverted", "Sensing", "Feeling", "Perceiving"),
    INFP("Introverted", "Intuitive", "Feeling", "Perceiving"),
    INTP("Introverted", "Intuitive", "Thinking", "Perceiving"),
    ESTP("Extroverted", "Sensing", "Thinking", "Perceiving"),
    ESFP("Extroverted", "Sensing", "Feeling", "Perceiving"),
    ENFP("Extroverted", "Intuitive", "Feeling", "Perceiving"),
    ENTP("Extroverted", "Intuitive", "Thinking", "Perceiving"),
    ESTJ("Extroverted", "Sensing", "Thinking", "Judging"),
    ESFJ("Extroverted", "Sensing", "Feeling", "Judging"),
    ENFJ("Extroverted", "Intuitive", "Feeling", "Judging"),
    ENTJ("Extroverted", "Intuitive", "Thinking", "Judging");

    private final String attitude;
    private final String perception;
    private final String decision;
    private final String orientation;

    MbtiEnum(String attitude, String perception, String decision, String orientation) {
        this.attitude = attitude;
        this.perception = perception;
        this.decision = decision;
        this.orientation = orientation;
    }

    public String getAttitude() {
        return attitude;
    }

    public String getPerception() {
        return perception;
    }

    public String getDecision() {
        return decision;
    }

    public String getOrientation() {
        return orientation;
    }

}
