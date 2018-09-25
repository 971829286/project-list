package cn.ourwill.huiyizhan.baseEnum;

/**
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/4/10 17:47
 * @Description
 * @version 1.0
 */
public enum TicketStatus {
    CREATE_NOT("未生成",0),SIGN_NOT("未签到",1),SIGN("已签到",2),CHECK_NOT("待审核",3),CHECK_NOT_PASS("未通过",4),REFUNDED("已退票",9);
//    CREATE_NOT(0),SIGN_NOT(1),SIGN(2),CHECK_NOT(3),REFUNDED(9);
    private String name;
    private Integer index;

    TicketStatus(String name,Integer index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (TicketStatus t : TicketStatus.values()) {
            if (t.getIndex() == index) {
                return t.name;
            }
        }
        return null;
    }
    // get set 方法
    public String getName() {
        return name;
    }
    public int getIndex() {
        return index;
    }
}

//0:未生成，1：未签到，2：已签到，3：待审核，9：已退票