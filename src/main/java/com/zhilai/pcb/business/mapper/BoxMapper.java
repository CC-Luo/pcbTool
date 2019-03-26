package com.zhilai.pcb.business.mapper;

import com.zhilai.pcb.business.entity.Box;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BoxMapper {
    List<Box> findByObj(Box box);
    int update(Box box);
    int insert(Box box);
    int deleteAll();
    int deleteBox(String macAddr);
    int findCountByMac(String macAddr);
    //int resetBox();
}
