import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.application.Application.launch;

public class ClaimsManager {

    private final Stage primaryStage;
    private Connection connection;

    public ClaimsManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        connectToDatabase();  // Initialize the database connection
    }

    private void connectToDatabase() {
        try {
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/your_database";
            String user = "your_username";
            String password = "your_password";

            // Establish the connection
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    public void showClaimsActionsPage() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Button fileNewClaimButton = new Button("File a New Claim");
        fileNewClaimButton.setOnAction(e -> fileNewClaim());
        grid.add(fileNewClaimButton, 0, 0);

        Button viewClaimStatusButton = new Button("View Claim Status");
        viewClaimStatusButton.setOnAction(e -> viewClaimStatus());
        grid.add(viewClaimStatusButton, 1, 0);

        Button updateClaimInfoButton = new Button("Update Claim Information");
        updateClaimInfoButton.setOnAction(e -> updateClaimInformation());
        grid.add(updateClaimInfoButton, 0, 1);

        Button approveRejectClaimButton = new Button("Approve/Reject Claim");
        approveRejectClaimButton.setOnAction(e -> approveRejectClaim());
        grid.add(approveRejectClaimButton, 1, 1);

        Button processClaimPaymentButton = new Button("Process Claim Payment");
        processClaimPaymentButton.setOnAction(e -> processClaimPayment());
        grid.add(processClaimPaymentButton, 0, 2);

        Button reviewClaimHistoryButton = new Button("Review Claim History");
        reviewClaimHistoryButton.setOnAction(e -> reviewClaimHistory());
        grid.add(reviewClaimHistoryButton, 1, 2);

        Button requestAdditionalInfoButton = new Button("Request Additional Information");
        requestAdditionalInfoButton.setOnAction(e -> requestAdditionalInformation());
        grid.add(requestAdditionalInfoButton, 0, 3);

        Button trackClaimProgressButton = new Button("Track Claim Progress");
        trackClaimProgressButton.setOnAction(e -> trackClaimProgress());
        grid.add(trackClaimProgressButton, 1, 3);

        Button disputeResolutionButton = new Button("Dispute Resolution");
        disputeResolutionButton.setOnAction(e -> disputeResolution());
        grid.add(disputeResolutionButton, 0, 4);

        Button communicateWithClaimantButton = new Button("Communicate with Claimant");
        communicateWithClaimantButton.setOnAction(e -> communicateWithClaimant());
        grid.add(communicateWithClaimantButton, 1, 4);

        Button manageClaimDocumentsButton = new Button("Manage Claim Documents");
        manageClaimDocumentsButton.setOnAction(e -> manageClaimDocuments());
        grid.add(manageClaimDocumentsButton, 0, 5);

        // Set scene and show
        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fileNewClaim() {
        Stage fileNewClaimStage = new Stage();
        fileNewClaimStage.setTitle("File a New Claim");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);
        TextArea claimDetailsField = addTextArea(grid, "Claim Details:", 1);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();
            String claimDetails = claimDetailsField.getText().trim();

            if (claimId.isEmpty() || claimDetails.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
                return;
            }

            try {
                // Logic to file a new claim in the database
                String sql = "INSERT INTO claims (claim_id, claim_details) VALUES (?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, claimId);
                pstmt.setString(2, claimDetails);
                pstmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "New Claim Filed", "The new claim has been filed.");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to file the new claim.");
                ex.printStackTrace();
            }
            fileNewClaimStage.close();
        });
        grid.add(submitButton, 1, 2);

        Scene scene = new Scene(grid, 400, 300);
        fileNewClaimStage.setScene(scene);
        fileNewClaimStage.show();
    }

    private void viewClaimStatus() {
        Stage viewClaimStatusStage = new Stage();
        viewClaimStatusStage.setTitle("View Claim Status");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();

            if (claimId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a claim ID.");
                return;
            }

            try {
                // Logic to view claim status from the database
                String sql = "SELECT claim_status FROM claims WHERE claim_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, claimId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String status = rs.getString("claim_status");
                    showAlert(Alert.AlertType.INFORMATION, "Claim Status", "Status: " + status);
                } else {
                    showAlert(Alert.AlertType.WARNING, "No Results", "No claim found with the provided ID.");
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve the claim status.");
                ex.printStackTrace();
            }
            viewClaimStatusStage.close();
        });
        grid.add(submitButton, 1, 1);

        Scene scene = new Scene(grid, 400, 150);
        viewClaimStatusStage.setScene(scene);
        viewClaimStatusStage.show();
    }

    private void updateClaimInformation() {
        Stage updateClaimInformationStage = new Stage();
        updateClaimInformationStage.setTitle("Update Claim Information");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);
        TextArea newClaimInfoField = addTextArea(grid, "New Claim Information:", 1);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();
            String newClaimInfo = newClaimInfoField.getText().trim();

            if (claimId.isEmpty() || newClaimInfo.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
                return;
            }

            try {
                // Logic to update claim information in the database
                String sql = "UPDATE claims SET claim_details = ? WHERE claim_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, newClaimInfo);
                pstmt.setString(2, claimId);
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Claim Information Updated", "The claim information has been updated.");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Update Failed", "No claim found with the provided ID.");
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update the claim information.");
                ex.printStackTrace();
            }
            updateClaimInformationStage.close();
        });
        grid.add(submitButton, 1, 2);

        Scene scene = new Scene(grid, 400, 300);
        updateClaimInformationStage.setScene(scene);
        updateClaimInformationStage.show();
    }

    private void approveRejectClaim() {
        Stage approveRejectClaimStage = new Stage();
        approveRejectClaimStage.setTitle("Approve/Reject Claim");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);
        TextField actionField = addFormField(grid, "Action (approve/reject):", 1);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();
            String action = actionField.getText().trim();

            if (claimId.isEmpty() || action.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
                return;
            }

            try {
                // Logic to approve/reject claim in the database
                String sql = "UPDATE claims SET claim_status = ? WHERE claim_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, action.equalsIgnoreCase("approve") ? "Approved" : "Rejected");
                pstmt.setString(2, claimId);
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Claim Updated", "The claim has been " + action + ".");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Update Failed", "No claim found with the provided ID.");
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to approve/reject the claim.");
                ex.printStackTrace();
            }
            approveRejectClaimStage.close();
        });
        grid.add(submitButton, 1, 2);

        Scene scene = new Scene(grid, 400, 300);
        approveRejectClaimStage.setScene(scene);
        approveRejectClaimStage.show();
    }

    private void processClaimPayment() {
        Stage processClaimPaymentStage = new Stage();
        processClaimPaymentStage.setTitle("Process Claim Payment");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);
        TextField paymentAmountField = addFormField(grid, "Payment Amount:", 1);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();
            String paymentAmount = paymentAmountField.getText().trim();

            if (claimId.isEmpty() || paymentAmount.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
                return;
            }

            try {
                // Logic to process claim payment in the database
                String sql = "INSERT INTO claim_payments (claim_id, payment_amount) VALUES (?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, claimId);
                pstmt.setDouble(2, Double.parseDouble(paymentAmount));
                pstmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Payment Processed", "The claim payment has been processed.");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to process the claim payment.");
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Payment amount must be a number.");
            }
            processClaimPaymentStage.close();
        });
        grid.add(submitButton, 1, 2);

        Scene scene = new Scene(grid, 400, 300);
        processClaimPaymentStage.setScene(scene);
        processClaimPaymentStage.show();
    }

    private void reviewClaimHistory() {
        Stage reviewClaimHistoryStage = new Stage();
        reviewClaimHistoryStage.setTitle("Review Claim History");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();

            if (claimId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a claim ID.");
                return;
            }

            try {
                // Logic to review claim history from the database
                String sql = "SELECT claim_history FROM claims WHERE claim_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, claimId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String history = rs.getString("claim_history");
                    showAlert(Alert.AlertType.INFORMATION, "Claim History", "History: " + history);
                } else {
                    showAlert(Alert.AlertType.WARNING, "No Results", "No claim found with the provided ID.");
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve the claim history.");
                ex.printStackTrace();
            }
            reviewClaimHistoryStage.close();
        });
        grid.add(submitButton, 1, 1);

        Scene scene = new Scene(grid, 400, 300);
        reviewClaimHistoryStage.setScene(scene);
        reviewClaimHistoryStage.show();
    }

    private void requestAdditionalInformation() {
        Stage requestAdditionalInfoStage = new Stage();
        requestAdditionalInfoStage.setTitle("Request Additional Information");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);
        TextArea additionalInfoField = addTextArea(grid, "Additional Information:", 1);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();
            String additionalInfo = additionalInfoField.getText().trim();

            if (claimId.isEmpty() || additionalInfo.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
                return;
            }

            try {
                // Logic to request additional information in the database
                String sql = "UPDATE claims SET additional_info = ? WHERE claim_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, additionalInfo);
                pstmt.setString(2, claimId);
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Information Requested", "The request for additional information has been sent.");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Update Failed", "No claim found with the provided ID.");
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to request additional information.");
                ex.printStackTrace();
            }
            requestAdditionalInfoStage.close();
        });
        grid.add(submitButton, 1, 2);

        Scene scene = new Scene(grid, 400, 300);
        requestAdditionalInfoStage.setScene(scene);
        requestAdditionalInfoStage.show();
    }

    private void trackClaimProgress() {
        Stage trackClaimProgressStage = new Stage();
        trackClaimProgressStage.setTitle("Track Claim Progress");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();

            if (claimId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a claim ID.");
                return;
            }

            try {
                // Logic to track claim progress from the database
                String sql = "SELECT claim_progress FROM claims WHERE claim_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, claimId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String progress = rs.getString("claim_progress");
                    showAlert(Alert.AlertType.INFORMATION, "Claim Progress", "Progress: " + progress);
                } else {
                    showAlert(Alert.AlertType.WARNING, "No Results", "No claim found with the provided ID.");
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve the claim progress.");
                ex.printStackTrace();
            }
            trackClaimProgressStage.close();
        });
        grid.add(submitButton, 1, 1);

        Scene scene = new Scene(grid, 400, 300);
        trackClaimProgressStage.setScene(scene);
        trackClaimProgressStage.show();
    }
    private void communicateWithClaimant() {
        Stage communicateWithClaimantStage = new Stage();
        communicateWithClaimantStage.setTitle("Communicate with Claimant");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);
        TextArea messageField = addTextArea(grid, "Message:", 1);

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();
            String message = messageField.getText().trim();

            if (claimId.isEmpty() || message.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
                return;
            }

            try {
                // Simulating sending a message to the claimant
                String sql = "INSERT INTO claimant_communication (claim_id, message) VALUES (?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, claimId);
                pstmt.setString(2, message);
                pstmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Message Sent", "The message has been sent to the claimant.");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to send the message.");
                ex.printStackTrace();
            }
            communicateWithClaimantStage.close();
        });
        grid.add(sendButton, 1, 2);

        Scene scene = new Scene(grid, 400, 300);
        communicateWithClaimantStage.setScene(scene);
        communicateWithClaimantStage.show();
    }

    private void manageClaimDocuments() {
        Stage manageClaimDocumentsStage = new Stage();
        manageClaimDocumentsStage.setTitle("Manage Claim Documents");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);
        TextField documentField = addFormField(grid, "Document Path:", 1);

        Button uploadButton = new Button("Upload Document");
        uploadButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();
            String documentPath = documentField.getText().trim();

            if (claimId.isEmpty() || documentPath.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
                return;
            }

            try {
                // Simulating document upload process
                String sql = "INSERT INTO claim_documents (claim_id, document_path) VALUES (?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, claimId);
                pstmt.setString(2, documentPath);
                pstmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Document Uploaded", "The document has been uploaded.");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to upload the document.");
                ex.printStackTrace();
            }
            manageClaimDocumentsStage.close();
        });

        Button viewButton = new Button("View Documents");
        viewButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();

            if (claimId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a claim ID.");
                return;
            }

            try {
                String sql = "SELECT document_path FROM claim_documents WHERE claim_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, claimId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    StringBuilder documents = new StringBuilder("Documents:\n");
                    do {
                        documents.append(rs.getString("document_path")).append("\n");
                    } while (rs.next());

                    showAlert(Alert.AlertType.INFORMATION, "Claim Documents", documents.toString());
                } else {
                    showAlert(Alert.AlertType.WARNING, "No Documents", "No documents found for the provided claim ID.");
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve claim documents.");
                ex.printStackTrace();
            }
            manageClaimDocumentsStage.close();
        });

        grid.add(uploadButton, 1, 2);
        grid.add(viewButton, 1, 3);

        Scene scene = new Scene(grid, 400, 300);
        manageClaimDocumentsStage.setScene(scene);
        manageClaimDocumentsStage.show();
    }

    private void disputeResolution() {
        Stage disputeResolutionStage = new Stage();
        disputeResolutionStage.setTitle("Dispute Resolution");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField claimIdField = addFormField(grid, "Claim ID:", 0);
        TextArea resolutionField = addTextArea(grid, "Resolution Details:", 1);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();
            String resolution = resolutionField.getText().trim();

            if (claimId.isEmpty() || resolution.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
                return;
            }

            try {
                // Logic to update dispute resolution in the database
                String sql = "UPDATE claims SET dispute_resolution = ? WHERE claim_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, resolution);
                pstmt.setString(2, claimId);
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Dispute Resolved", "The dispute has been resolved.");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Update Failed", "No claim found with the provided ID.");
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to resolve the dispute.");
                ex.printStackTrace();
            }
            disputeResolutionStage.close();
        });
        grid.add(submitButton, 1, 2);

        Scene scene = new Scene(grid, 400, 300);
        disputeResolutionStage.setScene(scene);
        disputeResolutionStage.show();
    }

    private TextField addFormField(GridPane grid, String label, int row) {
        Label formLabel = new Label(label);
        grid.add(formLabel, 0, row);

        TextField textField = new TextField();
        grid.add(textField, 1, row);

        return textField;
    }

    private TextArea addTextArea(GridPane grid, String label, int row) {
        Label formLabel = new Label(label);
        grid.add(formLabel, 0, row);

        TextArea textArea = new TextArea();
        grid.add(textArea, 1, row);

        return textArea;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
