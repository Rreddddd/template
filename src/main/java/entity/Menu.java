package entity;

public class Menu {

    private Integer id;
    private int parentId;
    private Module module;
    private String title;
    private String iconClass;
    private String iconColor;
    private int order;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Menu copy() {
        Menu newMenu = new Menu();
        newMenu.setId(id);
        newMenu.setParentId(parentId);
        if (module != null) {
            Module newModule = new Module();
            newModule.setId(module.getId());
            newModule.setTitle(module.getTitle());
            newModule.setUrl(module.getUrl());
            newMenu.setModule(newModule);
        }
        newMenu.setTitle(title);
        newMenu.setIconClass(iconClass);
        newMenu.setIconColor(iconColor);
        newMenu.setOrder(order);
        return newMenu;
    }
}
