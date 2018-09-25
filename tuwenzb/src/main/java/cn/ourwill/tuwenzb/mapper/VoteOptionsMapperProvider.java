package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.entity.VoteOptions;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class VoteOptionsMapperProvider {
	private String colums="id,userFrom_type,user_type,username,nickname,password,salt,avatar,mob_phone,tel_phone,wechat_num,email,qq,company,address,license_type,due_date,remaining_days,pack_years_days,photo_license_type,photo_due_date,photo_remaining_days,photo_pack_years_days,bound_id, " +
			"c_id,c_time,u_id,u_time,level,refresh_token,refresh_token_c_time";
	private String simpleColums="id,userFrom_type,user_type,username,nickname,password,salt,avatar,mob_phone,tel_phone,wechat_num,email,qq,company,address,license_type,due_date,remaining_days,pack_years_days,photo_license_type,photo_due_date,photo_remaining_days,photo_pack_years_days,bound_id, " +
			"c_id,c_time,u_id,u_time,level";


	//保存
	public String save(final VoteOptions voteOptions){
		return new SQL(){
			{
				INSERT_INTO("vote_options");
				if(voteOptions.getOption()!=null){
					VALUES("option", "#{option}");
				}
				if(voteOptions.getVoteId()!=null){
					VALUES("vote_id", "#{voteId}");
				}
			}
		}.toString();
	}

	//保存
	public String saveList(@Param("voteOptions")List<VoteOptions> voteOptions){

			String sql = "	INSERT INTO vote_options (option,vote_id) VALUES ";
			for(VoteOptions vo :voteOptions ){
				sql = sql+"('"+vo.getOption()+"',"+vo.getVoteId().toString()+"),";
			}
			return sql.substring(0,sql.length()-1);
	}

	//根据voteId查找
	public String selectByVoteId(Integer voteId){
		return new SQL(){
			{
				SELECT("id,option,vote_id");
				FROM("vote_options");
				WHERE("vote_id=#{voteId}");
			}
		}.toString();
	}

	//根据id删除
	public String deleteById(Integer id){
		return new SQL(){
			{
				DELETE_FROM("vote_options");
				WHERE("id=#{id}");
			}
		}.toString();
	}

}
