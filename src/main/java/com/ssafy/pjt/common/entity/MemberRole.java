package com.ssafy.pjt.common.entity;

import lombok.Getter;

@Getter
public enum MemberRole {

    MASTER(1, "master"),
    SCHEDULE(2, "schedule"),
    FINANCE(3, "finance"),
    LOGISTICS(4, "logistics"),
    NORMAL(5, "normal");

    private final int id;
    private final String roleName;

    MemberRole(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
}
