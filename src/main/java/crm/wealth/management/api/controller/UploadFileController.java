package crm.wealth.management.api.controller;

import crm.wealth.management.service.AWSS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/file")
public class UploadFileController {

    @Autowired
    private AWSS3Service awsS3Service;

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String publicURL = awsS3Service.uploadFile(file);
        Map<String, String> response = new HashMap<>();
        response.put("publicURL", publicURL);
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<Map<String, String>>listFile(){
        Map<String, String > response = awsS3Service.listFiles();
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
}