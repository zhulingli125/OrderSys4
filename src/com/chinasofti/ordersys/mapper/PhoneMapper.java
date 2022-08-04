package com.chinasofti.ordersys.mapper;

import com.chinasofti.ordersys.vo.DishesInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.Mapper;


import java.util.Date;

@Mapper
public interface PhoneMapper {
    @Select("update verifyinfo set code=#{code},date=now() where phoneNum=#{phoneNum}")
    public int updateverifyinfoByPhoneNum(@Param("code") String code, @Param("now") Date now ,@Param("phoneNum") String phoneNum);
}
