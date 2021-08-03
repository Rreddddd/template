package controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pojo.MsgResult;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public MsgResult upload(@RequestParam("files") MultipartFile[] files){
        MsgResult msgResult = new MsgResult();
        return msgResult;
    }
}
