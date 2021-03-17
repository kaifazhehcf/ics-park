package com.ruoyi.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.SignatureUtils;
import com.ruoyi.system.service.IConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

/**
 * 认证过滤器
 *
 * @author jack
 */
@Slf4j
public class AuthFilter implements Filter {

    /**
     * 排除过滤的 uri 地址
     */
    private static final String[] whiteList = {
            "/auth/login", "/user/register", "/user/check_mobile", "/user/send_mobile",
            "/user/search_customer", "/user/check_code",
            "/password/send_mobile", "/password/forgot",
            "/captcha/check", "/captcha/get", "/auth/login/slide", "/auth/sms",
            "/user/register/sms", "/user/register/submit", "/admin/park/list",
            "/api/setting", "/api/wx_auth/register", "/wx/login","/park/list",
            "/activity/list","/dj/list","/dj/bannerList","/policy/list","/policy/bannerList",
            "/product/list","/product/newProducts","/product/getProduct","/product/getProductCategoryList","/product/selectParentIdList",
            "/product/getProductChildNodeList","/product/search","/productBanner/imagesList",
            "/productBanner/imagesList","/promotion/list","/promotion/hotList",
            "/room/list","/room/detail",
            "/manage/list",
            "/menu/list","/menu/bannerList",
            "/supplier/list"
    };

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> ops;
    @Autowired
    private IConfigService configService;

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(req);

        String url = req.getRequestURI();
        log.info("url:{}", url);


        // 跳过不需要验证的路径
        if (Arrays.asList(whiteList).contains(url) || url.startsWith(Constants.RESOURCE_PREFIX)) {
            chain.doFilter(mutableRequest, response);
            return;
        }

        // 系统配置Key
        /*String appKeyConfig = configService.selectConfigByKey("sys.setting.appKey");
        if (StringUtils.isEmpty(appKeyConfig)) {
            setUnauthorizedResponse(response, "app or key is not configured");
            return;
        }
        JSONObject appKey = JSON.parseObject(appKeyConfig);
        String appId = StringUtils.trim(appKey.getString("appId"));
        String key = StringUtils.trim(appKey.getString("key"));

        // 验证sign
        String sign = req.getHeader("sign");
        if (StringUtils.isEmpty(sign)) {
            setUnauthorizedResponse(response, "sign can't null or empty string");
            return;
        }

        // 比较sign
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("appId", appId);
        if (!SignatureUtils.checkSign(paramMap, key, sign)) {
            setUnauthorizedResponse(response, "sign verify error");
            return;
        }*/

        // token为空
        String token = req.getHeader("token");
        if (StringUtils.isBlank(token)) {
            setUnauthorizedResponse(response, "token can't null or empty string");
            return;
        }

        String userStr = ops.get(Constants.ACCESS_TOKEN + token);
        if (StringUtils.isBlank(userStr)) {
            setUnauthorizedResponse(response, "token verify error");
            return;
        }

        JSONObject jo = JSONObject.parseObject(userStr);
        String userId = jo.getString("userId");
        String parkId = jo.getString("parkId");
        // 查询token信息
        if (StringUtils.isBlank(userId)) {
            setUnauthorizedResponse(response, "token verify error");
            return;
        }

        mutableRequest.putHeader(Constants.CURRENT_ID, userId);
        mutableRequest.putHeader(Constants.PARK_ID, parkId);
        mutableRequest.putHeader(Constants.CURRENT_USERNAME, jo.getString("loginName"));
        chain.doFilter(mutableRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    /**
     * 返回到前端信息
     *
     * @param servletResponse
     * @param msg
     * @throws IOException
     */
    private void setUnauthorizedResponse(ServletResponse servletResponse, String msg) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        OutputStream outputStream = response.getOutputStream();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        byte[] responseByte = null;
        try {
            responseByte = JSON.toJSONString(R.error(401, msg)).getBytes(Constants.UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outputStream.write(responseByte);
    }

}