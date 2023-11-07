package main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    EventRect[][][] eventRect;

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        for (int map = 0; map < gp.maxMap; map++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                for (int row = 0; row < gp.maxWorldRow; row++) {
                    eventRect[map][col][row] = new EventRect();
                    eventRect[map][col][row].x = 23;
                    eventRect[map][col][row].y = 23;
                    eventRect[map][col][row].width = 2;
                    eventRect[map][col][row].height = 2;
                    eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
                    eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;
                }
            }
        }
    }

    public void checkEvent() {
        //TELEPORT TO THE MAZE
        if (hit(0, 33, 12, "any")) {

            teleport(1,3, 4);
        }
        //Teleport back to the house
        else if (hit(1,1, 2, "any")) {

            teleport(0, 32, 12);
        }
        //END MAZE - Teleport back to the house
        else if (hit(1,41, 44, "any")) {

            teleport(0, 32, 12);
        }
        //BOSS ARENA - Teleport inside
        else if (hit(0,40, 31, "any")) {
            System.out.println("bossarena");

            teleport(3, 14, 11);
        }
        //BOSS ARENA - Teleport outside
        else if (hit(3,12, 11, "any")) {
            System.out.println("bossarena");

            teleport(0, 39, 31);
        }

        //HOUSE - Teleport inside
        if (hit(0, 14, 18, "any") || hit(0, 15, 18, "any") || hit(0, 16, 18, "any") ||hit(0, 17, 18, "any")) {
            teleport(2,27, 29);
        }
        if (    hit(2, 27, 30, "any") ||
                hit(2, 28, 30, "any") ||
                hit(2, 29, 30, "any") ||
                hit(2, 30, 30, "any") ||
                hit(2, 31, 30, "any")){

            teleport(0,15, 20);
        }
    }

    public boolean hit(int map, int col, int row, String reqDirection) {
        boolean hit = false;

        if (map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

            if (gp.player.solidArea.intersects(eventRect[map][col][row])) {
                if (gp.player.direction.equals(reqDirection) || reqDirection.equals("any")) {
                    hit = true;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }

        return hit;
    }

    public void damagePit(int gameState) {
        gp.gameState = gameState;
        System.out.println("event checked");
        gp.player.life -= 1;
    }
    public void teleport(int map, int col, int row){
        gp.currentMap = map;
        gp.player.worldX = gp.tileSize * col;
        gp.player.worldY = gp.tileSize * row;
    }
}
