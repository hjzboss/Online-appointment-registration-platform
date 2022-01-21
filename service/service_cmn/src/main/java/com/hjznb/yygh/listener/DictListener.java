package com.hjznb.yygh.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hjznb.yygh.mapper.DictMapper;
import com.hjznb.yygh.model.cmn.Dict;
import com.hjznb.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/19 18:23
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {
    private final DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    //一行一行读取,操作
    @Override
    public void invoke(DictEeVo data, AnalysisContext context) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(data, dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
