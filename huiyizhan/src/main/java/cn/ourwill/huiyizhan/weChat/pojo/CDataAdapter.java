package cn.ourwill.huiyizhan.weChat.pojo;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @Package: cn.ourwill.huiyizhan.weChat.pojo
 * @Author: LiuFeng
 * @Description:
 * @Date: Created in 2018/4/26
 */
public class CDataAdapter extends XmlAdapter<String, String> {
    @Override
    public String marshal (String v) throws Exception
    {
        return "<![CDATA[" + v + "]]>";
    }
    @Override
    public String unmarshal (String v) throws Exception
    {
        if ("<![CDATA[]]>".equals (v))
        {
            return "";
        }
        String v1 = null;
        String v2 = null;
        String subStart = "<![CDATA[";
        int a = v.indexOf (subStart);
        if (a >= 0)
        {
            v1 = v.substring (subStart.length (), v.length ());
        }
        else
        {
            return v;
        }
        String subEnd = "]]>";
        int b = v1.indexOf (subEnd);
        if (b >= 0)
        {
            v2 = v1.substring (0, b);
        }
        return v2;
    }
}
