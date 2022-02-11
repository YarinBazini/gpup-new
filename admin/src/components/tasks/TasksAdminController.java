package components.tasks;

import components.adminEnums.AppFxmlPath;
import components.appScreen.AppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

public class TasksAdminController extends components.mainControllers.Controllers{
    private static PreTaskAdminController preTaskAdminController = null;
    private static Parent createNewTasksParent;
    private static TaskManagmentController taskManagementController = null;
    private static Parent taskManagementParent;

    public void setCreateNewTaskControllers() {
        if(preTaskAdminController == null) {
            setCreateNewTaskFxml();
        }
        this.setPreTaskScreen();
    }

    public static Parent getCreateNewTasksParent() {
        return createNewTasksParent;
    }

    public void setPreTaskScreen(){
        this.appController.setArea(getCreateNewTasksParent());
        this.preTaskAdminController.setTables();
    }

    void setCreateNewTaskFxml(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(AppFxmlPath.PRE_TASKS.toString());
            fxmlLoader.setLocation(url);
            this.createNewTasksParent = fxmlLoader.load(url.openStream());
            this.preTaskAdminController= fxmlLoader.getController();
            this.preTaskAdminController.setAppController(this.appController);
            this.preTaskAdminController.setMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTasksManagementControllers() {
        if(taskManagementController == null) {
            setTasksManagementFxml();
        }
        this.setTasksManagementScreen();
    }

    public static Parent getTasksManagementParent() {
        return taskManagementParent;
    }

    public void setTasksManagementScreen(){
        this.appController.setArea(getTasksManagementParent());
    }

    void setTasksManagementFxml(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(AppFxmlPath.TASKS_MANAGEMENT.toString());
            fxmlLoader.setLocation(url);
            this.taskManagementParent = fxmlLoader.load(url.openStream());
            this.taskManagementController= fxmlLoader.getController();
            this.taskManagementController.setAppController(this.appController);
            this.taskManagementController.setMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setArea(StackPane area, Parent data){
        area.getChildren().removeAll();
        area.getChildren().setAll(data);
    }

    @Override
    public void setAppController(AppController mainControllers) {
        this.appController = mainControllers;
    }
}