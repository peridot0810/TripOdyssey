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
-- Table `tripsaga`.`Content`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`Content` (
  `content_id` INT NOT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  `XY` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`Group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`Group` (
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
-- Table `tripsaga`.`Role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`Role` (
  `role_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`role_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`User` (
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
-- Table `tripsaga`.`GroupUserInfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`GroupUserInfo` (
  `user_id` VARCHAR(100) NOT NULL,
  `group_id` INT NOT NULL,
  `role_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`, `group_id`, `role_id`),
  INDEX `fk_GroupUserInfo_Group1_idx` (`group_id` ASC) VISIBLE,
  INDEX `fk_GroupUserInfo_Role1_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_GroupUserInfo_Group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `tripsaga`.`Group` (`group_id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_GroupUserInfo_Role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `tripsaga`.`Role` (`role_id`),
  CONSTRAINT `fk_GroupUserInfo_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `tripsaga`.`User` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`Location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`Location` (
  `location_id` INT NOT NULL,
  `XY` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`location_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`Schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`Schedule` (
  `order` INT NOT NULL,
  `day` INT NOT NULL,
  `Content_content_id` INT NOT NULL,
  `Group_group_id` INT NOT NULL,
  PRIMARY KEY (`Content_content_id`),
  INDEX `fk_Schedule_Group1_idx` (`Group_group_id` ASC) VISIBLE,
  CONSTRAINT `fk_Schedule_Content1`
    FOREIGN KEY (`Content_content_id`)
    REFERENCES `tripsaga`.`Content` (`content_id`),
  CONSTRAINT `fk_Schedule_Group1`
    FOREIGN KEY (`Group_group_id`)
    REFERENCES `tripsaga`.`Group` (`group_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `tripsaga`.`ScheduleProposal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tripsaga`.`ScheduleProposal` (
  `XY` VARCHAR(45) NULL DEFAULT NULL,
  `proposal_id` INT NOT NULL AUTO_INCREMENT,
  `Group_group_id` INT NOT NULL,
  `User_id` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`proposal_id`),
  INDEX `fk_ScheduleProposal_Group1_idx` (`Group_group_id` ASC) VISIBLE,
  INDEX `fk_ScheduleProposal_User1_idx` (`User_id` ASC) VISIBLE,
  CONSTRAINT `fk_ScheduleProposal_Group1`
    FOREIGN KEY (`Group_group_id`)
    REFERENCES `tripsaga`.`Group` (`group_id`),
  CONSTRAINT `fk_ScheduleProposal_User1`
    FOREIGN KEY (`User_id`)
    REFERENCES `tripsaga`.`User` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
