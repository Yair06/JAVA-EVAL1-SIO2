CREATE TABLE IF NOT EXISTS `targets` (
    hash VARCHAR(64) PRIMARY KEY NOT NULL,
    codeName VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `position` (
    id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    targetHash VARCHAR(64) NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    timestamp TIMESTAMP
);

ALTER TABLE `position` ADD FOREIGN KEY (targetHash) REFERENCES targets(hash);

