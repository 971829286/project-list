package com.souche.niu.model.workTable;

import java.io.Serializable;
import java.util.List;

public class GroupDo implements Serializable {

    private String group_name;
    private String group_eventKey;
    private List<ElementsDo2> elements;

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_eventKey() {
        return group_eventKey;
    }

    public void setGroup_eventKey(String group_eventKey) {
        this.group_eventKey = group_eventKey;
    }

    public List<ElementsDo2> getElements() {
        return elements;
    }

    public void setElements(List<ElementsDo2> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "GroupDo{" +
                "group_name='" + group_name + '\'' +
                ", group_eventKey='" + group_eventKey + '\'' +
                ", elements=" + elements +
                '}';
    }
}
