INSERT INTO `avcdbjpa`.`countries` (`id`, `name`) VALUES ('1', 'Israel');
INSERT INTO `avcdbjpa`.`countries` (`id`, `name`) VALUES ('2', 'Vietnam');

INSERT INTO `avcdbjpa`.`cities` (`id`, `name`, `country_id`) VALUES ('1', 'tel aviv', '1');
INSERT INTO `avcdbjpa`.`cities` (`id`, `name`, `country_id`) VALUES ('2', 'Jerusalem', '1');
INSERT INTO `avcdbjpa`.`cities` (`id`, `name`, `country_id`) VALUES ('3', 'Ho chi minh', '2');
INSERT INTO `avcdbjpa`.`cities` (`id`, `name`, `country_id`) VALUES ('4', 'Hanoi', '2');
INSERT INTO `avcdbjpa`.`cities` (`id`, `name`, `country_id`, `active`) VALUES ('5', 'Saigon', '2', false);


INSERT INTO `avcdbjpa`.`supply_categories` (`id`, `name`) VALUES ('1', 'cat1');
INSERT INTO `avcdbjpa`.`supply_categories` (`id`, `name`) VALUES ('2', 'cat2');
INSERT INTO `avcdbjpa`.`supply_categories` (`id`, `name`) VALUES ('3', 'cat3');
INSERT INTO `avcdbjpa`.`supply_categories` (`id`, `name`) VALUES ('4', 'cat4');

INSERT INTO `avcdbjpa`.`banks` (`id`, `name`) VALUES ('1', 'bank1');
INSERT INTO `avcdbjpa`.`banks` (`id`, `name`) VALUES ('2', 'bank2');
INSERT INTO `avcdbjpa`.`banks` (`id`, `name`) VALUES ('3', 'bank3');
INSERT INTO `avcdbjpa`.`banks` (`id`, `name`) VALUES ('4', 'bank4');

INSERT INTO `avcdbjpa`.`bank_branches` (`id`, `name`, `bank_id`) VALUES ('1', 'branch1', '1');
INSERT INTO `avcdbjpa`.`bank_branches` (`id`, `name`, `bank_id`) VALUES ('2', 'branch2', '1');
INSERT INTO `avcdbjpa`.`bank_branches` (`id`, `name`, `bank_id`) VALUES ('3', 'branch3', '1');
INSERT INTO `avcdbjpa`.`bank_branches` (`id`, `name`, `bank_id`) VALUES ('4', 'branch4', '2');
INSERT INTO `avcdbjpa`.`bank_branches` (`id`, `name`, `bank_id`) VALUES ('5', 'branch5', '2');
INSERT INTO `avcdbjpa`.`bank_branches` (`id`, `name`, `bank_id`) VALUES ('6', 'branch6', '2');

INSERT INTO `avcdbjpa`.`contract_types` (`id`, `code`, `name`) VALUES ('1', 'VAT', 'purchase');
INSERT INTO `avcdbjpa`.`contract_types` (`id`, `code`, `name`) VALUES ('2', 'IMP', 'process');

INSERT INTO `avcdbjpa`.`items` (`id`, `measure_unit`, `name`) VALUES ('1', 'KG', 'cashew');

INSERT INTO `avcdbjpa`.`po_codes` (`id`, `contract_type_id`) VALUES ('9000000', '1');

INSERT INTO `avcdbjpa`.`process_types` (`name`) VALUES ('CASHEW_ORDER');
INSERT INTO `avcdbjpa`.`process_types` (`name`) VALUES ('GENERAL_ORDER');