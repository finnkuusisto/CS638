# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table achievement_info (
  id                        varchar(255) not null,
  username                  varchar(255),
  first_event_creation      boolean,
  first_event_attend        boolean,
  first_event_attended_by_user boolean,
  first_follow_user         boolean,
  first_followed_by_user    boolean,
  constraint pk_achievement_info primary key (id))
;

create table attend (
  id                        varchar(255) not null,
  username                  varchar(255),
  event_id                  varchar(255),
  constraint pk_attend primary key (id))
;

create table comment (
  id                        varchar(255) not null,
  username                  varchar(255),
  comment                   varchar(255),
  time                      bigint,
  event_id                  varchar(255),
  constraint pk_comment primary key (id))
;

create table event_info (
  id                        varchar(255) not null,
  create_date               bigint,
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

create table race_time (
  id                        varchar(255) not null,
  username                  varchar(255),
  title                     varchar(255),
  time                      integer,
  km                        double,
  display_unit              integer,
  date                      bigint,
  constraint ck_race_time_display_unit check (display_unit in (0,1,2)),
  constraint pk_race_time primary key (id))
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
  zip_code                  varchar(255),
  public_email              boolean,
  full_name                 varchar(255),
  about                     TEXT,
  join_date                 bigint,
  url                       varchar(255),
  predicted5k               bigint,
  last_login                bigint,
  login_count               bigint,
  pass_hash                 varchar(255),
  salt                      varchar(255),
  constraint pk_user_info primary key (username))
;

create table zip_code_info (
  zipcode                   varchar(255) not null,
  state                     varchar(255),
  fips_region               varchar(255),
  city                      varchar(255),
  latitude                  double,
  longitude                 double,
  constraint pk_zip_code_info primary key (zipcode))
;

create sequence achievement_info_seq;

create sequence attend_seq;

create sequence comment_seq;

create sequence event_info_seq;

create sequence follow_seq;

create sequence race_time_seq;

create sequence user_group_seq;

create sequence user_info_seq;

create sequence zip_code_info_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists achievement_info;

drop table if exists attend;

drop table if exists comment;

drop table if exists event_info;

drop table if exists follow;

drop table if exists race_time;

drop table if exists user_group;

drop table if exists user_info;

drop table if exists zip_code_info;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists achievement_info_seq;

drop sequence if exists attend_seq;

drop sequence if exists comment_seq;

drop sequence if exists event_info_seq;

drop sequence if exists follow_seq;

drop sequence if exists race_time_seq;

drop sequence if exists user_group_seq;

drop sequence if exists user_info_seq;

drop sequence if exists zip_code_info_seq;

