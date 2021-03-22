package com.ruoyi.controller.dfs;

import com.google.common.collect.Lists;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.spring.SpringContextHolder;
import com.ruoyi.system.config.DfsConfig;
import com.ruoyi.system.service.IFileCloudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@RequestMapping({"dfs", "/api/dfs"})
public class FileController {

    @Autowired
    private DfsConfig dfsConfig;

    @Autowired
    private IFileCloudService fileCloudService;


    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("download")
    public void download(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.isValidFilename(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = dfsConfig.getPath() + fileName;
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("upload")
    public R upload(MultipartFile file) {
        try {
            // 上传文件路径
            String filePath = dfsConfig.getPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String active = SpringContextHolder.getActiveProfile();
            String url = "";
            if ("dev".equals(active)) {
                url = dfsConfig.getDomain() + fileName;
            } else if ("prod".equals(active)) {
                url = dfsConfig.getDomain() + "/api/" + fileName;
            }
            return R.ok().put("fileName", fileName).put("url", url);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("upload_file_list")
    public R upload_file_list(List<MultipartFile> files) {
        try {
            // 上传文件路径
            String filePath = dfsConfig.getPath();
            List<String> urls = Lists.newArrayList();
            // 上传并返回新文件名称
            for (MultipartFile file : files) {
                String fileName = FileUploadUtils.upload(filePath, file);
                String active = SpringContextHolder.getActiveProfile();
                String url = "";
                if ("dev".equals(active)) {
                    url = dfsConfig.getDomain() + fileName;
                } else if ("prod".equals(active)) {
                    url = dfsConfig.getDomain() + "/api/" + fileName;
                }
                urls.add(url);
            }
            return R.ok().put("errno", "0").put("data", urls);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("qiniu_upload")
    public R qiniu_upload(MultipartFile file) {
        Map<String, Object> fileInfo = fileCloudService.uploadMultipartFile(file);
        if (fileInfo != null) {
            return R.ok(fileInfo);
        } else {
            return R.error("上传失败！");
        }
    }
}