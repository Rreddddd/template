package service;

import entity.Property;

public interface PropertyService {

    void add(Property property);

    void updateValue(Property property);

    void addOrUpdateValue(Property property);

    Property get(String key);

    Property get(int targetId, String key);

    Property getImLeaveMsg();
}
