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
        initLeaveRoom();
    }

    /**
     * @Description: 接受邀请的逻辑
     * @Param: []
     * @return: void
     * @Author: Yang Yang
     * @Time: 9:17 2018/3/24
     **/
    private void  acceptInvite() {
        String b = JOptionPane.showInputDialog("请输入想要加入的房间名：");
        if (b == null || b.equals("")){
            JOptionPane.showInternalMessageDialog(BoardController.this.getContentPane(),
                    "房间名错误，请重新输入！" ,"接受邀请", JOptionPane.INFORMATION_MESSAGE);
        }else {
            minaUtil = MinaUtil.getInstance(new MySimpleMinaListener());
            //发送加入房间的请求
            MyData myData = new MyData();
            myData.setType(2);
            myData.setRoomName(b);
            minaUtil.sent(myData);
            isHost = false;
            canPlay = false;
        }
    }

    /**
     * @Description: 邀请按钮的逻辑
     * @Param: []
     * @return: void
     * @Author: Yang Yang
     * @Time: 9:17 2018/3/24
     **/
    private void inviteOther() {
        try {
            minaUtil = MinaUtil.getInstance(new MySimpleMinaListener());
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
                setTitle("等待加入……");
                setLeaveRoomItem(true);
                setJMenuItem(false);
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
               setSurrenderItem(true);
            } else {
                this.setTitle("轮到对方下了");
                setSurrenderItem(false);
            }
        }
    }


    /**
     * @program: Gobang
     * @description: 实现游戏Listener
     * @author: Yang Yang
     * @create:
     **/
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


    /**
     * @program: Gobang
     * @description: 鼠标点击事件的Listener
     * @author: Yang Yang
     * @create:
     **/
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
                setTitle("轮到对方下了哦");
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
     * @program: Gobang
     * @description: 简单Listener实现
     * @author: Yang Yang
     * @create:
     **/
    class MySimpleMinaListener implements SimpleMinaListener{

        @Override
        public void onReceive(Object obj, IoSession ioSession, int tag) {
            switch (tag){
                //1、2是服务器参数
                case 0:
                    if(canPlay){
                        setTitle("轮到对方下了");
                        setSurrenderItem(false);
                        canPlay = false;
                    } else {
                        setTitle("轮到你下了哦");
                        setSurrenderItem(true);
                        canPlay = true;
                    }
                    //下棋
                    MyData myData = (MyData) obj;
                    setChess(myData.getX(), myData.getY());
                    break;
                case 3:
                    //上线通知
                    //上线以后拒绝离开房间
                    setSurrenderItem(true);
                    setJMenuItem(false);
                    setLeaveRoomItem(false);
                    roomName = ((MyData) obj).getRoomName();
                    if(getTitle().equals("等待加入……")){
                        setTitle("对方已经上线，轮到你下了哦");
                        setSurrenderItem(true);
                        canPlay = true;
                    } else {
                        if(canPlay){
                            setTitle("轮到你下了哦");
                            setSurrenderItem(true);
                        } else {
                            setTitle("轮对方下了");
                            setSurrenderItem(false);
                        }
                    }
                    break;
                case 4:
                    //下线通知
                    boolean state = isStay();
                    MyData temp = new MyData();
                    if(state){
                        //留下
                        temp.setRoomName(roomName);
                        temp.setType(5);
                        minaUtil.sent(temp);
                        initStayRoom();
                        setJMenuItem(false);
                        setLeaveRoomItem(true);
                    } else {
                        //离开房间，但是不离开游戏
                        temp.setRoomName(roomName);
                        temp.setType(6);
                        minaUtil.sent(temp);
                        initLeaveRoom();
                    }
                    break;
                case 8:
                    //对方投降
                    surrenderAction();
                    break;
                case 9:
                    //查询房间状态结果，表示对方在线
                    int option = JOptionPane.showConfirmDialog(BoardController.this.getContentPane(),
                            "是否重新开始一局？", "游戏提示", JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, null);
                    switch (option) {
                        case JOptionPane.YES_NO_OPTION:
                            init();
                            break;
                        case JOptionPane.NO_OPTION:
                            //离开房间逻辑
                            System.exit(0);
                    }
                    break;

                case 10:

                    break;
                case -1:
                    //错误
                    JOptionPane.showInternalMessageDialog(BoardController.this.getContentPane(),
                            "不能加入自己建立的房间" ,"邀请", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("不能加入自己的房间");
                    canPlay = false;
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
            }
        } else {
            if(!board.addWhite(x,y)){
                stepCount --;
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

        //先看看是否在线，

        //TODO 下线广播一次；此处询问一次
        //TODO 此处不应该发送请求
        MyData myData = new MyData();
        myData.setType(9);
        myData.setRoomName(roomName);
        minaUtil.sent(myData);
    }
    /**
     * @Description: 显示是否留在房间的Dialog
     * @Param: []
     * @return: boolean
     * @Author: Yang Yang
     * @Time: 21:05 2018/3/23
     **/
    private boolean isStay(){
        int option = JOptionPane.showConfirmDialog(BoardController.this.getContentPane(),
                "对方已下线，是否要留在房间？","系统消息", JOptionPane.YES_NO_OPTION);
        switch (option){
            case JOptionPane.YES_OPTION:
                //呆在房间
                return true;
            case JOptionPane.NO_OPTION:
                //离开房间，不离开游戏
                return false;
        }
        return false;
    }

    /**
     * @Description: 留在房间时候需要做的初始化工作
     * @Param: []
     * @return: void
     * @Author: Yang Yang
     * @Time: 9:19 2018/3/24
     **/
    private void initStayRoom(){
        setTitle("等待加入……");
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

        JMenuItem inviteOtherItem = new JMenuItem("创建房间");
        JMenuItem acceptInviteItem = new JMenuItem("加入房间");
        JMenuItem leaveRoomItem = new JMenuItem("离开房间");
        JMenuItem surrenderItem = new JMenuItem("投降");
        leaveRoomItem.setEnabled(false);
        surrenderItem.setEnabled(false);
        JMenuItem exitItem = new JMenuItem("退出");
        settingMenu.add(inviteOtherItem);
        settingMenu.add(acceptInviteItem);
        settingMenu.add(leaveRoomItem);
        settingMenu.add(surrenderItem);
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

        leaveRoomItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leaveRoomAction();
            }
        });

        surrenderItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                surrenderActionSend();
            }
        });

        helpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Icon authorIcon = new ImageIcon("Images/ImgYY.png");
                JOptionPane.showMessageDialog(BoardController.this.getContentPane(),"作者：杨洋（2015级电子商务）\n邮箱：yangyangds@imudges.com\n版本：1.1.1beta","关于",JOptionPane.INFORMATION_MESSAGE,authorIcon);
            }
        });

        jLabel.setIcon(icon);
        jLabel.setBounds(0, 0, icon.getIconWidth(),icon.getIconHeight());
        jPanel.setBounds(0, 0, icon.getIconWidth(),icon.getIconHeight());
        jPanel.add(jLabel);
        this.add(jPanel);
        this.setVisible(true);
        jPanel.addMouseListener(new MyMouseListener());
        isHost = true;
        setTitle("等待加入……");
    }

    /**
     * @Description: 离开房间时候需要做的初始化操作，和刚开始进入游戏时候相同
     * @Param: []
     * @return: void
     * @Author: Yang Yang
     * @Time: 21:28 2018/3/23
     **/
    private void initLeaveRoom(){
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

        JMenuItem inviteOtherItem = new JMenuItem("创建房间");
        JMenuItem acceptInviteItem = new JMenuItem("加入房间");
        JMenuItem leaveRoomItem = new JMenuItem("离开房间");
        JMenuItem surrenderItem = new JMenuItem("投降");
        leaveRoomItem.setEnabled(false);
        surrenderItem.setEnabled(false);
        JMenuItem exitItem = new JMenuItem("退出");
        settingMenu.add(inviteOtherItem);
        settingMenu.add(acceptInviteItem);
        settingMenu.add(leaveRoomItem);
        settingMenu.add(surrenderItem);
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
        leaveRoomItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leaveRoomAction();
            }
        });
        surrenderItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                surrenderActionSend();
            }
        });
        helpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Icon authorIcon = new ImageIcon("Images/ImgYY.png");
                JOptionPane.showMessageDialog(BoardController.this.getContentPane(),"作者：杨洋（2015级电子商务）\n邮箱：yangyangds@imudges.com\n版本：1.1.1beta","关于作者",JOptionPane.INFORMATION_MESSAGE,authorIcon);
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
     * @Description: 离开房间时的逻辑
     * @Param: []
     * @return: void
     * @Author: Yang Yang
     * @Time: 9:21 2018/3/24
     **/
    private void leaveRoomAction(){
        setTitle("联机对战五子棋");
        setLeaveRoomItem(false);
        MyData myData = new MyData();
        myData.setType(6);
        myData.setRoomName(roomName);
        minaUtil.sent(myData);
        setJMenuItem(true);
        setSurrenderItem(false);
    }

    /**
     * @Description: 离开房间后对菜单栏的初始化
     * @Param: [tag]
     * @return: void
     * @Author: Yang Yang
     * @Time: 9:21 2018/3/24
     **/
    private void setLeaveRoomItem(boolean tag){
        JMenuBar jMenuBar = this.getJMenuBar();
        JMenu menu = jMenuBar.getMenu(0);
        JMenuItem menuItem = menu.getItem(2);
        menuItem.setEnabled(tag);
    }

    /**
     * @Description: 通用的设置Item的操作
     * @Param: [tag]
     * @return: void
     * @Author: Yang Yang
     * @Time: 9:22 2018/3/24
     **/
    private void setJMenuItem(boolean tag){
        JMenuBar jMenuBar = this.getJMenuBar();
        JMenu menu = jMenuBar.getMenu(0);
        //创建房间
        JMenuItem menuItem1 = menu.getItem(0);
        //加入房间
        JMenuItem menuItem2 = menu.getItem(1);

        menuItem1.setEnabled(tag);
        menuItem2.setEnabled(tag);
    }

    private void setSurrenderItem(boolean tag){
        JMenuBar jMenuBar = this.getJMenuBar();
        JMenu menu = jMenuBar.getMenu(0);
        //投降
        JMenuItem menuItem3 = menu.getItem(3);
        menuItem3.setEnabled(tag);
    }

    private void surrenderActionSend(){
        if(stepCount % 2 == 0){
            //发送请求
            MyData myData = new MyData();
            myData.setRoomName(roomName);
            myData.setType(7);
            minaUtil.sent(myData);
            //此时应该白色下，如果点了投降，就是白色投降
            board.surrender(false);
        } else {
            //发送请求
            MyData myData = new MyData();
            myData.setRoomName(roomName);
            myData.setType(7);
            minaUtil.sent(myData);
            //此时应该黑色下，如果点了投降，就是黑色投降
            board.surrender(true);
        }
    }

    private void surrenderAction(){
        if(stepCount % 2 ==0 ){
            board.surrender(false);
        } else {
            board.surrender(true);
        }
    }
}
