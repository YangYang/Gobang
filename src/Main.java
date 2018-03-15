import Listener.GameListener;
import View.Board;

import javax.swing.*;

/**
 * @program: MinaClientTest
 * @description: 程序入口
 * @author: Yang Yang
 * @create: 2018-03-13 16:35
 **/
public class Main {
    public static void main(String []args){
        System.out.println("Hello World");
        Board board = new Board(new GameListener() {
            @Override
            public void blackWin() {

            }

            @Override
            public void whiteWin() {

            }

            @Override
            public void draw(ImageIcon imageIcon) {

            }
        });
    }
}
