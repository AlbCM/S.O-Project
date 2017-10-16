package Views;


import Models.Process;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ReadyCellView extends ListCell<Process> {

    @FXML
    private Label name;

    @FXML
    private Label quantum;

    @FXML
    private Label pid;

    @FXML
    private Label Executions;

    @FXML
    private GridPane gridPane;

    @Override
    protected void updateItem(Process process, boolean empty) {
        super.updateItem(process, empty);

        if(empty || process == null) {
            setGraphic(null);
        }
        else
        {

            FXMLLoader mLLoader = new FXMLLoader(getClass().getResource("/Views/ReadyCellView.fxml"));
            mLLoader.setController(this);
            try
            {
                mLLoader.load();
                quantum.setText("Quantum: "+ String.valueOf(process.getQuantum()));
                pid.setText("Pid: "+ process.getPid());
                name.setText(process.getProcessName());
                Executions.setText("Ejecuciones: " + process.getExecutions());
                setGraphic(gridPane);
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }


        }

    }

}
