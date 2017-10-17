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


    // Properties
    @JacksonXmlProperty(localName = "Name")
    private StringProperty processName = new SimpleStringProperty();

    @JacksonXmlProperty(localName = "Pid")
    private StringProperty pid = new SimpleStringProperty();

    @JacksonXmlProperty(localName = "Quantum")
    private IntegerProperty quantum = new SimpleIntegerProperty();

    @JacksonXmlProperty(localName = "SourceFile")
    private StringProperty sourceFile = new SimpleStringProperty();

    private IntegerProperty executions = new SimpleIntegerProperty(0);

    private IntegerProperty arrivalTime = new SimpleIntegerProperty(0);


    // Attributes
    public boolean finished = false;
    private DataService service = DataService.getInstance();

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
    public IntegerProperty arrivalTimeProperty() {
        return arrivalTime;
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
    public int getArrivalTime() {
        return arrivalTime.get();
    }
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime.set(arrivalTime);
    }

    public int ExecutionTime(){
        return getExecutions() * getQuantum();
    }

}
