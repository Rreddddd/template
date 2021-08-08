package dao;

import entity.Module;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ModuleDao {

    List<Module> findAll();
}
