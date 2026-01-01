package studyPlanner;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import lifemanagmentsystem.MongodbConnection;
import org.bson.Document;

import java.util.ArrayList;

public class StudyInformationTransfer {

    private final MongoCollection<Document> collection;

    public StudyInformationTransfer() {
        MongoDatabase db = MongodbConnection.getDatabase();
        collection = db.getCollection("studyTracker");
    }


    public void addStudyRecord(StudyRecord record) {
        Document doc = new Document("userEmail", record.getUserEmail())
                .append("subject", record.getSubject())
                .append("hours", record.getHours())
                .append("date", record.getDate());
        collection.insertOne(doc);
    }


    public ArrayList<StudyRecord> getAllRecords(String userEmail) {
        ArrayList<StudyRecord> records = new ArrayList<>();
        FindIterable<Document> docs = collection.find(new Document("userEmail", userEmail));

        for (Document doc : docs) {
            StudyRecord r = new StudyRecord(
                    doc.getString("userEmail"),
                    doc.getString("subject"),
                    doc.getDouble("hours"),
                    doc.getString("date")
            );
            records.add(r);
        }
        return records;
    }


    public ArrayList<Document> getStudySummary(String userEmail) {
        ArrayList<Document> summary = new ArrayList<>();
        for (Document doc : collection.aggregate(
                java.util.Arrays.asList(
                        new Document("$match", new Document("userEmail", userEmail)),
                        new Document("$group", new Document("_id", "$subject")
                                .append("totalHours", new Document("$sum", "$hours")))
                )
        )) {
            summary.add(doc);
        }
        return summary;
    }
}
