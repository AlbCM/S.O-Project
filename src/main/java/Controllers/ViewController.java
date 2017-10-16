package Controllers;

import Views.ExecutingCellView;
import Views.FinishedCellView;
import Views.ReadyCellView;
import Models.DataService;
import Models.Process;
import Views.WaitingCellView;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    @FXML
    private JFXListView<Process> Ready = new JFXListView<>();

    @FXML
    private JFXListView<Process> Executing = new JFXListView<>();

    @FXML
    private JFXListView<Process> Waiting = new JFXListView<>();

    @FXML
    private JFXListView<Process> Finished = new JFXListView<>();


    private DataService service = DataService.getInstance();

    public void start() {
        // Do things that are reliant upon the FXML being loaded
        Process p = service.Ready.get(0);
        Thread t = new Thread(() -> {
            try {
                readAndWrite(p);
                p.setExecutions(p.getExecutions() + 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void readAndWrite(Process p) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(p.getSourceFile()));
        File source = new File(p.getSourceFile());
        String OutputFile = source.getParent().concat("/" + p.getPid() + ".txt");
        File output = new File(OutputFile);
        BufferedWriter writer;
        if (p.getExecutions() == 0 && output.exists()) {
            writer = new BufferedWriter(new FileWriter(OutputFile, false));
        } else {
            writer = new BufferedWriter(new FileWriter(OutputFile, true));
        }

        int tam = p.getQuantum() * 5;
        char[] buffer = new char[tam];

        int readCharacters = tam * p.getExecutions();
        for (int i = 0; i < readCharacters; i++) {
            reader.read();
        }

        if (reader.ready()) {
            reader.read(buffer);
            for (int i = 0; i < tam; i++) {
                if (buffer[i] != Character.MIN_VALUE) {
                    writer.write(buffer[i]);
                }
            }

        } else {
            p.finished = true;
        }
        writer.close();
        reader.close();
    }

    public void Task(Process p){
        while(! p.finished) {
            try {

                if(p.getExecutions() == 0){ // Está en listo y debe pasar a ejecución
                    service.Ready.remove(p);
                    Thread.sleep(200);
                    service.Executing.add(p);
                }
                else {
                    // Esta en espera y pasa a listo
                    service.Ready.remove(p);
                    Thread.sleep(200);
                    service.Executing.add(p);
                }

                //
                readAndWrite(p);

                p.sleep(1000);
                p.setExecutions(p.getExecutions() + 1);


                service.Executing.remove(p);
                Thread.sleep(200);
                service.Ready.add(p);



            } catch (IOException e) {

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        service.Executing.remove(p);
        service.Ready.remove(p);
        service.Finished.add(p);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Ready.setCellFactory(itemListView -> new ReadyCellView());
        Executing.setCellFactory(itemListView -> new ExecutingCellView());
        Waiting.setCellFactory(itemListView -> new WaitingCellView());
        Finished.setCellFactory(itemListView -> new FinishedCellView());
        Ready.setItems(service.Ready);
        Executing.setItems(service.Executing);
        Waiting.setItems(service.Waiting);
        Finished.setItems(service.Finished);
        // Rutina para determinar el turno
        // Cuando se sabe quien va ejecutarse pasarlo a la view de ejecución y quitarlo de la lista de LISTO
        // Ejecutar el metodo run del proceso seleccionado
            // Copiar n = Quantum * 5 caracteres
            // Esperar por n ms
        // Cuando termino dependiendo si hace falta pasarlo a Espera o si no a terminado
    }


}
