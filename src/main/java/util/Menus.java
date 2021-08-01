package util;

import entity.Menu;
import entity.Permission;
import entity.Position;
import entity.User;
import pojo.Constant;

import java.util.*;

public abstract class Menus {

    private static final Map<Integer, InnerMenu> MENU_MAP = new HashMap<>();
    private static final MenuComparator COMPARATOR = new MenuComparator();

    public static void init(Collection<Menu> menus) {
        MENU_MAP.clear();
        MENU_MAP.put(-1, new InnerMenu(null));
        putAll(menus);
    }

    public static void put(Menu menu) {
        InnerMenu innerMenu = new InnerMenu(menu);
        int parentId = menu.getParentId();
        getInner(parentId).children.add(innerMenu);
        MENU_MAP.put(menu.getId(), innerMenu);
    }

    public static void putAll(Collection<Menu> menus) {
        if (menus != null && menus.size() > 0) {
            for (Menu menu : menus) {
                put(menu);
            }
        }
    }

    public static void update(Menu menu) {
        if (menu != null) {
            InnerMenu cacheMenu = MENU_MAP.get(menu.getId());
            if (cacheMenu != null) {
                synchronized (cacheMenu) {
                    Menu origin = cacheMenu.menu;
                    origin.setId(menu.getId());
                    origin.setTitle(menu.getTitle());
                    origin.setIconClass(menu.getIconClass());
                    origin.setIconColor(menu.getIconColor());
                    if (menu.getParentId() != origin.getParentId()) {
                        getInner(origin.getParentId()).children.remove(cacheMenu);
                        getInner(menu.getParentId()).children.add(cacheMenu);
                        origin.setParentId(menu.getParentId());
                    }
                    if (menu.getOrder() != origin.getOrder()) {
                        origin.setOrder(menu.getOrder());
                        getInner(menu.getParentId()).reSort();
                    }
                    origin.setModule(menu.getModule());
                }
            }
        }
    }

    public static void update(int id, int type, Collection<Permission> permissions) {
        InnerMenu innerMenu = getInner(id);
        if (innerMenu != null) {
            synchronized (innerMenu) {
                Set<Permission> innerPermissions = new HashSet<>();
                for (Permission permission : innerMenu.permissions) {
                    if (type != permission.getType()) {
                        innerPermissions.add(permission);
                    }
                }
                if (permissions != null && !permissions.isEmpty()) {
                    innerPermissions.addAll(permissions);
                }
                innerMenu.permissions = innerPermissions;
            }
        }
    }

    public static void remove(int id) {
        InnerMenu innerMenu = getInner(id);
        if (innerMenu != null) {
            getInner(innerMenu.menu.getParentId()).children.remove(MENU_MAP.remove(id));
        }
    }

    public static Menu get(int id) {
        InnerMenu innerMenu = getInner(id);
        return innerMenu == null ? null : innerMenu.menu;
    }

    private static InnerMenu getInner(int id) {
        return MENU_MAP.get(id);
    }

    public static Set<InnerMenu> getCurrentMenus() {
        User user = Context.getUser();
        if (user == null) {
            return new TreeSet<>(COMPARATOR);
        }
        Set<InnerMenu> innerMenus = getInner(-1).children;
        Position position = user.getPosition();
        if (position != null && position.getId() == 1) {//超级管理员特殊处理
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
        private Set<Permission> permissions;
        private Set<InnerMenu> children;

        private InnerMenu(Menu menu) {
            this.menu = menu;
            this.permissions = new HashSet<>();
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
            return new InnerMenu(menu.copy());
        }

        public boolean checkPermission(User user) {
            if (user == null) {
                return false;
            }
            if (permissions.contains(new Permission(user.getId(), Constant.PERMISSION_USER))) {
                return true;
            }
            Position position = user.getPosition();
            return position != null && permissions.contains(new Permission(position.getId(), Constant.PERMISSION_POSITION));
        }

        public Menu getMenu() {
            return menu;
        }

        public Set<Permission> getPermissions() {
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
