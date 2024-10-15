import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ReportsManager {

    private Stage primaryStage;

    public ReportsManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showReportsPage() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Button memberReportsButton = new Button("Member Reports");
        memberReportsButton.setOnAction(e -> showMemberReports());
        grid.add(memberReportsButton, 0, 0);

        Button receiptingReportsButton = new Button("Receipting Reports");
        receiptingReportsButton.setOnAction(e -> showReceiptingReports());
        grid.add(receiptingReportsButton, 1, 0);

        Button brokerReportsButton = new Button("Agent Reports");
        brokerReportsButton.setOnAction(e -> showAgentReports());
        grid.add(brokerReportsButton, 0, 1);

        Button smsRemindersReportsButton = new Button("SMS Reminders Reports");
        smsRemindersReportsButton.setOnAction(e -> showSMSRemindersReports());
        grid.add(smsRemindersReportsButton, 1, 1);

        Button underwriterReportsButton = new Button("Underwriter Reports");
        underwriterReportsButton.setOnAction(e -> showUnderwriterReports());
        grid.add(underwriterReportsButton, 0, 2);

        Button transactionalReportsButton = new Button("Transactional Reports");
        transactionalReportsButton.setOnAction(e -> showTransactionalReports());
        grid.add(transactionalReportsButton, 1, 2);

        Button monthlyListsReportsButton = new Button("Monthly Lists Reports");
        monthlyListsReportsButton.setOnAction(e -> showMonthlyListsReports());
        grid.add(monthlyListsReportsButton, 0, 3);

        // Set scene and show
        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMemberReports() {
        new MemberReportsManager(primaryStage).showMemberReportsPage();
    }

    private void showReceiptingReports() {
        new ReceiptingReportsManager(primaryStage).showReceiptingReportsPage();
    }

    private void showAgentReports() {
        new AgentReportsManager(primaryStage).showAgentReportsPage();
    }

    private void showSMSRemindersReports() {
        new SMSRemindersReportsManager(primaryStage).showSMSRemindersReportsPage();
    }

    private void showUnderwriterReports() {
        new UnderwriterReportsManager(primaryStage).showMonthlyPaymentList();
    }

    private void showTransactionalReports() {
        new TransactionalReportsManager(primaryStage).showTransactionalReportsPage();
    }


    private void showMonthlyListsReports() {
        new MonthlyListsReportsManager(primaryStage).showMonthlyListsReportsPage();
    }
}
