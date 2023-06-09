package motscroises6;

import java.util.Map;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ControleurMenu {

	@FXML
	private Pane menu;
	
	@FXML 
	private Button quitter;
	
	private MotsCroisesTP6 mc;
		
	private Parent root;
	
	
	private Map<Integer, String> grilles;
	
	

	@FXML // pour rendre la méthode visible depuis SceneBuilder
	private void initialize() { 
		

	}
	

	
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
			Random r = new Random();
			int randomGrille = r.nextInt(grilles.keySet().size());
			while (randomGrille == 0) {
				randomGrille = r.nextInt(grilles.keySet().size());
			}
			
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
	private void pickGrid(ActionEvent event) {
		ChargerGrille gc = new ChargerGrille();
		
		try {
			grilles = gc.grillesDisponibles();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ListeGrilles.fxml")) ;
			root = loader.load();
			
			ControleurListe cl = loader.getController();
			cl.loadGrilles(grilles);
			
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			
		}catch(Exception e) {
			System.out.println(e);
		}

	}
}
