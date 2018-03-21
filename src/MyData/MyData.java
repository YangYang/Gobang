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
     * 2 申请加入房间
     * 1 申请建立房间
     * 0 下棋
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
