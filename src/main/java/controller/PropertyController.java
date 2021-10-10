package controller;

import entity.Property;
import entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pojo.MsgResult;
import service.PropertyService;
import util.Context;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common")
public class PropertyController {

    @Resource
    private PropertyService propertyService;

    @PostMapping("/updateProperty")
    public MsgResult updateProperty(@RequestParam("key") String key, @RequestParam(value = "value", defaultValue = "") String value) {
        try {
            User user = Context.getUser();
            Property property = new Property();
            assert user != null;
            property.setTargetId(user.getId());
            property.setKey(key);
            property.setValue(value);
            propertyService.addOrUpdateValue(property);
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }
}
