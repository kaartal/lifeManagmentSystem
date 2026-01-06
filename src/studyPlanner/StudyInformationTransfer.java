package studyPlanner;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import lifemanagmentsystem.MongodbConnection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Map;

public class StudyInformationTransfer {

    private final MongoCollection<Document> collection;

    public StudyInformationTransfer() {
        MongoDatabase db = MongodbConnection.getDatabase();
        collection = db.getCollection("schoolTracker");
    }

    // Dodavanje ili update studenta
    public void addOrUpdateRecord(StudyRecord record) {
        Document doc = new Document("studentEmail", record.getStudentEmail());
        for (Map.Entry<String, ArrayList<Integer>> entry : record.getGrades().entrySet()) {
            doc.append(entry.getKey(), entry.getValue()); // spremamo cijelu listu ocjena
        }
        collection.replaceOne(
                new Document("studentEmail", record.getStudentEmail()),
                doc,
                new com.mongodb.client.model.ReplaceOptions().upsert(true)
        );
    }

    public StudyRecord getRecord(String studentEmail) {
        FindIterable<Document> docs = collection.find(new Document("studentEmail", studentEmail));
        Document doc = docs.first();
        StudyRecord record = new StudyRecord(studentEmail);

        if (doc != null) {
            for (String subject : record.getGrades().keySet()) {
                // Dohvati listu ocjena iz MongoDB-a
                @SuppressWarnings("unchecked")
                ArrayList<Integer> gradesFromDB = (ArrayList<Integer>) doc.get(subject);
                if (gradesFromDB != null) {
                    record.getGrades().put(subject, gradesFromDB); // postavi listu ocjena
                }
            }
        }
        return record;
    }
}
