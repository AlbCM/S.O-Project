package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataService {

    public ObservableList<Process> Ready;
    public ObservableList<Process> Waiting;
    public ObservableList<Process> Executing;
    public ObservableList<Process> Finished;

    private static DataService instance = null;
    private DataService(){
        Ready = FXCollections.observableArrayList();
        Waiting = FXCollections.observableArrayList();
        Executing = FXCollections.observableArrayList();
        Finished = FXCollections.observableArrayList();
    }

    public static DataService getInstance() {
        if(instance == null){
            instance = new DataService();
        }
        return instance;
    }
}
