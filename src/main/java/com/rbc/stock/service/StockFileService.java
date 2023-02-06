package com.rbc.stock.service;

import com.rbc.stock.Constants;
import com.rbc.stock.StockFileController;
import com.rbc.stock.model.LoadFile;
import com.rbc.stock.repository.UploadFileRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.rbc.stock.model.Stock;
import org.apache.commons.io.IOUtils;
import org.bson.BsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Service
public class StockFileService {

    private static Logger logger = LoggerFactory.getLogger(StockFileService.class);

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;


    @Autowired
    private UploadFileRepository uploadFileRepository;

    String result = Constants.SUCCESS;

    public String addFile(MultipartFile file) throws IOException {

        logger.info("Entering the addFile function with file-->"+file.getName());

        //define additional metadata
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", file.getSize());

        /******Start of logic to upload the contents into the database ******/

        BufferedReader fileReader = new BufferedReader(new
                InputStreamReader(file.getInputStream(), "UTF-8"));

        List<Stock> stockLists = new ArrayList<Stock>();
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.EXCEL.withHeader());

        Iterable<CSVRecord> csvRecords = csvParser.getRecords();


        for (CSVRecord csvRecord : csvRecords) {
            Stock stock = new Stock(
                    csvRecord.get("quarter"),
                    csvRecord.get("stock"),
                    csvRecord.get("date"),
                    csvRecord.get("open"),
                    csvRecord.get("high"),
                    csvRecord.get("low"),
                    csvRecord.get("close"),
                    csvRecord.get("volume"),
                    csvRecord.get("percent_change_price"),
                    csvRecord.get("percent_change_volume_over_last_wk"),
                    csvRecord.get("previous_weeks_volume"),
                    csvRecord.get("next_weeks_open"),
                    csvRecord.get("next_weeks_close"),
                    csvRecord.get("percent_change_next_weeks_price"),
                    csvRecord.get("days_to_next_dividend"),
                    csvRecord.get("percent_return_next_dividend")


            );

            stockLists.add(stock);

        }

        //End of Logic for reading the file

        logger.info("The number of records added are =>"+stockLists.size());
        uploadFileRepository.saveAll(stockLists);


        logger.info("Exiting the addFile function");



        return result;




    }

    public LoadFile downloadFile(String id) throws IOException {

        //search file
        GridFSFile gridFSFile = template.findOne( new Query(Criteria.where("_id").is(id)) );


        //convert uri to byteArray
        //save data to LoadFile class
        LoadFile loadFile = new LoadFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setFilename( gridFSFile.getFilename() );

            loadFile.setFileType( gridFSFile.getMetadata().get("_contentType").toString() );

            loadFile.setFileSize( gridFSFile.getMetadata().get("fileSize").toString() );

            loadFile.setFile( IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()) );
        }

        return loadFile;
    }

    public void downloadFilesAsZip(HttpServletResponse response) throws IOException {

        //get all files in db
        List<GridFSFile> fileList = new ArrayList<>();
        template.find(new Query()).into(fileList);

        //if fileList is not empty, loop through the list
        if (fileList.size() > 0) {

            //create a zip file
            ZipOutputStream zipOutputStream  = new ZipOutputStream(response.getOutputStream());

            for (GridFSFile gridFSFile : fileList) {
                //file id is returning as a bson value
                BsonValue bsonValue = gridFSFile.getId();
                String file_id = String.valueOf(bsonValue.asObjectId().getValue());

                //find and retrieve file (using previous download method)
                LoadFile file = downloadFile(file_id);

                //add file to the zip file entry
                ZipEntry zipEntry = new ZipEntry(file.getFilename());
                zipEntry.setSize(Long.parseLong(file.getFileSize()));

                zipOutputStream.putNextEntry(zipEntry);

                ByteArrayResource fileResource = new ByteArrayResource(file.getFile());
                StreamUtils.copy(fileResource.getInputStream(), zipOutputStream);

                zipOutputStream.closeEntry();
            }

            zipOutputStream.finish();
            zipOutputStream.close();
        }

    }


    public List<Stock> searchRecords(String stockName){

        logger.info("Entering the searchRecords function for stock =>"+stockName);
        List<Stock> stockList = uploadFileRepository.findByStock(stockName);
        logger.info("Entering the Exiting function for stock =>"+stockName);
        return stockList;
    }


    public String uploadSingleRecord(String record) throws IOException {
        logger.info("Entering the uploadSingleRecord function");
        try{
            List<String> recordStr = Arrays.asList(record);
            List<Stock> list = recordStr.stream()
                    .map(Stock::new)
                    .collect(Collectors.toList());
            uploadFileRepository.saveAll(list);
        }catch (Exception e){

        logger.warn("An exception has occurred while inserting the record ");
e. printStackTrace();
        return Constants.FAILURE;

        }
        logger.info("Exiting the uploadSingleRecord function");
        return result;


    }
}
