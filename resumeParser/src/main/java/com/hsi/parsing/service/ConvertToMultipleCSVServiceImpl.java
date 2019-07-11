package com.hsi.parsing.service;

import com.hsi.parsing.parsers.AbstractParser;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ConvertToMultipleCSVServiceImpl implements ConvertToMultipleCSVService {

	  @Autowired
	  AbstractParser docParser;
	  @Autowired
	  AbstractParser docxParser;
	  @Autowired
	  AbstractParser pdfParser;
	  
	  public void  convertToCSV(MultipartFile[] files) throws IOException {
		  
		
		  
		  for(MultipartFile file:files) {
				String fileName = file.getOriginalFilename();

				System.out.println(fileName);
				String ext = FilenameUtils.getExtension(fileName);
				System.out.println("extension " + ext);
				switch(ext) {
					case "doc":
						docParser.convert(file);
						break;
					case "docx":
						docxParser.convert(file);
						break;
					case "pdf":
						pdfParser.convert(file);
						break;
				}
					
				
	  
		  }
		

	    
	  }}


