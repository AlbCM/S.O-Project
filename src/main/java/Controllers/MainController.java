package Controllers;

import Models.DataService;
import Models.Process;
import Models.ProcessList;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    /* UI Attributes */
    private FileChooser fileChooser;
    @FXML
    private TableView<Process> processTable;
    @FXML
    private TableColumn<Process, String> NameColumn;
    @FXML
    private TableColumn<Process, String> PidColumn;
    @FXML
    private TableColumn<Process, Integer> QuantumColumn;
    @FXML
    private TableColumn<Process, String> SourceFileColumn;
    @FXML
    private JFXButton StartButton;
    @FXML
    private Text MessageLabel;

    private DataService service;


    public MainController() {
        service = DataService.getInstance();
    }

    /* User Actions */
    public void importXML() {
        File xml = pickXMLFile();
        if(xml != null){
            List<Process> data = deserializeXML(xml);
            // Add list to TableView
            service.Ready.addAll(data);
        }


    }

    public void beginScheduling() throws IOException, InterruptedException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/Views/visualizacion.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();

        Stage stage = new Stage();
        JFXDecorator decorator = new JFXDecorator(stage, root1);
        Scene scene = new Scene(decorator);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();


        //stage.close();
    }

    public void showNewProcessModal() throws IOException {
        // Load fxml view
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/Views/addProcess.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        JFXDecorator decorator = new JFXDecorator(stage, root1);
        Scene scene = new Scene(decorator);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("ABC");
        stage.setScene(scene);
        stage.show();
    }


    /* Helpers */
    public File pickXMLFile() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione un archivo XML");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File file = fileChooser.showOpenDialog(null);
        return file;
    }

    public List<Process> deserializeXML(File file) {
        XmlMapper xmlMapper = new XmlMapper();
        String xml = null;
        try {
            xml = inputStreamToString(new FileInputStream(file));
            List<Process> data = xmlMapper.readValue(xml, ProcessList.class).getProcesses();
            return data;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Validate table visible binding
        IntegerBinding listSize = Bindings.size(service.Ready);
        processTable.visibleProperty().bind(listSize.greaterThan(0));

        // Message Label visible binding
        MessageLabel.visibleProperty().bind(listSize.isEqualTo(0));

        // Validate start button disable binding
        StartButton.disableProperty().bind(listSize.isEqualTo(0));

        // setting table columns
        NameColumn.setCellValueFactory(cellData -> cellData.getValue().processNameProperty());
        PidColumn.setCellValueFactory(cellData -> cellData.getValue().pidProperty() );
        QuantumColumn.setCellValueFactory(cellData -> cellData.getValue().quantumProperty().asObject());
        SourceFileColumn.setCellValueFactory(cellData -> cellData.getValue().sourceFileProperty());

        processTable.setItems(service.Ready);


    }
}
