package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.HelpDocument;

import java.util.List;

public interface IHelpDocumentService extends IBaseService<HelpDocument> {

    //保存
    public Integer save(HelpDocument helpDocument);
    //修改
    public Integer update(HelpDocument helpDocument);
    //根据id查找对象
    public HelpDocument getById(Integer id);
    //查找所有
    public List<HelpDocument> findAll();
    //按照多条ID删除
    public Integer deleteBatch(List<Integer> ids);

}
