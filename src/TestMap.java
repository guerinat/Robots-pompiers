import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DataFormatException;

import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;

import donnees.*;
import donnees.carte.*;
import io.*;
import donnees.robots.*;

public class TestMap{
    public static void main(String[] args) {
        // crée la fenêtre graphique dans laquelle dessiner
        // crée la carte, en l'associant à la fenêtre graphique précédente

        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            DonneesSimulation data = LecteurDonnees.creeDonnees(args[0]);

            GUISimulator gui = new GUISimulator(data.carte.getTailleCases()/6*(data.carte.getNbColonnes()+2), data.carte.getTailleCases()/6*(data.carte.getNbLignes()+2), Color.WHITE);
            Map map = new Map(gui, Couleur.Blanc.toAwtColor(), data);

            System.err.println(data);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}


class Map implements Simulable {

    /** L'interface graphique associée */
    private GUISimulator gui;
    
    private Color mapColor;	

    private DonneesSimulation data;

    private int x;
    private int y;

    public Map(GUISimulator gui, Color mapColor, DonneesSimulation data) {
        this.gui = gui;
        gui.setSimulable(this);				// association a la gui!
        this.mapColor = mapColor;
        this.data = data;

        planCoordinates();
        draw();
    }

    private void planCoordinates() {
        int xMin = 60;
        int yMin = 40;
        int xMax = gui.getWidth() - xMin - 80;
        xMax -= xMax % 10;
        int yMax = gui.getHeight() - yMin - 120;
        yMax -= yMax % 10;
    }



    @Override
    public void next() {	
        draw();
    }

    @Override
    public void restart() {
        planCoordinates();
        draw();
    }


    private void draw() {
        Couleur[] tabCouleurs = {Couleur.Bleu, Couleur.Vertf, Couleur.Gris, Couleur.Vertc, Couleur.Jaune};
        gui.reset();	// clear the window
        int incendie=0;
        for (int ligne=0 ; ligne < data.carte.getNbLignes() ; ++ligne ){
            for (int colonne=0 ; colonne < data.carte.getNbColonnes() ; ++colonne){
                if (data.incendies[incendie].getPosition().getLigne() != ligne | data.incendies[incendie].getPosition().getColonne() != colonne){
                    gui.addGraphicalElement(new Rectangle(data.carte.getTailleCases()/6*(1+colonne) ,data.carte.getTailleCases()/6*(1+ligne),Couleur.Blanc.toAwtColor(), tabCouleurs[data.carte.getCase(ligne,colonne).getNature().ordinal()].toAwtColor(), data.carte.getTailleCases()/6));
                }
                else{
                    gui.addGraphicalElement(new Rectangle(data.carte.getTailleCases()/6*(1+colonne) ,data.carte.getTailleCases()/6*(1+ligne),Couleur.Blanc.toAwtColor(), Couleur.Rouge.toAwtColor(), data.carte.getTailleCases()/6));
                    if (incendie!=data.incendie_size()-1){
                        incendie+=1;
                    }
                
                }
            }
        }

    }
}
