package donnees.robots;

import donnees.carte.Case;
import donnees.carte.NatureTerrain;

public class Drone extends Robot {

    public Drone(Case position, double vitesse) {

        super(
            position, //position (Case)
            new double[]{vitesse,vitesse,vitesse,vitesse,vitesse}, //vitesseTerrain (EAU, FORET, ROCHE, TERRAIN_LIBRE, HABITAT)
            false, //utilisePoudre
            true, //remplitSurEau
            10000, //quant_reservoire (L)
            10000, //quant_eau (L)
            30*60, //duree_remplissage (secondes)
            10000, //quant_eau_intervention (L)
            30 //duree_intervention (secondes)
        );
    }

    @Override
    public String toString() {
        return "DRONE (pos:"+super.getPosition().toString()+
                        ", vitesse:"+super.getVitesse(NatureTerrain.TERRAIN_LIBRE)+
                        ", eau:"+super.getQuantEau()+"/"+super.getQuantReservoire()+
                ")";
    }

    
}
