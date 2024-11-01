package donnees.robots;

import donnees.carte.*;

public class RobotAPattes extends Robot {

    public RobotAPattes(Case position) {
        super(
            position, //position (Case)
            new double[] {0,30,10,30,30}, //vitesseTerrain (EAU, FORET, ROCHE, TERRAIN_LIBRE, HABITAT)
            true, //utilisePoudre
            10, //quant_eau_intervention (L)
            1 //duree_intervention (secondes)
        );
    }

    @Override
    public String toString() {
        return "PATTES (pos:"+super.getPosition().toString()+
                        ", vitesse:"+super.getVitesse(NatureTerrain.TERRAIN_LIBRE)+
                ")";
    }
}
