package com.ws.word.search;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ws.word.search.exception.ExceptionBO;
import com.ws.word.search.exception.ValidationBO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.xml.bind.annotation.XmlRootElement;


@RestController
@RequestMapping("/search")
@Api(value = "Search", description = "Operations pertaining to search")
@XmlRootElement(name = "SEARCHRESULT")
@ApiModel(value = "SEARCHRESULT")
public class WordSearchServiceController {
	
/*	@ApiModelProperty(required = true, name = "MATCHED-FILES")
	@JsonProperty("MATCHED-FILES")*/
	//List<String> result = new ArrayList<String>();
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(WordSearchServiceController.class);
		@CrossOrigin
		@ApiOperation(value = "Search single word in the files and return the matched files", response = WordSearchServiceController.class)
		@ApiResponses(value = {
		@ApiResponse(code = 200, message = "List of files matched", response = WordSearchServiceController.class),
		@ApiResponse(code = 500, message = "Error Fetching", response = ExceptionBO.class),
		@ApiResponse(code = 400, message = "Validation Failed", response = ValidationBO.class) })
	    @RequestMapping(value= "/restservice/wordsearch/v1/searchFilesWithSingleKeyword", method = RequestMethod.GET, produces = "application/json")
	    public @ResponseBody List<String> searchFilesWithSingleKeyword(
	    @ApiParam(value = "Please provide the Base file Path/floder location where the search will begin", required = false) @RequestParam(value="basefilePath", required=false) String basefilePath,
	    @ApiParam(value = "Please provide search word to begin the search. The search will be on single keyword", required = false) @RequestParam(value="pattern", required=false) String pattern) throws Exception{
			 
			 logger.info("searchFilesWithSingleKeyword :: basefilePath :: "+ basefilePath);
			 logger.info("searchFilesWithSingleKeyword :: pattern :: "+ pattern);
			 

			 List<String> result = new ArrayList<String>();
			 
			 File file = new File(basefilePath);
			 
				try {
					result = searchFiles(file,pattern);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
	    }
	    
		@CrossOrigin
		@ApiOperation(value = "Search words in the files and return the matched files", response = WordSearchServiceController.class)
		@ApiResponses(value = {
		@ApiResponse(code = 200, message = "List of files matched", response = WordSearchServiceController.class),
		@ApiResponse(code = 500, message = "Error Fetching", response = ExceptionBO.class),
		@ApiResponse(code = 400, message = "Validation Failed", response = ValidationBO.class) })
	    @RequestMapping(value= "/restservice/wordsearch/v1/searchFilesWithMultipleKeyword", method = RequestMethod.GET, produces = "application/json")
	    public @ResponseBody List<String> searchFilesWithMultipleKeyword(
	    @ApiParam(value = "Please provide the Base file Path/floder location where the search will begin", required = false) @RequestParam(value="basefilePath", required=false) String basefilePath,
	    @ApiParam(value = "Please send search words. Multiple words should be placed one after another deliminated by comma", required = false) @RequestParam(value="pattern", required=false) List<String> multipattern) throws Exception{
			 
			 logger.info("searchFilesWithSingleKeyword :: basefilePath :: "+ basefilePath);
			 logger.info("searchFilesWithSingleKeyword :: pattern :: "+ multipattern);
			 
			 List<String> result = new ArrayList<String>();
			 List<String> intermresult = new ArrayList<String>();
			 
			 File file = new File(basefilePath);
			 for(int i=0;i<multipattern.size();i++)
			 {
				 String pattern=multipattern.get(i);
			 
				try {
					intermresult = searchFiles(file,pattern);
					for(int a=0;a<intermresult.size();a++)
					{
						String filename = intermresult.get(a);
						result.add(filename);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			 }
				return result;
	    }
	
	
	    
		private List<String> searchFiles(File file, String pattern) throws Exception  {
			List<String> fileList = new ArrayList<String>();
			List<String> intermresult = new ArrayList<String>();

		    File[] files = file.listFiles();

		    if (files != null) {
		        for (File currentFile : files) {
		            if (currentFile.isDirectory()) {
		            	System.out.println("Current Directory : "+currentFile);
		            	intermresult= searchFiles(currentFile, pattern);
		            	for(int i=0;i<intermresult.size();i++)
		            	{
		            		String Name=intermresult.get(i);
		            		fileList.add(Name);
		            	}
		                System.out.println("Current Directory Result"+ searchFiles(currentFile, pattern));
		            } else {
		                Scanner scanner = new Scanner(currentFile);
		                if (scanner.findWithinHorizon(pattern, 0) != null) {
		                	fileList.add(currentFile.getName());
		                }
		                scanner.close();
		            }
		        }
		    }
		    return fileList;
		}
	

}
