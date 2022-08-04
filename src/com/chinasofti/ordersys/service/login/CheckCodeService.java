package com.chinasofti.ordersys.service.login;

import com.chinasofti.ordersys.mapper.PhoneMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CheckCodeService {
    @Autowired
    PhoneMapper phoneMapper;

    public int updatecode(String code,Date now,String phoneNum){
       return phoneMapper.updateverifyinfoByPhoneNum(code,now,phoneNum);
    }
}
