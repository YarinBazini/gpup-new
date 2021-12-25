package appScreen;

import enums.FxmlPath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import menu.MenuController;

import java.io.IOException;
import java.net.URL;

public class AppController {
    @FXML private StackPane menu_area;
    private static Parent menuParent;
    private static MenuController menuComponentController = null;

    @FXML private StackPane content_area;
    private boolean isLoadFile = false;

    @FXML
    public void initialize() {
            if(menuComponentController == null) {
                setMenuFxml();
            }
            setMenu(this.menuParent);
    }

    private void setMenuFxml(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("../" + FxmlPath.MENU);
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
}