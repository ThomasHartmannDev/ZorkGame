package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

public class NPC_OldMan extends Entity{
    GamePanel gp;
    UtilityTool uTool = new UtilityTool();
    public NPC_OldMan(GamePanel gp){

        super(gp);
        this.gp = gp;
        direction = "down";
        speed =  0; //old man walks slow

        getImage();
        setDialogue();
    }

    public void getImage() {

        //up1 = setup("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
        //up2 = setup("/npc/oldman_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        //left1  = setup("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
        //left2 = setup("/npc/oldman_left_2", gp.tileSize, gp.tileSize);
        //right1 = setup("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
        //right2 = setup("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue(){

        dialogues[0] = "Hello, friend.\n1/6"; //Inputting character dialogues
        dialogues[1] = "So, you've come to this island\nto defeat the skeleton monster?\n2/6";
        dialogues[2] = "His castle is locked, but I know where\nthe key is!!\n3/6";
        dialogues[3] = "But first, I recommend you stop by my\nhouse, on the left and pick up any\n things to help you!\n4/6";
        dialogues[4] = "The key is at the end of the maze,\nI can never reach the end...\n5/6";
        dialogues[5] = "I wish you luck my brave warrior.\n6/6";
    }



    public void speak(){

        //Do this character specific stuff
        super.speak();
    }
}
