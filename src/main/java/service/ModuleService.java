package service;

import entity.Module;
import pojo.TableData;

import java.util.List;

public interface ModuleService {

    List<Module> findAll();

    List<Module> findPage(int pageIndex, int pageSize);

    Integer findCount();

    void add(Module module);

    void update(Module module);

    void delete(int id);
}