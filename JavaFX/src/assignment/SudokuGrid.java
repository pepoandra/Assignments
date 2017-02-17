package assignment;

/*
 * Make interfaces for formatting resources and graphic
 */
import static assignment.ConstantsInterface.N_OF_COLS;
import static assignment.ConstantsInterface.N_OF_ROWS;
import static assignment.ConstantsInterface.SUBGRID_COLS;
import static assignment.ConstantsInterface.SUBGRID_ROWS;
import static assignment.ConstantsInterface.input_background;
import assignment.packGrid.clsPackGrid;
import assignment.packSubGrid.clsPackSubGrid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 *
 * @author Alex Kapranos el kpo
 */
public class SudokuGrid extends Application implements ConstantsInterface {

    static int[][] board = new int[N_OF_ROWS][N_OF_COLS];
    static int[][] solution = new int[N_OF_ROWS][N_OF_COLS];
    static Button DisplaySolution;
    static Button VerifyInput;

    public void start(final Stage primaryStage) {

        GridPane gridPane1 = new GridPane();
        gridPane1.setHgap(0);
        gridPane1.setVgap(0);
        gridPane1.setGridLinesVisible(true);

        final FlowPane panels[][] = new FlowPane[SUBGRID_ROWS][SUBGRID_COLS];
        final TextField labels[][] = new TextField[N_OF_ROWS][N_OF_COLS];

        final StackPane root = new StackPane();

        FlowPane panelsSolution[][] = new FlowPane[SUBGRID_ROWS][SUBGRID_COLS];
        final TextField labelsSolution[][] = new TextField[N_OF_ROWS][N_OF_COLS];

        GridPane gridPane2 = new GridPane();
        gridPane2.setHgap(0);
        gridPane2.setVgap(0);
        gridPane2.setGridLinesVisible(true);

        for (int i = 0; i < SUBGRID_COLS; i++) {
            for (int k = 0; k < SUBGRID_ROWS; k++) {
                panelsSolution[i][k] = new FlowPane();
                panelsSolution[i][k].setPrefWrapLength(90);
                panelsSolution[i][k].setStyle(style_outter);
                gridPane2.add(panelsSolution[i][k], i, k);
            }
        }

        for (int i = 0; i < N_OF_ROWS; i++) {
            for (int k = 0; k < N_OF_COLS; k++) {
                labelsSolution[i][k] = new TextField("");
                labelsSolution[i][k].setPrefHeight(30);
                labelsSolution[i][k].setPrefWidth(30);
                labelsSolution[i][k].setStyle(style_inner);
                panelsSolution[i / SUBGRID_COLS][k / SUBGRID_ROWS].getChildren().add(labelsSolution[i][k]);
            }
        }

        for (int j = 0; j < SUBGRID_COLS; j++) {
            for (int z = 0; z < SUBGRID_ROWS; z++) {
                panels[j][z] = new FlowPane();
                panels[j][z].setPrefWrapLength(90);
                panels[j][z].setStyle(style_outter);
                gridPane1.add(panels[j][z], j, z);
            }
        }

        for (int j = 0; j < N_OF_ROWS; j++) {
            for (int z = 0; z < N_OF_COLS; z++) {

                labels[j][z] = new TextField("");
                labels[j][z].setPrefHeight(30);
                labels[j][z].setPrefWidth(30);
                labels[j][z].setStyle(style_inner);
                panels[j / SUBGRID_COLS][z / SUBGRID_ROWS].getChildren().add(labels[j][z]);
            }
        }

        Label Input = new Label("§ Input §");
        Label Solution = new Label("§ Solution §");

        VBox GridsHolder = new VBox(10);
        GridsHolder.getChildren().addAll(Input, gridPane1, Solution, gridPane2);
        GridsHolder.setAlignment(Pos.CENTER);
        Input.setStyle(font_style);
        Solution.setStyle(font_style);

        VBox ButtonHolder = new VBox(10);
        Button DisplayGrid = new Button("Display Sudoku Grid");

        EventHandler<ActionEvent> listener;
        listener = new EventHandler<ActionEvent>() {

            public int readFile(java.io.File file, int i, int j) {
                StringBuilder stringBuffer = new StringBuilder();
                BufferedReader bufferedReader = null;

                try {
                    bufferedReader = new BufferedReader(new FileReader(file));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        stringBuffer.append(text);
                    }

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SudokuGrid.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(SudokuGrid.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SudokuGrid.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                return Character.getNumericValue(stringBuffer.charAt(2 * ((i * 9) + j)));

            }

            @Override
            public void handle(ActionEvent event) {
                int counter = 0;

                int[][] innerBoard = new int[N_OF_ROWS][N_OF_COLS];

                Button btn = (Button) event.getSource();
                String str = btn.getText();

                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    for (int i = 0; i < N_OF_ROWS; ++i) {
                        for (int j = 0; j < N_OF_COLS; ++j) {
                            innerBoard[i][j] = readFile(file, i, j);
                        }

                        System.out.println("");
                    }

                    for (int d = 0; d < SUBGRID_ROWS; ++d) {
                        for (int c = 0; c < SUBGRID_COLS; ++c) {
                            for (int b = 0; b < SUBGRID_ROWS; ++b) {
                                for (int a = 0; a < SUBGRID_COLS; ++a) {

                                    if (innerBoard[b * SUBGRID_ROWS + c][a + (d * SUBGRID_COLS)] != 0) {
                                        if (str.equals("Display Sudoku Grid")) {
                                            labels[counter / N_OF_ROWS][counter % N_OF_COLS].setText(" " + innerBoard[b * SUBGRID_ROWS + c][a + (d * SUBGRID_COLS)]);
                                            labels[counter / N_OF_ROWS][counter % N_OF_COLS].setStyle(input_background);
                                            labels[counter / N_OF_ROWS][counter % N_OF_COLS].setEditable(false);
                                        } else {
                                            labelsSolution[counter / N_OF_ROWS][counter % N_OF_COLS].setText(" " + innerBoard[b * SUBGRID_ROWS + c][a + (d * SUBGRID_COLS)]);
                                            labelsSolution[counter / N_OF_ROWS][counter % N_OF_COLS].setStyle(input_background);
                                            labelsSolution[counter / N_OF_ROWS][counter % N_OF_COLS].setEditable(false);

                                        }

                                    } else {
                                        labels[counter / N_OF_ROWS][counter % N_OF_COLS].setEditable(false);
                                        labelsSolution[counter / N_OF_ROWS][counter % N_OF_COLS].setEditable(false);
                                        labels[counter / N_OF_ROWS][counter % N_OF_COLS].setText("  ");
                                    }
                                    ++counter;
                                }
                            }
                        }
                    }
                }

                if (str.equals("Display Sudoku Grid")) {
                    for (int b = 0; b < N_OF_ROWS; ++b) {
                        for (int a = 0; a < N_OF_COLS; ++a) {
                            board[a][b] = innerBoard[a][b];
                        }
                    }
                    if (clsPackGrid.DigRepetInCol(board) || clsPackGrid.DigRepetInRow(board)
                            || clsPackSubGrid.DigRepetInSubGrid(board)) {

                        final JPanel panel = new JPanel();
                        JOptionPane.showMessageDialog(panel, "Invalid grid. Please insert a new sudoku grid.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Invalid grid. \n Please insert new \nSudoku grid.");
                        DisplaySolution.setDisable(true);

                    } else {
                        System.out.println("Valid grid. \nPlease insert new \nSolution grid.");
                        DisplaySolution.setDisable(false);
                    }
                } else {

                    for (int b = 0; b < N_OF_ROWS; ++b) {
                        for (int a = 0; a < N_OF_COLS; ++a) {
                            solution[a][b] = innerBoard[a][b];
                        }
                    }
                    if (clsPackGrid.DigRepetInCol(solution) || clsPackGrid.DigRepetInRow(solution)
                            || clsPackSubGrid.DigRepetInSubGrid(solution) || clsPackGrid.isComplete(solution)) {
                        final JPanel panel = new JPanel();
                        JOptionPane.showMessageDialog(panel, "Invalid solution. Please insert a new solution grid.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Invalid solution. \n Please insert new \nSolution grid.");
                        VerifyInput.setDisable(true);

                    } else {
                        System.out.println("Valid Solution. \nPlease press \nVerify Input.");
                        VerifyInput.setDisable(false);
                    }
                }
            }
        };

        DisplayGrid.setOnAction(listener);

        DisplaySolution = new Button("Display Solution Grid");
        DisplaySolution.setOnAction(listener);
        DisplaySolution.setDisable(true);

        VerifyInput = new Button("Verify Input");
        VerifyInput.setDisable(true);

        VerifyInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean works = true;
                for (int b = 0; b < N_OF_ROWS; ++b) {
                    for (int a = 0; a < N_OF_COLS; ++a) {
                        if (solution[a][b] != board[a][b] && board[a][b] != 0) {
                            works = false;
                        }
                    }
                }
                if (works) {
                    System.out.println("Solution matches. \nCongratulations. \nYou are victorious.");

                    ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/assignment/VictoryAnimationImage.png")));

                    Media pick = new Media(Paths.get("/ClassPathDir/Assignment/src/assignment/soviet-anthem.mp3").toUri().toString());
                    MediaPlayer player = new MediaPlayer(pick);
                    player.setStartTime(Duration.millis(0));
                    player.setStopTime(Duration.millis(30000));

                    player.play();
                    MediaView mediaView = new MediaView(player);

                    FadeTransition ft = new FadeTransition(Duration.millis(15000), iv);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    ft.setCycleCount(2);
                    ft.setAutoReverse(true);
                    ft.play();

                    root.getChildren().addAll(iv, mediaView);

                } else {
                    System.out.println("Solution doesn't match");

                    ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/assignment/SadAnimationImage.png")));

                    Media pick = new Media(Paths.get("/ClassPathDir/Assignment/src/assignment/chopin-funeral-march.mp3").toUri().toString());
                    MediaPlayer player = new MediaPlayer(pick);
                    player.setStartTime(Duration.millis(0));
                    player.setStopTime(Duration.millis(30000));

                    player.play();
                    MediaView mediaView = new MediaView(player);

                    FadeTransition ft = new FadeTransition(Duration.millis(15000), iv);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    ft.setCycleCount(2);
                    ft.setAutoReverse(true);
                    ft.play();

                    root.getChildren().addAll(iv, mediaView);
                }

            }
        });
        Button ExitButton = new Button("Back to Main Menu");
        ExitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Application menu = new MainMenu();
                Stage anotherStage = new Stage();
                try {
                    menu.start(anotherStage);
                    primaryStage.close();
                } catch (Exception ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        DisplayGrid.setMaxWidth(Double.MAX_VALUE);
        DisplaySolution.setMaxWidth(Double.MAX_VALUE);
        VerifyInput.setMaxWidth(Double.MAX_VALUE);
        ExitButton.setMaxWidth(Double.MAX_VALUE);

        DisplayGrid.setStyle(style_button);
        DisplaySolution.setStyle(style_button);
        VerifyInput.setStyle(style_button);
        ExitButton.setStyle(style_button);

        ButtonHolder.getChildren().add(DisplayGrid);
        ButtonHolder.getChildren().add(DisplaySolution);
        ButtonHolder.getChildren().add(VerifyInput);
        ButtonHolder.getChildren().add(ExitButton);

        ButtonHolder.setAlignment(Pos.CENTER);

        FlowPane VBoxHolder = new FlowPane();

        VBoxHolder.setHgap(40);
        VBoxHolder.setPrefWrapLength(450);
        VBoxHolder.setAlignment(Pos.CENTER);

        VBoxHolder.getChildren().add(GridsHolder);
        VBoxHolder.getChildren().add(ButtonHolder);
        VBoxHolder.setStyle(background_style);
        root.getChildren().add(VBoxHolder);
        Scene scene = new Scene(root, 490, 640);

        primaryStage.getIcons().add(new Image(icon_path));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Assignment 3");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
