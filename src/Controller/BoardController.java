package Controller;

import Listener.GameListener;
import Mina.MinaUtil;
import Mina.SimpleMinaListener;
import MyData.MyData;
import Util.Toolkit;
import View.Board;
import org.apache.mina.core.session.IoSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
    private boolean canPlay = false;
    //计算轮数
    private int count = 0;
    //步数计数
    private int stepCount = 0;
    //房间名
    private String roomName = null;


    //mina
    private MinaUtil minaUtil = null;

    private boolean isHost = false;

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
                inviteOther();
            }
        });
        acceptInviteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptInvite();
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

    private void  acceptInvite() {
        String b = JOptionPane.showInputDialog("请输入想要加入的房间名：");
        if (b == null || b.equals("")){
            JOptionPane.showInternalMessageDialog(BoardController.this.getContentPane(),
                    "房间名错误，请重新输入！" ,"接受邀请", JOptionPane.INFORMATION_MESSAGE);
        }else {
//            isServer = false;
            minaUtil = MinaUtil.getInstance(new MySimppleMinaListener());
            //发送加入房间的请求
            MyData myData = new MyData();
            myData.setType(2);
            myData.setRoomName(b);
            minaUtil.sent(myData);
            isHost = false;
            canPlay = false;
        }
    }

    private void inviteOther() {
        try {
            minaUtil = MinaUtil.getInstance(new MySimppleMinaListener());
            String b = JOptionPane.showInputDialog("请输入您的房间名：");
            if(b == null || b.equals("")){
                JOptionPane.showMessageDialog(BoardController.this.getContentPane(),
                        "房间名无效，请重新输入！","邀请别人",JOptionPane.INFORMATION_MESSAGE);
                return ;
            } else {
                //发送建立房间的请求
                roomName = b;
                MyData myData = new MyData();
                myData.setType(1);
                myData.setRoomName(roomName);
                minaUtil.sent(myData);
                isHost = true;
                canPlay = false;
                System.out.println(b);
//            JOptionPane.showInternalMessageDialog(BoardController.this.getContentPane(),
//                    "你的IP地址为：" + InetAddress.getLocalHost().getHostAddress(), "邀请别人", JOptionPane.INFORMATION_MESSAGE);
//            isServer = true;
                setTitle("等待加入……");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showInternalMessageDialog(BoardController.this.getContentPane(),
                    "发生未知错误" ,"邀请别人", JOptionPane.INFORMATION_MESSAGE);
        }
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
        if((isHost && count % 2 == 0) || (!isHost && count % 2 == 1)){
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
            //TODO 发送请求

            showResult(true);
        }

        @Override
        public void whiteWin() {
            //TODO 发送请求

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
                myData.setX(e.getX());
                myData.setY(e.getY());
                myData.setType(0);
                myData.setRoomName(roomName);
                canPlay = false;
                //必须先发，再下
                minaUtil.sent(myData);
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

    class MySimppleMinaListener implements SimpleMinaListener{

        @Override
        public void onReceive(Object obj, IoSession ioSession, int tag) {
            switch (tag){
                //1、2是服务器参数
                case 0:
                    if(canPlay){
                        setTitle("轮到对方下了");
                        canPlay = false;
                    } else {
                        setTitle("轮到你下了哦");
                        canPlay = true;
                    }
                    //下棋
                    MyData myData = (MyData) obj;
                    //TODO 错误的逻辑
                    setChess2View(myData.getX(), myData.getY());

                    break;
                case 3:
                    roomName = ((MyData) obj).getRoomName();
                    if(getTitle().equals("等待加入……")){
                        setTitle("对方已经上线，轮到你下了哦");
                        canPlay = true;
                    } else {
                        if(canPlay){
                            setTitle("轮到你下了哦");
                        } else {
                            setTitle("轮对方下了");
                        }
                    }
                    break;
                case -1:
                    //错误
                    JOptionPane.showInternalMessageDialog(BoardController.this.getContentPane(),
                            "不能加入自己建立的房间" ,"邀请", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("不能加入自己的房间");
                    break;
                case -2:
                    JOptionPane.showInternalMessageDialog(BoardController.this.getContentPane(),
                            "房间已满/不存在房间，请重新输入！" ,"邀请", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("该房间不存在！");
                    break;
            }
        }

        @Override
        public void onLine(String msg) {
            setTitle(msg);

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
        if(stepCount++ % 2 == 0){
            if(!board.addBlack(x,y)){
                stepCount--;
            } else {
                if(canPlay){
                    setTitle("轮到你下了哦");
                } else {
                    setTitle("轮对方下了");
                }
            }
        } else {
            if(!board.addWhite(x,y)){
                stepCount --;
            } else {
                if(canPlay){
                    setTitle("轮到你下了哦");
                } else {
                    setTitle("轮对方下了");
                }
            }
        }
    }

    //TODO 错误的逻辑
    private void setChess2View(int x,int y){
        if(stepCount++ % 2 == 0){
            if(!board.addBlack2View(x,y)){
                stepCount--;
            } else {
            }
        } else {
            if(!board.addWhite2View(x,y)){
                stepCount --;
            } else {
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
