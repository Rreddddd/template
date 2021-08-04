package controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.MsgResult;
import util.WebPathUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public MsgResult upload(@RequestParam("files") MultipartFile[] files) {
        MsgResult msgResult = new MsgResult();
        List<String> uris = new ArrayList<>();
        if (files.length == 0) {
            msgResult.setErrorCode(MsgResult.SUCCESS_CODE);
        } else {
            String tempDir;
            for (MultipartFile file : files) {
                tempDir = WebPathUtil.newTempDir() + File.separator + file.getResource().getFilename();
                try (InputStream inputStream = file.getInputStream();
                     OutputStream outputStream = new FileOutputStream(tempDir)) {
                    WebPathUtil.copyFile(inputStream, outputStream);
                    uris.add(WebPathUtil.convertToUri(WebPathUtil.getRelativePath(tempDir)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        msgResult.put("uris", uris);
        return msgResult;
    }

    @GetMapping("/file/{uri}")
    public ResponseEntity<byte[]> upload(@PathVariable("uri") String uri) throws IOException {
        File file = new File(WebPathUtil.ensureAbsolutePath(WebPathUtil.convertToUrl(uri)));
        try (InputStream inputStream = new FileInputStream(file);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            WebPathUtil.copyFile(inputStream, outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attchement;filename=" + file.getName());
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}