//package com.icloud;
//
//import com.alibaba.fastjson.JSON;
//import com.icloud.common.ConfigUtil;
//import com.icloud.config.global.MyPropertitys;
//import com.icloud.thirdinterfaces.score.entity.LongChargeEntity;
//import com.icloud.thirdinterfaces.score.service.LongbiServiceImpl;
//import com.icloud.thirdinterfaces.score.utils.LongCoinUtil;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes= Application.class)
//public class LongCoinChargeTest {
//
//    @Autowired
//    private LongbiServiceImpl longbiServiceImpl;
//    @Autowired
//    private LongCoinUtil longCoinUtil;
//    @Autowired
//    private MyPropertitys myPropertitys;
//
//    @Test
//    public void apiTest(){
//        try {
//            LongChargeEntity entity = new LongChargeEntity();
//            entity.setSid(myPropertitys.getLongcoin().getSid());
//            entity.setKey(myPropertitys.getLongcoin().getKey());
//            entity.setSeq(longCoinUtil.getSerialNumber());
////            entity.setSid(myPropertitys.getLongcoin().getSid_signup());
////            entity.setKey(myPropertitys.getLongcoin().getKey_signup());
////            entity.setSeq(longCoinUtil.getSerialNumber_signup());
//            entity.setUseraccount("oREekjpYosWes5b16jyeSTmv3CcA");
//            entity.setAccounttype("2");
//            entity.setRechargetype(longCoinUtil.getRechargetype());
//            entity.setRechargeamount("1");
//            entity.setTimestamp(longCoinUtil.getTimeStamp());
//
//            System.out.println("LongQueryEntity=="+ JSON.toJSONString(entity));
//            longbiServiceImpl.recharge(entity.getRequestParamMap());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}
