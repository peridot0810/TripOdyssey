-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema tripsaga
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tripsaga
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tripsaga` DEFAULT CHARACTER SET utf8mb3 ;
USE `tripsaga` ;

-- -----------------------------------------------------
-- Table `tripsaga`.`location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`location` (
  `location_id` INT NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NULL DEFAULT NULL,
  `imgUrl` VARCHAR(100) NULL DEFAULT NULL,
  `tel` VARCHAR(45) NULL DEFAULT NULL,
  `address` VARCHAR(45) NULL DEFAULT NULL,
  `overview` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`location_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`content`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`content` (
  `content_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  `location_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  INDEX `fk_content_location1_idx` (`location_id` ASC) VISIBLE,
  CONSTRAINT `fk_content_location1`
    FOREIGN KEY (`location_id`)
    REFERENCES `tripsaga`.`location` (`location_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`group` (
  `group_id` INT NOT NULL AUTO_INCREMENT,
  `groupcol` VARCHAR(45) NULL,
  `name` VARCHAR(100) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` ENUM('planning', 'onGoing', 'finished', 'canceled') NOT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`group_id`))
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
-- Table `tripsaga`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`user` (
  `id` VARCHAR(100) NOT NULL,
  `pw` VARCHAR(300) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `gender` ENUM('male', 'female') NOT NULL,
  `nickname` VARCHAR(100) NOT NULL,
  `age` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`group_user_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`group_user_info` (
  `user_id` VARCHAR(100) NOT NULL,
  `group_id` INT NOT NULL,
  `role_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`, `group_id`, `role_id`),
  INDEX `fk_GroupUserInfo_Group1_idx` (`group_id` ASC) VISIBLE,
  INDEX `fk_GroupUserInfo_Role1_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_GroupUserInfo_Group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`group` (`group_id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_GroupUserInfo_Role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `tripsaga`.`role` (`role_id`),
  CONSTRAINT `fk_GroupUserInfo_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`schedule` (
  `order` INT NOT NULL,
  `day` INT NOT NULL,
  `content_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  PRIMARY KEY (`content_id`),
  INDEX `fk_Schedule_Group1_idx` (`group_id` ASC) VISIBLE,
  CONSTRAINT `fk_Schedule_Content1`
    FOREIGN KEY (`content_id`)
    REFERENCES `tripsaga`.`content` (`content_id`),
  CONSTRAINT `fk_Schedule_Group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`group` (`group_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`schedule_proposal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`schedule_proposal` (
  `proposal_id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  `location_id` INT NOT NULL,
  PRIMARY KEY (`proposal_id`),
  INDEX `fk_ScheduleProposal_Group1_idx` (`group_id` ASC) VISIBLE,
  INDEX `fk_ScheduleProposal_User1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_scheduleproposal_location1_idx` (`location_id` ASC) VISIBLE,
  CONSTRAINT `fk_ScheduleProposal_Group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`group` (`group_id`),
  CONSTRAINT `fk_scheduleproposal_location1`
    FOREIGN KEY (`location_id`)
    REFERENCES `tripsaga`.`location` (`location_id`),
  CONSTRAINT `fk_ScheduleProposal_User1`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`transportation_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`transportation_type` (
  `type_id` INT NOT NULL AUTO_INCREMENT,
  `type_name` VARCHAR(45) NULL,
  PRIMARY KEY (`type_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tripsaga`.`transportation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`transportation` (
  `transportation_id` INT NOT NULL AUTO_INCREMENT,
  `type_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  `departure` DATETIME NULL,
  `arrival` DATETIME NULL,
  `from` VARCHAR(45) NULL,
  `to` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`transportation_id`),
  INDEX `fk_transportation_transportation_type1_idx` (`type_id` ASC) VISIBLE,
  INDEX `fk_transportation_group1_idx` (`group_id` ASC) VISIBLE,
  CONSTRAINT `fk_transportation_transportation_type1`
    FOREIGN KEY (`type_id`)
    REFERENCES `tripsaga`.`transportation_type` (`type_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transportation_group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`group` (`group_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tripsaga`.`accommodation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`accommodation` (
  `accommodation_id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `location` VARCHAR(45) NULL,
  `duration_start` INT NULL,
  `duration_end` INT NULL,
  PRIMARY KEY (`accommodation_id`),
  INDEX `fk_accommodation_group1_idx` (`group_id` ASC) VISIBLE,
  CONSTRAINT `fk_accommodation_group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`group` (`group_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tripsaga`.`expense_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`expense_category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(45) NULL,
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tripsaga`.`expense_tracker`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`expense_tracker` (
  `expense_id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NOT NULL,
  `content_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  `datetime` DATETIME NULL,
  `description` VARCHAR(45) NULL,
  `amount` INT NULL,
  INDEX `fk_expense_tracker_group1_idx` (`group_id` ASC) VISIBLE,
  PRIMARY KEY (`expense_id`),
  INDEX `fk_expense_tracker_expense_category1_idx` (`category_id` ASC) VISIBLE,
  INDEX `fk_expense_tracker_content1_idx` (`content_id` ASC) VISIBLE,
  CONSTRAINT `fk_expense_tracker_group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`group` (`group_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_expense_tracker_expense_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `tripsaga`.`expense_category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_expense_tracker_content1`
    FOREIGN KEY (`content_id`)
    REFERENCES `tripsaga`.`content` (`content_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tripsaga`.`member_expense_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`member_expense_info` (
  `user_id` VARCHAR(100) NOT NULL,
  `group_id` INT NOT NULL,
  `expense_to_pay` INT NOT NULL,
  `paid_amount` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`, `group_id`),
  INDEX `fk_member_expense_info_group1_idx` (`group_id` ASC) VISIBLE,
  CONSTRAINT `fk_member_expense_info_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_member_expense_info_group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`group` (`group_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
