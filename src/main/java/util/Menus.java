package util;

import entity.*;

import java.util.*;

public abstract class Menus {

    private static final Map<Integer, InnerMenu> MENU_MAP = new HashMap<>();
    private static final Map<Integer, InnerMenu> MODULE_MENU_MAP = new HashMap<>();
    private static final MenuComparator COMPARATOR = new MenuComparator();

    public static void init(Collection<Menu> menus, List<Permission> permissions) {
        MENU_MAP.clear();
        MODULE_MENU_MAP.clear();
        MENU_MAP.put(-1, new InnerMenu(null, null));
        putAll(menus, permissions);
    }

    public static void put(Menu menu, List<Permission> permissions) {
        menu.setModule(menu.getModule() == null ? null : Modules.get(menu.getModule().getId()));
        InnerMenu innerMenu = new InnerMenu(menu, permissions);
        int parentId = menu.getParentId();
        getInner(parentId).children.add(innerMenu);
        MENU_MAP.put(menu.getId(), innerMenu);
        if (menu.getModule() != null) {
            MODULE_MENU_MAP.put(menu.getModule().getId(), innerMenu);
        }
    }

    public static void putAll(Collection<Menu> menus, List<Permission> permissions) {
        if (menus != null && menus.size() > 0) {
            Map<Integer, List<Permission>> map = new HashMap<>();
            List<Permission> menuPermissions;
            for (Permission permission : permissions) {
                menuPermissions = map.computeIfAbsent(permission.getMenuId(), (e) -> new ArrayList<>());
                menuPermissions.add(permission);
            }
            for (Menu menu : menus) {
                put(menu, map.get(menu.getId()));
            }
        }
    }

    public static Menu get(int id) {
        InnerMenu innerMenu = getInner(id);
        return innerMenu == null ? null : innerMenu.menu;
    }

    public static Menu getByModuleId(int moduleId) {
        InnerMenu innerMenu = getInnerByModuleId(moduleId);
        return innerMenu == null ? null : innerMenu.menu;
    }

    public static InnerMenu getInner(int id) {
        return MENU_MAP.get(id);
    }

    public static InnerMenu getInnerByModuleId(int moduleId) {
        return MODULE_MENU_MAP.get(moduleId);
    }

    public static InnerMenu getInnerTree() {
        return MENU_MAP.get(-1);
    }

    public static Set<InnerMenu> getCurrentMenus() {
        User user = Context.getUser();
        if (user == null) {
            return new TreeSet<>(COMPARATOR);
        }
        Set<InnerMenu> innerMenus = getInner(-1).children;
        List<Position> positions = user.getPositions();
        if (positions != null && positions.size() > 0 && positions.contains(Position.ADMIN_POSITION)) {//超级管理员特殊处理
            return innerMenus;
        }
        return getCurrentMenus(user, innerMenus);
    }

    private static Set<InnerMenu> getCurrentMenus(User user, Set<InnerMenu> innerMenus) {
        if (innerMenus.isEmpty()) {
            return new TreeSet<>(COMPARATOR);
        }
        Set<InnerMenu> userMenus = new TreeSet<>(COMPARATOR);
        InnerMenu userMenu;
        for (InnerMenu innerMenu : innerMenus) {
            Set<InnerMenu> children = innerMenu.children;
            if (children.isEmpty()) {
                if (innerMenu.menu.getModule() != null && innerMenu.checkPermission(user)) {
                    userMenus.add(innerMenu.copy());
                }
            } else {
                Set<InnerMenu> childMenus = getCurrentMenus(user, innerMenu.children);
                if (!childMenus.isEmpty()) {
                    userMenu = innerMenu.copy();
                    userMenu.children = childMenus;
                    userMenus.add(userMenu);
                }
            }
        }
        return userMenus;
    }

    public static class InnerMenu {

        private final Menu menu;
        private final List<Permission> permissions;
        private Set<InnerMenu> children;

        private InnerMenu(Menu menu, List<Permission> permissions) {
            this.menu = menu;
            this.permissions = permissions;
            reSort();
        }

        private void reSort() {
            if (children == null) {
                children = new TreeSet<>(COMPARATOR);
            } else if (!children.isEmpty()) {
                children = new TreeSet<>(children);
            }
        }

        private InnerMenu copy() {
            return new InnerMenu(menu == null ? null : (Menu) menu.clone(), permissions == null ? null : new ArrayList<>(permissions));
        }

        public boolean checkPermission(User user) {
            if (user == null || menu == null) {
                return false;
            }
            if (user.getPositions() != null && user.getPositions().contains(Position.ADMIN_POSITION)) {
                return true;
            }
            if (permissions != null && permissions.size() > 0) {
                List<Position> positions;
                for (Permission permission : permissions) {
                    if (permission.getType() == Permission.USER_TYPE && permission.getAuthId() == user.getId()) {
                        return true;
                    }
                    positions = user.getPositions();
                    if (positions != null && positions.size() > 0) {
                        for (Position position : positions) {
                            if (permission.getType() == Permission.POSITION_TYPE && permission.getAuthId() == position.getId()) {
                                return true;
                            }
                        }
                    }
                }
            }
            return getInner(menu.getParentId()).checkPermission(user);
        }

        public Menu getMenu() {
            return menu;
        }

        public List<Permission> getPermissions() {
            return permissions;
        }

        public Set<InnerMenu> getChildren() {
            return children;
        }
    }

    private static class MenuComparator implements Comparator<InnerMenu> {

        @Override
        public int compare(InnerMenu o1, InnerMenu o2) {
            if (o1.menu.getOrder() < o2.menu.getOrder()) {
                return -1;
            } else if (o1.menu.getOrder() > o2.menu.getOrder()) {
                return 1;
            }
            return 0;
        }
    }
}
