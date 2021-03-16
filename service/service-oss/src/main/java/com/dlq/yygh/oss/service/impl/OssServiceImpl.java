package com.dlq.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.dlq.yygh.oss.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}
