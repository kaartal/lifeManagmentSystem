package studyPlanner;

public class StudyRecord {
    private String userEmail;
    private String subject;
    private double hours;
    private String date;        

    public StudyRecord(String userEmail, String subject, double hours, String date) {
        this.userEmail = userEmail;
        this.subject = subject;
        this.hours = hours;
        this.date = date;
    }

    public String getUserEmail() { return userEmail; }
    public String getSubject() { return subject; }
    public double getHours() { return hours; }
    public String getDate() { return date; }
}
