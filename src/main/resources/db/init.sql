CREATE TYPE E_METHODS AS ENUM ('GET', 'POST', 'PUT', 'PATCH', 'DELETE');
CREATE TABLE tbl_activities (
	id SERIAL,
    method E_METHODS,
	url VARCHAR(1024) NOT NULL,
	PRIMARY KEY(id)
);
INSERT INTO tbl_activities (id, method,  url) values
(1, 'GET', '/api/v1/users'),
(2, 'GET', '/api/v1/users/{id}'),
(3, 'POST', '/api/v1/users'),
(4, 'PUT', '/api/v1/users/{id}'),
(5, 'PATCH', '/api/v1/users/{id}'),
(6, 'DELETE', '/api/v1/users/{id}');


CREATE TYPE E_PERMISSIONS AS ENUM ('VIEW', 'EDIT', 'DELETE', 'APPROVE');
CREATE TABLE tbl_permissions (
	id SERIAL,
	name E_PERMISSIONS,
    enabled BOOLEAN,
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);
INSERT INTO tbl_permissions (id, name, enabled) VALUES
(1, 'VIEW', true),
(2, 'EDIT', true),
(3, 'DELETE', true),
(4, 'APPROVE', true);


CREATE TABLE tbl_roles (
	id SERIAL,
	name VARCHAR(255) NOT NULL,
    enabled BOOLEAN,
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	UNIQUE (name),
	PRIMARY KEY(id)
);
INSERT INTO tbl_roles (id, name, enabled) VALUES
(1, 'SYSTEM_ADMIN', true),
(2, 'ROLE_MAKER', true),
(3, 'ROLE_CHECKER', true),
(4, 'ROLE_MANAGER', true);


CREATE TABLE tbl_role_permission_activities (
	role_id INT NOT NULL,
	permission_id INT NOT NULL,
	activity_id INT NOT NULL,
	PRIMARY KEY(role_id, permission_id, activity_id),
	CONSTRAINT tbl_role_permission_activities_fk1 FOREIGN KEY (role_id) REFERENCES tbl_roles(id),
	CONSTRAINT tbl_role_permission_activities_fk2 FOREIGN KEY (permission_id) REFERENCES tbl_permissions(id),
	CONSTRAINT tbl_role_permission_activities_fk3 FOREIGN KEY (activity_id) REFERENCES tbl_activities(id)
);
INSERT INTO tbl_role_permission_activities (role_id, permission_id, activity_id) VALUES
(1, 1, 1),
(1, 1, 2),
(1, 2, 3),
(1, 2, 4),
(1, 2, 5),
(1, 3, 6),
(4, 2, 3);

CREATE TYPE E_ENABLE AS ENUM ('true', 'false');
CREATE TABLE tbl_users (
	id SERIAL,
	first_name VARCHAR(50) NOT NULL,
	middle_name VARCHAR(50),
	surname VARCHAR(50) NOT NULL,
	full_name VARCHAR(255) NOT NULL,
	phone VARCHAR(30),
	email VARCHAR(50) NOT NULL,
	address VARCHAR(255),
	username VARCHAR(50) NOT NULL,
	password VARCHAR(255) NOT NULL,
	enabled E_ENABLE,
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id),
	UNIQUE(username, email)
);
INSERT INTO tbl_users (first_name,middle_name,surname,full_name,phone,email,address,username,"password",enabled,created_date,updated_date) VALUES
	 ('Admin',NULL,'System','System Admin','0975-118-228','admin@email.com','Cau Giay, Hanoi','sysadmin','$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6','true','2022-06-02 16:58:30.374977','2022-06-02 16:58:30.374977'),
	 ('Maker',NULL,'User','Maker User','0975-118-228','maker@email.com','Cau Giay, Hanoi','maker','$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6','true','2022-06-02 17:03:10.09535','2022-06-02 17:03:10.09535'),
	 ('Checker',NULL,'User','Checker User','0975-118-228','checker@email.com','Cau Giay, Hanoi','checker','$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6','true','2022-06-02 17:03:10.09832','2022-06-02 17:03:10.09832'),
	 ('Manager',NULL,'User','Manager User','0975-118-228','manager@email.com','Cau Giay, Hanoi','manager','$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6','true','2022-06-02 17:03:10.100924','2022-06-02 17:03:10.100924');

CREATE TABLE tbl_user_roles (
	user_id INT NOT NULL,
	role_id INT NOT NULL,
    PRIMARY KEY(user_id, role_id),
CONSTRAINT tbl_user_role_fk1 FOREIGN KEY (user_id) REFERENCES tbl_users(id),
CONSTRAINT tbl_user_role_fk2 FOREIGN KEY (role_id) REFERENCES tbl_roles(id)
);
INSERT INTO tbl_user_roles (user_id, role_id) VALUES (1, 1),(4, 4);

CREATE TABLE tbl_client (
	id SERIAL,
	first_name VARCHAR(50) NOT NULL,
	middle_name VARCHAR(50),
	surname VARCHAR(50) NOT NULL,
	full_name VARCHAR(255) NOT NULL,
	phone VARCHAR(30),
	email VARCHAR(50) NOT NULL,
	address VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id),
	UNIQUE(email)
);
CREATE TYPE E_REQUEST_TYPE AS ENUM ('Add', 'Update', 'Delete');
CREATE TYPE E_REQUEST_STATUS AS ENUM ('New', 'Pending', 'Approved', 'Rejected', 'Cancel');
CREATE TYPE E_REQUEST_PRIORITY AS ENUM ('Low', 'Medium', 'High');
CREATE TABLE tbl_request (
	id SERIAL,
	name VARCHAR(255),
	content TEXT,
	note VARCHAR(1000),
	type E_REQUEST_TYPE,
	status E_REQUEST_STATUS,
	priority E_REQUEST_PRIORITY,
	created_by VARCHAR(255),
	update_by VARCHAR(255),
	approved_by VARCHAR(255),
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	approved_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);
INSERT INTO tbl_client (first_name,middle_name,surname,full_name,phone,email,address,created_date,updated_date) VALUES
	 ('John','D','Doe','John Doe','0975-118-228','johndoe@email.com','Cau Giay, Hanoi','2022-06-07 10:24:18.718395','2022-06-07 10:24:18.718395'),
	 ('Critiano',NULL,'Ronaldo','Cristiano Ronaldo','0123-456-789','ronaldo@email.com','Lisbon, Portugal','2022-06-07 10:24:18.726365','2022-06-07 10:24:18.726365');
