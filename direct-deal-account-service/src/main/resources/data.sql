insert into account (account_id, email, password_hash, name, activated, created_by, last_modified_by) 
values (1, 'admin@directdeal.co.kr', '$2a$10$9arZnwNlbgpxMHdLv82ZXuOLID5ODJR0BhciQ1wxvds2ei1hzG8he', 'admin', 1, 'admin', 'admin');
 
insert into authority (authority_name) values ('role_user');
insert into authority (authority_name) values ('role_admin');
 
insert into account_authority (account_id, authority_name) values (1, 'role_user');
insert into account_authority (account_id, authority_name) values (1, 'role_admin');