package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.TrxOrder;
import cn.ourwill.tuwenzb.entity.VoteOptions;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author feng.Liu @ourwill.com.cn
 * @Time 2018/2/27 11:11
 * @Version1.0
 */
@Repository
public interface VoteOptionsMapper extends IBaseMapper<VoteOptions>{
    String columns="id, order_no, user_id, open_id, transaction_id,prepay_id,prepayid_duetime, from_type,create_ip, type, price, number, amount, fee_type, bank_type, create_time, " +
            "finish_time, transaction_status, order_status,trade_type,code_url,photo_live, version";

    @InsertProvider(type = VoteOptionsMapperProvider.class,method ="save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer save(VoteOptions voteOptions);

    @InsertProvider(type = VoteOptionsMapperProvider.class,method ="saveList")
    public Integer saveList(@Param("voteOptions") List<VoteOptions> voteOptions);

    @SelectProvider(type = VoteOptionsMapperProvider.class,method = "selectByVoteId")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "option",property = "option"),
            @Result( column = "vote_id",property = "voteId")
    })
    public List<VoteOptions> getByVoteId(Integer id);

    @Delete("<script> " +
            "delete from vote_options " +
            "where  vote_id in "+
            "<foreach collection=\"voteIds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\" > "+
            "#{item}"+
            "</foreach> " +
            "</script>" )
    Integer deleteOptionsMapperes(@Param("voteIds")List<Integer> voteIds);

    @Update("<script>" +
            "INSERT INTO vote_options(id,option,vote_id) "+
            "VALUES " +
            "<foreach collection=\"voteOptions\" index=\"index\" item=\"item\" separator=\",\"> "+
            "(#{item.id},#{item.option},#{item.voteId}) "+
            "</foreach> "+
            "ON DUPLICATE KEY UPDATE "+
            "option = VALUES(option) "+
            "</script>" )
    Integer updateVoteOptions(@Param("voteOptions") List<VoteOptions> voteOptions);

    @DeleteProvider(type = VoteOptionsMapperProvider.class,method = "deleteById")
    Integer deleteOptionById(Integer optionId);
}
