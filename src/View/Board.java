package View;

import Config.Config;
import Listener.GameListener;
import Util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

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


    /**
     * @Description: 初始化一个棋盘
     * @Param: []
     * @return: javax.swing.Icon
     * @Author: Yang Yang
     * @Time: 21:38 2018/3/16
     **/
    public Icon init(){
        bufferedImage = ImageUtil.scale("Images/ImgBoard.png",650,650);
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

    public boolean addBlack(int x,int y){
        Coord coord = getCoord(x,y);
        System.out.println("x = " + coord.getX());
        System.out.println("y = " + coord.getY());
        if(chesses[coord.getX()][coord.getY()] == 0 && isBlack){
            BufferedImage blackBufferedImage = ImageUtil.scale("Images/ImgBlack.png",Config.ChessSize,Config.ChessSize);//将图片缩放后加载到内存
            Graphics g = bufferedImage.getGraphics();
            // x,y 图像左上角坐标
            g.drawImage(blackBufferedImage,(int)(coord.getX()*32.5 + 17),(int)(coord.getY()*32.5 + 17),null);
            BufferedImage zxBufferedImage = ImageUtil.scale("Images/ImgCenter.png",Config.ChessSize,Config.ChessSize);

            //画中心的十字
            BufferedImage centerBufferedImage = new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(),bufferedImage.getType());
            centerBufferedImage.setData(bufferedImage.getData());
            g = centerBufferedImage.getGraphics();
            g.drawImage(zxBufferedImage,(int)(coord.getX()*32.5 + 17),(int)(coord.getY()*32.5 + 17),null);

            ImageIcon imageIcon = new ImageIcon(centerBufferedImage);
            gameListener.draw(imageIcon);
            chesses[coord.getY()][coord.getY()] = 1;
            return true;
        }
        return false;
    }

    public boolean addWhite(int x,int y){
        Coord coord = getCoord(x,y);
        System.out.println("x = " + coord.getX());
        System.out.println("y = " + coord.getY());
        if(chesses[coord.getX()][coord.getY()] == 0 && !isBlack){
            BufferedImage blackBufferedImage = ImageUtil.scale("Images/ImgWhite.png",Config.ChessSize,Config.ChessSize);//将图片缩放后加载到内存
            Graphics g = bufferedImage.getGraphics();
            // x,y 图像左上角坐标
            g.drawImage(blackBufferedImage,(int)(coord.getX()*32.5 + 17),(int)(coord.getY()*32.5 + 17),null);
            BufferedImage zxBufferedImage = ImageUtil.scale("Images/ImgCenter.png",Config.ChessSize,Config.ChessSize);

            //画中心的十字
            BufferedImage centerBufferedImage = new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(),bufferedImage.getType());
            centerBufferedImage.setData(bufferedImage.getData());
            g = centerBufferedImage.getGraphics();
            g.drawImage(zxBufferedImage,(int)(coord.getX()*32.5 + 17),(int)(coord.getY()*32.5 + 17),null);

            ImageIcon imageIcon = new ImageIcon(centerBufferedImage);
            gameListener.draw(imageIcon);
            chesses[coord.getX()][coord.getY()] = -1;
            return true;
        }
        return false;
    }

    /**
     * 棋盘上每点的坐标
     * */
    class Coord{
        private int x;
        private int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    /**
     * @Description: 鼠标点击坐标转换为棋盘坐标
     * @Param: [x, y]
     * @return: View.Board.Coord
     * @Author: Yang Yang
     * @Time: 15:28 2018/3/20
     **/
    private Coord getCoord(int x,int y){
        Coord coord = new Coord();
        coord.setX((int)((x-15.75)/32.5));
        coord.setY((int)((y-21.75)/32.5));
        return  coord;
    }
}

