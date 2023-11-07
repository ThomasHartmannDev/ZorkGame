package entity;


import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity{

        KeyHandler keyH;
        public final int screenX;
        public final int screenY;
        public ArrayList<Entity> inventory = new ArrayList<>();
        public final int maxinventorySize = 20;
        public int hasKey;
        public Player(GamePanel gp, KeyHandler keyH){

            super(gp);//Calling constructor of super class

            this.keyH = keyH;

            screenX = gp.screenWidth/2 - (gp.tileSize / 2);
            screenY = gp.screenHeight/2 - (gp.tileSize / 2);

            solidArea = new Rectangle(8, 16, 32,32); // X,Y,width, height

            solidAreaDefaultX  = solidArea.x;
            solidAreaDefaultY  = solidArea.y;

            attackArea.width = 36;
            attackArea.height = 36;

            setDefaultValues();
            getPlayerImage();
            getPlayerAttackImage();
            setItems();
        }

        public void setDefaultValues(){
            //Setting the starting position for the player (we can change it later)
            worldX = gp.tileSize * 23;//gp.tileSize * 25;
            worldY = gp.tileSize * 24;//gp.tileSize * 23;
            speed = 4;
            direction = "down";

            //Play status
            level = 1;
            maxLife = 10;
            life = maxLife;
            strength = 1;  //The more strength he has, the more damage he can do.
            dexterity = 1; //The more dexterity he has, the less damage he receives.
            exp = 0;
            nextLevelExp = 5;
            coin = 0;
            currentWeapon = new OBJ_Sword_Normal(gp);
            currentShield = new OBJ_Shield_Wood(gp);
            attack = getAttack(); //The total attack value is decided by strength and weapon
            defense = getDefense(); //The total defense value is decided by dexterity and shield
        }

        public void setItems(){

            inventory.add(currentWeapon);
            inventory.add(currentShield);
        }

        public int getAttack(){

            return attack = strength * currentWeapon.attackValue;
        }

        public int getDefense(){

            return defense = dexterity * currentShield.defenseValue;
        }

        public void getPlayerImage() {
        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1  = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);
    }
        public void getPlayerAttackImage() {
        attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
        attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
        attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
        attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
        attackLeft1  = setup("/player/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
        attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize*2, gp.tileSize);
        attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize*2, gp.tileSize);
        attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize*2, gp.tileSize);
    }
        public void update(){
            /*
            * The player will just move when those keys are pressed.
            * otherwise he is going to stop!
            * */
            if(attacking){
                attacking();
            }
            else if(keyH.upPressed|| keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed){
                if(keyH.upPressed){
                    direction = "up";
                }
                else if(keyH.downPressed){
                    direction  = "down";
                }
                else if(keyH.leftPressed){
                    direction = "left";
                }
                else if(keyH.rightPressed){
                    direction = "right";
                }
                //Check tile collision
                collisionOn = false;
                gp.cChecker.checkTile(this);

                //Check object collision
                int objIndex = gp.cChecker.checkObject(this,true);
                pickUpObject(objIndex);

                //CHECK EVENTS
                gp.eHandler.checkEvent();

                //Check NPC collision
                int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
                interactNPC(npcIndex);

                //Check Monster collision
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                contactMonster(monsterIndex);

                //If collision is false, player can move
                if(!collisionOn && !keyH.enterPressed){
                    switch (direction){
                        case "up":
                            worldY -= speed;
                            break;
                        case "down":
                            worldY += speed;
                            break;
                        case "left":
                            worldX -= speed;
                            break;
                        case "right":
                            worldX += speed;
                            break;
                    }
                }
                gp.keyH.enterPressed = false;
                /**
                 * Here is made the change loop for update the sprite
                 * and make the feeling we are moving
                 * **/
                spriteCounter++;
                if(spriteCounter > 12){
                    if(spriteNum == 1){
                        spriteNum = 2;
                    } else if (spriteNum == 2){
                        spriteNum = 1;
                    }
                    spriteCounter = 0;
                }
            }

            //Invincible
            if(invincible == true){
                invincibleCounter++;
                if(invincibleCounter > 60){ // 60frames = 1 second
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
            if(life <= 0){
                gp.gameState = gp.gameOver;
            }


        }

        private void attacking() {
            spriteCounter++;
            if(spriteCounter <= 5){
                spriteNum = 1;
            }
            if(spriteCounter > 5 && spriteCounter<= 25){
                spriteNum = 2;
                // Save the current worldX, worldY, solidArea;
                int currentWorldX = worldX;
                int currentWorldY = worldY;
                int solidAreaWidth = solidArea.width;
                int solidAreaHeight = solidArea.height;

                //Adjust players worldX/Y for the attack area
                switch(direction){
                    case "up": worldY -= attackArea.height; break;
                    case "down": worldY += attackArea.height; break;
                    case "left": worldX -= attackArea.width; break;
                    case "right": worldX += attackArea.width; break;
                }

                //atackarea
                solidArea.width = attackArea.width;
                solidArea.height = attackArea.height;

                //checking collision from the sword
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                dmgMonster(monsterIndex);
                worldX = currentWorldX;
                worldY = currentWorldY;
                solidArea.width = solidAreaWidth;
                solidArea.height = solidAreaHeight;

            }
            if(spriteCounter > 25){
                spriteNum= 1;
                spriteCounter = 0;
                attacking = false;
            }
    }

    public void dmgMonster(int i) {
        if(i != 999){
            if(gp.monster[gp.currentMap][i].invincible == false){
                gp.playSE(5);
                gp.monster[gp.currentMap][i].life -= 1;
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].dmgReaction();

                if(gp.monster[gp.currentMap][i].life <= 0){
                    gp.monster[gp.currentMap][i].dying = true;
                    if(gp.monster[gp.currentMap][i].dying == true && gp.monster[gp.currentMap][i].type == 3){
                        System.out.println("WINNER!!!!!!!!!!!!!");
                        gp.gameState = gp.winnerState;
                    }
                }
            }
        }
    }

    public void checkLevelUp(){

            if(exp >= nextLevelExp){

                level++;
                nextLevelExp = nextLevelExp*2;
                maxLife += 2;
                strength++;
                dexterity++;
                attack = getAttack();
                defense = getDefense();

                gp.gameState = gp.dialougeState;
                gp.ui.currentDialogue = "You are level " + level + "  now!\n"
                                         + "You feel stronger!";
            }
    }

    public void contactMonster(int i) {
        if(i != 999){
            if(invincible == false){
                gp.playSE(6);

                int damage = gp.monster[gp.currentMap][i].attack - gp.player.defense;
                if(damage < 0){

                    damage = 0;
                }

                gp.player.life -= damage;
                invincible = true;
            }

        }
    }
        public void pickUpObject(int i){

            if (i != 999){ //We chose 999, but any number is fine as long as it's not used by the object array's index

                String text = null;
                String objectName  = gp.obj[gp.currentMap][i].name;  //Using name from each object

                if(inventory.size() != maxinventorySize){
                    switch (objectName){

                        case "Key":
                            hasKey++;
                            //Key disappears when touched by the player
                            System.out.println("Key: " + hasKey);
                            inventory.add(gp.obj[gp.currentMap][i]);
                            text = "Got a " + gp.obj[gp.currentMap][i].name + "!";
                            gp.obj[gp.currentMap][i] = null;
                            break;

                        case "Door":
                            if (hasKey > 0) {
                                gp.obj[gp.currentMap][i] = null;
                                text = "YOU OPENED THE DOR";
                                hasKey--;
                            } else {
                                text = "You need a key!";
                            }
                            System.out.println("Key: " + hasKey);
                            break;
                        default:
                            inventory.add(gp.obj[gp.currentMap][i]);
                            text = "Got a " + gp.obj[gp.currentMap][i].name + "!";
                            gp.obj[gp.currentMap][i] = null;
                            break;

                    }

//                  
                }

              else{

                    text = "You can't carry any more!";               
              }
                gp.ui.addMessage(text);

            }
        }

        public void interactNPC(int i) {
            if (gp.keyH.enterPressed == true) {
                if (i != 999) {
                    //Dialoge is displayed when pressed Enter and collision exists
                    gp.gameState = gp.dialougeState;
                    gp.npc[gp.currentMap][i].speak();
                }else{
                attacking = true;
                }
            }
            gp.keyH.enterPressed = false;
        }

        public void draw(Graphics2D g2){
            //g2.setColor(Color.white); we no longer need this rectangle
            //g2.fillRect(x, y, gp.tileSize, gp.tileSize); //from GamePanel (=public final int tileSize)

            BufferedImage image = null;
            int tempScreenX = screenX;
            int tempScreenY = screenY;

            switch (direction){
                case "up":
                    if(!attacking){
                        if(spriteNum == 1){image = up1;}
                        if(spriteNum == 2){image = up2;}
                    } else {
                        tempScreenY = screenY - gp.tileSize;
                        if(spriteNum == 1){image = attackUp1;}
                        if(spriteNum == 2){image = attackUp2;}
                    }

                    break;

                case "down":
                    if(!attacking){
                        if(spriteNum == 1){image = down1;}
                        if(spriteNum == 2){image = down2;}
                    } else {
                        if(spriteNum == 1){image = attackDown1;}
                        if(spriteNum == 2){image = attackDown2;}
                    }

                    break;

                case "left":
                    if(!attacking){
                        if(spriteNum == 1){image = left1;}
                        if(spriteNum == 2){image = left2;}
                    } else {
                        tempScreenX = screenX - gp.tileSize;
                        if(spriteNum == 1){image = attackLeft1;}
                        if(spriteNum == 2){image = attackLeft2;}
                    }

                    break;

                case "right":
                    if(!attacking){
                        if(spriteNum == 1){image = right1;}
                        if(spriteNum == 2) {image = right2;}
                    } else {
                        if(spriteNum == 1){image = attackRight1;}
                        if(spriteNum == 2) {image = attackRight2;}
                    }

                    break;
            }
            if(invincible){
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            g2.drawImage(image, tempScreenX, tempScreenY, null); //null is an image observer

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            //DEBUG
//            g2.setFont(new Font("Arial", Font.PLAIN, 26));
//            g2.setColor(Color.white);
//            g2.drawString("invincibleCounter: " + invicibleCounter, 10, 400);

        }

    public void setDefaulPositions(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 24;
        speed = 4;
        direction = "down";
    }
    public void restoreLife(){
        life = maxLife;
        invincible = false;
    }

}
