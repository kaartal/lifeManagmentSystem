package sleepTracker;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lifemanagmentsystem.MongodbConnection;
import mainPanel.MainPanel;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;





import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class SleepTracker {

    private JPanel mainPanel;
    private JTextField dateField, startField, endField;
    private JButton addButton, backButton, exportButton;
    private JTable sleepTable;
    private DefaultTableModel tableModel;

    private String userEmail;
    private final MongoCollection<Document> collection;

    public SleepTracker(String loggedUserEmail) {
        userEmail = loggedUserEmail;
        collection = MongodbConnection.getDatabase().getCollection("sleepTracker");

        mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(236, 240, 241));

        backButton = createModernButton("← Nazad", new Color(127, 140, 141));
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(236, 240, 241));
        backPanel.add(backButton);
        topPanel.add(backPanel);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBackground(new Color(236, 240, 241));

        dateField = new JTextField(8);
        dateField.setText(LocalDate.now().toString());
        startField = new JTextField(5);
        startField.setText("22:00");
        endField = new JTextField(5);
        endField.setText("06:00");

        addButton = createModernButton("Dodaj san", new Color(72,201,176));
        exportButton = createModernButton("Export PDF", new Color(155,89,182));

        inputPanel.add(new JLabel("Datum (YYYY-MM-DD):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Početak (HH:mm):"));
        inputPanel.add(startField);
        inputPanel.add(new JLabel("Kraj (HH:mm):"));
        inputPanel.add(endField);
        inputPanel.add(addButton);
        inputPanel.add(exportButton);

        topPanel.add(inputPanel);

        tableModel = new DefaultTableModel(new String[]{"Datum","Početak","Kraj","Trajanje (sati)"},0);
        sleepTable = new JTable(tableModel);
        sleepTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(sleepTable);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addSleepRecord());
        exportButton.addActionListener(e -> exportPDF());
        backButton.addActionListener(e -> goBack());

        loadData();
    }

    private void addSleepRecord() {
        try {
            LocalDate date = LocalDate.parse(dateField.getText());
            LocalTime start = LocalTime.parse(startField.getText());
            LocalTime end = LocalTime.parse(endField.getText());

            // Trajanje u satima i minutama
            long minutes = java.time.Duration.between(start, end).toMinutes();
            if (minutes < 0) minutes += 24 * 60;
            long hoursPart = minutes / 60;
            long minutesPart = minutes % 60;
            String duration = String.format("%02d:%02d", hoursPart, minutesPart);

            Document doc = new Document("userEmail", userEmail)
                    .append("date", date.toString())
                    .append("start", start.toString())
                    .append("end", end.toString())
                    .append("duration", duration);
            collection.insertOne(doc);

            tableModel.addRow(new Object[]{date, start, end, duration});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Greška pri unosu. Provjerite format datuma i vremena!");
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Document doc : collection.find(new Document("userEmail", userEmail))) {
            tableModel.addRow(new Object[]{
                    doc.getString("date"),
                    doc.getString("start"),
                    doc.getString("end"),
                    doc.getString("duration") // sad uzima format sat:min
            });
        }
    }


    private void exportPDF() {
        try {
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            PdfWriter.getInstance(pdfDoc, new FileOutputStream("sleep_records.pdf"));
            pdfDoc.open();

            pdfDoc.add(new Paragraph("Sleep Tracker - " + userEmail,
                    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD)));

            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.addCell("Datum");
            pdfTable.addCell("Početak");
            pdfTable.addCell("Kraj");
            pdfTable.addCell("Trajanje (sati)");

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < 4; j++) {
                    pdfTable.addCell(tableModel.getValueAt(i,j).toString());
                }
            }

            pdfDoc.add(pdfTable);
            pdfDoc.close();

            JOptionPane.showMessageDialog(null, "PDF eksport uspješan!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Greška pri eksportu PDF-a: " + ex.getMessage());
        }
    }

    private void goBack() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        frame.setContentPane(new MainPanel(userEmail));
        frame.revalidate();
        frame.repaint();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JButton createModernButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        b.setFocusPainted(false);
        return b;
    }
}
