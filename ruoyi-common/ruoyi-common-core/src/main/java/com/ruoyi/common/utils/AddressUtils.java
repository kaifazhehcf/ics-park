package com.ruoyi.common.utils;

import cn.hutool.http.HttpUtil;
import com.ruoyi.common.json.JSON;
import com.ruoyi.common.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取地址类
 *
 * @author ruoyi
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    public static final String IP_URL = "http://ip-api.com/json/%s?lang=zh-CN";

    public static String getRealAddressByIP(String ip) {
        String address = "XX XX";
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }

        String rspStr = HttpUtil.get(String.format(IP_URL, ip));
        if (StringUtils.isEmpty(rspStr)) {
            log.error("获取地理位置异常 {}", ip);
            return address;
        }
        JSONObject obj;
        try {
            obj = JSON.unmarshal(rspStr, JSONObject.class);
            address = obj.getStr("country") + "," + obj.getStr("regionName") + "," + obj.getStr("city");
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", ip);
        }
        return address;
    }

}
