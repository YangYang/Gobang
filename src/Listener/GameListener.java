package Listener;

import javax.swing.*;

/**
 * @program: Gobang
 * @description: 整个游戏的监听者类
 * @author: Yang Yang
 * @create: 2018-03-15 19:25
 **/
public interface GameListener {
    /**
     * 黑色获胜
     * */
    public void blackWin();

    /**
     * 白色获胜
     * */
    public void whiteWin();

    /**
     * 画图时候回调
     * */
    public void draw(ImageIcon imageIcon);

}
