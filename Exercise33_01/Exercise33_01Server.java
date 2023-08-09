// Exercise31_01Server.java: The server can communicate with
// multiple clients concurrently using the multiple threads
import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;import java.net.*;
import java.io.*;


public class Exercise33_01Server extends Application {
  // Text area for displaying contents
  private TextArea ta = new TextArea();

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    ta.setWrapText(true);
   
    // Create a scene and place it in the stage
    Scene scene = new Scene(new ScrollPane(ta), 400, 200);
    primaryStage.setTitle("Exercise31_01Server"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    
    Loan mostRecentLoan = new Loan();
    
    new Thread(() -> {
      try {
        ServerSocket serverSocket = new ServerSocket(8000);
        Platform.runLater(() -> ta.appendText("Exercise 31_01 Server started at " + mostRecentLoan.getLoanDate() + '\n' + "Connected to a client at " + mostRecentLoan.getLoanDate() + '\n'));
        
        Socket socket = serverSocket.accept();
        
        DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
        DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
        
        while(true) {
          double yearIntRate = inputFromClient.readDouble();
          int yearNumb = inputFromClient.readInt();
          double loanNumb = inputFromClient.readDouble();
          
          mostRecentLoan.setAnnualInterestRate(yearIntRate);
          mostRecentLoan.setNumberOfYears(yearNumb);
          mostRecentLoan.setLoanAmount(loanNumb);
          
          double monthPayment = mostRecentLoan.getMonthlyPayment();
          double totalPayment = mostRecentLoan.getTotalPayment();
          
          outputToClient.writeDouble(monthPayment);
          outputToClient.writeDouble(totalPayment);
          
          Platform.runLater(() -> {
            ta.appendText("Annual Interest Rate: " + yearIntRate + '\n');
            ta.appendText("Number of years: " + yearNumb + '\n');
            ta.appendText("Loan Amount: " + loanNumb + '\n');
            ta.appendText("Monthly Payment: " + monthPayment + '\n');
            ta.appendText("Total Payment: " + totalPayment + '\n');
          });
          
        }
      }
      
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }).start();
  }
    
  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
