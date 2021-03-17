package com.ruoyi.system.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.exception.file.OssException;
import com.ruoyi.common.utils.ValidatorUtils;
import com.ruoyi.system.auth.annotation.HasPermissions;
import com.ruoyi.system.domain.Oss;
import com.ruoyi.system.oss.CloudConstant;
import com.ruoyi.system.oss.CloudConstant.CloudService;
import com.ruoyi.system.oss.CloudStorageConfig;
import com.ruoyi.system.oss.CloudStorageService;
import com.ruoyi.system.oss.OSSFactory;
import com.ruoyi.system.oss.valdator.AliyunGroup;
import com.ruoyi.system.oss.valdator.QcloudGroup;
import com.ruoyi.system.oss.valdator.QiniuGroup;
import com.ruoyi.system.service.IConfigService;
import com.ruoyi.system.service.IOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * 文件上传 提供者
 *
 * @author zmr
 * @date 2019-05-16
 */
@RestController
@RequestMapping("system/oss")
public class OssController extends BaseController {

    private final static String KEY = CloudConstant.CLOUD_STORAGE_CONFIG_KEY;

    @Autowired
    private IOssService ossService;

    @Autowired
    private IConfigService configService;

    /**
     * 云存储配置信息
     */
    @RequestMapping("config")
    @HasPermissions("sys:oss:config")
    public CloudStorageConfig config() {
        String jsonConfig = configService.selectConfigByKey(CloudConstant.CLOUD_STORAGE_CONFIG_KEY);
        // 获取云存储配置信息
        CloudStorageConfig config = JSON.parseObject(jsonConfig, CloudStorageConfig.class);
        return config;
    }

    /**
     * 保存云存储配置信息
     */
    @RequestMapping("saveConfig")
    @HasPermissions("sys:oss:config")
    public R saveConfig(CloudStorageConfig config) {
        // 校验类型
        ValidatorUtils.validateEntity(config);
        if (config.getType() == CloudService.QINIU.getValue()) {
            // 校验七牛数据
            ValidatorUtils.validateEntity(config, QiniuGroup.class);
        } else if (config.getType() == CloudService.ALIYUN.getValue()) {
            // 校验阿里云数据
            ValidatorUtils.validateEntity(config, AliyunGroup.class);
        } else if (config.getType() == CloudService.QCLOUD.getValue()) {
            // 校验腾讯云数据
            ValidatorUtils.validateEntity(config, QcloudGroup.class);
        }
        return toAjax(configService.updateValueByKey(KEY, new Gson().toJson(config)));
    }

    /**
     * 查询文件上传
     */
    @GetMapping("get/{id}")
    public Oss get(@PathVariable("id") Long id) {
        return ossService.selectSysOssById(id);
    }

    /**
     * 查询文件上传列表
     */
    @GetMapping("list")
    public R list(Oss oss) {
        startPage();
        return result(ossService.selectSysOssList(oss));
    }

    /**
     * 修改
     */
    @PostMapping("update")
    @HasPermissions("sys:oss:edit")
    public R editSave(@RequestBody Oss oss) {
        return toAjax(ossService.updateSysOss(oss));
    }

    /**
     * 修改保存文件上传
     *
     * @throws IOException
     */
    @PostMapping("upload")
    @HasPermissions("sys:oss:add")
    public R editSave(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new OssException("上传文件不能为空");
        }
        // 上传文件
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        CloudStorageService storage = OSSFactory.build();
        String url = storage.uploadSuffix(file.getBytes(), suffix);
        // 保存文件信息
        Oss ossEntity = new Oss();
        ossEntity.setUrl(url);
        ossEntity.setFileSuffix(suffix);
        ossEntity.setCreateBy(getLoginName());
        ossEntity.setFileName(fileName);
        ossEntity.setCreateTime(new Date());
        ossEntity.setService(storage.getService());
        return toAjax(ossService.insertSysOss(ossEntity));
    }

    /**
     * 删除文件上传
     */
    @PostMapping("remove")
    @HasPermissions("sys:oss:remove")
    public R remove(String ids) {
        return toAjax(ossService.deleteSysOssByIds(ids));
    }
}
