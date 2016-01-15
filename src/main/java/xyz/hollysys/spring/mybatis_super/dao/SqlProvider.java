package xyz.hollysys.spring.mybatis_super.dao;

import org.apache.ibatis.jdbc.SQL;
import org.apache.log4j.Logger;

import xyz.hollysys.spring.mybatis_super.model.User;

public class SqlProvider {
	static Logger logger = Logger.getLogger(SqlProvider.class);
	public String selectUser(int id){
		String sql = "select * from app_user where id=" + id;
		logger.info(sql);
		return sql;
	}
	
	public String updateUser(final User user){
		String sql =  new SQL() {
            {
                UPDATE("app_user");
                if (user.getEmail() != null) {
                    SET("email = #{email}");
                }
                if (user.getFirst_name() != null) {
                    SET("first_name = #{first_name}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
		
        logger.info(sql);
        
		return sql;
	}
}
