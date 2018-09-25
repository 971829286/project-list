package cn.ourwill.tuwenzb.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class VoteOptions implements Serializable {
    private Integer id;

    private Integer voteId;//投票id

    private String option;//投票选项

    private Integer optionTotal;//选项投票数

    private String optionPercent;//选项投百分比

    private Integer isVoted;//当前登陆人是否投票   0:未投票 1:已投票

}
