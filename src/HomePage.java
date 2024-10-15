import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HomePage extends Application {
    public static void main(String[] args) {
        Application.launch(HomePage.class, args);
    }

    private Stage primaryStage;
    private MemberManager memberManager; // Reference to MemberManager
    private ClaimsManager claimsManager; // Instance of ClaimsManager class

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.memberManager = new MemberManager(primaryStage); // Initialize MemberManager
        this.claimsManager = new ClaimsManager(primaryStage); // Initialize ClaimsManager class

        primaryStage.setTitle("Home Page");
        showHomePage();
    }

    private void showHomePage() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Create ComboBox for selections
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(
                "Member Actions",
                "Claims Actions",
                "Reports Actions",
                "Monthly Processes Actions",
                "Company Parameters Actions",
                "System Settings Actions",
                "Other Actions"
        );
        comboBox.setPromptText("Select an option");
        grid.add(comboBox, 0, 0);

        // Create submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleComboBoxSelection(comboBox.getValue()));
        grid.add(submitButton, 1, 0);

        // Set scene and show
        Scene scene = new Scene(grid, 400, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleComboBoxSelection(String selection) {
        if (selection == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Selection", "Please select a valid option.");
            return;
        }

        switch (selection) {
            case "Member Actions":
                memberManager.showMemberActionsPage(); // Use MemberManager for member actions
                break;
            case "Claims Actions":
                claimsManager.showClaimsActionsPage(); // Use the ClaimsManager class instance
                break;
            case "Reports Actions":
                ReportsManager reportsManager = new ReportsManager(primaryStage);
                reportsManager.showReportsPage();
                break;
            case "Monthly Processes Actions":
                MonthlyProcessesManager manager = new MonthlyProcessesManager(primaryStage);
                manager.showMonthlyProcessesPage();
                break;
            case "Company Parameters Actions":
                CompanyParametersManager companyParametersManager = new CompanyParametersManager(primaryStage);
                companyParametersManager.showCompanyParametersPage();  // Call the method to display the page
                break;
            case "System Settings Actions":
                SystemSettingsManager settingsManager = new SystemSettingsManager();
                settingsManager.showSystemSettingsSelection(); // Opens the selection menu
                break;

            case "Other Actions":
                OtherFeaturesManager otherFeaturesManager = new OtherFeaturesManager();
                otherFeaturesManager.showOtherFeatures();
                break;

            default:
                showAlert(Alert.AlertType.ERROR, "Invalid Choice", "Please try again.");
                break;
        }
    }

    private void addFormField(GridPane grid, String labelText, int rowIndex) {
        Label label = new Label(labelText);
        TextField textField = new TextField();
        grid.add(label, 0, rowIndex);
        grid.add(textField, 1, rowIndex);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}