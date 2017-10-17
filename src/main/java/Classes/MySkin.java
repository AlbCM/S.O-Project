package Classes;

import Models.Process;
import com.sun.javafx.scene.control.skin.ListViewSkin;
import javafx.scene.control.ListView;

public class MySkin  extends ListViewSkin<Process> {

    public MySkin(ListView<Process> listView) {
        super(listView);
    }

    public void refresh(){
        super.flow.recreateCells();
    }
}
