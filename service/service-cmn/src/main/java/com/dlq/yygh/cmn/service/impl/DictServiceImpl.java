package com.dlq.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dlq.yygh.cmn.listener.DictListener;
import com.dlq.yygh.cmn.mapper.DictMapper;
import com.dlq.yygh.cmn.service.DictService;
import com.dlq.yygh.model.cmn.Dict;
import com.dlq.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-11 17:03
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    /**
     * //根据数据id查询子数据列表
     * @param id
     * @return
     */
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChlidData(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(queryWrapper);
        boolean children = isChildren(id);
        dictList.forEach(dict -> {
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        });
        return dictList;
    }

    //导出数据字典接口
    @Override
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

        //查询数据库--获得信息
        List<Dict> dictList = baseMapper.selectList(null);
        ArrayList<DictEeVo> dictEeVos = new ArrayList<>(dictList.size());
        dictList.forEach(dict -> {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo);
            dictEeVos.add(dictEeVo);
        });

        //调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictEeVos);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * //导入数据字典接口
     * @param file
     */
    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        //如果dictCode为空，直接根据value查询
        if (StringUtils.isEmpty(dictCode)){
            //直接根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else {
            //如果dictCode不为空，直接根据dictCode和value查询
            Dict codeDict = this.getDictCodeByDictCode(dictCode);
            Long parent_id = codeDict.getId();
            Dict selectOne = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parent_id).eq("value", value));
            return selectOne.getName();
        }
    }

    /**
     * 根据dictCode获取下级节点
     */
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictCode获取对应id
        Dict dict = this.getDictCodeByDictCode(dictCode);
        if (dict == null){
            return null;
        }
        //获取子节点
        List<Dict> chlidData = this.findChlidData(dict.getId());
        if (chlidData != null){
            return chlidData;
        }
        return null;
    }

    private Dict getDictCodeByDictCode(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        return baseMapper.selectOne(wrapper);
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
