package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import edu.sdccd.cisc191.matrix.*;
import edu.sdccd.cisc191.linalg.*;

public class Server extends Application {

    @Override
    public void start(Stage primaryStage) {

        int rows = 3;
        int columns = 3;

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        TextField[][] matrixInputs = new TextField[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                TextField field = new TextField();
                field.setPrefWidth(50);
                gridPane.add(field, j, i);
                matrixInputs[i][j] = field;
            }
        }

        TextField[] vectorInputs = new TextField[rows];
        for (int i = 0; i < rows; i++) {
            TextField field = new TextField();
            field.setPrefWidth(50);
            gridPane.add(field, columns, i);
            vectorInputs[i] = field;
        }

        Button solveButton = new Button("Solve");
        Text solutionText = new Text();
        gridPane.add(solveButton, columns + 1, 0);
        gridPane.add(solutionText, columns + 1, 1, 1, rows - 1);

        solveButton.setOnAction(e -> {
            Double[][] matrixA = new Double[rows][columns];
            Double[][] vectorB = new Double[rows][1];

            try {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        matrixA[i][j] = Double.parseDouble(matrixInputs[i][j].getText());
                    }
                    vectorB[i][0] = Double.parseDouble(vectorInputs[i].getText());
                }

                Matrix A = new Matrix(matrixA);
                Matrix b = new Matrix(vectorB);

                try {
                    Matrix x = LinSystem.solveSystem(A, b);
                    solutionText.setText(x.toString());
                } catch (Exception ex) {
                    solutionText.setText(ex.getMessage());
                }

            } catch (NumberFormatException ex) {
                solutionText.setText("Invalid input. Please enter valid numbers.");
            }

        });

        // Set the scene and stage
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Matrix Solver");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
