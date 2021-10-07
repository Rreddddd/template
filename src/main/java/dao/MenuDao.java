package dao;

import entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuDao {

    List<Menu> findByParentId(int parentId);

    void deleteAll();

    void save(@Param("menus") List<Menu> menus);

    List<Menu> findWithModuleAll();
}
