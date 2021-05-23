package cn.balalals.liveboost.bean;


public class EntryRoomMessage {
    private String uname;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "EntryRoomMessage{" +
                "uname='" + uname + '\'' +
                '}';
    }
}
