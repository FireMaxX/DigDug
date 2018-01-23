package edu.uci.github.dig_dug;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by fxuyi on 5/22/2017.
 */

//Start once when GameOver=True, last for 1.5s
public class EndGameThread extends Thread {
    GameView gameView;
    public EndGameThread(GameView cv){
        gameView = cv;
    }

    public void run(){
        SurfaceHolder holder;
        Canvas canvas;
        holder = gameView.getHolder();
        canvas = holder.lockCanvas();

        //Keep End of Game Message for 1.5s
        if(canvas != null) {
            gameView.draw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
        try{
            sleep(1500);
        }
        catch(InterruptedException e) {
            System.out.println("Exception occured");
        }
        gameView.Exiting=true;
    }
}
