package it.polito.tdp.porto;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	txtResult.clear();
    	int id=boxPrimo.getValue().getId();
    	txtResult.appendText(model.getCoAutori(id).toString());

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	int id1=boxPrimo.getValue().getId();
    	int id2=boxSecondo.getValue().getId();
    	if(id1==id2) {
    		txtResult.appendText("I due autori devono essere diversi\n");
    		return;
    	}
    	if(model.isCoautore(id1, id2)) {
    		txtResult.appendText("Selezionare un secondo prof che non sia co-autore del primo!\nL'elenco è stato aggiornato\n");
    		List<Author> x=model.getNoCoAutori(id1);
        	Collections.sort(x);
        	boxSecondo.getItems().clear();
    		boxSecondo.getItems().addAll(x);
    		return;
    	}
    	txtResult.clear();
    	txtResult.appendText(model.sequenzaArticoli(boxPrimo.getValue(), boxSecondo.getValue()));
    	

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }
    
    public void setModel(Model m) {
    	this.model=m;
    	List<Author> x=m.getAutori();
    	Collections.sort(m.getAutori());
    	boxPrimo.getItems().addAll(x);
    	boxSecondo.getItems().addAll(x);
    	
    }
}
