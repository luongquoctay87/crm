--CREATE DATABASE crm_db;
--
--\c crm_db;

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
INSERT INTO public.tbl_users (first_name,middle_name,surname,full_name,phone,email,address,username,"password",enabled,created_date,updated_date) VALUES
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


CREATE TYPE E_PERSON AS ENUM ('Client', 'Staff');
CREATE TYPE E_GENDER AS ENUM ('Male', 'Female');
CREATE TYPE E_RISK_PROFILE AS ENUM ('risk_1', 'risk_2', 'risk_3');
CREATE TYPE E_PEP_STATUS AS ENUM ('pep_status_1', 'pep_status_2', 'pep_status_3');
CREATE TYPE E_STATUS AS ENUM ('enable', 'disable');
CREATE TABLE tbl_person (
	id SERIAL, 							-- 1. Unique ID
	title VARCHAR(255), 				-- 2. Title (Dropdown)
	first_name VARCHAR(50),				-- 3. First Name
	middle_name VARCHAR(50), 			-- 4. Middle Name/s
	surname VARCHAR(50), 				-- 5. Surname
	full_name VARCHAR(255),				-- 6. Full Name (Auto picks up 2-5)
	person_type E_PERSON,				-- 7. Person Type (Client / Staff etc)
	alias VARCHAR(255),					-- 8. Alias / Known As
	former_name VARCHAR(255),			-- 9. Former Name
	native_name VARCHAR(255),			-- 10. Native Name (i.e Chinese name)
	date_of_birth DATE,					-- 11. Date Of Birth
	gender E_GENDER,					-- 12. Gender
	nationality VARCHAR(255),			-- 13. Nationality
	nationality_2 VARCHAR(255),			-- 14. Nationality 2
	birth_city VARCHAR(255),			-- 15. Birth City
	birth_country VARCHAR(255),     	-- 16. Birth Country
	residence_country VARCHAR(255),		-- 17. Residence Country
	tax_residency VARCHAR(255),     	-- 18. Tax Residency
	tax_residency_2 VARCHAR(255),		-- 19. Tax Residency 2
	tax_residency_3 VARCHAR(255),		-- 20. Tax Residency 3
	person_id_document INTEGER,			-- 21. Person ID Document
	person_id_number INTEGER,			-- 22. Person ID Number
	client_risk_profile	E_RISK_PROFILE,	-- 23. Client Risk Profile (Dropdown)
	pep_status	E_PEP_STATUS,			-- 24. PEP Status (Dropdown)
	status E_STATUS,					-- 25. Status (Dropdown)
	occupation VARCHAR(255),			-- 26. Occupation
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);
INSERT INTO tbl_person (title,first_name,middle_name,surname,full_name,person_type,alias,former_name,native_name,date_of_birth,gender,nationality,nationality_2,birth_city,birth_country,residence_country,tax_residency,tax_residency_2,tax_residency_3,person_id_document,person_id_number,client_risk_profile,pep_status,status,occupation,created_date,updated_date) VALUES
	 ('title 1','Tay','Quoc','Luong','Luong Quoc Tay','Client','alias','former name','Lương Quốc Tây','1987-06-30','Male','vietnam','singapore','tuyenquang','vietnam','ciputra','tax residency','task_residency_2','task_residency_3',1000,1111,'risk_1','pep_status_1','enable','programer','2022-06-02 13:09:48.236587','2022-06-02 13:09:48.236587'),
	 ('title 2','Cristiano','','Ronaldo','Cristiano Ronaldo','Client','alias','fomer name','Cristiano D. Ronaldo','1987-06-30',NULL,'Portugal',NULL,'Lisbon','Portugal','Manchester','tax residency','task_residency_2','task_residency_2',120000,152330,'risk_2',NULL,NULL,'player','2022-06-02 13:16:00.288257','2022-06-02 13:16:00.288257');


CREATE TYPE E_COMPANY_TYPE AS ENUM ('company_type_1', 'company_type_2', 'company_type_3');
CREATE TABLE tbl_company (
	id SERIAL, 									-- 1. Unique ID
	company_name VARCHAR(255), 					-- 2. Company Name
	company_registration_number VARCHAR(255), 	-- 3. Company Registration Number
	company_type E_COMPANY_TYPE,				-- 4. Company Type (dropdown)
	company_jurisdiction VARCHAR(255), 			-- 5. Company Jurisdiction
	company_contact VARCHAR(255),				-- 6. Company Contact (link to Person entity)
	company_address VARCHAR(255),				-- 7. Company Registered Office Address
	lei_number INTEGER, 						-- 8. LEI Number
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);
INSERT INTO tbl_company (company_name,company_registration_number,company_type,company_jurisdiction,company_contact,company_address,lei_number,created_date,updated_date) VALUES
	 ('IBM','a123-ccc-abc','company_type_2','jurisdiction','contact','California - USA',334456,'2022-06-02 13:19:15.912727','2022-06-02 13:19:15.912727'),
	 ('BeetSoft','abc12334566','company_type_1','jurisdiction','contact','Cau Giay - Hanoi',45566,'2022-06-02 13:17:56.685976','2022-06-02 13:17:56.685976');


CREATE TYPE E_PORTFOLIO_TYPE AS ENUM ('portfolio_type_1', 'portfolio_type_2', 'portfolio_type_3');
CREATE TYPE E_PORTFOLIO_RISK_LEVEL AS ENUM ('portfolio_risk_level_1', 'portfolio_risk_level_2', 'portfolio_risk_level_3');
CREATE TYPE E_CURRENCY AS ENUM ('USD', 'EUR', 'JYP');
CREATE TABLE tbl_portfolio (
	id SERIAL,
	portfolio_number INTEGER, 						-- 1. Portfolio Number
	portfolio_description VARCHAR(255), 			-- 2. Portfolio Description
	portfolio_type E_PORTFOLIO_TYPE, 				-- 3. Portfolio Type (dropdown)
	portfolio_risk_level E_PORTFOLIO_RISK_LEVEL, 	-- 4. Portfolio Risk Level (dropdown)
	custodian_bank VARCHAR(255),					-- 5. Custodian Bank (linked to Company Entity)
	custodian_bank_contact VARCHAR(255), 			-- 6. Custodian Bank Contact
	portfolio_currency E_CURRENCY,					-- 7. Portfolio Currency
	portfolio_open_date DATE,						-- 8. Portfolio Open Date
	portfolio_status E_STATUS,						-- 9. Portfolio Status (dropdown)
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);
INSERT INTO tbl_portfolio (portfolio_number,portfolio_description,portfolio_type,portfolio_risk_level,custodian_bank,custodian_bank_contact,portfolio_currency,portfolio_open_date,portfolio_status,created_date,updated_date) VALUES
	 (1111,'description text','portfolio_type_2','portfolio_risk_level_3','bank A','bank A address','USD','2022-06-02','enable','2022-06-02 13:34:13.650368','2022-06-02 13:34:13.650368'),
	 (2222,'description text','portfolio_type_1','portfolio_risk_level_2','bank B','bank B address','EUR','2022-06-02','disable','2022-06-02 13:35:15.829003','2022-06-02 13:35:15.829003');


CREATE TYPE E_MAIN_SOURCE_INCOME AS ENUM ('main_source_1', 'main_source_2', 'main_source_3');
CREATE TYPE E_OTHER_SOURCE_INCOME AS ENUM ('other_source_1', 'other_source_2', 'other_source_3');
CREATE TABLE tbl_source_of_wealth (
	id SERIAL,
	personal_background TEXT,						-- 1. Personal Background (text)
	professional_business_background TEXT,			-- 2. Professional / Business Background (text)
	main_source_income E_MAIN_SOURCE_INCOME, 		-- 3. Main Source of income (Dropdown)
	other_main_source_income E_OTHER_SOURCE_INCOME,	-- 4. Other Sources of Income (Dropdown)
	growth_and_plan TEXT,							-- 5. Growth and Plans (text)
	economic_purpose_and_rationale TEXT,  			-- 6. Economic Purpose and rationale (free text)
	estimated_wealth VARCHAR(255),					-- 7. Estimated Wealth
	estimated_annual_income VARCHAR(255),			-- 8. Estimated Annual Income
	source_funds TEXT,								-- 9. Source of Funds (text)
	source_of_wealth_corroboration TEXT,			-- 10. Source Of Wealth Corroboration (text)
	sow_party_details VARCHAR(255),					-- 11. SOW Party Details
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);
INSERT INTO tbl_source_of_wealth (personal_background,professional_business_background,main_source_income,other_main_source_income,growth_and_plan,economic_purpose_and_rationale,estimated_wealth,estimated_annual_income,source_funds,source_of_wealth_corroboration,sow_party_details,created_date,updated_date) VALUES
	 ('Personal Background ','Professional / Business Background','main_source_1','other_source_1','Growth and Plans','Economic Purpose and rationale','100000','100','Source of Funds','Source Of Wealth Corroboration','SOW Party Details','2022-06-02 14:15:08.039396','2022-06-02 14:15:08.039396'),
	 ('Personal Background ','Professional / Business Background','main_source_2','other_source_3','Growth and Plans','Economic Purpose and rationale','1255000','300','Source of Funds','Source Of Wealth Corroboration','SOW Party Details','2022-06-02 14:15:08.043292','2022-06-02 14:15:08.043292');


CREATE TABLE tbl_contact (
	id SERIAL,
	registered_address VARCHAR(255),		-- 1. Registered Address
	correspondence_address	VARCHAR(255),	-- 2. Correspondence Address
	other_address VARCHAR(255),				-- 3. Other Address
	registered_contact_number VARCHAR(30),	-- 4. Registered Contact Number
	registered_email_address VARCHAR(50),	-- 5. Registered Email Address
	preferred_contact_method VARCHAR(255),	-- 6. Preferred Contact method
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);
INSERT INTO tbl_contact (registered_address,correspondence_address,other_address,registered_contact_number,registered_email_address,preferred_contact_method,created_date,updated_date) VALUES
	 ('Cau Giay, Hanoi','6th Floor, Mitec Tower - E2 Plot - Cau Giay New Urban, Yen Hoa ward, Cau Giay District, Hanoi, Vietnam','〒162-0823 Central Plaza 7F, 1-1 Kaguragashi, Shinjuku-ku, Tokyo, Japan','0975-118-228','someone@email.com','phone','2022-06-02 13:19:59.508243','2022-06-02 13:19:59.508243'),
	 ('Tokyo, Japan','6th Floor, Mitec Tower - E2 Plot - Cau Giay New Urban, Yen Hoa ward, Cau Giay District, Hanoi, Vietnam','〒162-0823 Central Plaza 7F, 1-1 Kaguragashi, Shinjuku-ku, Tokyo, Japan','0123-456-789','someone@email.com','chat','2022-06-02 13:19:59.511503','2022-06-02 13:19:59.511503');


CREATE TYPE E_CONTACT_TYPE AS ENUM ('phone', 'email', 'skype', 'chat', 'other');
CREATE TABLE tbl_contact_entry (
	id SERIAL,							 	-- 1. Contact Report Number (unique ID)
	contact_date_and_time TIMESTAMP,		-- 2. Contact Date and Time
	contact_type E_CONTACT_TYPE,			-- 3. Contact Type (Dropdown)
	location VARCHAR(255),					-- 4. Location
	subject_discussion VARCHAR(255),		-- 5. Subject Discussion
	content_of_discussion VARCHAR(1000),	-- 6. Content of Discussion
	action_required VARCHAR(255),			-- 7. Action Required
	due_date TIMESTAMP, 					-- 8. Due Date
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);
INSERT INTO tbl_contact_entry (contact_date_and_time,contact_type,"location",subject_discussion,content_of_discussion,action_required,due_date,created_date,updated_date) VALUES
	 ('2022-06-02 00:00:00','phone','location','subject discussion','content of discuss','action require','2022-06-02 00:00:00','2022-06-02 14:25:24.976644','2022-06-02 14:25:24.976644'),
	 ('2022-06-02 00:00:00','email','someone@email.com','subject discussion','content of discuss','action require','2022-06-02 00:00:00','2022-06-02 14:26:02.116233','2022-06-02 14:26:02.116233');


CREATE TYPE E_DOCUMENT_TYPE AS ENUM ('Passport', 'Driving License', 'Pan Card', 'Aadhaar Card', 'Electricity Bill', 'Gas Bill', 'Bank Account Statement', 'Phone Bill', 'Self-Certification Form', 'W8-BEN', 'W8-BEN-E', 'W8-IMY', 'W9-BEN');
CREATE TYPE E_MANDATORY_FLAG AS ENUM ('true', 'false');
CREATE TABLE tbl_document (
	id SERIAL,
	document_type E_DOCUMENT_TYPE,
	document_number	INTEGER,					-- 2. Document Number
	document_issue_date DATE,					-- 3. Document Issue Date
	document_expiry DATE,						-- 4. Document Expiry Date
	document_country_of_issuance VARCHAR(255),	-- 5. Document Country of Issuance
	comments VARCHAR(255),						-- 6. Comments
	mandatory_flag E_MANDATORY_FLAG,			-- 7. Mandatory Flag (True/False)
	expected_date_of_receipt DATE,				-- 8. Expected Date of receipt
	actual_date_of_receipt DATE,				-- 9. Actual Date of receipt
	status E_STATUS,							-- 10. Status (dropdown)
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);
INSERT INTO tbl_document (document_type,document_number,document_issue_date,document_expiry,document_country_of_issuance,"comments",mandatory_flag,expected_date_of_receipt,actual_date_of_receipt,status,created_date,updated_date) VALUES
	 ('Driving License',123,'2020-10-10','2025-09-10','vietnam','Hello viet nam','true','2022-06-15','2022-06-14','enable','2022-06-02 13:28:13.325488','2022-06-02 13:28:13.325488'),
	 ('Passport',125,'2019-02-02','2030-02-02','usa','hello usa','true','2022-06-15','2022-06-14','enable','2022-06-02 13:30:14.723769','2022-06-02 13:30:14.723769');
	

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
INSERT INTO public.tbl_request ("name","content",note,"type",status,priority,created_by,update_by,approved_by,created_date,updated_date,approved_date) VALUES
	 ('Customer: Leonel Messi','{}','coming up','Add','New','Low','2','NULL',NULL,'2022-06-02 16:17:08.105594','2022-06-02 16:17:08.105594','2022-06-02 16:17:08.105594'),
	 ('Customer: Cristiano','{}','coming up','Add','Pending','High','2',NULL,NULL,'2022-06-02 16:19:05.187967','2022-06-02 16:19:05.187967','2022-06-02 16:19:05.187967');
