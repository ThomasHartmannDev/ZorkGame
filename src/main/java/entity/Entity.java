package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

//This stores variables that will be used in player, monster and NPC classes
public class Entity {

    GamePanel gp;


    public int worldX, worldY;


    //BufferedImage in Java is a class used to work with images, offering the ability to store, modify, and process images in various formats.
    //It's commonly employed for tasks like image editing, graphics rendering, and GUI development in Java applications.
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage image, image2, image3;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;


    //STATE
    public int spriteNum = 1;
    public String direction = "down";
    public boolean collision = false;
    public boolean invincible = false;
    public boolean collisionOn = false;
    boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;


    //COUNTER
    public int actionLockCounter = 0;
    int dialogueIndex = 0;

    public int invincibleCounter = 0;

    //Character status

    // Obj



    //Character attributes

    public int dyingCounter = 0;
    public int hpBarCounter = 0;


    //CHARACTER ATTRIBUTTES
    public int speed;
    public int maxLife, life;
    public String name;



    String dialogues[] = new String[20];

    //Character status

    // Obj
    public int type; // 0 = play,c 1 = np, 2 monster
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;


    //Item attributes
     public int attackValue;
     public int defenseValue;
     public String description = "";

    //Counter
    public int spriteCounter = 0;


    public Entity(GamePanel gp){

        this.gp = gp;
    }

    public void setAction(){}
    public void dmgReaction(){}

    public void speak(){

        if(dialogues[dialogueIndex] == null){

            dialogueIndex = 0;
        }

        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction){ //fixing the direction of the npc when talking to player
            case "up":
                direction = "down";
                break;

            case "down":
                direction = "up";
                break;

            case "left":
                direction = "right";
                break;

            case "right":
                direction = "left";
                break;
        }
    }

    public void update(){

        setAction(); //it will be called by every npc

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this,false);
        gp.cChecker.checkEntity(this,gp.npc);
        gp.cChecker.checkEntity(this,gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if(this.type == 2 && contactPlayer){
            if(!gp.player.invincible){
                gp.playSE(6);

                int damage = attack - defense;
                if(damage < 0){

                    damage = 0;
                }

                life -= damage;

                gp.player.invincible = true;
            }
        }
        if(this.type == 3 && contactPlayer){
            if(!gp.player.invincible){
                gp.playSE(6);
                gp.player.life -=1;
                gp.player.invincible = true;
            }
        }

        //If collision is false, player can move
        if(!collisionOn){
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

        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 40){ // 60frames = 1 second
                invincible = false;
                invincibleCounter = 0;
            }
        }

    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(     worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

            switch (direction){
                case "up":
                    if(spriteNum == 1){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }

                    break;

                case "down":
                    if(spriteNum == 1){
                        image = down1;
                    }
                    if(spriteNum == 2){
                        image = down2;
                    }
                    break;

                case "left":
                    if(spriteNum == 1){
                        image = left1;
                    }
                    if(spriteNum == 2){
                        image = left2;
                    }

                    break;

                case "right":
                    if(spriteNum == 1){
                        image = right1;
                    }
                    if(spriteNum == 2) {
                        image = right2;
                    }
                    break;
            }

            //Moster HP bar
            if(type == 2 && hpBarOn == true){
                double oneScale = (double) gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1, screenY - 11, gp.tileSize+2, 12);
                g2.setColor(Color.red);
                g2.fillRect(screenX, screenY - 10, (int)hpBarValue, 10);
                hpBarCounter++;
                if(hpBarCounter > 600){
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }
            if(type == 3 && hpBarOn == true){
                double oneScale = (double) gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1, screenY - 15, gp.tileSize*5+2, 12);
                g2.setColor(Color.red);
                g2.fillRect(screenX, screenY - 16, (int)hpBarValue*5, 10);
                hpBarCounter++;

                if(hpBarCounter > 600){
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if(invincible){
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.3f);
            }
            if(dying == true){
                dyingAnimation(g2);
            }
            g2.drawImage(image, screenX, screenY, image.getWidth(), image.getHeight(), null);
            changeAlpha(g2, 1f);
        }

    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        System.out.println("DYING");
        int i = 5; // Time interval
        if(dyingCounter <= i){changeAlpha(g2, 0f);}
        if(dyingCounter > i && dyingCounter <= i*2){changeAlpha(g2, 1f);System.out.println(dyingCounter);}
        if(dyingCounter > i*2 && dyingCounter <= i*3){changeAlpha(g2, 0f);System.out.println(dyingCounter);}
        if(dyingCounter > i*3 && dyingCounter <= i*4){changeAlpha(g2, 1f);System.out.println(dyingCounter);}
        if(dyingCounter > i*4 && dyingCounter <= i*5){changeAlpha(g2, 0f);System.out.println(dyingCounter);}
        if(dyingCounter > i*5 && dyingCounter <= i*6){changeAlpha(g2, 1f);System.out.println(dyingCounter);}
        if(dyingCounter > i*6 && dyingCounter <= i*7){changeAlpha(g2, 0f);System.out.println(dyingCounter);}
        if(dyingCounter > i*7 && dyingCounter <= i*8){changeAlpha(g2, 1f);System.out.println(dyingCounter);}
        if(dyingCounter < i*8){
            System.out.println(dyingCounter);
            dying = false;
            alive = false;
        }
    }
    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setup(String imagePath, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream( imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);

        } catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }
}
