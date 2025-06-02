USE tripsaga;

-- 지출 카테고리 
INSERT INTO `expense_category` (`category_name`) 
VALUES 	('Transportation'),
		('Accommodation'),
		('Food'),
		('Leisure'),
		('Shared Supplies'),
		('Others');
        
-- 교통편 카테고리 
INSERT INTO transportation_type (type_name) VALUES 
('Airplane'),
('Train'),
('Bus'),
('Car'),
('Taxi'),
('Subway'),
('Ferry'),
('Bicycle'),
('Walking');

-- 게시글 카테고리
INSERT INTO post_category(category_name) VALUES
("Community"),
("Recruitment");


INSERT INTO `role` (`name`) VALUES
('master'), ('schedule'), ('finance'), ('logistics'), ('normal');