package tests;

import io.LecteurDonnees;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import donnees.DonneesSimulation;

public class TestLecteurDonnees {
    
    /**
     * Test de la lecture des données
     *
     * @param args[0]  doit contenir le chemin vers le fichier de données.
     * @throws FileNotFoundException Si le fichier spécifié est introuvable ou illisible.
     * @throws DataFormatException Si le format du fichier de données est invalide.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            DonneesSimulation data = LecteurDonnees.creeDonnees(args[0]);
            System.err.println(data);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }

}

