package crm.wealth.management.service;

import crm.wealth.management.api.form.CompanyForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
@Slf4j
public class SchemaService {

    @Autowired
    private ConnectService connectService;

    public void createSchema(String schema) {
        Connection c = connectService.getConnection(null);
        Statement stmt = null;
        try {
            String sql = "CREATE SCHEMA " + schema;
            stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
            log.error("Create schema {} successful", schema);
        } catch (SQLException e) {
            log.error("Create schema fail");
        }
    }

    public void createTable(String schema) {
        Connection c = connectService.getConnection(schema);
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            StringBuilder sql = new StringBuilder();
            // Create data types
            sql.append("CREATE TYPE E_PERSON AS ENUM ('Client', 'Staff');");
            sql.append("CREATE TYPE E_GENDER AS ENUM ('Male', 'Female');");
            sql.append("CREATE TYPE E_RISK_PROFILE AS ENUM ('risk_1', 'risk_2', 'risk_3');");
            sql.append("CREATE TYPE E_PEP_STATUS AS ENUM ('pep_status_1', 'pep_status_2', 'pep_status_3');");
            sql.append("CREATE TYPE E_STATUS AS ENUM ('enable', 'disable');");
            sql.append("CREATE TYPE E_COMPANY_TYPE AS ENUM ('company_type_1', 'company_type_2', 'company_type_3');");
            sql.append("CREATE TYPE E_PORTFOLIO_TYPE AS ENUM ('portfolio_type_1', 'portfolio_type_2', 'portfolio_type_3');");
            sql.append("CREATE TYPE E_PORTFOLIO_RISK_LEVEL AS ENUM ('portfolio_risk_level_1', 'portfolio_risk_level_2', 'portfolio_risk_level_3');");
            sql.append("CREATE TYPE E_CURRENCY AS ENUM ('USD', 'EUR', 'JYP');");
            sql.append("CREATE TYPE E_MAIN_SOURCE_INCOME AS ENUM ('main_source_1', 'main_source_2', 'main_source_3');");
            sql.append("CREATE TYPE E_OTHER_SOURCE_INCOME AS ENUM ('other_source_1', 'other_source_2', 'other_source_3');");
            sql.append("CREATE TYPE E_CONTACT_TYPE AS ENUM ('phone', 'email', 'skype', 'chat', 'other');");
            sql.append("CREATE TYPE E_DOCUMENT_TYPE AS ENUM ('Passport', 'Driving License', 'Pan Card', 'Aadhaar Card', 'Electricity Bill', 'Gas Bill', 'Bank Account Statement', 'Phone Bill', 'Self-Certification Form', 'W8-BEN', 'W8-BEN-E', 'W8-IMY', 'W9-BEN');");
            sql.append("CREATE TYPE E_MANDATORY_FLAG AS ENUM ('true', 'false');");

            // create tables
            sql.append("CREATE TABLE tbl_person (");
            sql.append("id SERIAL,");
            sql.append("title VARCHAR(255),");
            sql.append("first_name VARCHAR(50),");
            sql.append("middle_name VARCHAR(50),");
            sql.append("surname VARCHAR(50),");
            sql.append("full_name VARCHAR(255),");
            sql.append("person_type E_PERSON,");
            sql.append("alias VARCHAR(255),");
            sql.append("former_name VARCHAR(255),");
            sql.append("native_name VARCHAR(255),");
            sql.append("date_of_birth DATE,");
            sql.append("gender E_GENDER,");
            sql.append("nationality VARCHAR(255),");
            sql.append("nationality_2 VARCHAR(255),");
            sql.append("birth_city VARCHAR(255),");
            sql.append("birth_country VARCHAR(255),");
            sql.append("residence_country VARCHAR(255),");
            sql.append("tax_residency VARCHAR(255),");
            sql.append("tax_residency_2 VARCHAR(255),");
            sql.append("tax_residency_3 VARCHAR(255),");
            sql.append("person_id_document INTEGER,");
            sql.append("person_id_number INTEGER,");
            sql.append("client_risk_profile E_RISK_PROFILE,");
            sql.append("pep_status E_PEP_STATUS,");
            sql.append("status E_STATUS,");
            sql.append("occupation VARCHAR(255),");
            sql.append("created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("PRIMARY KEY(id)");
            sql.append(");");

            sql.append("CREATE TABLE tbl_company (");
            sql.append("id SERIAL,");
            sql.append("company_name VARCHAR(255),");
            sql.append("company_registration_number VARCHAR(255),");
            sql.append("company_type E_COMPANY_TYPE,");
            sql.append("company_jurisdiction VARCHAR(255),");
            sql.append("company_contact VARCHAR(255),");
            sql.append("company_address VARCHAR(255),");
            sql.append("lei_number INTEGER,");
            sql.append("created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("PRIMARY KEY(id)");
            sql.append(");");

            sql.append("CREATE TABLE tbl_portfolio (");
            sql.append("id SERIAL,");
            sql.append("portfolio_number INTEGER,");
            sql.append("portfolio_description VARCHAR(255),");
            sql.append("portfolio_type E_PORTFOLIO_TYPE,");
            sql.append("portfolio_risk_level E_PORTFOLIO_RISK_LEVEL,");
            sql.append("custodian_bank VARCHAR(255),");
            sql.append("custodian_bank_contact VARCHAR(255),");
            sql.append("portfolio_currency E_CURRENCY,");
            sql.append("portfolio_open_date DATE,");
            sql.append("portfolio_status E_STATUS,");
            sql.append("created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("PRIMARY KEY(id)");
            sql.append(");");

            sql.append("CREATE TABLE tbl_source_of_wealth (");
            sql.append("id SERIAL,");
            sql.append("personal_background TEXT,");
            sql.append("professional_business_background TEXT,");
            sql.append("main_source_income E_MAIN_SOURCE_INCOME,");
            sql.append("other_main_source_income E_OTHER_SOURCE_INCOME,");
            sql.append("growth_and_plan TEXT,");
            sql.append("economic_purpose_and_rationale TEXT,");
            sql.append("estimated_wealth VARCHAR(255),");
            sql.append("estimated_annual_income VARCHAR(255),");
            sql.append("source_funds TEXT,");
            sql.append("source_of_wealth_corroboration TEXT,");
            sql.append("sow_party_details VARCHAR(255),");
            sql.append("created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("PRIMARY KEY(id)");
            sql.append(");");

            sql.append("CREATE TABLE tbl_contact (");
            sql.append("id SERIAL,");
            sql.append("registered_address VARCHAR(255),");
            sql.append("correspondence_address VARCHAR(255),");
            sql.append("other_address VARCHAR(255),");
            sql.append("registered_contact_number VARCHAR(30),");
            sql.append("registered_email_address VARCHAR(50),");
            sql.append("preferred_contact_method VARCHAR(255),");
            sql.append("created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("PRIMARY KEY(id)");
            sql.append(");");

            sql.append("CREATE TABLE tbl_contact_entry (");
            sql.append("id SERIAL,");
            sql.append("contact_date_and_time TIMESTAMP,");
            sql.append("contact_type E_CONTACT_TYPE,");
            sql.append("location VARCHAR(255),");
            sql.append("subject_discussion VARCHAR(255),");
            sql.append("content_of_discussion VARCHAR(1000),");
            sql.append("action_required VARCHAR(255),");
            sql.append("due_date TIMESTAMP,");
            sql.append("created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("PRIMARY KEY(id)");
            sql.append(");");

            sql.append("CREATE TABLE tbl_document (");
            sql.append("id SERIAL,");
            sql.append("document_type E_DOCUMENT_TYPE,");
            sql.append("document_number INTEGER,");
            sql.append("document_issue_date DATE,");
            sql.append("document_expiry DATE,");
            sql.append("document_country_of_issuance VARCHAR(255),");
            sql.append("comments VARCHAR(255),");
            sql.append("mandatory_flag E_MANDATORY_FLAG,");
            sql.append("expected_date_of_receipt DATE,");
            sql.append("actual_date_of_receipt DATE,");
            sql.append("status E_STATUS,");
            sql.append("created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
            sql.append("PRIMARY KEY(id)");
            sql.append(");");
            stmt.executeUpdate(sql.toString());

            stmt.close();
            c.close();
            log.error("Create tables successful");
        } catch (SQLException e) {
            log.error("Create tables fail");
        }
    }

    public void createData(String schema, CompanyForm form) {
        Connection c = connectService.getConnection(schema);
        PreparedStatement stmt = null;
        try {
            c.setAutoCommit(false);
            String company = "INSERT INTO tbl_company (company_name,company_registration_number,company_type,company_jurisdiction,company_contact,company_address,lei_number) VALUES(?,?,?,?,?,?,?);";
            stmt = c.prepareStatement(company);
            stmt.setString(1, form.getCompanyName());
            stmt.setString(2, form.getCompanyRegistrationNumber());
            stmt.setObject(3, form.getCompanyType(), Types.OTHER);
            stmt.setString(4, form.getCompanyJurisdiction());
            stmt.setString(5, form.getCompanyContact());
            stmt.setString(6, form.getCompanyAddress());
            stmt.setInt(7, form.getLeiNumber());
            stmt.execute();

            String company2 = "INSERT INTO tbl_company (company_name,company_registration_number,company_type,company_jurisdiction,company_contact,company_address,lei_number) VALUES(?,?,?,?,?,?,?);";
            stmt = c.prepareStatement(company2);
            stmt.setString(1, form.getCompanyName());
            stmt.setString(2, form.getCompanyRegistrationNumber());
            stmt.setObject(3, form.getCompanyType(), Types.OTHER);
            stmt.setString(4, form.getCompanyJurisdiction());
            stmt.setString(5, form.getCompanyContact());
            stmt.setString(6, form.getCompanyAddress());
            stmt.setInt(7, form.getLeiNumber());
            stmt.execute();

            stmt.close();
            c.commit();
            c.close();
            log.error("Save company successful");
        } catch (SQLException e) {
            // c.rollback();
            e.printStackTrace();
            log.error("Save company fail");
        }
    }
}
