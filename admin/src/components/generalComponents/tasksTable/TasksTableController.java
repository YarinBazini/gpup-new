package components.generalComponents.tasksTable;

import components.appScreen.AppController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TasksTableController extends components.mainControllers.Controllers implements Closeable {
    private Timer timer;
    private TimerTask listRefresher;
    private BooleanProperty autoUpdate = new SimpleBooleanProperty(true);
    private IntegerProperty totalTasks = new SimpleIntegerProperty(0);
    private String selected;
    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

    @FXML
    private TableView<TaskFX> table;
    @FXML
    private TableColumn<TaskFX, CheckBox> selectCol;

    @FXML
    private TableColumn<TaskFX, String> nameCol;

    @FXML
    private TableColumn<TaskFX, String> createdByCol;

    @FXML
    private TableColumn<TaskFX, String> graphNameCol;

    @FXML
    private TableColumn<TaskFX, Integer> numOfTargetsCol;

    @FXML
    private TableColumn<TaskFX, Integer> rootsCol;

    @FXML
    private TableColumn<TaskFX, Integer> middleCol;

    @FXML
    private TableColumn<TaskFX, Integer> leafsCol;

    @FXML
    private TableColumn<TaskFX, Integer> independentsCol;

    @FXML
    private TableColumn<TaskFX, Integer> totalPriceCol;

    @FXML
    private TableColumn<TaskFX, Integer>numOfWorkersCol;

    @Override
    public void close() {
        table.getItems().clear();
        totalTasks.set(0);
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

    @FXML
    public void initialize(){
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.createdByCol.setCellValueFactory(new PropertyValueFactory<>("admin"));
        this.graphNameCol.setCellValueFactory(new PropertyValueFactory<>("graphName"));
        this.selectCol.setCellValueFactory(new PropertyValueFactory<>("select"));
        this.numOfTargetsCol.setCellValueFactory(new PropertyValueFactory<>("countTargets"));
        this.rootsCol.setCellValueFactory(new PropertyValueFactory<>("countRoots"));
        this.middleCol.setCellValueFactory(new PropertyValueFactory<>("countMiddles"));
        this.leafsCol.setCellValueFactory(new PropertyValueFactory<>("countLeaves"));
        this.independentsCol.setCellValueFactory(new PropertyValueFactory<>("countIndependents"));
        this.totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        this.numOfWorkersCol.setCellValueFactory(new PropertyValueFactory<>("numOfWorkers"));
    }

    public BooleanProperty autoUpdatesProperty() {
        return autoUpdate;
    }

    public TableView<TaskFX> getTable() {
        return table;
    }

    private void updateTaskList(List<TaskFX> taskFXES) {
        Platform.runLater(() -> {
            ObservableList<TaskFX> items = table.getItems();
            items.clear();
            items.addAll(taskFXES);
            setSelectOnClick(table.getItems());
            if(this.selected != null){
                for (TaskFX taskFX : table.getItems()){
                    if(taskFX.getName().equals(selected)){
                        taskFX.getSelect().setSelected(true);
                    }
                }
            }
            totalTasks.set(taskFXES.size());
        });
    }

    private void setSelectOnClick(Collection<TaskFX> tasks){
        for (TaskFX taskFX : tasks){
            CheckBox selectCheckBox = taskFX.getSelect();
            selectCheckBox.selectedProperty().addListener((a,b,c)->{
                if(selectCheckBox.isSelected()){
                    this.selected = taskFX.getName();
                    this.isSelected.set(true);
                    this.appController.setSelectedTask(taskFX.getName());
                }else{
                    this.selected = null;
                    this.isSelected.set(false);
                    this.appController.setSelectTask(false);
                }
            });
            selectCheckBox.disableProperty().bind(isSelected.and(selectCheckBox.selectedProperty().not()) );
        }
    }

    public void startListRefresher() {
        listRefresher = new TaskTableRefresher(
                autoUpdate,
                this::updateTaskList);
        timer = new Timer();
        timer.schedule(listRefresher, 1000, 1000);
    }


    @Override
    public void setAppController(AppController mainControllers) {
        this.appController = mainControllers;
    }
}