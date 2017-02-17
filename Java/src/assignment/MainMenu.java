package assignment;

import static assignment.ConstantsInterface.icon_path;
import static assignment.ConstantsInterface.style_button;
import assignment.SudokuGrid;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainMenu extends Application implements ConstantsInterface {

    @Override
    public void start(final Stage stage) {
        HBox hbox = new HBox();

        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(100);
        flow.setHgap(100);
        flow.setPrefWrapLength(400); 

        Button buttons[] = new Button[4];
        for (int i = 0; i < 4; i++) {
            buttons[i] = new Button("Assignment " + (i + 3));
            buttons[i].setStyle(style_button);
            flow.getChildren().add(buttons[i]);
        }
        
        buttons[0].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Application Sudoku = new SudokuGrid();
                Stage anotherStage = new Stage();
                try {
                    Sudoku.start(anotherStage);
                    stage.close();
                } catch (Exception ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        
        buttons[1].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Application CarRace = new CarRace();
                Stage anotherStage = new Stage();
                try {
                    CarRace.start(anotherStage);
                    stage.close();
                } catch (Exception ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        
        buttons[2].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {        
                final JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "This section is under construction.", "Under construction", JOptionPane.WARNING_MESSAGE);

            }
        });
        
        buttons[3].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                final JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "This section is under construction.", "Under construction", JOptionPane.WARNING_MESSAGE);
                
            }
        });
        

        flow.setAlignment(Pos.CENTER);
        Button ExitButton = new Button("Exit");
        hbox.getChildren().add(flow);
        
        ExitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                System.exit(0);
                
            }
        });

        hbox.setAlignment(Pos.CENTER);

        BorderPane border = new BorderPane();
        border.setCenter(hbox);
        border.setBottom(ExitButton);
        
        ImageView logo = new ImageView(new javafx.scene.image.Image(getClass().getResourceAsStream("/assignment/logo.jpg")));
        border.setTop(logo);
        
        
        ExitButton.setMaxWidth(Double.MAX_VALUE);
        ExitButton.setStyle(exit_style_button);

        StackPane root = new StackPane();

        border.setStyle(background_style);

        root.getChildren().add(border);

        Scene scene = new Scene(root, 335, 400);

        stage.getIcons().add(new javafx.scene.image.Image(icon_path));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Main Menu");
        stage.show();

        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
