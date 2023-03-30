package motscroises6;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControleurListe implements Initializable {

	@FXML
	private ListView<String> gridList;
	
	private Map<Integer, String> grids;
	
	private MotsCroisesTP6 mc;
	
	private Parent root;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	public void loadGrilles(Map<Integer, String> grilles) {
		this.grids = grilles;
		
		for(String description: grids.values()) {
			gridList.getItems().add(description);
		}
		
		gridList.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	String currentGrid = gridList.getSelectionModel().getSelectedItem();
				int cpt = 0;
				
				for(Entry<Integer,String> entry: grids.entrySet()) {
				      if(entry.getValue().equals(currentGrid)) {
				        cpt = entry.getKey();
				        loadMC(event,cpt);
				        break;
				      }
				}
	        }
	    });
	}
	
	public void loadMC(MouseEvent event, int numGrid) {
		ChargerGrille gc = new ChargerGrille();
		try {
			mc = gc.extraireGrille(numGrid);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("VueTP6.fxml")) ;
			root = loader.load();
			
			ControleurV1 v1 = loader.getController();
			v1.getMc(mc);
			
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			
		}catch(Exception e) {
			System.out.println(e);
		}
		
	}

}
