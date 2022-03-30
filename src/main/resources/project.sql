CREATE DATABASE playerDB;

USE playerDB;

CREATE TABLE IF NOT EXISTS  player (
 player_id INT(8),
 player_name VARCHAR(60) NOT NULL,
 score INT(10) NOT NULL,
 record_time DATETIME,
 PRIMARY KEY (player_name,record_time)
 );

DROP table player;
-------------- This is just sample ------------------
INSERT INTO player VALUES(10, 'virat', 100, now());
INSERT INTO player VALUES(10, 'virat', 100, now());
INSERT INTO player VALUES(11, 'sachin', 20, now());
INSERT INTO player VALUES(11, 'rahul', 10, now());
INSERT INTO player VALUES(10, 'virat', 100, now());

select * from player;