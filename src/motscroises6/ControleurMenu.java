package motscroises6;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControleurMenu {

	@FXML
	private Pane menu;
	
	@FXML 
	private Button quitter;
	
	@FXML // pour rendre la méthode visible depuis SceneBuilder
	private void initialize() { 
		
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
}
