package motscroises6;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ControleurMenu {

	@FXML
	private Pane menu;
	
	@FXML 
	private Button quitter;
	
	private MotsCroisesTP6 mc;
	
	private ListView list;
	
	private Parent root;
	
	
	private Map<Integer, String> grilles;
	
	

	@FXML // pour rendre la méthode visible depuis SceneBuilder
	private void initialize() { 
		
//		try {
//			grilles = gc.grillesDisponibles();
//
//		}catch(Exception e) {
//			System.out.println(e);
//		}
		

	}
	
//	@FXML
//	private void showGridnumber(MouseEvent e) {
//
//		if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
//            TextField casse = (TextField) e.getSource();
//            int lig = ((int) casse.getProperties().get("gridpane-row")) + 1;
//            int col = ((int) casse.getProperties().get("gridpane-column")) + 1;
// 
//           
//        }
//	}
	
	@FXML
	private void closeAll() {
		// get a handle to the stage
	    Stage stage = (Stage) quitter.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}
	
	@FXML
	private void randomGrid(ActionEvent event) {
		ChargerGrille gc = new ChargerGrille();
		try {
			grilles = gc.grillesDisponibles();
			List<Integer> keysAsArray = new ArrayList<Integer>(grilles.keySet());
			Random r = new Random();
			
			int randomGrille = r.nextInt(keysAsArray.size());
			mc = gc.extraireGrille(randomGrille);
			
			
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
	
	@FXML
	private void pickGrid() {
		ChargerGrille gc = new ChargerGrille();
		
		try {
			grilles = gc.grillesDisponibles();
			
			Map<Integer, String> grilles = gc.grillesDisponibles();
			
			
			
			
		}catch(Exception e) {
			System.out.println(e);
		}

	}
}
