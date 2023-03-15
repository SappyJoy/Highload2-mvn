--liquibase formatted sql

--changeset SappyJoy:3
-- password is password
insert into `USER` (username,password)
 values ('sap','$2a$12$jugPfqpBDpGcQx5.KSiXau7fFBUDIy3AE1sWyvwP02zyMSjHZfkQC');
insert into `ROLE` (rolename) values ('USER')
insert into `ROLE` (rolename) values ('ADMIN')
insert into `ROLE` (rolename) values ('SUPER_ADMIN')
insert into `USER_ROLE` (user_id, roles) values
 ((SELECT id FROM `USER` WHERE username = 'sap'),(SELECT id FROM `USER` WHERE rolename = 'SUPER_ADMIN'))