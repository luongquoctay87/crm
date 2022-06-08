package crm.wealth.management.util;

public class DataType {

    public enum REQUEST_TYPE {
        ADD, UPDATE, DELETE
    }
    public enum REQUEST_STATUS {
        NEW, PENDING, REVIEWED, APPROVED, REJECTED, CANCEL
    }
    public enum REQUEST_PRIORITY {
        LOW, MEDIUM, HIGH
    }

    public enum PERSON_TYPE {
        CLIENT, STAFF
    }

    public enum GENDER {
        MALE, FEMALE
    }
}
