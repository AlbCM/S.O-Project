/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.DataService;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javax.swing.JOptionPane;
import Models.Process;


/**
 *
 * @author macha
 */
public class ChartsController implements Initializable{
    @FXML
    private BarChart<?, ?> LongTimeChart;
             
    @FXML
    private CategoryAxis x;
    
    @FXML
    private NumberAxis y;
    
    private DataService service;
    
    private SortedList<Process> sList = new SortedList<Process>(FXCollections.emptyObservableList());
    
    private int option;
    
   
    
    public ChartsController(int option){
        this.option = option;
        this.service = DataService.getInstance();
        switch(option){
            case 1: {
                sList =   service.Finished.sorted(
                    (Process p1, Process p2) -> p2.ExecutionTime() - p1.ExecutionTime()
                );
                break;
            } 
            case 2: {
                sList =   service.Finished.sorted(
                        Comparator.comparingInt(Process::ExecutionTime)
                );
                break;
            }
        }
    }

  
            
    @Override
    public void initialize(URL url, ResourceBundle rb){
        int temp=0;
        //JOptionPane.showMessageDialog(null, sList.size());
        
        XYChart.Series data = new XYChart.Series<>();
        for(int i = 0; i<sList.size(); i++){
            if(temp==5){break;}
            else{
                data.getData().add(new XYChart.Data(sList.get(i).getProcessName(),sList.get(i).ExecutionTime()));
                temp++;
            }
        }
        temp=0;
        if(option == 1){
            LongTimeChart.setTitle("5 mayores tiempos de finalizacion");
        }
        else{
            LongTimeChart.setTitle("5 menores tiempos de finalizacion");
        }
        LongTimeChart.getData().addAll(data);
        
        
    }
    
    
}
