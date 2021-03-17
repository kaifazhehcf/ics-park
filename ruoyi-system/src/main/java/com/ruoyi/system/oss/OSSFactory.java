package com.ruoyi.system.oss;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.oss.CloudConstant.CloudService;
import com.ruoyi.system.service.IConfigService;

/**
 * 文件上传Factory
 *
 * @author jack
 */
public final class OSSFactory {
    private static IConfigService sysConfigService;

    static {
        OSSFactory.sysConfigService = (IConfigService) SpringUtils.getBean(IConfigService.class);
    }

    public static CloudStorageService build() {
        String jsonConfig = sysConfigService.selectConfigByKey(CloudConstant.CLOUD_STORAGE_CONFIG_KEY);
        // 获取云存储配置信息
        CloudStorageConfig config = JSON.parseObject(jsonConfig, CloudStorageConfig.class);
        if (config.getType() == CloudService.QINIU.getValue()) {
            return new QiniuCloudStorageService(config);
        } else if (config.getType() == CloudService.ALIYUN.getValue()) {
            return new AliyunCloudStorageService(config);
        } else if (config.getType() == CloudService.QCLOUD.getValue()) {
            return new QcloudCloudStorageService(config);
        }
        return null;
    }
}
