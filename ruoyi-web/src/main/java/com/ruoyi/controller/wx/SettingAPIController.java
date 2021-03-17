package com.ruoyi.controller.wx;

import com.ruoyi.common.core.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序设置 - 设置接口类
 */
@Slf4j
@RestController
@RequestMapping
public class SettingAPIController {

    /**
     * 设置
     */
    @GetMapping("/api/setting")
    public R index() {
        Map<String, Object> topTextColor = new HashMap<>();
        topTextColor.put("text", "#ffffff");
        topTextColor.put("value", "20");

        Map<String, Object> wxapp = new HashMap<>();
        wxapp.put("top_background_color", "#00aa00");
        wxapp.put("wxapp_title", "小程序商城");

        wxapp.put("top_text_color", topTextColor);

        Map<String, Object> data = new HashMap<>();
        data.put("wxapp", wxapp);

        return R.ok(data);
    }

    @GetMapping("/api/help")
    public R help() {
        List<Map<String, Object>> helpItems = new ArrayList<>();

        Map<String, Object> item1 = new HashMap<>();
        item1.put("title", "测试");
        item1.put("content", "系统描述");
        helpItems.add(item1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("title", "客服电话");
        item2.put("content", "4004228888");
        helpItems.add(item2);

        Map<String, Object> data = new HashMap<>();
        data.put("list", helpItems);

        return R.data(data);

    }

}

