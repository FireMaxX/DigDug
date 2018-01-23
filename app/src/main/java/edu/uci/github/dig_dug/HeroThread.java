package edu.uci.github.dig_dug;

import java.lang.Thread;

/**
 * Created by fxuyi on 5/15/2017.
 */

//User Hero Control Thread, get onTouch event info from main Thread(gameView)
public class HeroThread extends Thread {
    int laserSleep=0;   //Lock lasergun for a certain time and delay remove laser effect on canvas
    GameView gameView;
    public HeroThread(GameView cv){
        gameView = cv;
    }

    public void run(){
        while(gameView.worker.status) {
            System.out.println(gameView.direction); //For debug
            if(laserSleep==0){  //No shoot, can move
                switch (gameView.direction){
                    case 0:{    // up
                        if (gameView.checkLandStatus(gameView.worker.x,gameView.worker.y-1)){
                            gameView.worker.move(0,-1);
                            gameView.worker.turn(gameView.direction);
                        }
                        break;
                    }
                    case 1:{    // down
                        if (gameView.checkLandStatus(gameView.worker.x,gameView.worker.y+1)){
                            gameView.worker.move(0,1);
                            gameView.worker.turn(gameView.direction);
                        }
                        break;
                    }
                    case 2:{    // left
                        if (gameView.checkLandStatus(gameView.worker.x-1,gameView.worker.y)){
                            gameView.worker.move(-1,0);
                            gameView.worker.turn(gameView.direction);
                        }
                        break;
                    }
                    case 3:{    // right
                        if (gameView.checkLandStatus(gameView.worker.x+1,gameView.worker.y)) {
                            gameView.worker.move(1,0);
                            gameView.worker.turn(gameView.direction);
                        }
                        break;
                    }
                    case 4:{    // attacck
                        if (laserSleep==0){
                            gameView.laserGun();
                            laserSleep=1;
                        }
                        break;
                    }
                    default:{
                        break;
                    }
                }
                if (gameView.Touch_UP_Marker==true){
                    gameView.direction=5;   //Stop and Hold
                };
                //Turn soil to channel
                gameView.blocks[gameView.worker.x][gameView.worker.y].setChannel();
            }

            //Delay
            else if (laserSleep==1){
                laserSleep++;
            }
            else if (laserSleep>=2){
                gameView.resetEffect(12);
                gameView.resetEffect(11);
                laserSleep=0;
            }

            try{
                sleep(800);
            }
            catch(InterruptedException e) {
                System.out.println("Exception occured");
            }
        }
    }
}
