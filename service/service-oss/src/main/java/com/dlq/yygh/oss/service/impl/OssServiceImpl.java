package com.dlq.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.dlq.yygh.oss.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-16 21:32
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String secretId;

    @Override
    public void removeLogo(String url) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, secretId);
        System.out.println(url);
        try {
            //https://dlq-mi-mall.oss-cn-beijing.aliyuncs.com/2020-10-13/b1371eb7-9008-4267-8b0b-4cbea8eb4e2e_AMD.jpg
            log.info("传入路径：{}", url);
            if (!StringUtils.isEmpty(url)) {
                String host = "https://" + bucket + "." + endpoint + "/";
                log.info("去除路径:{}", host);
                log.info("去除路径length:{}", host.length());
                String objectName = url.substring(host.length());
                log.info("截取的url：{}", objectName);
                // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
                ossClient.deleteObject(bucket, objectName);
            }
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            log.info(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public String fileupload(MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();

            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, secretId);
            //首先判断bucket存在不，如果不存在则创建
            if (!ossClient.doesBucketExist(bucket)) {
                ossClient.createBucket(bucket);
                ossClient.setBucketAcl(bucket, CannedAccessControlList.PublicRead);
            }

            //构建objectName：文件路径 2020/06/17/46348b9e0caa4e838d0325101a792941file.png
            //把文件按照日期进行分类存储
            //获取当前日期
            String floder = new DateTime().toString("yyyy/MM/dd");
            //拼接
            String fileName = UUID.randomUUID().toString();
            String module = "zhengjian";
            String fileExtesion = originalFilename.substring(originalFilename.lastIndexOf("."));
            String key = module + "/" + floder + "/" + fileName + fileExtesion;

            //调用oss方法实现上传
            //参数1：bucket名称  参数2：上传到oss文件路径和名称  例:/aa/bb/1.jpg  参数3：上传文件输入流
            ossClient.putObject(bucket, key, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //返回url https://dlqedu-01.oss-cn-beijing.aliyuncs.com/2020/06/17/46348b9e0caa4e838d0325101a792941file.png
            return "https://" + bucket + "." + endpoint + "/" + key;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
