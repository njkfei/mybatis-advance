package xyz.hollysys.spring.mybatis_super.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import xyz.hollysys.spring.mybatis_super.model.UserProfile;

//@Repository("userProfileDao")
public interface UserProfileDao {
	
	//@Select("select * from user_profile where id=#{user_id}")
	@Select("select uu.user_id as id, p.type as type from app_user_user_profile uu,user_profile p,app_user au where uu.user_profile_id=p.id and uu.user_id=#{user_id} and au.state='Active' and au.id=uu.user_id")
/*	 @Results(value = {  
	   @Result(property="id",column="id",javaType=Integer.class,jdbcType=JdbcType.INTEGER),  
	   @Result(property="type", column="type",javaType=String.class,jdbcType=JdbcType.VARCHAR)
	 }) */
	List<UserProfile> getUserProfiles(@Param("user_id")int user_id);
}

