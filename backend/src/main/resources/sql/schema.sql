-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema tripsaga
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `tripsaga`;
CREATE SCHEMA IF NOT EXISTS `tripsaga` DEFAULT CHARACTER SET utf8mb3 ;
USE `tripsaga` ;

-- -----------------------------------------------------
-- Table `tripsaga`.`travel_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`travel_group` (
  `group_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` ENUM('planning', 'onGoing', 'finished', 'canceled') NOT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  `profile_image` VARCHAR(255) NULL,
  PRIMARY KEY (`group_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`sidos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`sidos` (
  `no` INT NOT NULL AUTO_INCREMENT COMMENT '시도번호',
  `sido_code` INT NOT NULL COMMENT '시도코드',
  `sido_name` VARCHAR(20) NULL DEFAULT NULL COMMENT '시도이름',
  PRIMARY KEY (`no`),
  UNIQUE INDEX `sido_code_UNIQUE` (`sido_code` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 35
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '시도정보테이블';


-- -----------------------------------------------------
-- Table `tripsaga`.`guguns`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`guguns` (
  `no` INT NOT NULL AUTO_INCREMENT COMMENT '구군번호',
  `sido_code` INT NOT NULL COMMENT '시도코드',
  `gugun_code` INT NOT NULL COMMENT '구군코드',
  `gugun_name` VARCHAR(20) NULL DEFAULT NULL COMMENT '구군이름',
  PRIMARY KEY (`no`),
  INDEX `guguns_sido_to_sidos_cdoe_fk_idx` (`sido_code` ASC) VISIBLE,
  INDEX `gugun_code_idx` (`gugun_code` ASC) VISIBLE,
  CONSTRAINT `guguns_sido_to_sidos_cdoe_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `tripsaga`.`sidos` (`sido_code`))
ENGINE = InnoDB
AUTO_INCREMENT = 469
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '구군정보테이블';


-- -----------------------------------------------------
-- Table `tripsaga`.`contenttypes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`contenttypes` (
  `content_type_id` INT NOT NULL COMMENT '콘텐츠타입번호',
  `content_type_name` VARCHAR(45) NULL DEFAULT NULL COMMENT '콘텐츠타입이름',
  PRIMARY KEY (`content_type_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '콘텐츠타입정보테이블';


-- -----------------------------------------------------
-- Table `tripsaga`.`attractions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`attractions` (
  `no` INT NOT NULL AUTO_INCREMENT COMMENT '명소코드',
  `content_id` INT NULL DEFAULT NULL COMMENT '콘텐츠번호',
  `title` VARCHAR(500) NULL DEFAULT NULL COMMENT '명소이름',
  `content_type_id` INT NULL DEFAULT NULL COMMENT '콘텐츠타입',
  `area_code` INT NULL DEFAULT NULL COMMENT '시도코드',
  `si_gun_gu_code` INT NULL DEFAULT NULL COMMENT '구군코드',
  `first_image1` VARCHAR(100) NULL DEFAULT NULL COMMENT '이미지경로1',
  `first_image2` VARCHAR(100) NULL DEFAULT NULL COMMENT '이미지경로2',
  `map_level` INT NULL DEFAULT NULL COMMENT '줌레벨',
  `latitude` DECIMAL(20,17) NULL DEFAULT NULL COMMENT '위도',
  `longitude` DECIMAL(20,17) NULL DEFAULT NULL COMMENT '경도',
  `tel` VARCHAR(20) NULL DEFAULT NULL COMMENT '전화번호',
  `addr1` VARCHAR(100) NULL DEFAULT NULL COMMENT '주소1',
  `addr2` VARCHAR(100) NULL DEFAULT NULL COMMENT '주소2',
  `homepage` VARCHAR(1000) NULL DEFAULT NULL COMMENT '홈페이지',
  `overview` VARCHAR(10000) NULL DEFAULT NULL COMMENT '설명',
  PRIMARY KEY (`no`),
  INDEX `attractions_typeid_to_types_typeid_fk_idx` (`content_type_id` ASC) VISIBLE,
  INDEX `attractions_sido_to_sidos_code_fk_idx` (`area_code` ASC) VISIBLE,
  INDEX `attractions_sigungu_to_guguns_gugun_fk_idx` (`si_gun_gu_code` ASC) VISIBLE,
  CONSTRAINT `attractions_area_to_sidos_code_fk`
    FOREIGN KEY (`area_code`)
    REFERENCES `tripsaga`.`sidos` (`sido_code`),
  CONSTRAINT `attractions_sigungu_to_guguns_gugun_fk`
    FOREIGN KEY (`si_gun_gu_code`)
    REFERENCES `tripsaga`.`guguns` (`gugun_code`),
  CONSTRAINT `attractions_typeid_to_types_typeid_fk`
    FOREIGN KEY (`content_type_id`)
    REFERENCES `tripsaga`.`contenttypes` (`content_type_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 107559
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '명소정보테이블';


-- -----------------------------------------------------
-- Table `tripsaga`.`accommodation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`accommodation` (
  `accommodation_id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `name` VARCHAR(100) NULL DEFAULT NULL,
  `check_in_time` DATETIME NULL DEFAULT NULL,
  `check_out_time` DATETIME NULL DEFAULT NULL,
  `attractions_no` INT NULL,
  PRIMARY KEY (`accommodation_id`),
  INDEX `group_id` (`group_id` ASC) VISIBLE,
  INDEX `fk_accommodation_attractions1_idx` (`attractions_no` ASC) VISIBLE,
  CONSTRAINT `accommodation_ibfk_1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`travel_group` (`group_id`),
  CONSTRAINT `fk_accommodation_attractions1`
    FOREIGN KEY (`attractions_no`)
    REFERENCES `tripsaga`.`attractions` (`no`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`user` (
  `id` VARCHAR(100) NOT NULL,
  `pw` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `gender` ENUM('male', 'female') NOT NULL,
  `nickname` VARCHAR(100) NOT NULL,
  `age` INT UNSIGNED NOT NULL,
  `role` VARCHAR(45) NOT NULL DEFAULT 'USER',
  `activated` TINYINT(1) NOT NULL DEFAULT '1',
  `profile_image` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`available_schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`available_schedule` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_schedule_user_group_date` (`group_id` ASC, `user_id` ASC, `start_date` ASC, `end_date` ASC) VISIBLE,
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `available_schedule_ibfk_1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`travel_group` (`group_id`)
    ON DELETE CASCADE,
  CONSTRAINT `available_schedule_ibfk_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`user` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`post_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`post_category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`post` (
  `post_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `content` TEXT NOT NULL,
  `category_id` INT NOT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `views` INT NOT NULL DEFAULT 0,
  `author` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`post_id`),
  INDEX `category_id` (`category_id` ASC) VISIBLE,
  INDEX `author` (`author` ASC) VISIBLE,
  CONSTRAINT `post_ibfk_1`
    FOREIGN KEY (`category_id`)
    REFERENCES `tripsaga`.`post_category` (`category_id`),
  CONSTRAINT `post_ibfk_2`
    FOREIGN KEY (`author`)
    REFERENCES `tripsaga`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`comment` (
  `comment_id` INT NOT NULL AUTO_INCREMENT,
  `post_id` INT NOT NULL,
  `content` TEXT NOT NULL,
  `writer` VARCHAR(100) NOT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_id`),
  INDEX `post_id` (`post_id` ASC) VISIBLE,
  INDEX `writer` (`writer` ASC) VISIBLE,
  CONSTRAINT `comment_ibfk_1`
    FOREIGN KEY (`post_id`)
    REFERENCES `tripsaga`.`post` (`post_id`)
    ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_2`
    FOREIGN KEY (`writer`)
    REFERENCES `tripsaga`.`user` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`content`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`content` (
  `content_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL DEFAULT NULL,
  `description` VARCHAR(500) NULL DEFAULT NULL,
  `attractions_no` INT NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  UNIQUE INDEX `uq_name_attraction` (`name` ASC, `attractions_no` ASC) VISIBLE,
  INDEX `fk_content_attractions1_idx` (`attractions_no` ASC) VISIBLE,
  CONSTRAINT `fk_content_attractions1`
    FOREIGN KEY (`attractions_no`)
    REFERENCES `tripsaga`.`attractions` (`no`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`expense_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`expense_category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`expense_tracker`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`expense_tracker` (
  `expense_id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `content_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  `datetime` DATETIME NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `amount` INT NULL DEFAULT NULL,
  PRIMARY KEY (`expense_id`),
  INDEX `group_id` (`group_id` ASC) VISIBLE,
  INDEX `content_id` (`content_id` ASC) VISIBLE,
  INDEX `category_id` (`category_id` ASC) VISIBLE,
  CONSTRAINT `expense_tracker_ibfk_1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`travel_group` (`group_id`),
  CONSTRAINT `expense_tracker_ibfk_2`
    FOREIGN KEY (`content_id`)
    REFERENCES `tripsaga`.`content` (`content_id`),
  CONSTRAINT `expense_tracker_ibfk_3`
    FOREIGN KEY (`category_id`)
    REFERENCES `tripsaga`.`expense_category` (`category_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`group_progress`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`group_progress` (
  `group_id` INT NOT NULL,
  `schedule_coordination` TINYINT(1) NULL DEFAULT '0',
  `role_assignment` TINYINT(1) NULL DEFAULT '0',
  `trip_planning` TINYINT(1) NULL DEFAULT '0',
  `on_trip` TINYINT(1) NULL DEFAULT '0',
  `settlement` TINYINT(1) NULL DEFAULT '0',
  `finished` TINYINT(1) NULL DEFAULT '0',
  PRIMARY KEY (`group_id`),
  CONSTRAINT `group_progress_ibfk_1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`travel_group` (`group_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`role` (
  `role_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`role_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`group_role_limit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`group_role_limit` (
  `group_id` INT NOT NULL,
  `role_id` INT UNSIGNED NOT NULL,
  `limit_count` INT NOT NULL,
  PRIMARY KEY (`group_id`, `role_id`),
  INDEX `role_id` (`role_id` ASC) VISIBLE,
  CONSTRAINT `group_role_limit_ibfk_1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`travel_group` (`group_id`),
  CONSTRAINT `group_role_limit_ibfk_2`
    FOREIGN KEY (`role_id`)
    REFERENCES `tripsaga`.`role` (`role_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`group_user_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`group_user_info` (
  `user_id` VARCHAR(100) NOT NULL,
  `group_id` INT NOT NULL,
  `role_id` INT UNSIGNED NULL DEFAULT '5',
  PRIMARY KEY (`user_id`, `group_id`),
  INDEX `group_id` (`group_id` ASC) VISIBLE,
  INDEX `role_id` (`role_id` ASC) VISIBLE,
  CONSTRAINT `group_user_info_ibfk_1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`travel_group` (`group_id`)
    ON DELETE CASCADE,
  CONSTRAINT `group_user_info_ibfk_2`
    FOREIGN KEY (`role_id`)
    REFERENCES `tripsaga`.`role` (`role_id`),
  CONSTRAINT `group_user_info_ibfk_3`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`member_expense_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`member_expense_info` (
  `user_id` VARCHAR(100) NOT NULL,
  `group_id` INT NOT NULL,
  `expense_to_pay` INT NOT NULL DEFAULT '0',
  `paid_amount` INT NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`, `group_id`),
  INDEX `group_id` (`group_id` ASC) VISIBLE,
  CONSTRAINT `member_expense_info_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`user` (`id`),
  CONSTRAINT `member_expense_info_ibfk_2`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`travel_group` (`group_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`post_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`post_like` (
  `post_id` INT NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`post_id`, `user_id`),
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `post_like_ibfk_1`
    FOREIGN KEY (`post_id`)
    REFERENCES `tripsaga`.`post` (`post_id`)
    ON DELETE CASCADE,
  CONSTRAINT `post_like_ibfk_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`user` (`id`)
    ON DELETE CASCADE
    )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`schedule` (
  `order` INT NULL DEFAULT NULL,
  `day` INT NULL DEFAULT NULL,
  `content_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  `is_official` TINYINT(1) NULL DEFAULT '0',
  PRIMARY KEY (`content_id`),
  UNIQUE INDEX `uq_order_day` (`order` ASC, `day` ASC) VISIBLE,
  INDEX `content_id` (`content_id` ASC) VISIBLE,
  INDEX `schedule_ibfk_2` (`group_id` ASC) VISIBLE,
  CONSTRAINT `schedule_ibfk_1`
    FOREIGN KEY (`content_id`)
    REFERENCES `tripsaga`.`content` (`content_id`)
    ON DELETE CASCADE,
  CONSTRAINT `schedule_ibfk_2`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`travel_group` (`group_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`schedule_proposal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`schedule_proposal` (
  `proposal_id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  `attractions_no` INT NOT NULL,
  PRIMARY KEY (`proposal_id`),
  UNIQUE INDEX `uq_group_attraction` (`group_id` ASC, `attractions_no` ASC) VISIBLE,
  INDEX `group_id` (`group_id` ASC) VISIBLE,
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  INDEX `fk_schedule_proposal_attractions1_idx` (`attractions_no` ASC) VISIBLE,
  CONSTRAINT `fk_schedule_proposal_attractions1`
    FOREIGN KEY (`attractions_no`)
    REFERENCES `tripsaga`.`attractions` (`no`),
  CONSTRAINT `schedule_proposal_ibfk_1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`travel_group` (`group_id`),
  CONSTRAINT `schedule_proposal_ibfk_3`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`transportation_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`transportation_type` (
  `type_id` INT NOT NULL AUTO_INCREMENT,
  `type_name` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`type_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`transportation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`transportation` (
  `transportation_id` INT NOT NULL AUTO_INCREMENT,
  `type_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  `departure` DATETIME NULL DEFAULT NULL,
  `arrival` DATETIME NULL DEFAULT NULL,
  `from` VARCHAR(100) NULL DEFAULT NULL,
  `to` VARCHAR(100) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`transportation_id`),
  INDEX `type_id` (`type_id` ASC) VISIBLE,
  INDEX `group_id` (`group_id` ASC) VISIBLE,
  CONSTRAINT `transportation_ibfk_1`
    FOREIGN KEY (`type_id`)
    REFERENCES `tripsaga`.`transportation_type` (`type_id`),
  CONSTRAINT `transportation_ibfk_2`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`travel_group` (`group_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`user_experience`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`user_experience` (
  `user_id` VARCHAR(100) NOT NULL,
  `leader_exp` INT NULL DEFAULT '0',
  `schedule_exp` INT NULL DEFAULT '0',
  `finance_exp` INT NULL DEFAULT '0',
  `logistics_exp` INT NULL DEFAULT '0',
  `member_exp` INT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `user_experience_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`user` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`proposal_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`proposal_like` (
  `proposal_id` INT NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`proposal_id`, `user_id`),
  INDEX `fk_proposal_like_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_proposal_like_schedule_proposal1`
    FOREIGN KEY (`proposal_id`)
    REFERENCES `tripsaga`.`schedule_proposal` (`proposal_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_proposal_like_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
