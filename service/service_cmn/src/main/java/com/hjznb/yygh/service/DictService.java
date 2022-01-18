package com.hjznb.yygh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjznb.yygh.model.cmn.Dict;

import java.util.List;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/3 11:34
 */
public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);
}
