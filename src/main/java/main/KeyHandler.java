package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;

    boolean checkDrawnTime = false;



    public KeyHandler(GamePanel gp){

        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Returns the integer keyCode associated with the key in this event
        int code = e.getKeyCode();

        //Title state
        if(gp.gameState == gp.tittleState){
            if(code == KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 1;
                }
            }

            if(code == KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 1){
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0){
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if(gp.ui.commandNum == 1){
                    System.exit(0);
                }
            }
        }
        else if(gp.gameState == gp.gameOver || gp.gameState == gp.winnerState){
            if(code == KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 1;
                }
            }
            if(code == KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 1){
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0){
                    gp.gameState = gp.playState;
                    gp.restart();
                }
                if(gp.ui.commandNum == 1){
                    System.exit(0);
                }
            }
        }

        //Play state
        if(gp.gameState == gp.playState){

            playState(code);
        }

        //Pause state
        else if(gp.gameState == gp.pauseState){

            pauseState(code);
        }

        //Dialogue state
        else if(gp.gameState == gp.dialougeState){

            dialogueState(code);
        }

        //Character state
        else if(gp.gameState == gp.characterState){

                characterState(code);
            }

        }


    public void playState(int code){

        if(code == KeyEvent.VK_W){
            upPressed = true;
        }

        if(code == KeyEvent.VK_S){
            downPressed = true;
        }

        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }

        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }

        if(code == KeyEvent.VK_P){

            gp.gameState = gp.pauseState;
        }

        if (code == KeyEvent.VK_C){

            gp.gameState = gp.characterState;
        }

        if(code == KeyEvent.VK_ENTER){

            enterPressed = true;
        }
        if(code == KeyEvent.VK_T){

            checkDrawnTime = true;
        }
    }

    public void pauseState(int code){

        if(code == KeyEvent.VK_P){

            gp.gameState = gp.playState;
        }
    }

    public void dialogueState(int code){

        if(code == KeyEvent.VK_ENTER){

            gp.gameState = gp.playState;
        }
    }

    public void characterState(int code){

        if (code == KeyEvent.VK_C){

            gp.gameState = gp.playState;
        }

        if (code == KeyEvent.VK_W){
            if(gp.ui.slotRow != 0){

                gp.ui.slotRow--;
            }

        }

        if (code == KeyEvent.VK_A){
            if(gp.ui.slotCol != 0){

                gp.ui.slotCol--;
            }
        }

        if (code == KeyEvent.VK_S){
            if(gp.ui.slotRow != 3){

                gp.ui.slotRow++;
            }
        }

        if (code == KeyEvent.VK_D){
            if(gp.ui.slotCol != 4){

                gp.ui.slotCol++;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }

        if(code == KeyEvent.VK_S){
            downPressed = false;
        }

        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }

        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
    }
}
