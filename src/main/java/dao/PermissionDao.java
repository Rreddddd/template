package dao;

import entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionDao {

    List<Permission> findAll();

    void deleteAll();

    void save(@Param("permissions") List<Permission> permissions);

    List<Permission> findWithNameAll();
}
