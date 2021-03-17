package com.dlq.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-16 21:11
 */
public interface OssService {
    void removeLogo(String url);

    String fileupload(MultipartFile file);
}
