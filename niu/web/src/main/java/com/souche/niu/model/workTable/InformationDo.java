package com.souche.niu.model.workTable;

import java.io.Serializable;
import java.util.List;

public class InformationDo implements Serializable {

    private List<ElementsDo> elements;

    public List<ElementsDo> getElements() {
        return elements;
    }

    public void setElements(List<ElementsDo> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "InformationDo{" +
                "elements=" + elements +
                '}';
    }
}
