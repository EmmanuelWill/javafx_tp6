package motscroises6;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControleurV1 {
	private MotsCroisesTP6 mc;
	
	
	@FXML
	private GridPane grilleMC;
	private boolean horizontal = true;
	
	@FXML // pour rendre la méthode visible depuis SceneBuilder
	private void initialize() {
		
		
	}
	
	@FXML
	public void getMc(MotsCroisesTP6 mc) {

		this.mc = mc;
		try {
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
        }else if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1) {
        	TextField casse = (TextField) e.getSource();
        	if(casse.getText().equals(" ")) {
        		casse.clear();
        	}
        	
        }
	}
	
	private void resetGrid() {
		TextField modeleTF = (TextField) grilleMC.getChildren().get(0);
		
		grilleMC.getChildren().clear();
		
		for(int i=1; i <= mc.getHauteur(); i++) {
			
			for(int j=1; j <= mc.getLargeur(); j++) {
				
				if(!mc.estCaseNoire(i, j)) {
					TextField newField = new TextField();
					newField.setPrefWidth(modeleTF.getPrefWidth());
					newField.setPrefHeight(modeleTF.getPrefHeight());
					
					for (Object cle : modeleTF.getProperties().keySet())
					{
						newField.getProperties().put(cle, modeleTF.getProperties().get(cle));
					}
					addTextLimiter(newField, 1);

					grilleMC.add(newField, j-1, i-1);
					
				}
			}
			
		}
		TextField gridTF = (TextField) grilleMC.getChildren().get(0);
		gridTF.requestFocus();
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

				tf.setOnKeyPressed(e -> this.moveOn(e));
				tf.setOnKeyReleased(e -> this.remplirProp(e));
			}
			
		}
		
	}

	
	public void moveOn(KeyEvent e) {
        TextField maCase = (TextField) e.getSource();
        int lig = ((int) maCase.getProperties().get("gridpane-row"));
        int col = ((int) maCase.getProperties().get("gridpane-column"));
        if (e.getCode() == KeyCode.UP) {
            this.enHaut(lig, col);
            horizontal = false;
        } else if (e.getCode() == KeyCode.DOWN) {
            this.enBas(lig, col);
            horizontal = false;
        } else if (e.getCode() == KeyCode.LEFT) {
            this.aGauche(lig, col);
            horizontal = true;
        } else if (e.getCode() == KeyCode.RIGHT) {
            this.aDroite(lig, col);
            horizontal = true;
        } else if (e.getCode() == KeyCode.BACK_SPACE) {
            maCase.textProperty().setValue("");
            if(horizontal){
                this.aGauche(lig,col);
            }else {
                this.enHaut(lig,col);
            }
        }
    }
    private void enHaut(int lig, int col) {
        if (lig != 0) {
            int i = 1;
            TextField tf = (TextField) getNextField(lig - i, col);
            while (tf == null && (lig - i) != 0) {
                i++;
                tf = (TextField) getNextField(lig - i, col);
            }
            tf.requestFocus();
        }
    }
    private void enBas(int lig, int col) {
        if (lig != grilleMC.getRowCount() - 1) {
            int i = 1;
            TextField tf = (TextField) getNextField(lig + i, col);
            while (tf == null && (lig + i) != grilleMC.getRowCount() - 1) {
                i++;
                tf = (TextField) getNextField(lig + i, col);
            }
            tf.requestFocus();
        }
    }
    private void aGauche(int lig, int col) {
        if (col != 0) {
            int i = 1;
            TextField tf = (TextField) getNextField(lig, col - 1);
            while (tf == null && (col - 1) != 0) {
                i++;
                tf = (TextField) getNextField(lig, col - i);
            }
            tf.requestFocus();
        }
    }
    private void aDroite(int lig, int col) {
        if (col != grilleMC.getColumnCount() - 1) {
            int i = 1;
            TextField tf = (TextField) getNextField(lig, col + 1);
            while (tf == null && (col + 1) != grilleMC.getColumnCount() - 1) {
                i++;
                tf = (TextField) getNextField(lig, col + i);
            }
            tf.requestFocus();
        }
    }
    public void remplirProp(KeyEvent e) {
        TextField maCase = (TextField) e.getSource();
        int lig = ((int) maCase.getProperties().get("gridpane-row"));
        int col = ((int) maCase.getProperties().get("gridpane-column"));
       
        if (e.getCode().isLetterKey()) {
            String prop = e.getText().toUpperCase();
            maCase.textProperty().set(prop);
            if (horizontal) {
                this.aDroite(lig, col);
            } else {
                this.enBas(lig, col);
            }
        }else{
            maCase.textProperty().set(" ");
    }
        
}

	
	private Node getNextField(int lig, int col) {
		
		for (Node n : grilleMC.getChildren()){ 
			if(lig == grilleMC.getRowIndex(n) && col == grilleMC.getColumnIndex(n)) {
				return n;
			}
		}
		return null;
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
	
	public static void addTextLimiter(TextField tf, int maxLength) {
	    tf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (tf.getText().length() > maxLength) {
	                String s = tf.getText().substring(0, maxLength);
	                tf.setText(s);
	            }
				
			}
	    });
	}
}
