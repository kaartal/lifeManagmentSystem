package lifemanagmentsystem;

public class SessionManager {

    private static String currentUserEmail = null;

    public static void login(String email) {
        currentUserEmail = email;
    }

    public static void logout() {
        currentUserEmail = null;
    }

    //public static boolean isLoggedIn() {
     //   return currentUserEmail != null;
    //}

    //public static String getCurrentUserEmail() {
      //  return currentUserEmail;
    //}
}
