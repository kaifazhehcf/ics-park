package com.ruoyi.system.service.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.ruoyi.system.service.IFileCloudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * 七牛云上传（第三方上传服务）
 *
 * @author zzm
 * @date 2021-02-26
 */
@Slf4j
@Service
public class FileCloudServiceImpl implements IFileCloudService {


    @Value("${qiNiu.accessKey}")
    private String accessKey;

    @Value("${qiNiu.secretKey}")
    private String secretKey;

    @Value("${qiNiu.bucketName}")
    private String bucketName;

    @Value("${qiNiu.imageUrl}")
    private String imageUrl;

    /**
     * 流文件上传
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> uploadMultipartFile(MultipartFile file) {
        Map<String, Object> fileInfo = Maps.newHashMap();
        try {
            Configuration cfg = new Configuration(Region.region2());
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucketName);
            // 创建上传对象
            UploadManager uploadManager = new UploadManager(cfg);
            byte[] byteArr = file.getBytes();
            InputStream inputStream = new ByteArrayInputStream(byteArr);
            try {
                Response response = uploadManager.put(inputStream, null, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                fileInfo.put("fileName", putRet.key);
                fileInfo.put("url", imageUrl + putRet.key);
                return fileInfo;
            } catch (QiniuException ex) {
                Response r = ex.response;
                log.error(r.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> uploadFile(File file) {
        Map<String, Object> fileInfo = Maps.newHashMap();
        try {
            Configuration cfg = new Configuration(Region.region2());
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucketName);
            // 创建上传对象
            UploadManager uploadManager = new UploadManager(cfg);

            InputStream inputStream = new FileInputStream(file);
            try {
                Response response = uploadManager.put(inputStream, null, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                fileInfo.put("fileName", putRet.key);
                fileInfo.put("url", imageUrl + putRet.key);
                return fileInfo;
            } catch (QiniuException ex) {
                Response r = ex.response;
                log.error(r.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据http文件连接下载
     * @param urlPath
     * @return
     */
    @Override
    public File downloadFile(String urlPath) {
        File file = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000*5);
            //设置请求方式，默认是GET
            //httpURLConnection.setRequestMethod("POST");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            // 建立链接从请求中获取数据
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            // 创建临时文件
            file = File.createTempFile("tmp", ".png");

            OutputStream out = new FileOutputStream(file);
            int size = 0;
            byte[] buf = new byte[2048];
            while ((size = bin.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
            // 关闭资源
            bin.close();
            out.close();
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            return file;
        }
    }
}
