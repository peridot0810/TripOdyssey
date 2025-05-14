INSERT INTO `user` (`id`, `pw`, `email`, `gender`, `nickname`, `age`) VALUES
('user01', '1111', 'user01@example.com', 'male', '지훈', 25),
('user02', '1111', 'user02@example.com', 'female', '민지', 23),
('user03', '1111', 'user03@example.com', 'male', '현우', 28),
('user04', '1111', 'user04@example.com', 'female', '수빈', 22),
('user05', '1111', 'user05@example.com', 'male', '도윤', 27),
('user06', '1111', 'user06@example.com', 'female', '예은', 24),
('user07', '1111', 'user07@example.com', 'male', '시우', 26),
('user08', '1111', 'user08@example.com', 'female', '하은', 21),
('user09', '1111', 'user09@example.com', 'male', '건우', 29),
('user10', '1111', 'user10@example.com', 'female', '유진', 23),
('user11', '1111', 'user11@example.com', 'male', '지후', 30),
('user12', '1111', 'user12@example.com', 'female', '서연', 24),
('user13', '1111', 'user13@example.com', 'male', '재윤', 25),
('user14', '1111', 'user14@example.com', 'female', '지아', 26),
('user15', '1111', 'user15@example.com', 'male', '하준', 22),
('user16', '1111', 'user16@example.com', 'female', '채원', 27),
('user17', '1111', 'user17@example.com', 'male', '우진', 28),
('user18', '1111', 'user18@example.com', 'female', '소율', 24),
('user19', '1111', 'user19@example.com', 'male', '승호', 29),
('user20', '1111', 'user20@example.com', 'female', '보라', 25);


INSERT INTO `role` (`name`) VALUES
('master'), ('schedule'), ('finance'), ('logistics'), ('normal');


INSERT INTO `travel_group` (`name`, `created_at`, `status`, `start_date`, `end_date`) VALUES
('제주도 우정여행', NOW(), 'planning', '2025-07-01', '2025-07-10');

INSERT INTO `group_role_limit` (`group_id`, `role_id`, `limit_count`) VALUES
(1, 1, 1),  -- 방장
(1, 2, 2),  -- 일정 담당
(1, 3, 1),  -- 재무 담당
(1, 4, 1);  -- 물류 담당

INSERT INTO `group_user_info` (`user_id`, `group_id`, `role_id`) VALUES
('user01', 1, 1), -- 방장
('user02', 1, 2), ('user03', 1, 2), -- 일정
('user04', 1, 3), -- 재무
('user05', 1, 4), -- 물류
('user06', 1, 5), ('user07', 1, 5),
('user08', 1, 5), ('user09', 1, 5),
('user10', 1, 5); -- 일반 멤버

INSERT INTO `available_schedule` (`group_id`, `user_id`, `start_date`, `end_date`) VALUES
(1, 'user01', '2025-07-10', '2025-07-10'),
(1, 'user01', '2025-07-11', '2025-07-11'),
(1, 'user02', '2025-07-10', '2025-07-10'),
(1, 'user02', '2025-07-12', '2025-07-12'),
(1, 'user03', '2025-07-11', '2025-07-11'),
(1, 'user03', '2025-07-13', '2025-07-13'),
(1, 'user04', '2025-07-10', '2025-07-10'),
(1, 'user04', '2025-07-12', '2025-07-12'),
(1, 'user05', '2025-07-10', '2025-07-10'),
(1, 'user05', '2025-07-11', '2025-07-11'),
(1, 'user06', '2025-07-11', '2025-07-11'),
(1, 'user06', '2025-07-12', '2025-07-12'),
(1, 'user07', '2025-07-12', '2025-07-12'),
(1, 'user07', '2025-07-13', '2025-07-13'),
(1, 'user08', '2025-07-11', '2025-07-11'),
(1, 'user08', '2025-07-13', '2025-07-13'),
(1, 'user09', '2025-07-10', '2025-07-10'),
(1, 'user09', '2025-07-12', '2025-07-12'),
(1, 'user10', '2025-07-10', '2025-07-10'),
(1, 'user10', '2025-07-11', '2025-07-11');



