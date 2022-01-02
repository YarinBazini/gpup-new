package generalInfo;

import appScreen.AppController;
import enums.FxmlPath;
import generalInfo.showGraphInfo.ShowGraphInfoController;
import generalInfo.showTargetInfo.ShowTargetInfoController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import templates.LoadFileError;

import java.io.IOException;
import java.net.URL;

public class GeneralInfoController extends mainControllers.Controllers {
    private static ShowTargetInfoController showTargetInfoComponentController = null;
    private static Parent showTargetInfoParent;
    private static ShowGraphInfoController showGraphInfoComponentController = null;
    private static Parent showGraphInfoParent;

    @FXML
    private Label lable;

    @Override
    public void setAppController(AppController mainControllers) {
        this.appController = mainControllers;
    }

    public void setTargetInfoControllers() {
        if(showTargetInfoComponentController == null) {
            setTargetInfoFxml();
        }
        this.setLoadFileHandlingTargetInfo();
    }

    public void setLoadFileHandlingTargetInfo(){
        this.appController.setArea(getShowTargetInfoParent());
        if(!this.appController.getLoadFile()){
            showTargetInfoComponentController.getDetailsGrid().setVisible(false);
            LoadFileError.setLoadFileError(showTargetInfoComponentController.getDataArea(), this.appController);
        } else {
            LoadFileError.removeLoadFileError(showTargetInfoComponentController.getDataArea());
            showTargetInfoComponentController.getDetailsGrid().setVisible(true);
            showTargetInfoComponentController.setTableScreen();
            showTargetInfoComponentController.setPageScreen();
        }
    }

    void setTargetInfoFxml(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(FxmlPath.TARGET_INFO.toString());
            fxmlLoader.setLocation(url);
            this.showTargetInfoParent = fxmlLoader.load(url.openStream());
            this.showTargetInfoComponentController= fxmlLoader.getController();
            this.showTargetInfoComponentController.setAppController(this.appController);
            this.showTargetInfoComponentController.setMainController(this);
            this.showTargetInfoComponentController.isLightProperty().bind(this.appController.isLightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGraphInfoControllers() {
        if(showGraphInfoComponentController == null) {
            setGraphInfoFxml();
        }
        this.setLoadFileHandlingGraphInfo();
    }

    void setGraphInfoFxml(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(FxmlPath.GRAPH_INFO.toString());
            fxmlLoader.setLocation(url);
            this.showGraphInfoParent = fxmlLoader.load(url.openStream());
            this.showGraphInfoComponentController= fxmlLoader.getController();
            this.showGraphInfoComponentController.setAppController(this.appController);
            this.showGraphInfoComponentController.setMainController(this);
            this.showGraphInfoComponentController.isLightProperty().bind(this.appController.isLightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLoadFileHandlingGraphInfo(){
        this.appController.setArea(getShowGraphInfoParent());
        if(!this.appController.getLoadFile()){
            showGraphInfoComponentController.getDetailsGrid().setVisible(false);
            LoadFileError.setLoadFileError(showGraphInfoComponentController.getDataArea(), this.appController);
        } else {
            LoadFileError.removeLoadFileError(showGraphInfoComponentController.getDataArea());
            showGraphInfoComponentController.getDetailsGrid().setVisible(true);
            showGraphInfoComponentController.setTableScreen();
            showGraphInfoComponentController.setPageScreen();
        }
    }

    public void setArea(StackPane area, Parent data){
        area.getChildren().removeAll();
        area.getChildren().setAll(data);
    }

    public Parent getShowTargetInfoParent() { return this.showTargetInfoParent; }

    public Parent getShowGraphInfoParent() { return this.showGraphInfoParent; }

}
