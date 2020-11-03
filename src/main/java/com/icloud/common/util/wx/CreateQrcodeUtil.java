package com.icloud.common.util.wx;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ImageUtil;
import com.alibaba.fastjson.JSONObject;
import com.icloud.common.AppContext;
import com.icloud.common.util.AccessTokenAndJsapiTicketUtil;
import com.icloud.config.global.MyPropertitys;
import com.icloud.modules.wx.entity.WxUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

@Component
public class CreateQrcodeUtil {

    private final static Logger logger = LoggerFactory.getLogger(CreateQrcodeUtil.class);
    @Autowired
    private AccessTokenAndJsapiTicketUtil accessTokenAndJsapiTicketUtil;
    @Autowired
    private MyPropertitys myPropertitys;
    /**
     * 获取带场景值二维码
     * @param scene_id
     * @return
     */
    public byte[] createQrcode(String scene_id){
        //获取token
        String token = accessTokenAndJsapiTicketUtil.getAccessToken();
        JSONObject postData = new JSONObject();
        postData.put("expire_seconds", 2592000);
        postData.put("action_name", "QR_STR_SCENE");

        JSONObject action_info = new JSONObject();
        JSONObject scene = new JSONObject();
        scene.put("scene_str",scene_id);
        action_info.put("scene",scene);
        postData.put("action_info", action_info);

        logger.info("创建临时二维码参数:"+postData.toJSONString());
        //获取二维码
         JSONObject qrcodeJson = HttpRequestUtil.httpRequest(WxConst.qrcode_create.replace("TOKEN", token), "POST", postData.toJSONString());
        //获取图片流
        String ticket = qrcodeJson.getString("ticket");
        byte[] bytes = HttpRequestUtil.httpRequestByteArr(WxConst.qrcode_show.replace("TICKET", ticket),"GET",null);
        return bytes;
    }


    /**
     * 生成明片
     * @param senceId
     * @param nickName
     * @return
     * @throws IOException
     */
    public   String createMasterCard(String senceId,String nickName) throws IOException {
        Font fontNick  = new Font("宋体", Font.BOLD, 30);
        //昵称
        BufferedImage loadImage = ImageUtil.pressText(
                ImageIO.read(FileUtil.file("card.jpg")), "我是"+nickName, Color.WHITE,
                fontNick,
                -16, -15, 0.8f);

        //过期时间
       /* Font fontDate  = new Font("宋体", Font.BOLD, 18);
        loadImage = ImageUtil.pressText(
                loadImage, dateStr, Color.WHITE,
                fontDate,
                -16, 544, 0.8f);*/

        byte[] bytes = this.createQrcode(senceId);
        InputStream is = new ByteArrayInputStream(bytes);
        String basePath = File.separator+"card"+File.separator;
        String cardName = senceId+".jpg";
        if(!FileUtil.exist(myPropertitys.getCdnPath()+basePath)){
            FileUtil.mkdir(myPropertitys.getCdnPath()+basePath);
        }
        String userCardPath = myPropertitys.getCdnPath()+basePath+cardName;
        logger.info("名片路径:"+userCardPath);

        Image m = ImageUtil.scale(ImageIO.read(is), 0.7f);//压缩尺寸
        ImageUtil.pressImage(loadImage, FileUtil.file(userCardPath),  m, -8, 192, 0.8f);
        return myPropertitys.getCdnPath()+basePath+cardName;
    }

    /**
     * 生成明片
     * @return
     * @throws IOException
     */
    public String createMasterCard(WxUser user) throws IOException {
        Font fontNick  = new Font("宋体", Font.BOLD, 30);
        //昵称
        logger.info("card.jpgpath==="+AppContext.baseDirectory()+"/card.jpg");
        BufferedImage loadImage = ImageUtil.pressText(
                ImageIO.read(FileUtil.file(AppContext.baseDirectory()+"/card.jpg")), "我是"+user.getNickname(), Color.WHITE,
                fontNick,
                -16, -18, 0.8f);

        //过期时间
        StringBuffer dateStr = new StringBuffer("推多多名片有效期:");

        Calendar ca = Calendar.getInstance();
        ca.setTime(user.getRecommendUrlTime());

        dateStr.append(DateUtil.format(ca.getTime(), "yyyy年MM月dd日")).append("-");

        ca.set(Calendar.DAY_OF_MONTH, ca.get(Calendar.DAY_OF_MONTH)+30);

        dateStr.append(DateUtil.format(ca.getTime(), "yyyy年MM月dd日"));

        Font fontDate  = new Font("宋体", Font.BOLD, 18);
        loadImage = ImageUtil.pressText(
                loadImage, dateStr.toString(), Color.red,
                fontDate,
                -16, 544, 0.8f);

        byte[] bytes = this.createQrcode(user.getTddCode());
        InputStream is = new ByteArrayInputStream(bytes);
        String basePath = File.separator+"card"+File.separator;
        String cardName = user.getTddCode()+".jpg";
        if(!FileUtil.exist(myPropertitys.getCdnPath()+basePath)){
            FileUtil.mkdir(myPropertitys.getCdnPath()+basePath);
        }
        String userCardPath = myPropertitys.getCdnPath()+basePath+cardName;
        logger.info("名片路径:"+userCardPath);

        Image m = ImageUtil.scale(ImageIO.read(is), 0.7f);//压缩尺寸
        ImageUtil.pressImage(loadImage, FileUtil.file(userCardPath),  m, -8, 192, 0.8f);
        return myPropertitys.getCdnPath()+basePath+cardName;
    }


}
