package donnees.carte;



public class Carte {
    private int tailleCases;
    private int nbLignes;
    private int nbColonnes;
    private Case[][] carte;

    public Carte(int tailleCases, int nbLignes, int nbColonnes) {

        this.tailleCases = tailleCases;
        this.nbLignes = nbLignes ;
        this.nbColonnes = nbColonnes ;
        this.carte = new Case[nbLignes][nbColonnes];
    }

    public int getNbLignes () {
        return this.nbLignes;
    }

    public int getNbColonnes () {
        return this.nbColonnes;
    }

    public int getTailleCases() {
        return this.tailleCases;
    }

    public Case getCase(int ligne, int colonne) {
        return this.carte[ligne][colonne];
    }

    public Case setCase(Case src, int ligne, int colonne) {
        return this.carte[ligne][colonne] = src;
    }

    public boolean voisinExiste(Case src, Direction dir) {

        switch(dir) {
            case NORD:
                return (src.getLigne() != 0);
            case SUD:
                return (src.getLigne() != this.nbLignes - 1);
            case OUEST:
                return (src.getColonne() != 0);
            case EST:
                return (src.getColonne() != this.nbColonnes - 1);
            default:
                throw new Error("[!] Direction inconnue.");
        }
    }
    
    public Case getVoisin(Case src, Direction dir) {

        if (!voisinExiste(src, dir))
            throw new Error("[!] Le voisin n'existe pas dans cette direction");
        
        switch(dir) {
            case NORD:
                return this.carte[src.getLigne()-1][src.getColonne()];
            case SUD:
                return this.carte[src.getLigne()+1][src.getColonne()];
            case OUEST:
                return this.carte[src.getLigne()][src.getColonne()-1];
            case EST:
                return this.carte[src.getLigne()][src.getColonne()+1];
            default:
                throw new Error("[!] Direction inconnue.");
        }
    }

    @Override
    public String toString () {
        String s = "# CARTE ("+nbLignes+", "+nbColonnes+", taille:"+tailleCases+")\n";
        for(int i = 0; i < this.nbLignes; i++) {
            for(int j = 0; j < this.nbColonnes; j++) {
                s += this.carte[i][j].toString() + "  ";
            }
            s+="\n";
        }
        return s;
    } 
} 