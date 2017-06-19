package bsep.sw.security;


public class Privileges {
    // ------------------------- AGENT PRIVILEGES ------------------------- //
    public static final String WRITE_AGENT = "PRIVILEGE_WRITE_AGENT";
    public static final String READ_AGENT = "PRIVILEGE_READ_AGENT";
    public static final String DOWNLOAD_AGENT = "PRIVILEGE_DOWNLOAD_AGENT";

    // ------------------------- ALARM PRIVILEGES ------------------------- //
    public static final String READ_ALARM = "PRIVILEGE_READ_ALARM";
    public static final String WRITE_ALARM = "PRIVILEGE_WRITE_ALARM";

    // ---------------------------- ALARM DEFINITION PRIVILEGES ---------------------------- //
    public static final String READ_ALARM_DEFINITION = "PRIVILEGE_READ_ALARM_DEFINITION";
    public static final String WRITE_ALARM_DEFINITION = "PRIVILEGE_WRITE_ALARM_DEFINITION";
    public static final String REMOVE_ALARM_DEFINITION = "PRIVILEGE_REMOVE_ALARM_DEFINITION";

    // ------------------------- PROJECT PRIVILEGES ------------------------- //
    public static final String READ_PROJECT = "PRIVILEGE_READ_PROJECT";
    public static final String WRITE_PROJECT = "PRIVILEGE_WRITE_PROJECT";
    public static final String REMOVE_PROJECT = "PRIVILEGE_REMOVE_PROJECT";
    public static final String READ_PROJECT_COLLABORATORS = "PRIVILEGE_READ_COLLABORATORS";
    public static final String WRITE_PROJECT_COLLABORATORS = "PRIVILEGE_WRITE_COLLABORATORS";

    // ------------------------- REPORT PRIVILEGES ------------------------- //
    public static final String READ_REPORT = "PRIVILEGE_READ_REPORT";

    // ------------------------- USER PRIVILEGES ------------------------- //
    public static final String READ_SELF_INFO = "PRIVILEGE_READ_SELF_INFO";
}
