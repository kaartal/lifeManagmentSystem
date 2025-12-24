package lifemanagmentsystem;

import java.awt.*;

public class SessionManager {

    private static String currentUserEmail = null;

    public static void login(String email) {

        currentUserEmail = email;
    }

    public static void logout() {
        currentUserEmail = null;
    }

    public static Color getColorFromTheme(String theme) {
        if (theme == null) {
            return new Color(166, 244, 136);
        }

        switch (theme) {
            case "Plava":
                return new Color(173, 216, 230);
            case "Roza":
                return new Color(255, 228, 225);
            case "Narandzasta":
                return new Color(205, 162, 132);
            case "Tamna/Dark":
                return new Color(40, 44, 52);
            case "Cyberpunk":
                return new Color(10, 10, 20);
            default:
                return new Color(166, 244, 136);
        }
    }

    //public static boolean isLoggedIn() {
     //   return currentUserEmail != null;
    //}

    //public static String getCurrentUserEmail() {
      //  return currentUserEmail;
    //}
}
