package com.example.vitamate.domain.enums;


public enum ChallengeDuration {
    ONE_WEEK(7), ONE_MONTH(30), THREE_MONTHS(90), SIX_MONTHS(180), ONE_YEAR(365);

    private final int days;

    ChallengeDuration(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
