package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {
    GamePanel gp;
    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "Wood Shield";
        down1 = setup("/objects/shield_wood",gp.tileSize, gp.tileSize);
        defenseValue = 1;
        description = "[" + name +  "]\nMade by wood.\nWeight: 0.5kg";
    }
}
