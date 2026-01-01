package fitnesTracker;

public class FitnessRecord {
    private String userEmail;
    private double calories;
    private String date;

    public FitnessRecord(String userEmail, double calories, String date) {
        this.userEmail = userEmail;
        this.calories = calories;
        this.date = date;
    }

    public String getUserEmail() { return userEmail; }
    public double getCalories() { return calories; }
    public String getDate() { return date; }
}
