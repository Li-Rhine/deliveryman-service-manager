package com.imooc.deliverymanservicemanager.po;

import com.imooc.deliverymanservicemanager.enummeration.DeliverymanStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 17:21
 **/

@Getter
@Setter
@ToString
public class DeliverymanPO {

    private Integer id;
    private String name;
    private String district;
    private DeliverymanStatus status;
    private Date date;
}
