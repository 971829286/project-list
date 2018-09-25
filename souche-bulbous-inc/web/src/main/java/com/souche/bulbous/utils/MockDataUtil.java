package com.souche.bulbous.utils;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.souche.bulbous.vo.CarStatisticBean;
import com.souche.bulbous.vo.QuoteBean;
import com.souche.bulbous.vo.UserManagerBean;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.JsonUtils;

public class MockDataUtil {
    public static List<String> mockNames = Lists.newArrayList();
    public static List<String> mockTitles = Lists.newArrayList();
    public static List<String> mockAppNames = Lists.newArrayList();
    public static List<String> mockPhones = Lists.newArrayList();

    static {
        mockNames.add("张三");
        mockNames.add("李斯");
        mockNames.add("李逍遥");
        mockNames.add("Alex");
        mockNames.add("董卓");
        mockNames.add("夜华");
        mockNames.add("马叔叔");
        mockNames.add("刘德麻");
        mockNames.add("郭芙蓉");
        mockNames.add("王婆");
        mockNames.add("西门大官人");

        mockTitles.add("凉凉天意 潋滟一身花色");
        mockTitles.add("落入凡尘 伤情着我");
        mockTitles.add("生劫易渡 情劫难了");
        mockTitles.add("折旧的心 还有几分前生的恨");
        mockTitles.add("还有几分前生的恨");
        mockTitles.add("也曾鬓微霜 也曾因你回光");
        mockTitles.add("悠悠岁月漫长 怎能浪费时光");
        mockTitles.add("去流浪（去流浪）去换成长");
        mockTitles.add("灼灼桃花凉 今生愈渐滚烫");
        mockTitles.add("一朵已放心上 足够三生三世背影成双（背影成双）");
        mockTitles.add("在水一方");
        mockTitles.add("凉凉夜色 为你思念成河");
        mockTitles.add("化作春泥 呵护着我");
        mockTitles.add("浅浅岁月 拂满爱人袖");
        mockTitles.add("片片芳菲 入水流");

        mockAppNames.add("dafengche");
        mockAppNames.add("cheniu");
        mockAppNames.add("tangeche");

        mockPhones.add("13673592302");
        mockPhones.add("13773592302");
        mockPhones.add("13873592302");
        mockPhones.add("13603592302");
        mockPhones.add("12673592302");
        mockPhones.add("19673592302");

    }

    private static Integer lastId = 0;

    public static Integer getNextId() {
        Integer nextId = lastId + 1;
        lastId++;
        return nextId;
    }

    public static Date getRandomDate() {
        Date now = new Date();
        return getRandomDate(now);
    }

    public static Date getRandomDate(Date now) {
        long time = now.getTime();
        int seed = 100000;
        time = time + (long) (Math.random() * seed);
        return new Date(time);
    }

    private static String getRandom(List<String> list) {
        int seed = list.size();
        int index = (int) (Math.random() * seed);
        return list.get(index);
    }

    public static String getRandomNames() {
        int seed = 10;
        int total = (int) (Math.random() * seed);
        List<String> names = Lists.newArrayList();
        for (int i = 0; i < total; i++) {
            names.add(getRandomName());
        }
        return CollectionUtils.combineListToString(names, ',');
    }

    public static int getRandonCountDown() {
        int seed = 5;
        int countDown = (int) (Math.random() * seed);
        return countDown == 0 ? 1 : countDown;
    }

    public static String getRandomName() {
        return getRandom(mockNames);
    }

    public static String getRandomTitle() {
        return getRandom(mockTitles);
    }

    public static String getRandomAppName() {
        return getRandom(mockAppNames);
    }

    public static String getRandomPhone() {
        return getRandom(mockPhones);
    }

    public static CarStatisticBean getCarStatisticBean() {

        CarStatisticBean carStatisticBean = new CarStatisticBean();
        carStatisticBean.setCarId(getNextId() + "");
        carStatisticBean.setUserPhone(getRandomPhone());
        carStatisticBean.setNickName(getRandomName());
        carStatisticBean.setModeName(getRandomTitle());
        carStatisticBean.setDateSell(getRandomDate().getTime() + "");
        carStatisticBean.setCarPrice(getRandonCountDown() + 0.0);
        // carStatisticBean.setCarPrice(getRandonCountDown() + 0L);
        carStatisticBean.setQuoteCount(getRandonCountDown());
        carStatisticBean.setStatus(getRandonCountDown() + "");
        return carStatisticBean;
    }

    public static QuoteBean getQuoteBean() {
        QuoteBean bean = new QuoteBean();
        bean.setNickName(getRandomName());
        bean.setModeName(getRandomTitle());
        bean.setSalePrice(getRandonCountDown() + 0.0);
        bean.setQuoteTime(getRandomDate().getTime() + "");
        bean.setQuotePrice(getRandonCountDown() + 0.0);
        bean.setUserPhone(getRandomPhone());
        return bean;
    }

    public static UserManagerBean getUserManagerBean() {
        UserManagerBean bean = new UserManagerBean();
        bean.setNickName(getRandomName());
        bean.setPhone(getRandomPhone());
        bean.setDateCreate(getRandomDate().getTime() + "");
        bean.setQuoteCount(getRandonCountDown());
        bean.setSellNum(getRandonCountDown());
        return bean;
    }

    public static void main(String[] args) {
        CarStatisticBean CarStatisticBean = MockDataUtil.getCarStatisticBean();
        System.out.println(JsonUtils.toJson(CarStatisticBean));
    }
}
