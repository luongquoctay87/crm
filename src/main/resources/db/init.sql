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

CREATE TYPE E_ENABLE AS ENUM ('TRUE', 'FALSE');
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
	 ('Admin',NULL,'System','System Admin','0975-118-228','admin@email.com','Cau Giay, Hanoi','sysadmin','$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6','TRUE','2022-06-02 16:58:30.374977','2022-06-02 16:58:30.374977'),
	 ('Maker',NULL,'User','Maker User','0975-118-228','maker@email.com','Cau Giay, Hanoi','maker','$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6','TRUE','2022-06-02 17:03:10.09535','2022-06-02 17:03:10.09535'),
	 ('Checker',NULL,'User','Checker User','0975-118-228','checker@email.com','Cau Giay, Hanoi','checker','$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6','TRUE','2022-06-02 17:03:10.09832','2022-06-02 17:03:10.09832'),
	 ('Manager',NULL,'User','Manager User','0975-118-228','manager@email.com','Cau Giay, Hanoi','manager','$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6','TRUE','2022-06-02 17:03:10.100924','2022-06-02 17:03:10.100924');

CREATE TABLE tbl_user_roles (
	user_id INT NOT NULL,
	role_id INT NOT NULL,
	PRIMARY KEY(user_id, role_id),
	CONSTRAINT tbl_user_role_fk1 FOREIGN KEY (user_id) REFERENCES tbl_users(id),
	CONSTRAINT tbl_user_role_fk2 FOREIGN KEY (role_id) REFERENCES tbl_roles(id)
);
INSERT INTO tbl_user_roles (user_id, role_id) VALUES (1, 1),(4, 4);

CREATE TYPE E_PERSON AS ENUM ('CLIENT', 'STAFF');
CREATE TYPE E_GENDER AS ENUM ('MALE', 'FEMALE');
CREATE TABLE tbl_client (
	id SERIAL, 							-- 1. Unique ID
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
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);

CREATE TYPE E_REQUEST_TYPE AS ENUM ('ADD', 'UPDATE', 'DELETE');
CREATE TYPE E_REQUEST_STATUS AS ENUM ('NEW', 'PENDING', 'REVIEWED', 'APPROVED', 'REJECTED', 'CANCEL');
CREATE TYPE E_REQUEST_PRIORITY AS ENUM ('LOW', 'MEDIUM', 'HIGH');
CREATE TABLE tbl_request (
	id SERIAL,
	name VARCHAR(255),
	client_info TEXT,
	comment TEXT,
	type E_REQUEST_TYPE,
	status E_REQUEST_STATUS,
	priority E_REQUEST_PRIORITY,
	assignee INTEGER,
	created_by INTEGER,
	checked_by INTEGER,
	approved_by INTEGER,
	last_modified_by INTEGER,
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	checked_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	approved_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);
INSERT INTO tbl_request ("name",client_info,"comment","type",status,priority,assignee,created_by,checked_by,approved_by,last_modified_by,created_date,checked_date,approved_date,last_modified_date) VALUES
('Request create new client','{"person":{"title":{"value":"Title A","approved":false,"comment":""},"firstName":{"value":"Tay","approved":false,"comment":""},"middleName":{"value":"Quoc","approved":false,"comment":""},"surname":{"value":"Luong","approved":false,"comment":""},"fullName":{"value":"Luong Quoc Tay","approved":false,"comment":""},"personType":{"value":"Client","approved":false,"comment":""},"alias":{"value":"alias","approved":false,"comment":""},"formerName":{"value":"formerName","approved":false,"comment":""},"nativeName":{"value":"nativeName","approved":false,"comment":""},"dateOfBirth":{"value":"1987-06-30","approved":false,"comment":""},"gender":{"value":"MALE","approved":false,"comment":""},"nationality":{"value":"Vietnam","approved":false,"comment":""},"nationality2":{"value":"Singapore","approved":false,"comment":""},"birthCity":{"value":"Tuyen Quang","approved":false,"comment":""},"birthCountry":{"value":"Vietnam","approved":false,"comment":""},"residenceCountry":{"value":"Residence Country","approved":false,"comment":""},"taxResidency":{"value":"Tax Residency","approved":false,"comment":""},"taxResidency2":{"value":"Tax Residency 2","approved":false,"comment":""},"taxResidency3":{"value":"Tax Residency 3","approved":false,"comment":""},"personIdDocument":{"value":"123","approved":false,"comment":""},"personIdNumber":{"value":"123","approved":false,"comment":""},"clientRiskProfile":{"value":"risk_1","approved":false,"comment":""},"pepStatus":{"value":"pep_status_1","approved":false,"comment":""},"status":{"value":"enable","approved":false,"comment":""},"occupation":{"value":"Occupation","approved":false,"comment":""}},"company":{"companyName":{"value":"BeeSotf Co.., Ltd","approved":false,"comment":""},"companyRegistrationNumber":{"value":"abc-1234-999","approved":false,"comment":""},"companyType":{"value":"company_type_1","approved":false,"comment":""},"companyJurisdiction":{"value":"Company Jurisdiction","approved":false,"comment":""},"companyContact":{"value":"contact@beetsoft.com.vn ","approved":false,"comment":""},"companyAddress":{"value":"Cau Giay, Ha noi, Viet nam","approved":false,"comment":""},"leiNumber":{"value":"1111","approved":false,"comment":""}},"portfolio":{"portfolioNumber":{"value":"1","approved":false,"comment":""},"portfolioDescription":{"value":"Portfolio Description","approved":false,"comment":""},"portfolioType":{"value":"portfolio_type_1","approved":false,"comment":""},"portfolioRiskLevel":{"value":"portfolio_risk_level_1","approved":false,"comment":""},"custodianBank":{"value":"Vietcombak ","approved":false,"comment":""},"custodianBankContact":{"value":"Ba Dinh, Ha noi, Viet nam","approved":false,"comment":""},"portfolioCurrency":{"value":"USD","approved":false,"comment":""},"portfolioOpenDate":{"value":"2020-01-01","approved":false,"comment":""},"portfolioStatus":{"value":"enable","approved":false,"comment":""}},"sourceOfWealth":{"personalBackground":{"value":"Personal Background","approved":false,"comment":""},"professionalBusinessBackground":{"value":"Professional / Business Background","approved":false,"comment":""},"mainSourceIncome":{"value":"main_source_1","approved":false,"comment":""},"otherMainSourceIncome":{"value":"other_source_1","approved":false,"comment":""},"growthAndPlan":{"value":"Growth and Plans ","approved":false,"comment":""},"economicPurposeAndRationale":{"value":"Economic Purpose and rationale","approved":false,"comment":""},"estimatedWealth":{"value":"Estimated Wealth","approved":false,"comment":""},"estimatedAnnualIncome":{"value":"Estimated Annual Income","approved":false,"comment":""},"sourceFunds":{"value":"Source of Funds","approved":false,"comment":""},"sourceOfWealthCorroboration":{"value":"Source Of Wealth Corroboration","approved":false,"comment":""},"sowPartyDetails":{"value":"SOW Party Details","approved":false,"comment":""}},"contact":{"registeredAddress":{"value":"Registered Address","approved":false,"comment":""},"correspondenceAddress":{"value":"Correspondence Address","approved":false,"comment":""},"otherAddress":{"value":"Other Address","approved":false,"comment":""},"registeredContactNumber":{"value":"Registered Contact Number","approved":false,"comment":""},"registeredEmailAddress":{"value":"registered@email.com","approved":false,"comment":""},"preferredContactMethod":{"value":"Preferred Contact method","approved":false,"comment":""}},"contactEntry":{"contactDateAndTime":{"value":"Registered Address","approved":false,"comment":""},"contactType":{"value":"PHONE","approved":false,"comment":""},"location":{"value":"Location","approved":false,"comment":""},"subjectDiscussion":{"value":"Subject Discussion","approved":false,"comment":""},"contentOfDiscussion":{"value":"Content of Discussion","approved":false,"comment":""},"actionRequired":{"value":"Action Required","approved":false,"comment":""},"dueDate":{"value":"2022-06-30","approved":false,"comment":""}},"document":{"documentType":{"value":"Passport","approved":false,"comment":""},"documentNumber":{"value":"1234","approved":false,"comment":""},"documentIssueDate":{"value":"2020-06-30","approved":false,"comment":""},"documentExpiry":{"value":"2027-06-30","approved":false,"comment":""},"documentCountryOfIssuance":{"value":"Vietnam","approved":false,"comment":""},"comments":{"value":"Comments","approved":false,"comment":""},"mandatoryFlag":{"value":"TRUE","approved":false,"comment":""},"expectedDateOfReceipt":{"value":"2022-06-30","approved":false,"comment":""},"actualDateOfReceipt":{"value":"2022-06-30","approved":false,"comment":""},"status":{"value":"ENABLE","approved":false,"comment":""}}}','Review immidiately','ADD','NEW','HIGH',2,2,NULL,NULL,NULL,'2022-06-09 04:04:47.568',NULL,NULL,NULL);


CREATE SCHEMA client;

SET search_path TO client;

CREATE TYPE E_PERSON AS ENUM ('CLIENT', 'STAFF');
CREATE TYPE E_GENDER AS ENUM ('MALE', 'FEMALE');
CREATE TYPE E_RISK_PROFILE AS ENUM ('RISK_1', 'RISK_2', 'RISK_3');
CREATE TYPE E_PEP_STATUS AS ENUM ('PEP_STATUS_1', 'PEP_STATUS_2', 'PEP_STATUS_3');
CREATE TYPE E_STATUS AS ENUM ('ENABLE', 'DISABLE');
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
	client_risk_profile E_RISK_PROFILE,	-- 23. Client Risk Profile (Dropdown)
	pep_status E_PEP_STATUS,			-- 24. PEP Status (Dropdown)
	status E_STATUS,					-- 25. Status (Dropdown)
	occupation VARCHAR(255),			-- 26. Occupation
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);

CREATE TYPE E_COMPANY_TYPE AS ENUM ('COMPANY_TYPE_1', 'COMPANY_TYPE_2', 'COMPANY_TYPE_3');
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

CREATE TYPE E_PORTFOLIO_TYPE AS ENUM ('PORTFOLIO_TYPE_1', 'PORTFOLIO_TYPE_2', 'PORTFOLIO_TYPE_3');
CREATE TYPE E_PORTFOLIO_RISK_LEVEL AS ENUM ('PORTFOLIO_RISK_LEVEL_1', 'PORTFOLIO_RISK_LEVEL_2', 'PORTFOLIO_RISK_LEVEL_3');
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

CREATE TYPE E_MAIN_SOURCE_INCOME AS ENUM ('MAIN_SOURCE_1', 'MAIN_SOURCE_2', 'MAIN_SOURCE_3');
CREATE TYPE E_OTHER_SOURCE_INCOME AS ENUM ('OTHER_SOURCE_1', 'OTHER_SOURCE_2', 'OTHER_SOURCE_3');
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

CREATE TABLE tbl_contact (
	id SERIAL,
	registered_address VARCHAR(255),		-- 1. Registered Address
	correspondence_address VARCHAR(255),	-- 2. Correspondence Address
	other_address VARCHAR(255),				-- 3. Other Address
	registered_contact_number VARCHAR(30),	-- 4. Registered Contact Number
	registered_email_address VARCHAR(50),	-- 5. Registered Email Address
	preferred_contact_method VARCHAR(255),	-- 6. Preferred Contact method
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);

CREATE TYPE E_CONTACT_TYPE AS ENUM ('PHONE', 'EMAIL', 'SKYPE', 'CHAT', 'OTHER');
CREATE TABLE tbl_contact_entry (
	id SERIAL,							 	-- 1. Contact Report Number (unique ID)
	contact_date_and_time TIMESTAMP,		-- 2. Contact Date and Time
	contact_type E_CONTACT_TYPE,			-- 3. Contact Type (Dropdown)
	location VARCHAR(255),					-- 4. Location
	subject_discussion VARCHAR(255),		-- 5. Subject Discussion
	content_of_discussion TEXT,	-- 6. Content of Discussion
	action_required VARCHAR(255),			-- 7. Action Required
	due_date TIMESTAMP, 					-- 8. Due Date
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);

CREATE TYPE E_DOCUMENT_TYPE AS ENUM ('Passport', 'Driving License', 'Pan Card', 'Aadhaar Card', 'Electricity Bill', 'Gas Bill', 'Bank Account Statement', 'Phone Bill', 'Self-Certification Form', 'W8-BEN', 'W8-BEN-E', 'W8-IMY', 'W9-BEN');
CREATE TYPE E_MANDATORY_FLAG AS ENUM ('TRUE', 'FALSE');
CREATE TABLE tbl_document (
	id SERIAL,
	document_type E_DOCUMENT_TYPE,
	document_number INTEGER,					-- 2. Document Number
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

CREATE OR REPLACE FUNCTION clone_schema(source_schema text, dest_schema text) RETURNS void AS
$$

DECLARE
  object text;
  buffer text;
default_ text;
column_ text;
BEGIN
  EXECUTE 'CREATE SCHEMA ' || dest_schema;

  FOR object IN
    SELECT sequence_name::text FROM information_schema.SEQUENCES WHERE sequence_schema = source_schema
  LOOP
    EXECUTE 'CREATE SEQUENCE ' || dest_schema || '.' || object;
  END LOOP;

  FOR object IN
    SELECT table_name::text FROM information_schema.TABLES WHERE table_schema = source_schema
  LOOP
    buffer := dest_schema || '.' || object;
    EXECUTE 'CREATE TABLE ' || buffer || ' (LIKE ' || source_schema || '.' || object || ' INCLUDING CONSTRAINTS INCLUDING INDEXES INCLUDING DEFAULTS)';

    FOR column_, default_ IN
      SELECT column_name::text, replace(column_default::text, source_schema, dest_schema) FROM information_schema.COLUMNS where table_schema = dest_schema AND table_name = object AND column_default LIKE 'nextval(%' || source_schema || '%::regclass)'
    LOOP
      EXECUTE 'ALTER TABLE ' || buffer || ' ALTER COLUMN ' || column_ || ' SET DEFAULT ' || default_;
    END LOOP;
  END LOOP;

  EXECUTE 'SET search_path TO ' || dest_schema;

  CREATE TYPE E_PERSON AS ENUM ('CLIENT', 'STAFF');
  CREATE TYPE E_GENDER AS ENUM ('MALE', 'FEMALE');
  CREATE TYPE E_RISK_PROFILE AS ENUM ('RISK_1', 'RISK_2', 'RISK_3');
  CREATE TYPE E_PEP_STATUS AS ENUM ('PEP_STATUS_1', 'PEP_STATUS_2', 'PEP_STATUS_3');
  CREATE TYPE E_STATUS AS ENUM ('ENABLE', 'DISABLE');
  CREATE TYPE E_COMPANY_TYPE AS ENUM ('COMPANY_TYPE_1', 'COMPANY_TYPE_2', 'COMPANY_TYPE_3');
  CREATE TYPE E_PORTFOLIO_TYPE AS ENUM ('PORTFOLIO_TYPE_1', 'PORTFOLIO_TYPE_2', 'PORTFOLIO_TYPE_3');
  CREATE TYPE E_PORTFOLIO_RISK_LEVEL AS ENUM ('PORTFOLIO_RISK_LEVEL_1', 'PORTFOLIO_RISK_LEVEL_2', 'PORTFOLIO_RISK_LEVEL_3');
  CREATE TYPE E_CURRENCY AS ENUM ('USD', 'EUR', 'JYP');
  CREATE TYPE E_MAIN_SOURCE_INCOME AS ENUM ('MAIN_SOURCE_1', 'MAIN_SOURCE_2', 'MAIN_SOURCE_3');
  CREATE TYPE E_OTHER_SOURCE_INCOME AS ENUM ('OTHER_SOURCE_1', 'OTHER_SOURCE_2', 'OTHER_SOURCE_3');
  CREATE TYPE E_CONTACT_TYPE AS ENUM ('PHONE', 'EMAIL', 'SKYPE', 'CHAT', 'OTHER');
  CREATE TYPE E_DOCUMENT_TYPE AS ENUM ('Passport', 'Driving License', 'Pan Card', 'Aadhaar Card', 'Electricity Bill', 'Gas Bill', 'Bank Account Statement', 'Phone Bill', 'Self-Certification Form', 'W8-BEN', 'W8-BEN-E', 'W8-IMY', 'W9-BEN');
  CREATE TYPE E_MANDATORY_FLAG AS ENUM ('TRUE', 'FALSE');

END;

$$ LANGUAGE plpgsql VOLATILE;






































