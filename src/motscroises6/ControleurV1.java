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

		} catch (Exception e) {
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
		} else if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1) {
			TextField casse = (TextField) e.getSource();
			if (casse.getText().equals(" ")) {
				casse.clear();
			}

		}
	}

	private void resetGrid() {
		TextField modeleTF = (TextField) grilleMC.getChildren().get(0);

		grilleMC.getChildren().clear();

		for (int i = 1; i <= mc.getHauteur(); i++) {

			for (int j = 1; j <= mc.getLargeur(); j++) {

				if (!mc.estCaseNoire(i, j)) {
					TextField newField = new TextField();
					newField.setPrefWidth(modeleTF.getPrefWidth());
					newField.setPrefHeight(modeleTF.getPrefHeight());

					for (Object cle : modeleTF.getProperties().keySet()) {
						newField.getProperties().put(cle, modeleTF.getProperties().get(cle));
					}
					addTextLimiter(newField, 1);

					grilleMC.add(newField, j - 1, i - 1);

				}
			}

		}
		TextField gridTF = (TextField) grilleMC.getChildren().get(0);
		gridTF.requestFocus();
	}

	private void initGrid() {

		for (Node n : grilleMC.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n;
				int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
				int col = ((int) n.getProperties().get("gridpane-column")) + 1;

				// Binding bidirectional(1.3)
				tf.textProperty().bindBidirectional(mc.propositionProperty(lig, col));

				// Info-Bullles(1.4)
				String texte = getToolTip(lig, col);
				tf.setTooltip(new Tooltip(texte));

				// Montrer solution(1.5)
				tf.setOnMouseClicked(e -> this.clicLettre(e));

				tf.setOnKeyPressed(e -> this.moveOn(e));
				tf.setOnKeyReleased(e -> this.setProposition(e));
			}

		}

	}

	public void moveOn(KeyEvent e) {
		TextField maCase = (TextField) e.getSource();
		int lig = ((int) maCase.getProperties().get("gridpane-row"));
		int col = ((int) maCase.getProperties().get("gridpane-column"));
		if (e.getCode() == KeyCode.UP) {
			this.moveOnTop(lig, col);
			horizontal = false;
		} else if (e.getCode() == KeyCode.DOWN) {
			this.moveOnBottom(lig, col);
			horizontal = false;
		} else if (e.getCode() == KeyCode.LEFT) {
			this.moveOnLeft(lig, col);
			horizontal = true;
		} else if (e.getCode() == KeyCode.RIGHT) {
			this.moveOnRight(lig, col);
			horizontal = true;
		} else if (e.getCode() == KeyCode.BACK_SPACE) {
			maCase.textProperty().setValue("");
			if (horizontal) {
				this.moveOnLeft(lig, col);
			} else {
				this.moveOnTop(lig, col);
			}
		} else if (e.getCode() == KeyCode.ENTER) {
			this.checkAllCase(e);
		}
	}

	public void setProposition(KeyEvent e) {
		TextField currField = (TextField) e.getSource();
		int lig = ((int) currField.getProperties().get("gridpane-row"));
		int col = ((int) currField.getProperties().get("gridpane-column"));
		String prop = e.getText().toUpperCase();

		if (e.getCode().isLetterKey()) {
			currField.textProperty().set(prop);
			mc.setProposition(lig + 1, col + 1, prop.charAt(0));
			currField.setStyle("-fx-background-color: #FFFFFF");
			if (horizontal) {
				this.moveOnRight(lig, col);
			} else {
				this.moveOnBottom(lig, col);
			}
		} else if (e.getCode() != KeyCode.ENTER && (currField.getText().isBlank())) {
			currField.textProperty().set("");
		}
	}

	public void checkAllCase(KeyEvent e) {
		for (int i = 1; i <= mc.getHauteur(); i++) {
			for (int j = 1; j <= mc.getLargeur(); j++) {

				TextField currField = (TextField) getNextField(i - 1, j - 1);
				String solution = Character.toString(mc.getSolution(i, j)).toLowerCase();

				String proposition = currField != null ? currField.getText().toLowerCase() : "";

				if (currField != null && proposition != " " && solution.equals(proposition)) {
					currField.setStyle("-fx-background-color: #B0F2B6");
				}

			}
		}
	}

	private void moveOnTop(int lig, int col) {
		if (lig != 0) {
			int i = 1;
			TextField gridTF = (TextField) getNextField(lig - i, col);
			while (gridTF == null && (lig - i) != 0) {
				i++;
				gridTF = (TextField) getNextField(lig - i, col);
			}
			if (gridTF != null)
				gridTF.requestFocus();
		}
	}

	private void moveOnBottom(int lig, int col) {
		if (lig != grilleMC.getRowCount() - 1) {
			int i = 1;
			TextField gridTF = (TextField) getNextField(lig + i, col);
			while (gridTF == null && (lig + i) != grilleMC.getRowCount() - 1) {
				i++;
				gridTF = (TextField) getNextField(lig + i, col);
			}
			if (gridTF != null)
				gridTF.requestFocus();
		}
	}

	private void moveOnLeft(int lig, int col) {
		if (col != 0) {
			int i = 1;
			TextField gridTF = (TextField) getNextField(lig, col - 1);
			while (gridTF == null && (col - 1) != 0) {
				i++;
				gridTF = (TextField) getNextField(lig, col - i);
			}
			if (gridTF != null)
				gridTF.requestFocus();
		}
	}

	private void moveOnRight(int lig, int col) {
		try {
			boolean stop = false;
			if (col < grilleMC.getColumnCount() - 1) {
				int i = 1;
				TextField gridTF = (TextField) getNextField(lig, col + 1);
				while (gridTF == null && !stop) {
					if (lig + 1 >= grilleMC.getRowCount() && col + 1 >= grilleMC.getColumnCount()) {
						stop = true;
					}

					if ((col + 1) <= grilleMC.getColumnCount() - 1) {
						col++;
						gridTF = (TextField) getNextField(lig, col);
					} else {
						if (lig <= grilleMC.getRowCount() - 1) {
							lig++;
							col = -1;
							i = 1;
							gridTF = (TextField) getNextField(lig, col + i);
						}else {
							stop = true;
						}
					}
				}
				if (gridTF != null)
					gridTF.requestFocus();
			} else {
				if (lig < grilleMC.getRowCount() - 1) {
					lig++;
					col = -1;
					moveOnRight(lig, col);
				}
			}
		} catch (Exception e) {
			// System.out.println(e);
		}
	}

	private Node getNextField(int lig, int col) {
		for (Node n : grilleMC.getChildren()) {
			if (lig == GridPane.getRowIndex(n) && col == GridPane.getColumnIndex(n)) {
				return n;
			}
		}
		return null;
	}

	private String getToolTip(int lig, int col) {

		String isHorizontal = mc.getDefinition(lig, col, true);
		String isVertical = mc.getDefinition(lig, col, false);
		String texte = null;

		if (isHorizontal != null && isVertical != null) {
			texte = isHorizontal + "/" + isVertical;
		} else if (isVertical == null) {
			texte = isHorizontal;
		} else if (isHorizontal == null) {
			texte = isVertical;
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
