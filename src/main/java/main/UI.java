package main;

import entity.Entity;
import object.OBJ_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UI { //This class handles all the onscreen UI, so we can display text message, item icons, and so on.

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage heart_full, heart_half, heart_blank;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public int commandNum = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int slotCol = 0;
    public int slotRow = 0;


    public UI(GamePanel gp){

        this.gp = gp;
        arial_40  = new Font("Arial", Font.PLAIN, 40); //set font for text
        arial_80B = new Font("Arial", Font.BOLD, 80); //set font for text

        //Create hud object
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void addMessage(String text){

        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2){ //Display the number of keys the player currently has

        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        //Play state
        if(gp.gameState == gp.playState){
            drawPlayerLife();
            drawMessage();
        }

        if(gp.gameState == gp.winnerState){
            drawWinnerScreen();
        }
        if(gp.gameState == gp.tittleState){
            drawTittleScreen();
        }

        if(gp.gameState == gp.gameOver){
            drawGameOver();
        }

        //Pause state
        if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }
        //Pause state
        if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }
        if(gp.gameState == gp.gameOver){
            drawPlayerLife();
            drawGameOver();
        }
        //Dialogue state
        if(gp.gameState == gp.dialougeState){

            drawPlayerLife();
            drawDialogueScreen();
        }
        //Character state
        if (gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory();
        }
    }
    public void drawPlayerLife(){
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        while (i < gp.player.maxLife/2){
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x+= gp.tileSize;
        }
        //Reset
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;
        //Draw current life
        while(i < gp.player.life){
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }


    }

    public void drawMessage(){

        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));

        for(int i = 0; i < message.size(); i++){

            if(message.get(i) != null){

                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50;

                if(messageCounter.get(i) > 180){

                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    public void drawPauseScreen(){

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }



    public void drawDialogueScreen(){

        //Dialogue window
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width  = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F)); //Dialogue font
        x += gp.tileSize; //Draw dialogue on window
        y += gp.tileSize; //Draw dialogue on window

        for (String line : currentDialogue.split("\n")){ //line breaks when reading \n in text

            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawCharacterScreen(){

        //Make a frame
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;

        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        //Text
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;

        //Names
        g2.drawString("Level", textX,  textY);
        textY += lineHeight;
        g2.drawString("Life", textX,  textY);
        textY += lineHeight;
        g2.drawString("Strength", textX,  textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX,  textY);
        textY += lineHeight;
        g2.drawString("Attack", textX,  textY);
        textY += lineHeight;
        g2.drawString("Defense", textX,  textY);
        textY += lineHeight;
        g2.drawString("Exp", textX,  textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX,  textY);
        textY += lineHeight;
        g2.drawString("Coin", textX,  textY);
        textY += lineHeight + 20;
        g2.drawString("Weapon", textX,  textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX,  textY);
        textY += lineHeight;

        //Values
        int tailX = (frameX + frameWidth) -30;
        //Reset textY
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life +  "/" + gp.player.maxLife);
        textX = getXforAlignToRightText(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 14,null);
        textY +=   gp.tileSize;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 14,null);
        textY +=   gp.tileSize;
    }

    public void drawInventory(){

        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight =  gp.tileSize*5;

        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        //Slot
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize =gp.tileSize+3;

        //Draw player's items (inventory)
        for(int i = 0; i < gp.player.inventory.size(); i++){

            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);

            slotX += slotSize;

            if(i == 4 || i == 9 || i == 14){

                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        //Cursor
        int cursorX = slotXstart + (slotSize *  slotCol);
        int cursorY = slotYstart + (slotSize *  slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        //Draw cursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight,10,10);

        //Description frame
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize*3;

        int textX = dFrameX +  20;
        int textY = dFrameY +  gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(38F));

        int itemIndex = getItemIndexOnSlot();
        if(itemIndex < gp.player.inventory.size()){

            drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);

            for(String line: gp.player.inventory.get(itemIndex).description.split("\n")){

                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    public int getItemIndexOnSlot(){

        int itemIndex = slotCol + (slotRow*5);
        return itemIndex;
    }

    public void drawSubWindow(int x, int y, int width, int height){

        Color c = new Color(0,0,0,210); //RGB number for color black (4th number indicates transparency level)
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255); //RGB number for color white
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5)); //Defines the width of outlines of graphics (aka the border)
        g2.drawRoundRect(x + 5,y + 5,width -10,height -10,25,25);
    }

    public void drawGameOver() {
        gp.player.direction = "down";
        g2.setColor(new Color(0,0,0, 150));
        g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;

        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "Game Over";

        //Shadow
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = gp.tileSize*4;
        g2.drawString(text, x, y);

        //Main
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);

        //Retry Option
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y+= gp.tileSize * 4;
        g2.drawString(text, x,y);
        if(commandNum == 0){
            g2.drawString(">", x-40, y);
        }
        //Back to titleScreen
        text = "Quit";
        x = getXforCenteredText(text);
        y+= 55;
        g2.drawString(text, x,y);
        if(commandNum == 1){
            g2.drawString(">", x-40, y);
        }
    }

    public void drawWinnerScreen(){
        gp.player.direction = "down";
        g2.setColor(new Color(0,0,0, 150));
        g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;

        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "YOU WIN!!!";

        //Shadow
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = gp.tileSize*4;
        g2.drawString(text, x, y);

        //Main
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);

        //Retry Option
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Restart";
        x = getXforCenteredText(text);
        y+= gp.tileSize * 4;
        g2.drawString(text, x,y);
        if(commandNum == 0){
            g2.drawString(">", x-40, y);
        }
        //Back to titleScreen
        text = "Quit";
        x = getXforCenteredText(text);
        y+= 55;
        g2.drawString(text, x,y);
        if(commandNum == 1){
            g2.drawString(">", x-40, y);
        }
    }

    public void drawTittleScreen() {
        //title name
        g2.setColor(Color.black);
        g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96f));
        String text = "ZORK GAME";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        g2.setColor(Color.gray);
        g2.drawString(text, x+4,y+4);

        g2.setColor(Color.white);
        g2.drawString(text, x,y);
        //Menu
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));
        text = "START GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 3.5;
        g2.drawString(text,x,y);
        if(commandNum == 0){
            g2.drawString(">", x-gp.tileSize,y);
        }
        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize*2;
        g2.drawString(text,x,y);
        if(commandNum == 1){
            g2.drawString(">", x-gp.tileSize,y);
        }
    }

    public int getXforCenteredText(String text){

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;

        return x;
    }

    public int getXforAlignToRightText(String text, int tailX){

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;

        return x;
    }
}
