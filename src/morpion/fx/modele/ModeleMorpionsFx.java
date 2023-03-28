package morpion.fx.modele;

public class ModeleMorpionsFx implements SpecifModeleMorpions {
	public static final int TAILLE = 3;
	public static final int NBJOUEURS = 2;
	private int[][] morpGrid = new int[3][3];
	private int nbCoupsJoues;
	private Etat etatJeu;
	
	
	public ModeleMorpionsFx() {
		this.nbCoupsJoues = 0;
		this.etatJeu = Etat.J1_JOUE;
	}

	public Etat getEtatJeu() {
		return this.etatJeu;
	}
	
	public boolean estFinie() {
		return this.etatJeu != Etat.J1_JOUE && this.etatJeu != Etat.J2_JOUE;
	}

	public int getJoueur() {
		if (this.etatJeu == Etat.J1_JOUE) {
			return 1;
		} else {
			return this.etatJeu == Etat.J2_JOUE ? 2 : 0;
		}
	}
	
	public int getVainqueur() {
		if (this.etatJeu == Etat.J1_VAINQUEUR) {
			return 1;
		} else {
			return this.etatJeu == Etat.J2_VAINQUEUR ? 2 : 0;
		}
	}

	public int getNombreCoups() {
		return this.nbCoupsJoues;
	}

	public boolean estCoupAutorise(int ligne, int colonne) {
		return !this.estFinie() && this.caseValide(ligne, colonne) && this.morpGrid[ligne-1][colonne-1] == 0;
	}

	public void jouerCoup(int ligne, int colonne) {
		
		if (this.estCoupAutorise(ligne, colonne)) {
			this.morpGrid[ligne - 1][colonne - 1] = this.getJoueur();
			++this.nbCoupsJoues;
			this.reCalculerEtat();
		}

	}

	private boolean caseValide(int ligne, int colonne) {
		return 1 <= ligne && ligne <= 3 && 1 <= colonne && colonne <= 3;
	}

	private int rechercheVainqueur() {
		int lig;
		int col;
		int produit;
		
		for (lig = 1; lig <= 3; ++lig) {
			produit = 1;

			for (col = 1; col <= 3; ++col) {
				produit *= this.morpGrid[lig-1][col-1];
			}

			if (produit == 8) {
				return 2;
			}

			if (produit == 1) {
				return 1;
			}
		}

		for (col = 1; col <= 3; ++col) {
			produit = 1;

			for (lig = 1; lig <= 3; ++lig) {
				produit *= this.morpGrid[lig-1][col-1];
			}

			if (produit == 8) {
				return 2;
			}

			if (produit == 1) {
				return 1;
			}
		}

		produit = 1;

		for (lig = 1; lig <= 3; ++lig) {
			produit *= this.morpGrid[lig-1][lig-1];
		}

		if (produit == 8) {
			return 2;
		} else if (produit == 1) {
			return 1;
		} else {
			produit = 1;

			for (lig = 1; lig <= 3; ++lig) {
				produit *= this.morpGrid[lig-1][4-lig-1];
			}

			if (produit == 8) {
				return 2;
			} else if (produit == 1) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private void reCalculerEtat() {
		int vainqueur = this.rechercheVainqueur();
		if (vainqueur == 1) {
			this.etatJeu = Etat.J1_VAINQUEUR;
		} else if (vainqueur == 2) {
			this.etatJeu = Etat.J2_VAINQUEUR;
		} else if (this.nbCoupsJoues == 9) {
			this.etatJeu = Etat.MATCH_NUL;
		} else if (this.etatJeu == Etat.J1_JOUE) {
			this.etatJeu = Etat.J2_JOUE;
		} else {
			this.etatJeu = Etat.J1_JOUE;
		}

	}
}
