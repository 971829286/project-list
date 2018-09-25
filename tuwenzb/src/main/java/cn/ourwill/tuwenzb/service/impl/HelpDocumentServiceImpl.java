package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.HelpDocument;
import cn.ourwill.tuwenzb.mapper.HelpDocumentMapper;
import cn.ourwill.tuwenzb.service.IHelpDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelpDocumentServiceImpl extends BaseServiceImpl<HelpDocument> implements IHelpDocumentService {

    @Autowired
    private HelpDocumentMapper helpDocumentMapper;

    //保存
    public Integer save(HelpDocument helpDocument) {
        return helpDocumentMapper.save(helpDocument);
    }

    //修改
    public Integer update(HelpDocument helpDocument) {
        return helpDocumentMapper.update(helpDocument);
    }

    //根据id查找对象
    public HelpDocument getById(Integer id) {
        return helpDocumentMapper.getById(id);
    }

    //查找所有
    public List<HelpDocument> findAll() {
        return helpDocumentMapper.findAll();
    }

    //删除多条记录
    public Integer deleteBatch(List<Integer> ids){return helpDocumentMapper.deleteBatch(ids);}

}
