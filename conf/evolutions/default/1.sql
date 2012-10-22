# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table event_info (
  id                        varchar(255) not null,
  creator_username          varchar(255),
  name                      varchar(255),
  description               varchar(255),
  distance                  double,
  pace                      double,
  route_description         double,
  constraint pk_event_info primary key (id))
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

create sequence event_info_seq;

create sequence user_group_seq;

create sequence user_info_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists event_info;

drop table if exists user_group;

drop table if exists user_info;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists event_info_seq;

drop sequence if exists user_group_seq;

drop sequence if exists user_info_seq;

