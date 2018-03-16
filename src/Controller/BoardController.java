package Controller;

import Listener.GameListener;
import Mina.MinaUtil;
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

    private JPanel jPanel = null;
    private JLabel jLabel = null;
    private boolean canPlay;
    //计算局数
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


        /**
         * 添加一块棋盘
         * */
        this.setLayout(null);
        jPanel = new JPanel();
        jLabel = new JLabel();
        Icon icon = board.init();

        //设置菜单栏
        JMenuBar jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);
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

    class MyListener implements GameListener{

        @Override
        public void blackWin() {

        }

        @Override
        public void whiteWin() {

        }

        @Override
        public void draw(ImageIcon imageIcon) {

        }
    }

    class MyMouseListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

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
}
