package motscroises6;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class ControleurV1 {
	private MotsCroisesTP6 mc;
	
	
	@FXML
	private GridPane grilleMC;
	
	@FXML // pour rendre la méthode visible depuis SceneBuilder
	private void initialize() { 
		ChargerGrille gc = new ChargerGrille();
		
		try {
			mc = gc.extraireGrille(10);
			resetGrid();
			initGrid();
			
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	@FXML
	public void clicLettre(MouseEvent e) {

		if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            TextField casse = (TextField) e.getSource();
            int lig = ((int) casse.getProperties().get("gridpane-row")) + 1;
            int col = ((int) casse.getProperties().get("gridpane-column")) + 1;
 
            casse.textProperty().bindBidirectional(mc.propositionProperty(lig, col));
            mc.montrerSolution(lig, col);
        }
	}
	
	private void resetGrid() {
		
		TextField modeleTF = (TextField) grilleMC.getChildren().get(0);
		
		grilleMC.getChildren().clear();
		
		for(int i=1; i< mc.getHauteur(); i++) {
			for(int j=1; j< mc.getLargeur(); j++) {
				if(!mc.estCaseNoire(i, j)) {
					TextField newField = new TextField();
					newField.setPrefWidth(modeleTF.getPrefWidth());
					newField.setPrefHeight(modeleTF.getPrefHeight());
					
					for (Object cle : modeleTF.getProperties().keySet())
					{
						newField.getProperties().put(cle, modeleTF.getProperties().get(cle));
					}
					grilleMC.add(newField, i-1, j-1);
				}
			}
		}
	}
	
	private void initGrid() {

		for (Node n : grilleMC.getChildren()){ 
			if (n instanceof TextField) {
				TextField tf = (TextField) n ;
				int lig = ((int) n.getProperties().get("gridpane-row")) + 1 ;
				int col = ((int) n.getProperties().get("gridpane-column")) + 1 ;
				
				//Binding bidirectional(1.3)
				tf.textProperty().bindBidirectional(mc.propositionProperty(lig, col));
				
				//Info-Bullles(1.4)
				String texte = getToolTip(lig, col);
				tf.setTooltip(new Tooltip(texte));
				
				//Montrer solution(1.5)
				tf.setOnMouseClicked(e -> this.clicLettre(e)); 
			}
			
		}
		
	}

	private String getToolTip(int lig, int col) {
		
		String horizontal = mc.getDefinition(lig, col, true);
		String vertical = mc.getDefinition(lig, col, false);
		String texte = null;
		
		if(horizontal != null && vertical != null) {
			texte = horizontal+"/"+vertical;
		}else if(vertical == null) {
			texte = horizontal;
		}else if(horizontal == null) {
			texte = vertical;
		}
		
		return texte;
	}
}