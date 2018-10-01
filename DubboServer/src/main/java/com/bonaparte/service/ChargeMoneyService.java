package com.bonaparte.service;

import com.bonaparte.dubboapi.ChargeMoney;
import org.springframework.stereotype.Service;

/**
 * Created by yangmingquan on 2018/10/1.
 */
@Service
public class ChargeMoneyService implements ChargeMoney{

    public double getChargeMoney(Integer uid){
        return 0;
    }
}
