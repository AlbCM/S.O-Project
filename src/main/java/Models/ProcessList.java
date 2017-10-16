package Models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName="ProcessList")
public class ProcessList {

    @JacksonXmlProperty(localName = "Process")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Process> process;

    public List<Process> getProcesses(){
        return process;
    }

    public void setProcess(List<Process> process) {
        this.process = process;
    }
}
