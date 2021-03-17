package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.IEnum;
import com.ruoyi.common.redis.util.RedisUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISmsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * Service - 短信
 *
 * @author jack
 */
@Log4j2
@Service
public class SmsServiceImpl implements ISmsService {

    @Autowired
    private RedisUtils redis;

    @Value("${aliyun.sms.domain}")
    private String domain;

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Value("${aliyun.sms.templateCode}")
    private String templateCode;


    /**
     * 短信类型
     */
    public enum SmsType implements IEnum<Integer> {
        /**
         * 会员登录
         */
        MEMBER_LOGIN("_MEMBER_LOGIN", 0),

        /**
         * 会员注册
         */
        MEMBER_REGISTER("_MEMBER_REGISTER", 1),

        /**
         * 忘记密码
         */
        FORGOT_PASSWORD("_FORGOT_PASSWORD", 2),

        /**
         * 重置密码
         */
        RESET_PASSWORD("_RESET_PASSWORD", 3),

        /**
         * 重置手机
         */
        RESET_MOBILE("_RESET_MOBILE", 4),

        /**
         * 服务申请
         */
        APPLY_SERVICE("_APPLY_SERVICE", 5);


        private String name;
        private int value;

        SmsType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }

    }

    /**
     * 发送会员注册短信
     *
     * @param mobile  手机
     * @param content 内容
     */
    @Override
    public String sendRegisterSms(String mobile, String content) {
        // 设置短信有效时间
        redis.set(mobile + SmsType.MEMBER_REGISTER.name, content, 300);
        // 发送短信
        // 测试坏境先不发真实短信
        return send(mobile, content);
//        return "OK";
    }

    /**
     * 发送忘记密码短信
     *
     * @param mobile  手机
     * @param content 内容
     */
    @Override
    public String sendForgotPasswordSms(String mobile, String content) {
        // 设置短信有效时间
        redis.set(mobile + SmsType.FORGOT_PASSWORD.name, content, 300);
        // 发送短信
        // 测试坏境先不发真实短信
        return send(mobile, content);
//        return "OK";
    }

    /**
     * 发送重置手机短信
     *
     * @param mobile
     *            手机
     * @param content
     * 	 		 内容
     * 	@return
     */
    @Override
    public String sendResetSms(String mobile, String content) {
        // 设置短信有效时间
        redis.set(mobile + SmsType.RESET_MOBILE.name, content, 300);
        // 发送短信
        // 测试坏境先不发真实短信
        return send(mobile, content);
//        return "OK";
    }

    /**
     * 发送登录手机短信
     *
     * @param mobile
     *            手机
     * @param content
     * 	 		 内容
     * 	@return
     */
    @Override
    public String sendLoginSms(String mobile, String content) {
        // 设置短信有效时间
        redis.set(mobile + SmsType.MEMBER_LOGIN.name, content, 60);
        // 发送短信
        // 测试坏境先不发真实短信
        return send(mobile, content);
//        return "OK";
    }

    @Override
    public String sendApplyServiceSms(String mobile, String content) {
        // 设置短信有效时间
        redis.set(mobile + SmsType.APPLY_SERVICE.name, content, 60);
        // 发送短信
        // 测试坏境先不发真实短信
        return send(mobile, content);
//        return "OK";
    }

    @Override
    public String send(String mobile, String content) {
        Assert.hasText(mobile, "[Assertion failed] - mobile must have text; it must not be null, empty, or blank");
        Assert.hasText(content, "[Assertion failed] - content must have text; it must not be null, empty, or blank");

        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysProtocol(ProtocolType.HTTPS);
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(domain);
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        //接受验证码的手机号
        request.putQueryParameter("PhoneNumbers", mobile);
        //签名
        request.putQueryParameter("SignName", signName);
        //模板代码
        request.putQueryParameter("TemplateCode", templateCode);
        //用户定义的验证码内容
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + content + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            String result = response.getData();
            log.info(result);
            JSONObject resultJson = JSONObject.parseObject(result);
            return resultJson.getString("Message");
        } catch (ServerException e) {
            log.error(e.getErrMsg());
        } catch (ClientException e) {
            log.error(e.getErrMsg());
        }
        return null;
    }


    /**
     * 验证短信有效性
     *
     * @param mobile
     * 			手机
     * @param captcha
     * 			验证码
     * @param smsType
     * 			验证类型
     * @return
     *
     */
    @Override
    public boolean verify(String mobile, String captcha, SmsType smsType) {
        String key = mobile + smsType.getName();
        // 检验
        String code = redis.get(key);
        if (StringUtils.equals(captcha, code)) {
            redis.delete(key);
            return true;
        }
        return false;
    }

}