package com.hjznb.yygh.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjznb.yygh.listener.DictListener;
import com.hjznb.yygh.mapper.DictMapper;
import com.hjznb.yygh.model.cmn.Dict;
import com.hjznb.yygh.service.DictService;
import com.hjznb.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/15 16:44
 */

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {



    //根据数据id查询子数据列表
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        //向list集合每个dict对象中设置hasChildren
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return dictList;
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

    //导出数据字典接口
    @Override
    @CacheEvict(value = "value", allEntries = true)
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        //Content-Disposition就是当用户想把请求所得的内容存为一个文件的时候提供一个默认的文件名
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        //查询数据库
        List<Dict> dictList = baseMapper.selectList(null);
        //Dict --> DictEeVo
        ArrayList<DictEeVo> dictEeVos = new ArrayList<>();
        for (Dict dict : dictList) {
            DictEeVo vo = new DictEeVo();
            BeanUtils.copyProperties(dict, vo);
            dictEeVos.add(vo);
        }

        //调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class)
                    .sheet("dict")
                    .doWrite(dictEeVos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //导入数据字典
    @Override
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper))
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
