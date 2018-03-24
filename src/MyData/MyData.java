package MyData;

import java.io.Serializable;

/**
 * @program: MinaClient
 * @description:
 * @author: Yang Yang
 * @create: 2018-03-21 18:08
 **/
public class MyData implements Serializable {
    private int x;
    private int y;

    /**
     * 8 对方投降（这一条是针对客户端的，也就是说服务器发给客户端）
     * 7 发送请求方投降（这一条是针对服务器的，也就是说是客户端发送给服务器的）
     * 6 离开房间
     * 5 留在房间
     * 4 对方下线通知
     * 3 上线通知
     * 2 申请加入房间
     * 1 申请建立房间
     * 0 下棋
     * -1 不能加入自己的房间
     * -2 房间已满或者不存在房间
     * */
    private int type;

    private String roomName;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
