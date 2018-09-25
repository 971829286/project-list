package cn.ourwill.huiyizhan.baseEnum;

/**
 * 类型索引：0-填空，1-单选，2-多选，3-打星题，4-图片选择题
 *
 * @author liupenghao
 */
public enum QuType {

    FillBLANK("填空题", 0),

    RADIO("单选题", 1),

    CHECKBOX("多选题", 2),

    TEXT("打星题", 3),

    PICTURECHECK("图片选择题", 4);//组合填空题

    private String qType;
    private int index;

    QuType(String qType, int index) {
        this.qType = qType;
        this.index = index;
    }

    public String getQType() {
        return this.qType;
    }

    public void setQType(String qType) {
        this.qType = qType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public static String getNameByIndex(Integer index) {
        String name = null;
        switch (index) {
            case 0:
                name = "填空题";
                break;
            case 1:
                name = "单选题";
                break;
            case 2:
                name = "多选题";
                break;
            case 3:
                name = "打星题";
                break;
            case 4:
                name = "图片选择题";
                break;
        }
        return name;
    }
}
