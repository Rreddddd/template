package service.impl;

import dao.PropertyDao;
import entity.Property;
import entity.User;
import org.springframework.stereotype.Service;
import pojo.Constant;
import service.PropertyService;
import util.Context;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Resource
    private PropertyDao propertyDao;

    private static PropertyServiceImpl instance;

    public static PropertyServiceImpl getInstance() {
        return instance;
    }

    @Override
    public void add(Property property) {
        propertyDao.add(property);
        afterChange(property);
    }

    @Override
    public void updateValue(Property property) {
        propertyDao.updateValue(property);
        afterChange(property);
    }

    @Override
    public void addOrUpdateValue(Property property) {
        Property oldProperty = get(property.getTargetId(), property.getKey());
        if (oldProperty == null) {
            add(property);
        } else {
            updateValue(property);
        }
    }

    @Override
    public Property get(String key) {
        User user = Context.getUser();
        assert user != null;
        return propertyDao.get(user.getId(), key);
    }

    @Override
    public Property get(int targetId, String key) {
        return propertyDao.get(targetId, key);
    }

    @Override
    public Property getImLeaveMsg() {
        Property property = get(Constant.PROPERTY_IM_LEAVE_MSG);
        if (property == null) {
            property = get(Property.ADMIN_TARGET_ID, Constant.PROPERTY_DEFAULT_LEAVE_MSG);
        }
        return property;
    }

    /**
     * 改变属性值后对一些持久化变量更新
     *
     * @param property 属性
     */
    private void afterChange(Property property) {
        switch (property.getKey()) {
            //消息通知
            case Constant.PROPERTY_BROADCAST_IM:

                break;
            //聊天离开状态自动回复信息
            case Constant.PROPERTY_IM_LEAVE_MSG:

                break;
        }
    }

    @PostConstruct
    private void cacheInstance() {
        instance = this;
    }
}
