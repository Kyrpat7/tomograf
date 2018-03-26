package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import sample.util.SinogramUtils;

import javax.swing.plaf.FileChooserUI;
import java.io.*;

public class Controller {

    public ScrollBar stepScrollBar;
    public ScrollBar angleScrollBar;
    public ScrollBar detectorScrollBar;
    public Label detectorLabel;
    public Label angleLabel;
    public Label stepLabel;
    public Label fileLabel;
    public Pane originalPane;
    public Pane sinogramPane;
    public Pane reconstructedPane;
    private String filePath;



    @FXML
    void initialize() throws FileNotFoundException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        scrollBarsSetUp();
    }


    private void settingPane() throws FileNotFoundException{
        Image image = new Image(new FileInputStream(filePath), 400, 400, true, false );

        originalPane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));

        reconstructedPane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
    }

    public void execute(ActionEvent actionEvent) {
        // =======SINOGRAM====
        MatOfByte bytes = new MatOfByte();
        Mat sinogram_mat = SinogramUtils.createSinogram(filePath, (int)stepScrollBar.getValue(), (int)angleScrollBar.getValue(), (int)detectorScrollBar.getValue());
        Highgui.imencode(".bmp", sinogram_mat, bytes);
        Image sin = new Image(new ByteArrayInputStream(bytes.toArray()), 400, 400, true, false);
        sinogramPane.setBackground(new Background(new BackgroundImage(sin, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
    }

    public void chooseFile(ActionEvent actionEvent) throws IOException{
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(stage);

        if(file!=null){
            filePath = file.getAbsolutePath();
            fileLabel.setText("Opened file: " + file.getName());
            settingPane();
        }
    }

    private void scrollBarsSetUp(){
        stepScrollBar.setMin(0);
        stepScrollBar.setMax(10);
        stepScrollBar.setValue(1);
        stepLabel.setText("1");
        angleScrollBar.setMin(0);
        angleScrollBar.setMax(270);
        angleScrollBar.setValue(180);
        angleLabel.setText("180");
        detectorScrollBar.setMin(0);
        detectorScrollBar.setMax(500);
        detectorScrollBar.setValue(250);
        detectorLabel.setText("250");

        stepScrollBar.valueProperty().addListener(
                (observable, oldvalue, newvalue) ->
                {
                    int i = newvalue.intValue();
                    stepLabel.setText(Integer.toString(i));
                }
        );

        angleScrollBar.valueProperty().addListener(
                (observable, oldvalue, newvalue) ->
                {
                    int i = newvalue.intValue();
                    angleLabel.setText(Integer.toString(i));
                }
        );

        detectorScrollBar.valueProperty().addListener(
                (observable, oldvalue, newvalue) ->
                {
                    int i = newvalue.intValue();
                    detectorLabel.setText(Integer.toString(i));
                }
        );
    }
}
