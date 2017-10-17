package Controllers;

import Models.Process;
import Models.DataService;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ReportController implements Initializable {

    private DataService service;
    
    @FXML
    private TableView<Process> table;
    
    @FXML
    private TableColumn<Process, String> pid;
    
    @FXML
    private TableColumn<Process, String> name;
      
    @FXML
    private TableColumn<Process, Integer> quantum;
       
    @FXML
    private TableColumn<Process, Integer> finalizacion;
    
    @FXML
    private TableColumn<Process, String> sourceFile;

    @FXML
    private TableColumn<Process, Integer> arrivalTime;

    
    private ObservableList<Process> list = FXCollections.emptyObservableList();
    
    
    public List<Process> findDuplicates(){
        List<Process> duplicates = new ArrayList<>();
        service.Finished.forEach( (item) -> {
            int count = 0;
           for(int i=0; i<service.Finished.size(); i++){
               if(service.Finished.get(i).ExecutionTime() == item.ExecutionTime()){
                   count++;
               }
           }
           if(count > 1){
               duplicates.add(item);
           } 
        });
        return duplicates;
    }
    
    public ReportController(int option){
         this.service = DataService.getInstance();
        switch(option){
            case 1: {
                list = service.Finished;
                break;
            } 
            case 2: {
                list = FXCollections.observableList(findDuplicates());
                break;
            }
            case 3: {
                
                List<Process> first5 = service.Finished.stream()
                       .sorted(Comparator.comparing(x -> x.ExecutionTime()))
                        .limit(5)
                        .collect(Collectors.toList());
                
                list = FXCollections.observableList(first5);
                break;
            }
            case 4: {
                List<Process> processOrdered= service.Finished.stream()
                       .sorted(Comparator.comparing(x -> x.ExecutionTime()))
                        .collect(Collectors.toList());
                
                list = FXCollections.observableList(processOrdered);
                break;
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       pid.setCellValueFactory(cellData -> cellData.getValue().pidProperty());
       name.setCellValueFactory(cellData -> cellData.getValue().processNameProperty());
       quantum.setCellValueFactory(value -> value.getValue().quantumProperty().asObject());
       sourceFile.setCellValueFactory( data -> data.getValue().sourceFileProperty());
       arrivalTime.setCellValueFactory( data -> data.getValue().arrivalTimeProperty().asObject());

       finalizacion.setCellValueFactory((cell) -> {
           int intValue = cell.getValue().ExecutionTime();
           return new SimpleIntegerProperty(intValue).asObject();    
       });
      table.getItems().setAll(list);
    }
}