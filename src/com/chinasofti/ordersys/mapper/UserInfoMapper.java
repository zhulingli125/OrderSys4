package com.chinasofti.ordersys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.Mapper;

import com.chinasofti.ordersys.vo.UserInfo;
@Mapper
public interface UserInfoMapper {

	@Select("select userId,userAccount,userPass,locked,roleId,roleName,faceimg from userinfo,roleinfo where userinfo.role=roleinfo.roleId order by userId")
	public List<UserInfo> getAllUser();

	@Insert("insert into userinfo(userAccount,userPass,role,faceImg) values(#{info.userAccount},#{info.userPass},#{info.roleId},#{info.faceimg})")
	@Options(useGeneratedKeys = true, keyProperty = "info.userId")
	public Integer addUser(@Param("info") UserInfo user);

	@Select("select userId,userAccount,userPass,locked,roleId,roleName,faceimg from userinfo,roleinfo where userinfo.role=roleinfo.roleId order by userId limit #{first},#{max}")
	public List<UserInfo> getUserByPage(@Param("first") int first,
			@Param("max") int max);

	@Select("select count(*) from userinfo")
	public Long getMaxPage();

	@Delete("delete from userinfo where userId=#{userId}")
	public void deleteUser(@Param("userId") Integer userId);

	@Update("update userinfo set userPass=#{info.userPass},faceimg=#{info.faceimg} where userId=#{info.userId}")
	public void modify(@Param("info") UserInfo info);

	@Update("update userinfo set userPass=#{info.userPass},faceimg=#{info.faceimg},role=#{info.roleId} where userId=#{info.userId}")
	public void adminModify(@Param("info") UserInfo info);

	@Select("select userId,userAccount,userPass,locked,roleId,roleName,faceimg from userinfo,roleinfo where userinfo.role=roleinfo.roleId and userId=#{userId}")
	public UserInfo getUserById(@Param("userId") Integer userId);

	@Select("select userId,userAccount,userPass,locked,roleId,roleName from userinfo,roleinfo where userinfo.role=roleinfo.roleId and userinfo.userId=#{info.userId}")
	public List<UserInfo> checkPass(@Param("info") UserInfo info);
	
	@Select("select userId,userAccount,userPass,locked,roleId,roleName,faceimg from userinfo,roleinfo where userinfo.role=roleinfo.roleId and userinfo.userAccount=#{userAccount}")
	public List<UserInfo> findUsersByName(@Param("userAccount")String userAccount);

}
