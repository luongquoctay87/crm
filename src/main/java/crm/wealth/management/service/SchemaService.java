package crm.wealth.management.service;

import crm.wealth.management.api.form.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;

@Service
@Slf4j
public class SchemaService {

    @Autowired
    private ConnectService connectService;

    private final String TEMPLATE_SCHEMA = "client";

    @Transactional
    public void createSchema(String schema) throws SQLException {
        Connection c = connectService.getConnection(TEMPLATE_SCHEMA);
        String sql = String.format("SELECT clone_schema('%s','%s');", TEMPLATE_SCHEMA, schema);
        PreparedStatement stmt = c.prepareStatement(sql);
        if (!stmt.execute()) {
            log.error("Create schema fail");
        }
        stmt.close();
        c.close();
    }

    @Transactional
    public void saveData(String schema, ClientInfo clientInfo) throws SQLException {
        log.info("Begin execute: save client");

        Connection connection = connectService.getConnection(schema);
        connection.setAutoCommit(false);
        PreparedStatement stmt = null;

        String person_sql = "INSERT INTO tbl_person" +
                "(title, first_name, middle_name, surname, full_name, person_type, alias, former_name, native_name, date_of_birth, gender, nationality, nationality_2, " +
                "birth_city, birth_country, residence_country, tax_residency, tax_residency_2, tax_residency_3, person_id_document, person_id_number, " +
                "client_risk_profile, pep_status, status, occupation, created_date, updated_date) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
        stmt = connection.prepareStatement(person_sql);
        Person person = clientInfo.getPerson();

        stmt.setString(1, person.getTitle().getValue());
        stmt.setString(2, person.getFirstName().getValue());
        stmt.setString(3, person.getMiddleName().getValue());
        stmt.setString(4, person.getSurname().getValue());
        stmt.setString(5, person.getFullName().getValue());
        stmt.setObject(6, person.getPersonType().getValue().toUpperCase(), Types.OTHER);
        stmt.setString(7, person.getAlias().getValue());
        stmt.setString(8, person.getFormerName().getValue());
        stmt.setString(9, person.getNativeName().getValue());
        stmt.setDate(10, Date.valueOf(person.getDateOfBirth().getValue()));
        stmt.setObject(11, person.getGender().getValue().toUpperCase(), Types.OTHER);
        stmt.setString(12, person.getNationality().getValue());
        stmt.setString(13, person.getNationality2().getValue());
        stmt.setString(14, person.getBirthCity().getValue());
        stmt.setString(15, person.getBirthCountry().getValue());
        stmt.setString(16, person.getResidenceCountry().getValue());
        stmt.setString(17, person.getTaxResidency().getValue());
        stmt.setString(18, person.getTaxResidency2().getValue());
        stmt.setString(19, person.getTaxResidency3().getValue());
        stmt.setInt(20, Integer.valueOf(person.getPersonIdDocument().getValue()));
        stmt.setInt(21, Integer.valueOf(person.getPersonIdNumber().getValue()));
        stmt.setObject(22, person.getClientRiskProfile().getValue().toUpperCase(), Types.OTHER);
        stmt.setObject(23, person.getPepStatus().getValue().toUpperCase(), Types.OTHER);
        stmt.setObject(24, person.getStatus().getValue().toUpperCase(), Types.OTHER);
        stmt.setString(25, person.getOccupation().getValue());
        stmt.execute();
        log.info("Execute 1: Save person successful");

        // Save company
        String company = "INSERT INTO tbl_company (company_name,company_registration_number,company_type,company_jurisdiction,company_contact,company_address,lei_number) VALUES(?,?,?,?,?,?,?);";
        stmt = connection.prepareStatement(company);
        Company c = clientInfo.getCompany();

        stmt.setString(1, c.getCompanyName().getValue());
        stmt.setString(2, c.getCompanyRegistrationNumber().getValue());
        stmt.setObject(3, c.getCompanyType().getValue().toUpperCase(), Types.OTHER);
        stmt.setString(4, c.getCompanyJurisdiction().getValue());
        stmt.setString(5, c.getCompanyContact().getValue());
        stmt.setString(6, c.getCompanyAddress().getValue());
        stmt.setInt(7, Integer.valueOf(c.getLeiNumber().getValue()));
        stmt.execute();
        log.info("Execute 2: Save company successful");

        // Save portfolio
        String portfolio = "INSERT INTO tbl_portfolio " +
                "(portfolio_number, portfolio_description, portfolio_type, portfolio_risk_level, custodian_bank, custodian_bank_contact, portfolio_currency, portfolio_open_date, portfolio_status, created_date, updated_date) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
        stmt = connection.prepareStatement(portfolio);
        Portfolio p = clientInfo.getPortfolio();

        stmt.setInt(1, Integer.valueOf(p.getPortfolioNumber().getValue()));
        stmt.setString(2, p.getPortfolioDescription().getValue());
        stmt.setObject(3, p.getPortfolioType().getValue().toUpperCase(), Types.OTHER);
        stmt.setObject(4, p.getPortfolioRiskLevel().getValue().toUpperCase(), Types.OTHER);
        stmt.setString(5, p.getCustodianBank().getValue());
        stmt.setString(6, p.getCustodianBankContact().getValue());
        stmt.setObject(7, p.getPortfolioCurrency().getValue().toUpperCase(), Types.OTHER);
        stmt.setDate(8, Date.valueOf(p.getPortfolioOpenDate().getValue()));
        stmt.setObject(9, p.getPortfolioStatus().getValue().toUpperCase(), Types.OTHER);
        stmt.execute();
        log.info("Execute 3: Save portfolio successful");

        // Save source of wealth
        String source = "INSERT INTO tbl_source_of_wealth " +
                "(personal_background, professional_business_background, main_source_income, other_main_source_income, growth_and_plan, economic_purpose_and_rationale, estimated_wealth, estimated_annual_income, source_funds, source_of_wealth_corroboration, sow_party_details, created_date, updated_date)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
        stmt = connection.prepareStatement(source);
        SourceOfWealth s = clientInfo.getSourceOfWealth();

        stmt.setString(1, s.getPersonalBackground().getValue());
        stmt.setString(2, s.getProfessionalBusinessBackground().getValue());
        stmt.setObject(3, s.getMainSourceIncome().getValue().toUpperCase(), Types.OTHER);
        stmt.setObject(4, s.getOtherMainSourceIncome().getValue().toUpperCase(), Types.OTHER);
        stmt.setString(5, s.getGrowthAndPlan().getValue());
        stmt.setString(6, s.getEconomicPurposeAndRationale().getValue());
        stmt.setString(7, s.getEstimatedWealth().getValue());
        stmt.setString(8, s.getEstimatedAnnualIncome().getValue());
        stmt.setString(9, s.getSourceFunds().getValue());
        stmt.setString(10, s.getSourceOfWealthCorroboration().getValue());
        stmt.setString(11, s.getSowPartyDetails().getValue());
        stmt.execute();
        log.info("Execute 4: Save source of wealth successful");

        // Save contact
        String contact = "INSERT INTO tbl_contact" +
                "(registered_address, correspondence_address, other_address, registered_contact_number, registered_email_address, preferred_contact_method, created_date, updated_date)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
        stmt = connection.prepareStatement(contact);
        Contact ct = clientInfo.getContact();

        stmt.setString(1, ct.getRegisteredAddress().getValue());
        stmt.setString(2, ct.getCorrespondenceAddress().getValue());
        stmt.setString(3, ct.getOtherAddress().getValue());
        stmt.setString(4, ct.getRegisteredContactNumber().getValue());
        stmt.setString(5, ct.getRegisteredEmailAddress().getValue());
        stmt.setObject(6, ct.getPreferredContactMethod().getValue().toUpperCase(), Types.OTHER);
        stmt.execute();
        log.info("Execute 5: Save contact successful");

        // Save contact entry
        String contactEntry = "INSERT INTO tbl_contact_entry" +
                "(contact_date_and_time, contact_type, location, subject_discussion, content_of_discussion, action_required, due_date, created_date, updated_date) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
        stmt = connection.prepareStatement(contactEntry);
        ContactEntry entry = clientInfo.getContactEntry();

        stmt.setTimestamp(1, Timestamp.valueOf("2018-09-01 09:01:15"));
        stmt.setObject(2, entry.getContactType().getValue().toUpperCase(), Types.OTHER);
        stmt.setString(3, entry.getLocation().getValue());
        stmt.setString(4, entry.getSubjectDiscussion().getValue());
        stmt.setString(5, entry.getContentOfDiscussion().getValue());
        stmt.setString(6, entry.getActionRequired().getValue());
        stmt.setTimestamp(7, Timestamp.valueOf("2018-09-01 09:01:15"));
        stmt.execute();
        log.info("Execute 6: Save contact entry successful");


        // Save document
        String document = "INSERT INTO tbl_document " +
                "(document_type, document_number, document_issue_date, document_expiry, document_country_of_issuance, \"comments\", mandatory_flag, expected_date_of_receipt, actual_date_of_receipt, status, created_date, updated_date) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
        stmt = connection.prepareStatement(document);
        Document d = clientInfo.getDocument();

        stmt.setObject(1, d.getDocumentType().getValue(), Types.OTHER);
        stmt.setInt(2, Integer.valueOf(d.getDocumentNumber().getValue()));
        stmt.setDate(3, Date.valueOf(d.getDocumentIssueDate().getValue()));
        stmt.setDate(4, Date.valueOf(d.getDocumentExpiry().getValue()));
        stmt.setString(5, d.getDocumentCountryOfIssuance().getValue());
        stmt.setString(6, d.getComments().getValue());
        stmt.setObject(7, d.getMandatoryFlag().getValue().toUpperCase(), Types.OTHER);
        stmt.setDate(8, Date.valueOf(d.getExpectedDateOfReceipt().getValue()));
        stmt.setDate(9, Date.valueOf(d.getActualDateOfReceipt().getValue()));
        stmt.setObject(10, d.getStatus().getValue().toUpperCase(), Types.OTHER);
        stmt.execute();
        log.info("Execute 7: Save document successful");

        stmt.close();
        connection.commit();
        connection.close();

        log.info("End execute: save client successful");
    }
}
