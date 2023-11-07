package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {
    GamePanel gp;
    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "Normal Sword";
        down1 = setup("/objects/sword_normal", gp.tileSize, gp.tileSize);
        attackValue = 1;
        description = "[" + name +  "]\nAn old sword.\nWeight: 0.5kg";
    }
}
