package com.dlq.yygh.cmn.controller;

import com.dlq.yygh.cmn.service.DictService;
import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-11 17:09
 */
@Api(tags = "字典管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictConroller {

    @Autowired
    private DictService dictService;

    //导入数据字典接口
    @PostMapping("/importData")
    public Result importData(MultipartFile file){
        dictService.importData(file);
        return Result.ok();
    }

    //导出数据字典接口
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
        dictService.exportDictData(response);
    }

    //根据数据id查询子数据列表
    @GetMapping("/findChildData/{id}")
    public Result<List<Dict>> findChlidData(@PathVariable("id") Long id){
        List<Dict> list= dictService.findChlidData(id);
        return Result.ok(list);
    }

    //根据dictcode和value查询
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode")String dictCode,@PathVariable("value")String value){
        String dictName = dictService.getDictName(dictCode,value);
        return dictName;
    }

    //根据value查询
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable("value")String value){
        String dictName = dictService.getDictName("",value);
        return dictName;
    }

    /**
     * 根据dictCode获取下级节点
     */
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public Result<List<Dict>> findByDictCode(@PathVariable("dictCode")String dictCode){
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }
}
