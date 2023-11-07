package main;

import entity.Entity;
import entity.Player;
import tiles.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//This class inherits JPanel class
public class GamePanel extends JPanel implements Runnable{

    //Screen Settings
    final int originalTileSize = 16;
    final int scale = 3;


    //It needs to be "public" when you want to access from other packages
    public final int tileSize = originalTileSize * scale; // 48X48 tile;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    //Ratio from 4:3
    public final int screenWidth = tileSize * maxScreenCol; // 768 Pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 Pixels

    //World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;
    public final int tittleState = 0;
    public final int gameOver = 4;
    public final int winnerState =  6;


    //FPS
    int FPS = 60; // Updates the screen 60 times per second


    //System
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread; //It keeps your programm running (when we start this gameThread, the run method is automatically called)


    //Entity and object
    public Player player = new Player(this,keyH);
    public Entity obj[][] = new Entity[maxMap][10];// 10 objects can be displayed at the same time
    public Entity npc[][] = new Entity[maxMap][10];
    public Entity monster[][] = new Entity[maxMap][20];
    ArrayList<Entity> entityList = new ArrayList<>();


    //Game state
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialougeState = 3;
    public final int characterState =  5;



    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObject(); //make this method in order to add other setup stuff in the future
        aSetter.setNPC();
        aSetter.setMonster();
        //playMusic(0); //passing the index 0 for the background
        gameState = tittleState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();

    }
    @Override
    public void run(){
        double drawInterval = 1000000000/FPS; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null){
            //returns the current value of the  running  Java Virtual Machine's high-resolution time source, in nanoseconds
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            //timer += (currentTime - lastTime);
            lastTime = currentTime;
            if(delta >= 1){
                //calling the methods update and repaint as long  as the loop is running
                update();
                repaint();
                delta--;
            }

        }
    }

    public void update() {

        if (gameState == playState){

            //Player
            player.update();
            //NPC
            for (int i = 0; i < npc[1].length; i++){
                // When we add [1] we are telling java to too the length from the second array
                if(npc[currentMap][i] != null){
                    npc[currentMap][i].update();
                }
            }
            for(int i=0; i< monster[1].length; i++){
                if(monster[currentMap][i] != null){
                    if(monster[currentMap][i].alive && !monster[currentMap][i].dying){
                        monster[currentMap][i].update();
                    }
                    if(!monster[currentMap][i].alive){
                        System.out.println("monster dying");
                        monster[currentMap][i] = null;
                    }

                }
            }


            }
            //For the next thing we are adding like monster follow the same logic from NPC


        if(gameState  == pauseState){

            //nothing for now
        }
        if(gameState == gameOver){

        }

    }

    //build in method in Java (=paintComponent)
    //Graphics is a class that has many functions to draw objects on the screen
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //We're creating a new drawing object g2 (Graphics2D) and initializing it with the existing drawing object g.
        //This allows us to use more advanced drawing features for our graphics.
        Graphics2D g2 = (Graphics2D)g;

        //DEBUG
        long drawStart = 0;
        if(keyH.checkDrawnTime == true){
            drawStart = System.nanoTime();
        }
        //Adding entities
        //TITTLE SCREEN
        if(gameState == tittleState){
            ui.draw(g2);
        } else{
            //TILE
            tileM.draw(g2);

            entityList.add(player);
            for(int i=0; i<obj[1].length; i++){
                if(obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }
            for(int i=0; i<npc[1].length; i++){
                if(npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }
            for(int i=0; i< monster[1].length; i++){
                if(monster[currentMap][i] != null){
                    entityList.add(monster[currentMap][i]);
                }
            }

            //sort
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity o1, Entity o2) {
                    return Integer.compare(o1.worldY, o2.worldX);
                }
            });
            //DRAW ENTITIES
            for(int i=0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }

            // EMPTY ENTITY LIST
            entityList.clear();


            if(keyH.checkDrawnTime == true){
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;
                Font myFont = new Font ("Courier New", 1, 24);
                g2.setFont(myFont);
                g2.setColor(Color.white);
                //g2.drawString("Drawtime:" + passed, 10, 300);
                g2.drawString("WorldX:" + player.worldX, 10, 350);
                g2.drawString("WorldY:" + player.worldY, 10, 375);
                g2.drawString("Collum:" + ((player.worldX/tileSize)+1) , 10, 400);
                g2.drawString("Row:" +((player.worldY/tileSize)+1) , 10, 425);
                //System.out.println("Drawtime:" + passed);
            }
            //UI
            ui.draw(g2);
            //PLAYER
            player.draw(g2);
            //g2.dispose(); frees up resources used by the graphics drawing object g2, ensuring efficient memory usage and preventing resource leaks in graphical applications.
            g2.dispose();

        }


    }
    public void playMusic(int i){

        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){

        music.stop();
    }

    public void playSE(int i){
        se.setFile(i);
        se.play();
    }

    public void restart(){
        player.setDefaulPositions();
        player.restoreLife();
        aSetter.setNPC();
        aSetter.setObject();
        aSetter.setMonster();
    }
}
