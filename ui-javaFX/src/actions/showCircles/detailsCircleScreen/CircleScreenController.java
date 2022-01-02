package actions.showCircles.detailsCircleScreen;

import actions.showCircles.ShowCirclesController;
import actions.showPaths.ShowPathsController;
import appScreen.AppController;
import dtoObjects.TargetDTO;
import enums.StyleSheetsPath;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

import java.util.LinkedHashSet;
import java.util.List;

public class CircleScreenController extends mainControllers.Controllers {
    private ShowCirclesController mainController;
    private BooleanProperty isLight;

    @FXML
    private StackPane fall_screen_SP;

    @FXML
    private Label target_label;

    @FXML
    private Label paths_message_label;

    @FXML
    private TextArea paths_TA;

    @FXML
    private Button find_btn;

    @FXML
    public void initialize() {
        this.isLight = new SimpleBooleanProperty(true);
        this.isLight.addListener((a,b,c)->{
            if(this.isLight.getValue()){
                this.fall_screen_SP.getStylesheets().remove(StyleSheetsPath.MAIN_CSS_DARK.toString());
                this.fall_screen_SP.getStylesheets().remove(StyleSheetsPath.ACTIONS_DARK.toString());
                this.fall_screen_SP.getStylesheets().add(StyleSheetsPath.MAIN_CSS_LIGHT.toString());
                this.fall_screen_SP.getStylesheets().add(StyleSheetsPath.ACTIONS_LIGHT.toString());
            }else{
                this.fall_screen_SP.getStylesheets().remove(StyleSheetsPath.MAIN_CSS_LIGHT.toString());
                this.fall_screen_SP.getStylesheets().remove(StyleSheetsPath.ACTIONS_LIGHT.toString());
                this.fall_screen_SP.getStylesheets().add(StyleSheetsPath.MAIN_CSS_DARK.toString());
                this.fall_screen_SP.getStylesheets().add(StyleSheetsPath.ACTIONS_DARK.toString());
            }
        });
    }

    public BooleanProperty isLightProperty() {
        return isLight;
    }

    @FXML
    void clickFind(ActionEvent event) {
        this.paths_TA.setText("");
        if(this.target_label.getText().equals("")){
            this.paths_TA.setText("Please select one target from the table.");
        } else {
            LinkedHashSet<TargetDTO> list = this.appController.getTargetCircle(this.target_label.getText());
            setCircleTable(list);
        }
    }

    public TextArea getPaths_TA() {
        return paths_TA;
    }

    private void setCircleTable(LinkedHashSet<TargetDTO> list){
        if(list.size() == 0)
            this.paths_TA.setText("There is no circle with the selected target.");
        else {
            this.paths_TA.appendText("( ");
            for (TargetDTO targetDTO : list) {
                this.paths_TA.appendText(targetDTO.getName() + " ");
            }
            this.paths_TA.appendText(")");
        }
    }

    public Button getFind_btn() {
        return find_btn;
    }

    public Label getTarget_label() {
        return target_label;
    }

    public StackPane getFall_screen_SP() {
        return fall_screen_SP;
    }

    @Override
    public void setAppController(AppController mainControllers) { this.appController = mainControllers; }

    public void setMainController(ShowCirclesController mainControllers) {
        this.mainController = mainControllers;
    }

}
