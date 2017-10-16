package Models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;

@JacksonXmlRootElement
public class Process extends Thread {


    @JacksonXmlProperty(localName = "Name")
    private StringProperty processName = new SimpleStringProperty();

    @JacksonXmlProperty(localName = "Pid")
    private StringProperty pid = new SimpleStringProperty();

    @JacksonXmlProperty(localName = "Quantum")
    private IntegerProperty quantum = new SimpleIntegerProperty();

    @JacksonXmlProperty(localName = "SourceFile")
    private StringProperty sourceFile = new SimpleStringProperty();

    private IntegerProperty executions = new SimpleIntegerProperty(0);

    public boolean finished = false;

    public volatile boolean active = false;

    private DataService service = DataService.getInstance();


    public Process(){

    }

    // Properties methods

    public StringProperty processNameProperty() {
        return processName;
    }
    public StringProperty pidProperty() {
        return pid;
    }
    public IntegerProperty quantumProperty() {
        return quantum;
    }
    public StringProperty sourceFileProperty() {
        return sourceFile;
    }
    public IntegerProperty executionsProperty(){
        return executions;
    }

    // Getters and Setters
    public String getProcessName() {
        return processName.get();
    }
    public void setProcessName(String processName) {
        this.processName.set(processName);
    }
    public String getPid() {
        return pid.get();
    }
    public void setPid(String pid) {
        this.pid.set(pid);
    }
    public int getQuantum() {
        return quantum.get();
    }
    public void setQuantum(int quantum) {
        this.quantum.set(quantum);
    }
    public String getSourceFile() {
        return sourceFile.get();
    }
    public void setSourceFile(String sourceFile) {
        this.sourceFile.set(sourceFile);
    }
    public int getExecutions() {
        return executions.get();
    }
    public void setExecutions(int executions) {
        this.executions.set(executions);
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void run() {
        try {
            if (getExecutions() == 0) { // Está en listo y debe pasar a ejecución
                service.Ready.remove(this);
                this.sleep(200);
                service.Executing.add(this);
            } else {
                // Esta en espera y pasa a listo
                service.Ready.remove(this);
                this.sleep(200);
                service.Executing.add(this);
            }

            //
            readAndWrite(this);

            this.sleep(1000);
            setExecutions(getExecutions() + 1);


            service.Ready.add(this);
            Thread.sleep(200);
            service.Executing.remove(this);

        } catch (IOException e) {

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
            finished = true;
        }
        writer.close();
        reader.close();
    }

}
