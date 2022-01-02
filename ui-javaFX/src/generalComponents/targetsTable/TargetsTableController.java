package generalComponents.targetsTable;


import Enums.TargetPosition;
import dtoObjects.TargetFXDTO;
import enums.StyleSheetsPath;
import generalComponents.GeneralComponent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Collection;

public class TargetsTableController extends GeneralComponent {
    private int maxSelect;
    private ArrayList<TargetFXDTO> curSelected;
    private IntegerProperty selectedCounter;
    private BooleanProperty isMaxSelected;
    private BooleanProperty isLight;

    @FXML
    private StackPane main_screen;

    @FXML
    private TableView<TargetFXDTO> table;

    @FXML
    private TableColumn<TargetFXDTO, String> nameCol;

    @FXML
    private TableColumn<TargetFXDTO, Integer> positionCol;

    @FXML
    private TableColumn<TargetFXDTO, Integer> directDependsOnCol;

    @FXML
    private TableColumn<TargetFXDTO, Integer> totalDependsOnCol;

    @FXML
    private TableColumn<TargetFXDTO, Integer> directRequiredForCol;

    @FXML
    private TableColumn<TargetFXDTO, Integer> totalRequiredForCol;

    @FXML
    private TableColumn<TargetFXDTO, String> freeTextCol;

    @FXML
    private TableColumn<TargetFXDTO, Integer> serialSetsCol;

    @FXML
    private TableColumn<TargetFXDTO, CheckBox> selectCol;

    public TableView<TargetFXDTO> getTable() {
        return table;
    }

    public TargetsTableController(Collection<TargetFXDTO> targets){
        table.setItems(getTargets(targets));
    }

    public ObservableList<TargetFXDTO> getTargets(Collection<TargetFXDTO> targets){
        ObservableList<TargetFXDTO> targetsObs = FXCollections.observableArrayList();
        for (TargetFXDTO resTargetDTO : targets){
            targetsObs.add(new TargetFXDTO(resTargetDTO));
        }
        return targetsObs;
    }

    public int getMaxSelect() {
        return maxSelect;
    }

    public void setMaxSelect(int maxSelect) {
        this.maxSelect = maxSelect;
        setSelectOnClick(this.table.getItems());
    }

    public ArrayList<TargetFXDTO> getCurSelected() {
        return curSelected;
    }

    public int getSelectedCounter() {
        return selectedCounter.get();
    }

    public IntegerProperty selectedCounterProperty() {
        return selectedCounter;
    }

    @FXML
    public void initialize(){
        this.curSelected = new ArrayList<>();
        this.isMaxSelected = new SimpleBooleanProperty();
        this.selectedCounter = new SimpleIntegerProperty();

        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.directDependsOnCol.setCellValueFactory(new PropertyValueFactory<>("directDependsOn"));
        this.totalDependsOnCol.setCellValueFactory(new PropertyValueFactory<>("totalDependsOn"));
        this.directRequiredForCol.setCellValueFactory(new PropertyValueFactory<>("directRequiredFor"));
        this.totalRequiredForCol.setCellValueFactory(new PropertyValueFactory<>("totalRequiredFor"));
        this.freeTextCol.setCellValueFactory(new PropertyValueFactory<>("generalInfo"));
        this.serialSetsCol.setCellValueFactory(new PropertyValueFactory<>("serialSets"));
        this.positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        this.selectCol.setCellValueFactory(new PropertyValueFactory<>("select"));
        this.selectCol.setSortable(false);
        Collection<TargetFXDTO> targets = this.getAppController().getAllTargets();
        this.table.setItems(getTargets(targets));
        this.isLight = new SimpleBooleanProperty(true);
        this.isLight.addListener((a,b,c)->{
            if(this.isLight.getValue()){
                this.main_screen.getStylesheets().remove(StyleSheetsPath.TARGETS_TABLE_DARK.toString());
                this.main_screen.getStylesheets().add(StyleSheetsPath.TARGETS_TABLE_LIGHT.toString());
            }else{
                this.main_screen.getStylesheets().remove(StyleSheetsPath.TARGETS_TABLE_LIGHT.toString());
                this.main_screen.getStylesheets().add(StyleSheetsPath.TARGETS_TABLE_DARK.toString());
            }
        });
    }

    public BooleanProperty isLightProperty() {
        return isLight;
    }

    private void setSelectOnClick(Collection<TargetFXDTO> targets){
        for (TargetFXDTO targetFXDTO : targets){
            CheckBox selectCheckBox = targetFXDTO.getSelect();
            selectCheckBox.selectedProperty().addListener((a,b,c)->{
                if(selectCheckBox.isSelected()){
                    this.curSelected.add(targetFXDTO);
                    this.selectedCounter.setValue(this.curSelected.size());
                    if(this.selectedCounter.getValue() == this.maxSelect){
                        this.isMaxSelected.set(true);
                    }
                }else{
                    this.curSelected.remove(targetFXDTO);
                    this.selectedCounter.setValue(this.curSelected.size());
                    if(this.selectedCounter.getValue() < this.maxSelect) {
                        this.isMaxSelected.set(false);
                    }
                }

            });
            selectCheckBox.disableProperty().bind(isMaxSelected.and(selectCheckBox.selectedProperty().not()) );
        }
    }

    public void deselectAll(){
        for(TargetFXDTO targetFXDTO: this.table.getItems()){
            if(targetFXDTO.getSelect().isSelected()){
                targetFXDTO.getSelect().setSelected(false);
            }
        }
    }


    public TargetsTableController(){

    }

    public void setSelectDisable(){
        for(TargetFXDTO targetFXDTO: this.table.getItems()){
            targetFXDTO.getSelect().setDisable(true);
        }
    }

}
