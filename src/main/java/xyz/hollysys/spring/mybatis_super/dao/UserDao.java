package xyz.hollysys.spring.mybatis_super.dao;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import xyz.hollysys.spring.mybatis_super.model.User;

//@Repository("userDao")
public interface UserDao {
	// 一对多示例
	@Select("select * from app_user where id=#{id}")
	@Results(value = {
			 @Result(id = true, column = "id", property = "id"),  
            @Result(property="userProfiles", javaType=List.class, column="id",
                   many=@Many(select="xyz.hollysys.spring.mybatis_super.dao.UserProfileDao.getUserProfiles"))
            })
	User findById(@Param("id") int id);
	
	@Select("select * from app_user where sso_id=#{sso_id}")
	@Results(value = {
			 @Result(id = true, column = "id", property = "id")
	})
	User findBySSO(@Param("sso_id")String sso_id);
	
	@SelectProvider(type = SqlProvider.class, method = "selectUser")
	@Results(value = {
            @Result(property="userProfiles", javaType=List.class, column="id",
                   many=@Many(select="xyz.hollysys.spring.mybatis_super.dao.UserProfileDao.getUserProfiles"))
            })
	User getUser(int id);
	
	@SelectProvider(type = SqlProvider.class, method = "updateUser")
	void update(User user);
}

