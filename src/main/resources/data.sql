insert into USERS(ID,NAME,DOB) values(1001,'Ranga',current_date());
insert into USERS(ID,NAME,DOB) values(1002,'Ravi',current_date());
insert into USERS(ID,NAME,DOB) values(1003,'Ram',current_date());

insert into post(id, description, user_id) values(101,'first post by Ranga',1001);
insert into post(id, description, user_id) values(102,'second post by Ranga',1001);
insert into post(id, description, user_id) values(103,'first post by Ravi',1002);
insert into post(id, description, user_id) values(104,'first post by Ram',1003);
insert into post(id, description, user_id) values(105,'second post by Ram',1003);
insert into post(id, description, user_id) values(106,'first post by Ram',1003);