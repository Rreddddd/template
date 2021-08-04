package util;

import org.springframework.security.core.token.Sha512DigestUtils;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class WebPathUtil {

    private static final String WEB_ROOT_PATH;
    private static final String FILE_STORE = "file_store";
    private static final String TEMP_DIR = "temp";
    private static final String FILE_DIR = "file";
    private static final String DIR_SEPARATOR = File.separator;
    private static final String URI_SEPARATOR = "/";
    private static final String URI_PREFIX = URI_SEPARATOR + "file";

    static {
        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        path = path.substring(0, path.lastIndexOf("/"));
        WEB_ROOT_PATH = path;
    }

    public static String getWebRootPath() {
        return WEB_ROOT_PATH;
    }

    public static String getFileStorePath() {
        return getWebRootPath() + DIR_SEPARATOR + FILE_STORE;
    }

    public static String getFilePath() {
        return getFileStorePath() + DIR_SEPARATOR + FILE_DIR;
    }

    public static String getTempPath() {
        return getFileStorePath() + DIR_SEPARATOR + TEMP_DIR;
    }

    public static String newDir(String rootDir, boolean mkdirs, String... tiers) {
        if (tiers == null || tiers.length == 0) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DATE);
            tiers = new String[2];
            tiers[0] = year + "_" + (month < 10 ? "0" + month : month) + "_" + (day < 10 ? "0" + day : day);
            tiers[1] = UUID.randomUUID().toString();
        }
        rootDir += DIR_SEPARATOR + String.join(DIR_SEPARATOR, tiers);
        if (mkdirs) {
            File file = new File(rootDir);
            if (!file.mkdirs()) {
                throw new RuntimeException("文件路径创建失败");
            }
        }
        return rootDir;
    }

    public static String newTempDir() {
        return newTempDir(true);
    }

    public static String newTempDir(boolean mkdirs, String... tiers) {
        return newDir(getTempPath(), mkdirs, tiers);
    }

    public static String newFileDir() {
        return newFileDir(true);
    }

    public static String newFileDir(boolean mkdirs, String... tiers) {
        return newDir(getFilePath(), mkdirs, tiers);
    }

    public static String getFileName(String path) {
        if (path == null) {
            return "";
        }
        return path.substring(path.lastIndexOf(DIR_SEPARATOR) + 1);
    }

    public static String copyTempToFile(String tempPath) {
        String filePath = newFileDir() + DIR_SEPARATOR + getFileName(tempPath);
        copyFile(tempPath, filePath);
        return filePath;
    }

    public static void copyFile(String source, String destination) {
        if (StringUtils.isBlank(source)) {
            throw new RuntimeException("原文件路径不能为空");
        }
        if (StringUtils.isBlank(destination)) {
            throw new RuntimeException("目的地路径不能为空");
        }
        File sourceFile = new File(source);
        File destinationFile = new File(destination);
        try (InputStream inputStream = new FileInputStream(sourceFile);
             OutputStream outputStream = new FileOutputStream(destinationFile)) {
            copyFile(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        copyFile(inputStream, outputStream, null);
    }

    public static void copyFile(InputStream inputStream, OutputStream outputStream, byte[] buffer) throws IOException {
        if (inputStream == null || outputStream == null) {
            return;
        }
        if (buffer == null || buffer.length == 0) {
            buffer = new byte[1024 * 1024];
        }
        int len;
        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
    }

    public static String convertToUri(String url) {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("url不能为空");
        }
        HttpSession session = Context.getSession();
        if (session == null) {
            throw new RuntimeException("context is null");
        }
        String key = Sha512DigestUtils.shaHex(url + "_" + session.getId());
        session.setAttribute(key, url);
        return URI_PREFIX + URI_SEPARATOR + key;
    }

    public static String convertToUrl(String uri) {
        if (StringUtils.isBlank(uri)) {
            throw new RuntimeException("uri不能为空");
        }
        HttpSession session = Context.getSession();
        if (session == null) {
            throw new RuntimeException("context is null");
        }
        if (uri.startsWith(URI_PREFIX)) {
            uri = uri.substring(URI_PREFIX.length() + 1);
        }
        Object url = session.getAttribute(uri);
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("uri解析失败");
        }
        return url.toString();
    }

    public static String getRelativePath(String absolutePath) {
        return absolutePath.substring(WEB_ROOT_PATH.length() + 1);
    }

    public static String getAbsolutePath(String relativePath) {
        return WEB_ROOT_PATH + DIR_SEPARATOR + relativePath;
    }

    public static String ensureRelativePath(String path) {
        if (path.startsWith(WEB_ROOT_PATH)) {
            path = getRelativePath(path);
        }
        return path;
    }

    public static String ensureAbsolutePath(String path) {
        if (!path.startsWith(WEB_ROOT_PATH)) {
            path = getAbsolutePath(path);
        }
        return path;
    }
}