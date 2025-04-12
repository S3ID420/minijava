package view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public abstract class BaseView {
    protected BorderPane mainLayout;
    
    public BaseView() {
        mainLayout = new BorderPane();
    }
    
    public void setFooter(HBox footer) {
        mainLayout.setBottom(footer);
    }
}