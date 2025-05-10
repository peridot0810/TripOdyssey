package com.ssafy.pjt.user.entity;

import lombok.Data;

@Data
public class User {
	private String id;
	private String pw;
	private String email;
	private String gender;
	private String nickname;
	private Integer age;
}
