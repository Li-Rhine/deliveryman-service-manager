package com.imooc.deliverymanservicemanager.dao;

import com.imooc.deliverymanservicemanager.enummeration.DeliverymanStatus;
import com.imooc.deliverymanservicemanager.po.DeliverymanPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 17:23
 **/
@Repository
@Mapper
public interface DeliverymanDao {

    @Select("SELECT id, name, status, date FROM deliveryman WHERE status = #{status}")
    List<DeliverymanPO> selectDeliverymanByStatus(DeliverymanStatus status);
}
