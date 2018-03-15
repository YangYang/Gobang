package View;

import Listener.GameListener;
import Util.ImageUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * @program: Gobang
 * @description: 棋盘类
 * @author: Yang Yang
 * @create: 2018-03-15 19:14
 **/
public class Board {

    private BufferedImage bufferedImage = null;

    /**
     * 黑色：1
     * 白色：-1
     * 无棋子：0
     * */
    private int [][]chesses = new int[100][100];

    /**
     * 黑先白后，判断是否为黑色走
     * */
    private boolean isBlack = true;

    private GameListener gameListener;

    public Board(GameListener gameListener) {
        this.gameListener = gameListener;
    }


    public Icon init(){
        bufferedImage = ImageUtil.scale("../../Images/ImgBoard.png",650,650);
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        gameListener.draw(imageIcon);
        for(int i = 0;i< 100;i++){
            for(int j = 0;j<100;j++){
                chesses[i][j] = 0;
            }
        }
        isBlack = true;
        return imageIcon;
    }
}
