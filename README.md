## 项目简介
 基于spring mytabis 构建本项目,指在学习mybatis的高级功能。下列均有所涉及：
 * spring 提供框架管理功能。
 * mybatis 提供数据访问框架
 * mybatis采用注解方式使用
 * mybatis支持动态sql，例如更新对象数据时，只有有效的数据，才会进行更新，且通过注解的方式完成
 * mybatis支持对象与表的映射，且表的字段名字与对象的属性不相同
 * mytatis支持表间一对一，一对多，多对多查询。

 ## 相关注解
 * @Once : 表间一对一关联
 * @Many : 表间一对多关联
 * @SqlProvider : 动态自定义sql

 ## 关键代码
 ###　dao
 ``` bash
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
 ```
其中，@Many指定的查询，在另一个文件中实现。
``` bash
public interface UserProfileDao {
	@Select("select uu.user_id as id, p.type as type from app_user_user_profile uu,user_profile p,app_user au where uu.user_profile_id=p.id and uu.user_id=#{user_id} and au.state='Active' and au.id=uu.user_id")
	List<UserProfile> getUserProfiles(@Param("user_id")int user_id);
}
```

自定义sql,包括：
* 自定义查询
* 动态语句
``` bash
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
	
	// 如果传递的是LIST类型的参数，则必须使用MAP,如下所示
	public String  insertJifensSql(Map map){
        List<Jifen> jifens = (List<Jifen>) map.get("list");
        StringBuffer sb = new StringBuffer();

        sb.append("INSERT INTO `ysyy_user_jifen_bak`( `user_id`, `user_type`, `jf_type`, `jf_par1`, `jf_par2`, `jf_time`, `jf_value`, `jf_total`,`r_id`) ");
        sb.append("VALUES ");

        for (Jifen jifen:
            jifens) {
            sb.append("(");
            sb.append(jifen.getStrings());
            sb.append("),");
        }

        String sql =  sb.substring(0,sb.length()-1).toString();

        logger.info("=====================>" + sql);

        return sql;
    }
}
```


## 关于坑
自定义注解时，如批处理，则必须使用MAP，否则会报找反射异常。必须按如下操作。
org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.builder.BuilderException: Error invoking SqlProvider method 
本项目没有xml配置文件，全部以注解的方式，进行配置注入。

#### 项目参考：[www.websystique.com](http://www.websystique.com)
#### 个人blog: [wiki.niejinkun.com](http://wiki.niejinkun.com)
