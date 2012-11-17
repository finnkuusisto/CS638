# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table attend (
  id                        varchar(255) not null,
  username                  varchar(255),
  event_id                  varchar(255),
  constraint pk_attend primary key (id))
;

create table event_info (
  id                        varchar(255) not null,
  creator_username          varchar(255),
  name                      varchar(255),
  description               varchar(255),
  distance                  double,
  unit                      integer,
  pace                      varchar(255),
  route_description         varchar(255),
  constraint ck_event_info_unit check (unit in (0,1,2)),
  constraint pk_event_info primary key (id))
;

create table follow (
  id                        varchar(255) not null,
  follower                  varchar(255),
  followed                  varchar(255),
  constraint pk_follow primary key (id))
;

create table run_time (
  id                        varchar(255) not null,
  title                     varchar(255),
  time                      integer,
  distance                  double,
  unit                      integer,
  constraint ck_run_time_unit check (unit in (0,1,2)),
  constraint pk_run_time primary key (id))
;

create table user_group (
  id                        varchar(255) not null,
  creator_username          varchar(255),
  name                      varchar(255),
  description               varchar(255),
  constraint pk_user_group primary key (id))
;

create table user_info (
  username                  varchar(255) not null,
  email                     varchar(255),
  full_name                 varchar(255),
  about                     varchar(255),
  pass_hash                 varchar(255),
  salt                      varchar(255),
  constraint pk_user_info primary key (username))
;

create sequence attend_seq;

create sequence event_info_seq;

create sequence follow_seq;

create sequence run_time_seq;

create sequence user_group_seq;

create sequence user_info_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists attend;

drop table if exists event_info;

drop table if exists follow;

drop table if exists run_time;

drop table if exists user_group;

drop table if exists user_info;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists attend_seq;

drop sequence if exists event_info_seq;

drop sequence if exists follow_seq;

drop sequence if exists run_time_seq;

drop sequence if exists user_group_seq;

drop sequence if exists user_info_seq;

