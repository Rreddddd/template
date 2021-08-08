package util;

import entity.Module;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Modules {

    private static final Map<Integer, Module> MODULE_MAP = new ConcurrentHashMap<>();
    private static final Map<String, Integer> URL_ID_MAP = new ConcurrentHashMap<>();

    public static void init(Collection<Module> modules) {
        MODULE_MAP.clear();
        URL_ID_MAP.clear();
        putAll(modules);
    }

    public static void put(Module module) {
        MODULE_MAP.put(module.getId(), module);
        URL_ID_MAP.put(module.getUrl(), module.getId());
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
                    URL_ID_MAP.remove(cacheModule.getUrl());
                    cacheModule.setTitle(module.getTitle());
                    cacheModule.setUrl(module.getUrl());
                    URL_ID_MAP.put(module.getUrl(), module.getId());
                }
            }
        }
    }

    public static void remove(int id) {
        Module cacheModule = MODULE_MAP.remove(id);
        if (cacheModule != null) {
            URL_ID_MAP.remove(cacheModule.getUrl());
        }
    }

    public static Module get(int id) {
        return MODULE_MAP.get(id);
    }

    public static Module get(String url) {
        Integer id = URL_ID_MAP.get(url);
        if (id == null) {
            return null;
        }
        return get(id);
    }
}
