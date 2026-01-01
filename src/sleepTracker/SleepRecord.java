package sleepTracker;

public class SleepRecord {
    private String userEmail;
    private double hours;
    private String date;

    public SleepRecord(String userEmail, double hours, String date) {
        this.userEmail = userEmail;
        this.hours = hours;
        this.date = date;
    }

    public String getUserEmail() { return userEmail; }
    public double getHours() { return hours; }
    public String getDate() { return date; }
}
