package com.souche.niu.model.userInformation;

public class CollectionDo {

    private String protocol;

    private String reduction;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getReduction() {
        return reduction;
    }

    public void setReduction(String reduction) {
        this.reduction = reduction;
    }

    @Override
    public String toString() {
        return "CollectionDo{" +
                "protocol='" + protocol + '\'' +
                ", reduction='" + reduction + '\'' +
                '}';
    }
}
