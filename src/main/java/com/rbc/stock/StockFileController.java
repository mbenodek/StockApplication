package com.rbc.stock;

import com.rbc.stock.model.LoadFile;
import com.rbc.stock.model.Stock;
import com.rbc.stock.service.StockFileService;
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

    @Autowired
    private StockFileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file")MultipartFile file) throws IOException {
        return new ResponseEntity<>(fileService.addFile(file), HttpStatus.OK);
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
    public void uploadRecorded(@RequestParam("record") String record, HttpServletResponse response) throws IOException {
        System.out.println("Inside the uploadRecord");
        fileService.uploadSingleRecord(record);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    @GetMapping("/searchRecords")

    public String searchRecord(@RequestParam("stockName") String stockName, Model model) throws IOException {
        System.out.println("Inside the stockName");
        List<Stock> stockList = fileService.searchRecords(stockName);

        model.addAttribute("stockList", stockList);


      //  response.setStatus(HttpServletResponse.SC_OK);

        return "stock";


    }

}
