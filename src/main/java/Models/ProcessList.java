package Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName="ProcessList")
public class ProcessList {

    @JsonProperty("Process")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Process> process;

    @JsonIgnore
    public List<Process> getProcesses(){
        return process;
    }

    public void setProcess(List<Process> process) {
        this.process = process;
    }
}
