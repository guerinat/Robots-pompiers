package strategie;

import java.util.LinkedList;

import chemins.CaseDuree;
import chemins.PlusCoursChemin;
import donnees.DonneesSimulation;
import donnees.Incendie;
import donnees.robots.Robot;
import evenements.*;
import simulateur.*;
import donnees.carte.*;


/**
 * Classe abstraite du chef Pompier decidant de la stratégie.
**/
public abstract class ChefPompier {

    protected Simulateur simulateur;

    //Association bi-directionelle entre les robots et les incendies affectés
    protected AssociationRobotsIncendie affectes = new AssociationRobotsIncendie(); 


    /**
     * Associe un simulateur à l'instance de ChefPompier.
     * @param simulateur L'objet simulateur à associer pour gérer l'ajout d'événements.
     */
    public void setSimulateur(Simulateur simulateur) {
        this.simulateur = simulateur;
    }


    /**
     * Programme le déplacement d'un robot vers un incendie ainsi que les événements nécessaires
     * pour éteindre cet incendie une fois sur place.
     * @param robot Le robot à envoyer pour éteindre l'incendie.
     * @param incendie L'incendie à éteindre.
     * @param chemin Le chemin que le robot doit suivre pour atteindre l'incendie.
     * @param date_courante La date actuelle dans la simulation.
     * @param carte La carte.
     */
    public void envoyerRobotEteindreIncendie(Robot robot, Incendie incendie, LinkedList<CaseDuree> chemin, long date_courante, Carte carte) {

        //Deplacement
        LinkedList<Evenement> deplacements = PlusCoursChemin.deplacerRobotChemin(date_courante, chemin, robot, carte);
        long dateApresChemin = date_courante + PlusCoursChemin.duree_chemin(chemin);
        simulateur.ajouteEvenements(deplacements);

        //Vidage
        LinkedList<Evenement> vidages = Vidage.viderEntierementRobot(robot, incendie, dateApresChemin);
        long dateApresVidage = dateApresChemin + vidages.size()*Vidage.calcDuree(robot);
        simulateur.ajouteEvenements(vidages);

        //Changements des etats
        simulateur.ajouteEvenement(new ChangerEtat(date_courante, robot, Etat.DEPLACEMENT));
        simulateur.ajouteEvenement(new ChangerEtat(dateApresChemin, robot, Etat.VIDAGE));
        simulateur.ajouteEvenement(new ChangerEtat(dateApresVidage, robot, Etat.DISPONIBLE));
    }

    
    /**
     * Programme le déplacement d'un robot vers un incendie 
     * @param robot Le robot à envoyer pour éteindre l'incendie.
     * @param incendie L'incendie où se rendre.
     * @param chemin Le chemin que le robot doit suivre pour atteindre l'incendie.
     * @param date_courante La date actuelle dans la simulation.
     * @param carte La carte.
     */
    public void envoyerRobotSurIncendie(Robot robot, Incendie incendie, LinkedList<CaseDuree> chemin, long date_courante, Carte carte) {


        LinkedList<Evenement> deplacements = PlusCoursChemin.deplacerRobotChemin(date_courante, chemin, robot, carte);
        long dateApresChemin = date_courante + PlusCoursChemin.duree_chemin(chemin);
        simulateur.ajouteEvenements(deplacements);

        //Changements des etats
        simulateur.ajouteEvenement(new ChangerEtat(date_courante, robot, Etat.DEPLACEMENT));
        simulateur.ajouteEvenement(new ChangerEtat(dateApresChemin, robot, Etat.PRET_POUR_VIDAGE));
    }

    
    /**
     * Vide @param robot sur @param incendie, si l'incendie n'a pas était eteinte entre temps et si le @param
     * robot a encore de l'eau
     */
    public void viderRobot(Robot robot, Incendie incendie, long date_courante, Carte carte) {

        //Vérifier que le feu n’a pas été éteint entre temps ou que le robot est vide
        if (incendie.getEauNecessaire() == 0 || !robot.peutFaireIntervention(robot.getQuantEau())) {
            simulateur.ajouteEvenement(new ChangerEtat(date_courante, robot, Etat.DISPONIBLE));
            affectes.supprimer(robot);
            return;
        }

        Vidage vidage = new Vidage(date_courante, robot, incendie);
        long dateApresVidage = vidage.getdateFin();
        simulateur.ajouteEvenement(vidage);

        simulateur.ajouteEvenement(new ChangerEtat(date_courante, robot, Etat.VIDAGE));
        simulateur.ajouteEvenement(new ChangerEtat(dateApresVidage, robot, Etat.PRET_POUR_VIDAGE));
    }


    /**
     * Envoie un robot se remplir à la source d'eau la plus proche, si une telle source
     * existe, en programmant les événements nécessaires de déplacement et de remplissage.
     *
     * @param robot         Le robot qui doit être envoyé pour se remplir d'eau.
     * @param date_courante La date actuelle en temps de simulation, utilisée pour calculer
     * @param carte         La carte.
     */
    public void envoyerRobotSeRemplir(Robot robot, long date_courante, Carte carte) {

        //Chercher le chemin vers l'eau la plus proche
        LinkedList<CaseDuree> chemin = PlusCoursChemin.cheminPlusProcheEau(carte, robot);
        if (chemin == null) return;


        //Si le robot ne se remplit la dernière case du chemin
        if (!robot.getRemplitSurEau())
         chemin.removeLast();

        LinkedList<Evenement> deplacements = PlusCoursChemin.deplacerRobotChemin(date_courante, chemin, robot, carte);
        long dateApresChemin = date_courante + PlusCoursChemin.duree_chemin(chemin);
        simulateur.ajouteEvenements(deplacements);

        //Remplissage
        Evenement remplissage = new Remplissage(dateApresChemin, carte, robot, robot.getQuantEau());
        long dateApresRemplissage = remplissage.getdateFin();
        simulateur.ajouteEvenement(remplissage);

        //Changements des etats
        simulateur.ajouteEvenement(new ChangerEtat(date_courante, robot, Etat.DEPLACEMENT));
        simulateur.ajouteEvenement(new ChangerEtat(dateApresChemin, robot, Etat.REMPLISSAGE));
        simulateur.ajouteEvenement(new ChangerEtat(dateApresRemplissage, robot, Etat.DISPONIBLE));
    }


    //Stratégie des sous-classes
    abstract public void jouerStrategie(DonneesSimulation data, long date_courante);
}
