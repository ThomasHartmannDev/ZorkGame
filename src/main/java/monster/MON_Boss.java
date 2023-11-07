package monster;

import entity.Entity;
import main.GamePanel;
import java.util.Random;

public class MON_Boss extends Entity {
    GamePanel gp;
    public MON_Boss(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = 3;
        name = "Boss";
        speed = 2;
        maxLife = 50;
        life = maxLife;
        attack = 2;

        int size = gp.tileSize * 5;
        solidArea.x = 48;
        solidArea.y = 48;
        solidArea.width = size - 48*2;
        solidArea.height = size - 40;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }
    public void getImage(){
        int i = 5;
        up1 = setup("/monster/skeletonlord_up_1", gp.tileSize*i, gp.tileSize*i);
        up2 = setup("/monster/skeletonlord_up_2", gp.tileSize*i, gp.tileSize*i);
        down1 = setup("/monster/skeletonlord_down_1", gp.tileSize*i, gp.tileSize*i);
        down2 = setup("/monster/skeletonlord_down_2", gp.tileSize*i, gp.tileSize*i);
        left1  = setup("/monster/skeletonlord_left_1", gp.tileSize*i, gp.tileSize*i);
        left2 = setup("/monster/skeletonlord_left_2", gp.tileSize*i, gp.tileSize*i);
        right1 = setup("/monster/skeletonlord_right_1", gp.tileSize*i, gp.tileSize*i);
        right2 = setup("/monster/skeletonlord_right_2", gp.tileSize*i, gp.tileSize*i);
    }

    public void setAction(){
        actionLockCounter ++;

        if(actionLockCounter == 120){

            Random random = new Random();
            int i = random.nextInt(100) + 1; //pick up a number from 1 to 100

            if(i <= 25){

                direction  = "up";
            }

            if(i > 25 && i <= 50){

                direction = "down";
            }

            if(i > 50 && i <= 75){

                direction = "left";
            }

            if(i > 75 && i <= 100){

                direction = "right";
            }

            actionLockCounter = 0;
        }
    }
    public void dmgReaction(){

        actionLockCounter = 0;
        direction = gp.player.direction;

    }
}
