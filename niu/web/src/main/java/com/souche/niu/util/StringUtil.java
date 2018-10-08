package com.souche.niu.util;

import java.util.List;

public class StringUtil {

    public static String stringAppend(List list){
        if (list!=null && list.size()>0){
            StringBuilder stb = new StringBuilder();
            stb.append("\'").append(list.get(0)).append("\'");
            for (int i=1; i<list.size(); i++) {
                stb.append("|").append("\'").append(list.get(i)).append("\'");
            }
            return stb.toString();
        }
        return null;
    }


}
