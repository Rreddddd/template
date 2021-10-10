package dao;

import entity.Property;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyDao {

    void add(Property property);

    void updateValue(Property property);

    Property get(@Param("targetId") int targetId, @Param("key") String key);
}
