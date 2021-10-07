package service.impl;

import dao.MenuDao;
import dao.PermissionDao;
import entity.Menu;
import entity.Permission;
import org.springframework.stereotype.Service;
import service.MenuService;
import util.Menus;
import util.WebPathUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuDao menuDao;
    @Resource
    private PermissionDao permissionDao;

    @Override
    public List<Menu> findByParentId(int parentId) {
        return menuDao.findByParentId(parentId);
    }

    private List<Menu> findWithModuleAll() {
        return menuDao.findWithModuleAll();
    }

    @Override
    public void save(List<Menu> menus, List<Permission> permissions) {
        menuDao.deleteAll();
        menuDao.save(menus);
        permissionDao.deleteAll();
        if(permissions.size()>0){
            permissionDao.save(permissions);
        }
        initCache();
    }

    @Override
    public List<Map<String, Object>> getIconfont() {
        List<Map<String, Object>> fonts = new ArrayList<>();
        Map<String, Object> font = new HashMap<>();
        font.put("id", "-1");
        font.put("value", "无");
        fonts.add(font);
        String rootPath = WebPathUtil.getWebRootPath();
        rootPath = rootPath.substring(0, rootPath.lastIndexOf("/"));
        fonts.addAll(getIconfont(rootPath + File.separator + "global" + File.separator + "iconfont" + File.separator + "iconfont.css"));
        return fonts;
    }

    private List<Map<String, Object>> getIconfont(String fontPath) {
        List<Map<String, Object>> fonts = new ArrayList<>();
        try (InputStream is = new FileInputStream(fontPath);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                os.write(buffer, 0, len);
            }
            String classes = os.toString("utf-8");
            Pattern pattern = Pattern.compile("(?<=\\.)icon-[\\w,-]+(?!=before)");
            Matcher matcher = pattern.matcher(classes);
            Map<String, Object> font;
            while (matcher.find()) {
                font = new HashMap<>();
                String iconClass = "iconfont " + matcher.group();
                font.put("id", iconClass);
                font.put("value", iconClass);
                font.put("iconClass", iconClass);
                fonts.add(font);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fonts;
    }

    /**
     * 启动放入内存 在模块应用（modules）后
     */
    void initCache() {
        Menus.init(findWithModuleAll(), permissionDao.findWithNameAll());
    }
}
