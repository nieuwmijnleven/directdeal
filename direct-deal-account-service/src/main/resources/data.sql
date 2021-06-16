insert into account (account_id, account_email, account_password_hash, account_name, account_activated, created_by, last_modified_by) 
values (1, 'admin@directdeal.co.kr', '$2a$10$9arZnwNlbgpxMHdLv82ZXuOLID5ODJR0BhciQ1wxvds2ei1hzG8he', 'admin', 1, 'admin', 'admin');
 
insert into authority (authority_name) values ('ROLE_USER');
insert into authority (authority_name) values ('ROLE_ADMIN');
 
insert into account_authority (account_id, authority_name) values (1, 'ROLE_USER');
insert into account_authority (account_id, authority_name) values (1, 'ROLE_ADMIN');