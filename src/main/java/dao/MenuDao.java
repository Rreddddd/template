package dao;

import entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuDao {

    List<Menu> findByParentId(int parentId);
}
