package util;

import entity.Permission;
import entity.PermissionId;
import entity.Position;
import entity.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Permissions {

    private static final Map<PermissionId, Set<Integer>> PERMISSION_MAP = new ConcurrentHashMap<>();

    public static void init(Collection<Permission> permissions) {
        PERMISSION_MAP.clear();
        putAll(permissions);
    }

    public static void put(Permission permission) {
        PERMISSION_MAP.computeIfAbsent(permission.getPid(), id -> new HashSet<>()).add(permission.getMenuId());
    }

    public static void putAll(Collection<Permission> permissions) {
        if (permissions != null && permissions.size() > 0) {
            for (Permission permission : permissions) {
                put(permission);
            }
        }
    }

    public static boolean check(User user, int menuId) {
        if (user == null) {
            return false;
        }
        Set<Integer> menuIds = PERMISSION_MAP.get(PermissionId.createUserId(user.getId()));
        if (menuIds != null && menuIds.contains(menuId)) {
            return true;
        }
        List<Position> positions = user.getPositions();
        if (positions == null || positions.size() == 0) {
            return false;
        }
        for (Position position : positions) {
            if (position.equals(Position.ADMIN_POSITION)) {//超级管理员特殊处理
                return true;
            }
            menuIds = PERMISSION_MAP.get(PermissionId.createPositionId(position.getId()));
            if (menuIds != null && menuIds.contains(menuId)) {
                return true;
            }
        }
        return false;
    }
}
