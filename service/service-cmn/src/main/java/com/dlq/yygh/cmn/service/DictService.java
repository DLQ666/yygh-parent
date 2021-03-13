package com.dlq.yygh.cmn.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-10 15:33
 */
public interface DictService extends IService<Dict> {

    /**
     * //根据数据id查询子数据列表
     * @param id
     * @return
     */
    List<Dict> findChlidData(Long id);

    void exportDictData(HttpServletResponse response);

    void importData(MultipartFile file);

    String getDictName(String dictCode, String value);

    /**
     * 根据dictCode获取下级节点
     */
    List<Dict> findByDictCode(String dictCode);
}
