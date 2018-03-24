package View;

import Config.Config;
import Listener.GameListener;
import Util.ImageUtil;
import javax.swing.*;
import java.awt.*;
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

    /**
     * @Description: 显示一个黑色的棋子
     * @Param: [x, y] 鼠标点击的坐标
     * @return: boolean
     * @Author: Yang Yang
     * @Time: 16:24 2018/3/20
     **/
    public boolean addBlack(int x,int y){
        Coord coord = getCoord(x,y);
        System.out.println("x = " + coord.getX());
        System.out.println("y = " + coord.getY());
        if(chesses[coord.getX()][coord.getY()] == 0 ){
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
            chesses[coord.getX()][coord.getY()] = 1;
            checkWiner(coord,true);
            return true;
        }
        return false;
    }

    /**
     * @Description: 显示一个白色的棋子
     * @Param: [x, y] 鼠标点击的坐标
     * @return: boolean
     * @Author: Yang Yang
     * @Time: 16:24 2018/3/20
     **/
    public boolean addWhite(int x,int y){
        Coord coord = getCoord(x,y);
        System.out.println("x = " + coord.getX());
        System.out.println("y = " + coord.getY());
        if(chesses[coord.getX()][coord.getY()] == 0){
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
            //因为涉及到赢得比赛之后下一句第一个下棋的人，所以此处设置一个tag，查看是否检测成功。因为服务器发来的消息同时会出发这个函数，所以对白色方来说，最后一次addBlack，也就是黑色方赢以后，白色方的isBlack应该为true
            checkWiner(coord,false);
            return true;
        }
        return false;
    }

    /**
     * @Description: 判断胜负
     * @Param: [coord, color] coord 下棋位置，color true 黑棋 false 白棋
     * @return: boolean 返回true 代表有结果了，返回false 代表没有输赢
     * @Author: Yang Yang
     * @Time: 10:52 2018/3/21
     **/
    private boolean checkWiner(Coord coord,boolean color){

        if(coord == null){
            return false;
        }
        int type ;
        if(color == true){
            //黑棋
            type = 1;
        } else {
            //白棋
            type = -1;
        }
        int line = 0;

        //水平方向
        //往左
        for(int x = coord.getX(),y = coord.getY();x >= 0;x--){
            if(chesses[x][y] == type){
                line ++ ;
            } else {
                break;
            }
        }
        line --;
        //往右
        for(int x = coord.getX(),y = coord.getY();x <= 18;x++){
            if(chesses[x][y] == type){
                line ++ ;
            } else {
                break;
            }
        }
        //判断胜负
        if(line == 5){
            if(type == 1){
                gameListener.blackWin();
                return true;
            } else {
                gameListener.whiteWin();
                return true;
            }
        } else {
            line = 0;
        }

        //垂直方向
        //向上
        for(int x = coord.getX(),y = coord.getY();y >= 0;y--){
            if(chesses[x][y] == type){
                line ++ ;
            } else {
                break;
            }
        }
        line --;
        //向下
        for(int x = coord.getX(),y = coord.getY();y <= 18;y++){
            if(chesses[x][y] == type){
                line ++ ;
            } else {
                break;
            }
        }
        //判断胜负
        if(line == 5){
            if(type == 1){
                gameListener.blackWin();
                return true;
            } else {
                gameListener.whiteWin();
                return true;
            }
        } else {
            line = 0;
        }

        //对角线
        //左上
        for(int x = coord.getX(),y = coord.getY();x >= 0 && y >= 0;y--,x--){
            if(chesses[x][y] == type){
                line ++ ;
            } else {
                break;
            }
        }
        line --;
        //右下
        for(int x = coord.getX(),y = coord.getY();x <= 18 && y <= 18 ;x++,y++){
            if(chesses[x][y] == type){
                line ++ ;
            } else {
                break;
            }
        }
        //判断胜负
        if(line == 5){
            if(type == 1){
                gameListener.blackWin();
                return true;
            } else {
                gameListener.whiteWin();
                return true;
            }
        } else {
            line = 0;
        }

        //右上
        for(int x = coord.getX(),y = coord.getY();x <= 18 && y >= 0;y--,x++){
            if(chesses[x][y] == type){
                line ++ ;
            } else {
                break;
            }
        }
        line --;
        //左下
        for(int x = coord.getX(),y = coord.getY();x >= 0 && y <= 18 ;x--,y++){
            if(chesses[x][y] == type){
                line ++ ;
            } else {
                break;
            }
        }
        //判断胜负
        if(line == 5){
            if(type == 1){
                gameListener.blackWin();
                return true;
            } else {
                gameListener.whiteWin();
                return true;
            }
        } else {
            line = 0;
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
    Coord getCoord(int x,int y){
        Coord coord = new Coord();
        coord.setX((int)((x-15.75)/32.5));
        coord.setY((int)((y-21.75)/32.5));
        return  coord;
    }

    public void surrender(boolean tag){
        if(tag){
            //黑色
            gameListener.blackWin();
        } else {
            //白色
            gameListener.whiteWin();
        }
    }
}

