package com.ruoyi.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * 七牛云上传（第三方上传服务）
 *
 * @author zzm
 * @date 2021-02-26
 */
public interface IFileCloudService {

     /**
      * 流文件上传
      * @param file
      * @return
      */
     Map<String, Object> uploadMultipartFile(MultipartFile file);

     /**
      * 文件上传
      * @param file
      * @return
      */
     Map<String, Object> uploadFile(File file);

     /**
      * 根据http文件连接下载
      * @param urlPath
      * @return
      */
     File downloadFile(String urlPath);


}
