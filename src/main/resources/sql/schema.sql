-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
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
  `imgUrl` VARCHAR(100) NULL,
  `tel` VARCHAR(45) NULL,
  `address` VARCHAR(45) NULL,
  `overview` VARCHAR(45) NULL,
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
    REFERENCES `tripsaga`.`location` (`location_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`group` (
  `group_id` INT NOT NULL AUTO_INCREMENT,
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
-- Table `tripsaga`.`groupuserinfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`groupuserinfo` (
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
-- Table `tripsaga`.`scheduleproposal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`scheduleproposal` (
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
