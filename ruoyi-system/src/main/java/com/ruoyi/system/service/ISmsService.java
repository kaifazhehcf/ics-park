package com.ruoyi.system.service;

import com.ruoyi.system.service.impl.SmsServiceImpl;

/**
 * Service - 短信
 *
 * @author jack
 */
public interface ISmsService {

	/**
	 * 发送短信(异步)
	 * 
	 * @param mobile
	 *            手机号码
	 * @param content
	 *            内容
	 * @return
	 */
	String send(String mobile, String content);


	/**
	 * 发送会员注册短信
	 *
	 * @param mobile
	 *            手机
	 * @param content
	 * 	 		 内容
	 * 	@return
	 */
	String sendRegisterSms(String mobile, String content);

	/**
	 * 发送会员注册短信
	 *
	 * @param mobile
	 *            手机
	 * @param content
	 * 	 		 内容
	 * 	@return
	 */
	String sendForgotPasswordSms(String mobile, String content);

	/**
	 * 发送重置手机短信
	 *
	 * @param mobile
	 *            手机
	 * @param content
	 * 	 		 内容
	 * 	@return
	 */
	String sendResetSms(String mobile, String content);

	/**
	 * 发送登录手机短信
	 *
	 * @param mobile
	 *            手机
	 * @param content
	 * 	 		 内容
	 * 	@return
	 */
	String sendLoginSms(String mobile, String content);

	/**
	 * 发送服务申请手机短信
	 *
	 * @param mobile
	 *            手机
	 * @param content
	 * 	 		 内容
	 * 	@return
	 */
	String sendApplyServiceSms(String mobile, String content);

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
	boolean verify(String mobile, String captcha, SmsServiceImpl.SmsType smsType);

}