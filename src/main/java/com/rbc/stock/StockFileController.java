package com.rbc.stock;

import com.rbc.stock.model.LoadFile;
import com.rbc.stock.model.Stock;
import com.rbc.stock.service.StockFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

//@RestController
@Controller
//@CrossOrigin("*")
//@RequestMapping("file")

//@RequestMapping("/")
public class StockFileController {

    private static Logger logger = LoggerFactory.getLogger(StockFileController.class);

    @Autowired
    private StockFileService fileService;

    String result = Constants.SUCCESS;
    String status = null;
    String htmlPage = "success";

    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile file) throws IOException {
        logger.info("Entering the upload file function");


        status= fileService.addFile(file);

        return checkResult(status,htmlPage);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws IOException {
        LoadFile loadFile = fileService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.getFileType() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getFilename() + "\"")
                .body(new ByteArrayResource(loadFile.getFile()));
    }

    @GetMapping("/downloadZipFile")
    public void downloadAsZip(HttpServletResponse response) throws IOException {

        //Getting the time in milliseconds to create the zip file name
        Calendar calendar = Calendar.getInstance();
        String zipFileName = calendar.getTimeInMillis() + ".zip";

        //set headers to the response
        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFileName + "\"");

        //retrieve zip file to the response
        fileService.downloadFilesAsZip(response);

        //set status to OK
        response.setStatus(HttpServletResponse.SC_OK);

    }

    @PostMapping("/recordUpload")
    public String uploadRecord(@RequestParam("record") String record, HttpServletResponse response) throws IOException {
        logger.info("Entering the uploadRecord function");

         status = fileService.uploadSingleRecord(record);
        response.setStatus(HttpServletResponse.SC_OK);
        logger.info("Exiting the uploadRecord function");

        return checkResult(status,htmlPage);


    }

    private String checkResult(String status,String htmlPage) {
        logger.info("Entering the checkResult function");
        if(status.equalsIgnoreCase(result)) {
            return htmlPage;
        }else{
            return "error";
        }
    }

    @GetMapping("/searchRecords")
    public String searchRecord(@RequestParam("stockName") String stockName, Model model) throws IOException {
        logger.info("Entering the searchRecord function");

        System.out.println("Inside the stockName");
        List<Stock> stockList = fileService.searchRecords(stockName);
        model.addAttribute("stockList", stockList);
        logger.info("Exiting the searchRecord function");

        return "stock";


    }

}
