package dao;

import entity.Module;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModuleDao {

    List<Module> findAll();

    List<Module> findPage(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    Integer findCount();

    void add(Module module);

    void update(Module module);

    void delete(int id);
}
