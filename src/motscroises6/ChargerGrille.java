package motscroises6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ChargerGrille {

	private Connection connexion ;
	
	public ChargerGrille()
	{
		try { 
			this.connexion = connecterBD();
			
		}catch (Exception e) { 
			e.printStackTrace(); 
		}
	}
	
	public static Connection connecterBD() throws SQLException
	{
		
			Connection connect;		
			String url = "jdbc:mysql://mysqln.istic.univ-rennes1.fr/base_bousse?autoReconnect=true&useSSL=false";
			String user = "user_ehode";
			String password = "vW85spjgwbNKjrf";
			connect = DriverManager.getConnection(url, user, password);
			
			return connect;	
			
	}
	
	public Map<Integer, String> grillesDisponibles() throws SQLException
	{
		Statement stmt = this.connexion.createStatement();
		
		ResultSet result = stmt.executeQuery("SELECT * FROM TP5_GRILLE");
		
		HashMap<Integer, String> grilles = new HashMap<Integer, String>();
		
		while(result.next()) {
			String description = result.getString("nom_grille")+" ("+result.getInt("hauteur")+"X"+result.getInt("largeur")+")";
			
			grilles.put(result.getInt("num_grille"), description);
			
		}
		
		return grilles;
	}
	
	public MotsCroisesTP6 extraireGrille(int numGrille) throws SQLException
	{
		String getGrille = "SELECT hauteur, largeur FROM TP5_GRILLE WHERE num_grille = ?";
		
		PreparedStatement pstmt1 = this.connexion.prepareStatement(getGrille);
		pstmt1.setInt(1, numGrille);
		ResultSet dimensions = pstmt1.executeQuery();
		int largeur = 0; int hauteur = 0;
		
		if(dimensions.next()) {
			largeur = dimensions.getInt("largeur");
			hauteur = dimensions.getInt("hauteur");
		}
		
		MotsCroisesTP6 grilleExtraite = new MotsCroisesTP6(hauteur, largeur);
		for(int j = 1; j<= grilleExtraite.getHauteur(); j++) {
			for(int k = 1; k <= grilleExtraite.getLargeur(); k++) {
				grilleExtraite.setCaseNoire(j, k, false);
			}
		}
		
		String request = "SELECT * FROM TP5_MOT WHERE num_grille = ?";
		
		PreparedStatement pstmt = this.connexion.prepareStatement(request);
		pstmt.setInt(1, numGrille);
		
		ResultSet mots = pstmt.executeQuery();
		
		
		while(mots.next()) {
			
			int col = mots.getInt("colonne");
			int lig = mots.getInt("ligne");
			
			String def = mots.getString("definition");
			boolean horiz = (mots.getInt("horizontal") == 1);
			
			grilleExtraite.setDefinition(lig, col, horiz, def);
			
			String solution = mots.getString("solution");
			
			if(horiz) {
				for(int i = 0; i < solution.length(); i++) {
					grilleExtraite.setSolution(lig, col+i, solution.charAt(i));
				}
			}else {
				for(int i = 0; i < solution.length(); i++) {
					grilleExtraite.setSolution(lig+i, col, solution.charAt(i));
				}
			}				
		}
		return grilleExtraite;
	}
	
	public static void main(String[] args) throws SQLException {
		ChargerGrille test = new ChargerGrille();
		
	
		
		MotsCroisesTP6 jeu = test.extraireGrille(10);
		
		for(int i = 1; i<= jeu.getHauteur(); i++) {
			for(int j = 1; j<= jeu.getLargeur(); j++) {
				System.out.print(jeu.getSolution(i, j)+"|");
			}
			System.out.println("\n");
		}
		
		
	}
}
