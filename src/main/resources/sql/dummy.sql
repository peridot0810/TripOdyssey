USE tripsaga;

-- 지출 카테고리 
INSERT INTO `expense_category` (`category_name`) 
VALUES 	('Transportation'),
		('Accommodation'),
		('Food'),
		('Leisure'),
		('Shared Supplies'),
		('Others');


INSERT INTO `role` (`name`) VALUES
('master'), ('schedule'), ('finance'), ('logistics'), ('normal');

INSERT INTO `travel_group` (`name`, `created_at`, `status`, `start_date`, `end_date`) 
VALUES ('제주도 우정여행', NOW(), 'planning', '2025-07-01', '2025-07-10');


INSERT INTO `group_role_limit` (`group_id`, `role_id`, `limit_count`) 
VALUES  (1, 1, 1),  -- 방장
		(1, 2, 2),  -- 일정 담당
		(1, 3, 1),  -- 재무 담당
		(1, 4, 1);  -- 물류 담당

INSERT INTO `group_user_info` (`user_id`, `group_id`, `role_id`) 
VALUES	('user01', 1, 1), -- 방장
		('user02', 1, 2), ('user03', 1, 2), -- 일정
		('user04', 1, 3), -- 재무
		('user05', 1, 4), -- 물류
		('user06', 1, 5), ('user07', 1, 5),
		('user08', 1, 5), ('user09', 1, 5),
		('user10', 1, 5); -- 일반 멤버

INSERT INTO `available_schedule` (`group_id`, `user_id`, `start_date`, `end_date`) 
VALUES	(1, 'user01', '2025-07-10', '2025-07-10'),
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



