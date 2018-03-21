package Controller;

import Listener.GameListener;
import Mina.MinaUtil;
import MyData.MyData;
import View.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @program: Gobang
 * @description: 游戏的Controller
 * @author: Yang Yang
 * @create: 2018-03-15 19:34
 **/
public class BoardController  extends JFrame{
    private Board board = new Board(new MyListener());
    //jPanel 容器
    private JPanel jPanel = null;
    //基础组件，需要置于容器内
    private JLabel jLabel = null;
    //是否可以下棋
    private boolean canPlay = true;
    //计算轮数
    private int count = 0;
    //步数计数
    private int stepCount = 0;

    //mina
    private MinaUtil minaUtil = null;
    private boolean isServer = false;

    public BoardController() {
        this.setTitle("联机对战五子棋");
        this.setSize(new Dimension(650, 695));
        this.setResizable(false);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
        this.setLayout(null);


        //添加一块棋盘
        this.setLayout(null);
        jPanel = new JPanel();
        jLabel = new JLabel();
        Icon icon = board.init();

        //设置菜单栏
        JMenuBar jMenuBar = new JMenuBar();
        this.setJMenuBar(jMenuBar);
        JMenu settingMenu = new JMenu("功能");
        jMenuBar.add(settingMenu);

        JMenuItem inviteOtherItem = new JMenuItem("邀请别人");
        JMenuItem acceptInviteItem = new JMenuItem("接受邀请");
        JMenuItem exitItem = new JMenuItem("退出");
        settingMenu.add(inviteOtherItem);
        settingMenu.add(acceptInviteItem);
        settingMenu.add(exitItem);


        JMenu helpMenu = new JMenu("帮助");
        jMenuBar.add(helpMenu);
        JMenuItem helpItem = new JMenuItem("关于");
        helpMenu.add(helpItem);


        inviteOtherItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                inviteOther();
            }
        });
        acceptInviteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                acceptInvite();
            }
        });
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        helpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Icon authorIcon = new ImageIcon("zp.png");
//                JOptionPane.showMessageDialog(ChessController.this.getContentPane(),"作者：张鹏（14级软件工程2班）\n邮箱：zhangpeng@imudges.com\n版本：1.1.1beta","关于",JOptionPane. PLAIN_MESSAGE,authorIcon);
            }
        });

        jLabel.setIcon(icon);
        jLabel.setBounds(0, 0, icon.getIconWidth(),icon.getIconHeight());
        jPanel.setBounds(0, 0, icon.getIconWidth(),icon.getIconHeight());
        jPanel.add(jLabel);
        this.add(jPanel);
        this.setVisible(true);
        jPanel.addMouseListener(new MyMouseListener());
    }

    /**
     * @Description: 初始化棋盘
     * @Param: []
     * @return: void
     * @Author: Yang Yang
     * @Time: 8:41 2018/3/17
     **/
    public void init(){
        board.init();
        count ++;
        stepCount = 0;
        //根据canPlay设置谁可以下，每局过后互换颜色
        if((isServer && count % 2 == 1) || (!isServer && count % 2 == 0)){
            canPlay = true;
        } else {
            canPlay = false;
        }
        if(count >= 1){
            if(canPlay){
               this.setTitle("轮到你下了");
            } else {
                this.setTitle("轮到对方下了");
            }
        }
    }



    class MyListener implements GameListener{

        @Override
        public void blackWin() {
            showResult(true);
        }

        @Override
        public void whiteWin() {
            showResult(false);
        }

        @Override
        public void draw(ImageIcon imageIcon) {
            jLabel.setIcon(imageIcon);
        }
    }

    class MyMouseListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(canPlay){
                MyData myData = new MyData();
                myData.setY(e.getX());
                myData.setY(e.getY());
                // TODO
//                canPlay = false;
                setChess(e.getX(),e.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    /**
     * @Description: 在棋盘上放置棋子
     * @Param: [x, y]
     * @return: void
     * @Author: Yang Yang
     * @Time: 23:47 2018/3/17
     **/
    private void setChess(int x, int y) {
        //stepCount为偶数时候是黑色下
        if(stepCount % 2 == 0){
            if(board.addBlack(x,y)){
                stepCount++;
            }
        } else {
            if(board.addWhite(x,y)){
                stepCount++;
            }
        }
    }

    /**
     * @Description:显示输赢方的结果
     * @Param: [tag]
     * @return: void
     * @Author: Yang Yang
     * @Time: 8:49 2018/3/17
     **/
    private void showResult(boolean tag){
        if(tag){
            JOptionPane.showInternalMessageDialog(BoardController.this.getContentPane(),
                    "黑方胜","游戏结束", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showInternalMessageDialog(BoardController.this.getContentPane(),
                    "白方胜","游戏结束", JOptionPane.INFORMATION_MESSAGE);
        }

        int option = JOptionPane.showConfirmDialog(BoardController.this.getContentPane(),
                "是否重新开始一局？", "游戏提示", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null);
        switch (option) {
            case JOptionPane.YES_NO_OPTION:
                init();
                break;
            case JOptionPane.NO_OPTION:
                System.exit(0);
        }
    }


}
