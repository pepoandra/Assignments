/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

/**
 *
 * @author Alex
 */
public interface ConstantsInterface {

    int N_OF_ROWS = 9;
    int N_OF_COLS = 9;
    int SUBGRID_ROWS = 3;
    int SUBGRID_COLS = 3;

    String font_style = "-fx-font: 15px 'Back In The USSR DL';" + " -fx-text-fill: yellow;";
    String background_style = "-fx-background-color: red;";
    String style_outter = "-fx-border-color: yellow;"
            + "-fx-border-width: 1.5;";
    String style_inner = "-fx-border-color: yellow;"
            + "-fx-border-width: 0.5;";
    String style_button = "-fx-background-color: linear-gradient(#ff5400, #be1d00);\n"
            + "-fx-background-radius: 30;\n"
            + "-fx-background-insets: 0;\n"
            + "-fx-text-fill: yellow;"
            + "-fx-font: 12px 'Back In The USSR DL';";
    String exit_style_button = "-fx-background-color: linear-gradient(#ff5400, #be1d00);\n"
            + "-fx-text-fill: yellow;"
            + "-fx-font: 12px 'Back In The USSR DL';";
    

    String input_background = "-fx-background-image: url(http://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Hammer_and_sickle_red_on_transparent.svg/36px-Hammer_and_sickle_red_on_transparent.svg.png);"
            + "-fx-font-weight: bold;"
            + "-fx-stroke-width: 1;"
            + "-fx-background-color: red;"
            + "-fx-text-fill: yellow;"
            + "-fx-border-color: yellow;"
            + "-fx-border-width: 0.5;";
    String icon_path = "assignment/Karl_Marx_128x128.png"; 
    
    double CarFrom = 0;
    double CarTo = 750;
    int vCycle = 1;
    double BobbingFromX = 0;
    double BobbingToX = 10;
    double BobbingByX = 3;
    double DurationBobbing1 = 500;
    double DurationBobbing2 = 100;
    int MaxDurationCar = 5200;
    int MinDurationCar = 4000;
    
    

}
