package util;

import entity.Module;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Modules {

    private static final Map<Integer, Module> MODULE_MAP = new ConcurrentHashMap<>();

    public static void init(Collection<Module> modules) {
        MODULE_MAP.clear();
        putAll(modules);
    }

    public static void put(Module module) {
        MODULE_MAP.put(module.getId(), module);
    }

    public static void putAll(Collection<Module> modules) {
        if (modules != null && modules.size() > 0) {
            for (Module user : modules) {
                put(user);
            }
        }
    }

    public static void update(Module module) {
        if (module != null) {
            Module cacheModule = MODULE_MAP.get(module.getId());
            if (cacheModule != null) {
                synchronized (cacheModule) {
                    cacheModule.setTitle(module.getTitle());
                    cacheModule.setUrl(module.getUrl());
                }
            }
        }
    }

    public static void remove(int id) {
        MODULE_MAP.remove(id);
    }

    public static Module get(int id) {
        return MODULE_MAP.get(id);
    }
}
