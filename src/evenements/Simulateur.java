package evenements;

import java.util.LinkedList;
import java.util.ListIterator;

public class Simulateur {

    LinkedList<Evenement> evenements;
    private long date_courante;
    private long etape;

    public Simulateur(long etape) {
        this.evenements = new LinkedList<Evenement>();
        this.date_courante = 0;
        this.etape = etape;
    }

    public void ajouteEvenement(Evenement e) {

        ListIterator<Evenement> iterateur = evenements.listIterator();

        while (true) {
            if (!iterateur.hasNext()) {
                iterateur.add(e);
                return;
            }

            Evenement current = iterateur.next();
            if(e.getdateFin() < current.getdateFin()) {
                iterateur.previous();
                iterateur.add(e);
                return;
            }
        }
    }


    // Execute tout les évenement de date courante (compris) à date-courante + etape (non-compris)
    public void incrementeDate() {

        ListIterator<Evenement> iterateur = evenements.listIterator();
    
        while (iterateur.hasNext()) {
            Evenement current = iterateur.next(); 

            if (current.getdateFin() >= date_courante + etape)  
                break;

            if (current.getdateFin() >= date_courante) {
                current.execute();
                System.out.println("[t="+current.getdateFin()+"] "+current.toString());
            }
            
        }
        date_courante += etape;
    }

    public boolean simulationTerminee() {
        return date_courante > evenements.getLast().getdateFin();
    }

    @Override
    public String toString() {
        String s = "";
        for(Evenement e : evenements) {
            s+=e.toString()+"\n";
        }
        return s;
    }
}