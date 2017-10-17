package Controllers;

import Classes.MySkin;
import Models.DataService;
import Models.Process;
import Views.ExecutingCellView;
import Views.FinishedCellView;
import Views.ReadyCellView;
import Views.WaitingCellView;
import com.jfoenix.controls.JFXListView;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.swing.*;
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

        Task<Void> task = createTask();
        task.setOnSucceeded((WorkerStateEvent event) -> {
            System.out.println("Finished");
        });
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }


    public Task<Void> createTask(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int size = service.Ready.size();
                // start every process
                for(int i=0; i<size; i++){
                    Task(service.Ready.get(0));
                }
                // middle
                while(!service.Waiting.isEmpty()){
                    Task(service.Waiting.get(0));
                }
                return null;
            }
        };
        return task;
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
            // Aumentamos el numero de ejecuciones
            p.setExecutions(p.getExecutions() + 1);

        } else {
            p.finished = true;
        }
        writer.close();
        reader.close();
    }

    public void Task(Process p){
        try {
            if(p.getExecutions() == 0){
                // Está en listo y debe pasar a ejecución
                service.Ready.remove(p);
                ((MySkin) Waiting.getSkin()).refresh();
                ((MySkin) Ready.getSkin()).refresh();
            }
            else {
                // Esta en espera y pasa a listo
                service.Waiting.remove(p);
                ((MySkin) Waiting.getSkin()).refresh();
            }
            Thread.sleep(200);

            // Lo ponemos en ejecución
            service.Executing.add(p);
            ((MySkin) Executing.getSkin()).refresh();


            //Hacemos la rutina de escritura
            readAndWrite(p);

            // Esperamos
            Thread.sleep(1000);



            // Lo sacamos de ejecución
            service.Executing.remove(p);
            ((MySkin) Executing.getSkin()).refresh();

            Thread.sleep(200);

            // SI no ha terminado Lo ponemos en espera
            if(!p.finished){
                service.Waiting.add(p);
                ((MySkin) Waiting.getSkin()).refresh();
            }
            else{
                service.Finished.add(p);
                ((MySkin) Finished.getSkin()).refresh();
            }

            System.out.println("Process " + p.getProcessName() + " finished");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Ready.setCellFactory(itemListView -> new ReadyCellView());
        Ready.setSkin(new MySkin(this.Ready));
        Executing.setCellFactory(itemListView -> new ExecutingCellView());
        Executing.setSkin(new MySkin(this.Executing));
        Waiting.setCellFactory(itemListView -> new WaitingCellView());
        Waiting.setSkin(new MySkin(this.Waiting));
        Finished.setCellFactory(itemListView -> new FinishedCellView());
        Finished.setSkin(new MySkin(this.Finished));

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

        start();
    }


}
