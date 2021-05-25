package cn.balalals.liveboost.bean;

public class GiftMessage {
    private String action;
    private Integer giftId;
    private String giftName;
    private String uname;

    @Override
    public String toString() {
        return "GiftMessage{" +
                "action='" + action + '\'' +
                ", giftId=" + giftId +
                ", giftName='" + giftName + '\'' +
                ", uname='" + uname + '\'' +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}

