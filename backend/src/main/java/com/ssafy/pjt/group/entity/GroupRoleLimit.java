package com.ssafy.pjt.group.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRoleLimit {
    private int groupId;
    private int roleId;
    private int limitCount;
}
