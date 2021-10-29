create database if not exists test;

use test;

create table if not exists t_user
(
    `id`        varchar(64) primary key,
    `name`      varchar(64),
    `age`       int,
    `gender`    int,
    `nation`    varchar(8),
    `education` int
) engine = innodb
  default charset = utf8mb4;