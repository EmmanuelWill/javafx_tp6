package morpion.fx.v1;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import morpion.fx.modele.ModeleMorpionsFx;

public class ControleurV1 {
	
	private ModeleMorpionsFx morpion;
	@FXML
	private GridPane grille;
	
	@FXML // pour rendre la méthode visible depuis SceneBuilder
	private void initialize() { 
		this.morpion = new ModeleMorpionsFx();
		for (Node n : grille.getChildren()){ 
			n.setOnMouseClicked(e -> this.clicBouton(e)); 
		}
	}

	@FXML
	private void clicBouton(MouseEvent e) {
		Node n = (Node) e.getSource() ;
		Integer ligne = ((Integer) n.getProperties().get("gridpane-row"))+1;
		Integer colonne = ((Integer) n.getProperties().get("gridpane-column"))+1;
		morpion.jouerCoup(ligne, colonne) ;
		System.out.println("Coup joué : " + ligne + "/" + colonne);
		System.out.println("résultat: " + morpion.getEtatJeu());
	}

}
