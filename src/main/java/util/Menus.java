package util;

import entity.*;
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
        private Set<InnerMenu> children;

        private InnerMenu(Menu menu) {
            this.menu = menu;
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
            return new InnerMenu((Menu) menu.clone());
        }

        public boolean checkPermission(User user) {
            if (user == null || menu == null) {
                return false;
            }
            if (Permissions.check(user, menu.getId())) {
                return true;
            }
            return getInner(menu.getParentId()).checkPermission(user);
        }

        public Menu getMenu() {
            return menu;
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
