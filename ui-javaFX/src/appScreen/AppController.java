package appScreen;

import dtoObjects.TargetFXDTO;
import engineManager.EngineManager;
import enums.FxmlPath;
import exceptions.XmlException;
import generalComponents.GeneralComponent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import menu.MenuController;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public class AppController {
    @FXML private StackPane menu_area;
    private static Parent menuParent;
    private static MenuController menuComponentController = null;
    private Stage primaryStage;
    @FXML private StackPane content_area;
    private boolean isLoadFile = false;
    private EngineManager engineManager;
    private GeneralComponent generalComponent;

    public AppController(){
        this.engineManager = new EngineManager();
        this.generalComponent = new GeneralComponent();
        this.generalComponent.setAppController(this);
    }

    @FXML
    public void initialize() {
            if(menuComponentController == null) {
                setMenuFxml();
            }
            setMenu(this.menuParent);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void setMenuFxml(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(FxmlPath.MENU.toString());
            fxmlLoader.setLocation(url);
            this.menuParent = fxmlLoader.load(url.openStream());
            this.menuComponentController = fxmlLoader.getController();
            this.menuComponentController.setAppController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setArea(Parent data){
        content_area.getChildren().removeAll();
        content_area.getChildren().setAll(data);
    }

    public void setMenu(Parent data){
        menu_area.getChildren().removeAll();
        menu_area.getChildren().setAll(data);
    }

    public MenuController getMenuComponentController() {
        return menuComponentController;
    }

    public void setLoadFile(boolean val){
        this.isLoadFile = val;
    }

    public boolean getLoadFile(){
        return isLoadFile;
    }

    public void loadFile(String path) throws XmlException {
        this.engineManager.load(path);
    }

    public Collection<TargetFXDTO> getAllTargets(){
        return this.engineManager.getAllTargets();
    }
}
