package bd.com.elites.ab.contactsyncapp.utils;

/**
 * Created by tarik on 4/20/16.
 */
public class Constants {

    public static final String SHARED_PREFERENCE_NAME = "radar_pref";

    public static class WEB_SERVIES {

        //public static String BASE_URL = "http://asifrahman.net16.net/mgmtapi/";
        //public static String BASE_URL = "https://gpradarapp.grameenphone.com/mgmtapi/";
        public static String BASE_URL = "http://192.168.202.1:80/ScriptAppSync/ContactSyncApp/insert_contact.php";

        public static String GET_CIRCLES = "get_circles.php";
        public static String GET_CIRCLE_BY_ID = "get_circle.php";
        public static String GET_REGIONS_BY_CIRCLE = "get_regions_by_circle.php";
        public static String GET_REGION_BY_ID = "get_region.php";
        public static String GET_KPI_LIST = "get_kpi_list.php";
        public static String GET_KPI_FOR_REGIONS = "get_kpi_for_region.php";
        public static String GET_KPI_FOR_CIRCLE = "get_kpi_for_circle.php";
        public static String LOGIN = "sign_in.php";
        public static String RETRIEVE_PASSWORD = "resend_password.php";
        public static String CHANGE_PASSWORD = "change_password.php";
        public static String RESET_PASSWORD = "reset_password.php";
        public static String IS_LOGGED_IN = "is_logged_in.php";
        public static String LOG_OUT = "sign_out.php";
        public static String IS_PASSWORD_ALIVE = "is_password_alive.php";
    }


    public static class ASYNCTASK_IDENTIFIERS {
        public static int GET_CIRCLES = 1;
        public static int GET_CIRCLE_BY_ID = 2;
        public static int GET_REGIONS_BY_CIRCLE = 3;
        public static int GET_KPI_LIST = 4;
        public static int GET_REGION_BY_ID = 5;
        public static int GET_KPI_FOR_REGIONS = 6;
        public static int GET_KPI_FOR_CIRCLE = 7;
        public static int LOGIN = 8;
        public static int RETRIEVE_PASSWORD = 9;
        public static int CHANGE_PASSWORD = 10;
        public static int RESET_PASSWORD = 11;
        public static int IS_LOGGED_IN = 12;
        public static int LOG_OUT = 13;
        public static int IS_PASSWORD_ALIVE = 14;
    }

    public static class INTENT_EXTRAS {
        public static String PASSWORD_EXPIRED = "password_expired";
    }
}
