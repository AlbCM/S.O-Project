package Controllers;

import Models.DataService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

import Models.Process;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;


public class AddController implements Initializable {

    /* UI Atrributes */
    @FXML
    JFXTextField NameEntry = new JFXTextField();
    @FXML
    JFXTextField QuantumEntry = new JFXTextField();
    @FXML
    JFXTextField SourceFileEntry = new JFXTextField();
    @FXML
    JFXButton SaveButton = new JFXButton();

    private DataService service = DataService.getInstance();

    @FXML
    public void pickFile()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione un archivo");
        File file = fileChooser.showOpenDialog(null);
        SourceFileEntry.setText(file.getAbsolutePath());
    }

    @FXML
    public void saveProcess(){
        Process process = new Process();
        process.setProcessName(NameEntry.getText());
        process.setQuantum(Integer.parseInt(QuantumEntry.getText()));
        process.setSourceFile(SourceFileEntry.getText());
        process.setPid(String.valueOf(process.getId()));
        if(service.Ready.isEmpty()){
            process.setArrivalTime(0);
        }
        else {
            int last_time = service.Ready.get(service.Ready.size() - 1).getArrivalTime();
            process.setArrivalTime(last_time + 1);
        }
        service.Ready.add(process);

        // Close this window.
        Stage stage = (Stage) SaveButton.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Binding disable button property
        SaveButton.disableProperty().bind(
                Bindings.isEmpty(NameEntry.textProperty())
                        .or(Bindings.isEmpty(QuantumEntry.textProperty()))
                        .or(Bindings.isEmpty(SourceFileEntry.textProperty()))
        );

        NameEntry.setLabelFloat(true);
        NameEntry.setPromptText("Nombre");
        // Validator for NameEntry
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Este campo es obligatorio");
        FontAwesomeIconView fontAwesomeIconView =
                new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        validator.setIcon(fontAwesomeIconView);
        NameEntry.getValidators().add(validator);
        NameEntry.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal) NameEntry.validate();
        });
        // QuantumEntry Label
        QuantumEntry.setLabelFloat(true);
        QuantumEntry.setPromptText("Quantum");
        // Validator for QuantumEntry
        QuantumEntry.getValidators().add(validator);
        QuantumEntry.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal) QuantumEntry.validate();
        });

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        QuantumEntry.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(),null, integerFilter)
        );
        // SourceFileEntry Label
        SourceFileEntry.setLabelFloat(true);
        SourceFileEntry.setPromptText("Archivo Origen");
        // Validator for SourceFileEntry
        SourceFileEntry.getValidators().add(validator);
        SourceFileEntry.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal) SourceFileEntry.validate();
        });

    }
}
