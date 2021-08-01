package dao;

import entity.Module;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ModuleDao {

    Module findOne(int id);
}
