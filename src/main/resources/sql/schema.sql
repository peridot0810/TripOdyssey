-- MySQL Workbench Forward Engineering (최종 수정본)

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

DROP SCHEMA IF EXISTS `tripsaga`;
CREATE SCHEMA IF NOT EXISTS `tripsaga` DEFAULT CHARACTER SET utf8mb3;
USE `tripsaga`;

-- travel_group
CREATE TABLE IF NOT EXISTS `tripsaga`.`travel_group` (
  `group_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` ENUM('planning', 'onGoing', 'finished', 'canceled') NOT NULL,
  `start_date` DATE,
  `end_date` DATE,
  PRIMARY KEY (`group_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- user
CREATE TABLE IF NOT EXISTS `tripsaga`.`user` (
  `id` VARCHAR(100) NOT NULL,
  `pw` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `gender` ENUM('male', 'female') NOT NULL,
  `nickname` VARCHAR(100) NOT NULL,
  `age` INT UNSIGNED NOT NULL,
  `role` VARCHAR(45) NOT NULL DEFAULT "USER",
  `activated` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- location
CREATE TABLE IF NOT EXISTS `tripsaga`.`location` (
  `location_id` INT NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `type` VARCHAR(50),
  `imgUrl` VARCHAR(255),
  `tel` VARCHAR(45),
  `address` VARCHAR(100),
  `overview` VARCHAR(500),
  PRIMARY KEY (`location_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- content
CREATE TABLE IF NOT EXISTS `tripsaga`.`content` (
  `content_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100),
  `description` VARCHAR(500),
  `location_id` INT,
  PRIMARY KEY (`content_id`),
  FOREIGN KEY (`location_id`) REFERENCES `tripsaga`.`location` (`location_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- accommodation
CREATE TABLE IF NOT EXISTS `tripsaga`.`accommodation` (
  `accommodation_id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `name` VARCHAR(100),
  `check_in_time` DATETIME,
  `check_out_time` DATETIME,
  `location_id` INT NOT NULL,
  PRIMARY KEY (`accommodation_id`),
  FOREIGN KEY (`group_id`) REFERENCES `tripsaga`.`travel_group` (`group_id`),
  FOREIGN KEY (`location_id`) REFERENCES `tripsaga`.`location` (`location_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- schedule
CREATE TABLE IF NOT EXISTS `tripsaga`.`schedule` (
  `order` INT NOT NULL,
  `day` INT NOT NULL,
  `content_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  `is_official` BOOLEAN DEFAULT TRUE,
  PRIMARY KEY (`group_id`, `day`, `order`),
  FOREIGN KEY (`content_id`) REFERENCES `tripsaga`.`content` (`content_id`),
  FOREIGN KEY (`group_id`) REFERENCES `tripsaga`.`travel_group` (`group_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- schedule_proposal
CREATE TABLE IF NOT EXISTS `tripsaga`.`schedule_proposal` (
  `proposal_id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  `location_id` INT NOT NULL,
  PRIMARY KEY (`proposal_id`),
  FOREIGN KEY (`group_id`) REFERENCES `tripsaga`.`travel_group` (`group_id`),
  FOREIGN KEY (`location_id`) REFERENCES `tripsaga`.`location` (`location_id`),
  FOREIGN KEY (`user_id`) REFERENCES `tripsaga`.`user` (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- transportation_type
CREATE TABLE IF NOT EXISTS `tripsaga`.`transportation_type` (
  `type_id` INT NOT NULL AUTO_INCREMENT,
  `type_name` VARCHAR(50),
  PRIMARY KEY (`type_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- transportation
CREATE TABLE IF NOT EXISTS `tripsaga`.`transportation` (
  `transportation_id` INT NOT NULL AUTO_INCREMENT,
  `type_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  `departure` DATETIME,
  `arrival` DATETIME,
  `from` VARCHAR(100),
  `to` VARCHAR(100),
  `description` VARCHAR(255),
  PRIMARY KEY (`transportation_id`),
  FOREIGN KEY (`type_id`) REFERENCES `tripsaga`.`transportation_type` (`type_id`),
  FOREIGN KEY (`group_id`) REFERENCES `tripsaga`.`travel_group` (`group_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- role
CREATE TABLE IF NOT EXISTS `tripsaga`.`role` (
  `role_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- group_user_info
CREATE TABLE IF NOT EXISTS `tripsaga`.`group_user_info` (
  `user_id` VARCHAR(100) NOT NULL,
  `group_id` INT NOT NULL,
  `role_id` INT UNSIGNED NULL DEFAULT 5,
  PRIMARY KEY (`user_id`, `group_id`),
  FOREIGN KEY (`group_id`) REFERENCES `tripsaga`.`travel_group` (`group_id`) ON DELETE CASCADE,
  FOREIGN KEY (`role_id`) REFERENCES `tripsaga`.`role` (`role_id`),
  FOREIGN KEY (`user_id`) REFERENCES `tripsaga`.`user` (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- group_progress
CREATE TABLE IF NOT EXISTS `tripsaga`.`group_progress` (
  `group_id` INT NOT NULL,
  `schedule_coordination` BOOLEAN DEFAULT FALSE,
  `role_assignment` BOOLEAN DEFAULT FALSE,
  `trip_planning` BOOLEAN DEFAULT FALSE,
  `on_trip` BOOLEAN DEFAULT FALSE,
  `settlement` BOOLEAN DEFAULT FALSE,
  `finished` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`group_id`),
  FOREIGN KEY (`group_id`) REFERENCES `tripsaga`.`travel_group` (`group_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- group_role_limit
CREATE TABLE IF NOT EXISTS `tripsaga`.`group_role_limit` (
  `group_id` INT NOT NULL,
  `role_id` INT UNSIGNED NOT NULL,
  `limit_count` INT NOT NULL,
  PRIMARY KEY (`group_id`, `role_id`),
  FOREIGN KEY (`group_id`) REFERENCES `tripsaga`.`travel_group` (`group_id`),
  FOREIGN KEY (`role_id`) REFERENCES `tripsaga`.`role` (`role_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- user_experience
CREATE TABLE IF NOT EXISTS `tripsaga`.`user_experience` (
  `user_id` VARCHAR(100) NOT NULL,
  `leader_exp` INT DEFAULT 0,
  `schedule_exp` INT DEFAULT 0,
  `finance_exp` INT DEFAULT 0,
  `logistics_exp` INT DEFAULT 0,
  `member_exp` INT DEFAULT 0,
  PRIMARY KEY (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `tripsaga`.`user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- expense_category
CREATE TABLE IF NOT EXISTS `tripsaga`.`expense_category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(100),
  PRIMARY KEY (`category_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- expense_tracker
CREATE TABLE IF NOT EXISTS `tripsaga`.`expense_tracker` (
  `expense_id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `content_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  `datetime` DATETIME,
  `description` VARCHAR(255),
  `amount` INT,
  PRIMARY KEY (`expense_id`),
  FOREIGN KEY (`group_id`) REFERENCES `tripsaga`.`travel_group` (`group_id`),
  FOREIGN KEY (`content_id`) REFERENCES `tripsaga`.`content` (`content_id`),
  FOREIGN KEY (`category_id`) REFERENCES `tripsaga`.`expense_category` (`category_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- member_expense_info
CREATE TABLE IF NOT EXISTS `tripsaga`.`member_expense_info` (
  `user_id` VARCHAR(100) NOT NULL,
  `group_id` INT NOT NULL,
  `expense_to_pay` INT NOT NULL DEFAULT 0,
  `paid_amount` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`, `group_id`),
  FOREIGN KEY (`user_id`) REFERENCES `tripsaga`.`user` (`id`),
  FOREIGN KEY (`group_id`) REFERENCES `tripsaga`.`travel_group` (`group_id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- post_category
CREATE TABLE IF NOT EXISTS `tripsaga`.`post_category` (
  `category_id` INT AUTO_INCREMENT PRIMARY KEY,
  `category_name` VARCHAR(50) NOT NULL
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- post
CREATE TABLE IF NOT EXISTS `tripsaga`.`post` (
  `post_id` INT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(255) NOT NULL,
  `content` TEXT NOT NULL,
  `category_id` INT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `views` INT NOT NULL DEFAULT 0,
  `author` VARCHAR(100) NOT NULL,
  FOREIGN KEY (`category_id`) REFERENCES `tripsaga`.`post_category` (`category_id`),
  FOREIGN KEY (`author`) REFERENCES `tripsaga`.`user` (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- comment
CREATE TABLE IF NOT EXISTS `tripsaga`.`comment` (
  `comment_id` INT AUTO_INCREMENT PRIMARY KEY,
  `post_id` INT NOT NULL,
  `content` TEXT NOT NULL,
  `writer` VARCHAR(100) NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`post_id`) REFERENCES `tripsaga`.`post` (`post_id`),
  FOREIGN KEY (`writer`) REFERENCES `tripsaga`.`user` (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- post_like
CREATE TABLE IF NOT EXISTS `tripsaga`.`post_like` (
  `post_id` INT NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`post_id`, `user_id`),
  FOREIGN KEY (`post_id`) REFERENCES `tripsaga`.`post` (`post_id`),
  FOREIGN KEY (`user_id`) REFERENCES `tripsaga`.`user` (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

CREATE TABLE IF NOT EXISTS `available_schedule` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY uq_schedule_user_group_date (group_id, user_id, start_date, end_date),
  FOREIGN KEY (`group_id`) REFERENCES `travel_group`(`group_id`) ON DELETE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb3;

-- 마무리 설정 복구
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
