package com.hjznb.yygh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjznb.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/3 11:34
 */
public interface DictService extends IService<Dict> {
    //根据数据id查询子数据列表
    List<Dict> findChildData(Long id);

    //导出数据字典
    void exportDictData(HttpServletResponse response);

    //导入数据字典
    void importDictData(MultipartFile file);

    //根据dictcode和value查询
    String getDictName(String dictCode, String value);
}
