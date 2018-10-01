package com.bonaparte.controller;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bonaparte.dubboapi.ChargeMoney;

/**
 * Created by yangmingquan on 2018/10/1.
 */
@RestController
@RequestMapping(name = "/charge")
public class ChargeMoneyController {
    @Reference
    ChargeMoney chargeMoney;

    @RequestMapping(name = "/money/{uid}")
    public Double getMoney(@PathVariable Integer uid){
        chargeMoney.getChargeMoney(uid);
    }
}
