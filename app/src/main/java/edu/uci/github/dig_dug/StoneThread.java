package edu.uci.github.dig_dug;

import java.lang.Thread;

/**
 * Created by fxuyi on 5/20/2017.
 */

//Stones self detect and drop Thread
public class StoneThread extends Thread {
    GameView gameView;
    land Stone[];

    public StoneThread(GameView cv){
        gameView = cv;
    }

    public int height(land oneStone){
        int counter=0;
        for (int i=oneStone.y+1;i<gameView.AmountofRow;i++){
            if (gameView.blocks[oneStone.x][i].channel){
                counter++;
            }
            else{
                break;
            }
        }
        return counter;
    }

    public void run() {
        boolean stone=false;
        int index=0;
        int h=0;
        //Create Shortcuts
        Stone = new land[3];
        Stone[0] = gameView.blocks[3][2];
        Stone[1] = gameView.blocks[2][10];
        Stone[2] = gameView.blocks[7][8];

        while (!gameView.getGameOver()) {
            for (int i=0;i<3;i++){
                h=height(Stone[i]);
                if ((Stone[i].stone)&&(h>0)){
                    stone=true;
                    index=i;
                    try{
                        sleep(1000);
                    }
                    catch(InterruptedException e) {
                        System.out.println("Exception occured");
                    }
                    //Stone drop
                    gameView.effect[Stone[i].x][Stone[i].y+h].setStone();
                    gameView.effect[Stone[i].x][Stone[i].y].reSet();
                    Stone[i].setStone(false);
                    //Kill all monsters and hero under stone
                    for (int k = 1; k <= h; k++) {
                        if ((gameView.worker.x==Stone[i].x)&&(gameView.worker.y==Stone[i].y+k)){
                            gameView.worker.kill();
                        }
                        for (int j=0;j<4;j++) {gameView.killMonseter(Stone[i].x, Stone[i].y + k);}
                    }
                    break;
                }
            }
            //UnDraw Stone
            try{
                sleep(1000);
                if (stone){;
                    gameView.effect[Stone[index].x][Stone[index].y+h].reSet();
                    gameView.resetEffect(12);
                    stone=false;
                }
            }
            catch(InterruptedException e) {
                System.out.println("Exception occured");
            }
        }
    }
}
