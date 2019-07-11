package com.hsi.parsing.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.SingletonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hsi.parsing.Util.ResumeParserConstant;
import com.hsi.parsing.exception.ResumeParsingException;
import com.hsi.parsing.model.CandidateDetails;
import com.hsi.parsing.service.CSVReaderServiceImpl;
import com.hsi.parsing.service.ConvertToMultipleCSVService;

@CrossOrigin
@RestController
@RequestMapping("/v1/")
public class ResumeParserController {

	@Autowired
	CSVReaderServiceImpl csvReader;

	@Autowired
	ConvertToMultipleCSVService convertToMultipleCSV;
	
	
	static Logger logger = Logger.getLogger(ResumeParserController.class.getName());

	@PostMapping(value = ("/convertToCSV"))
	public ResponseEntity<Map> convertToCSVFormat(@RequestParam("File") MultipartFile[] files,
			HttpServletResponse response) throws IOException {
		try {
			logger.info(ResumeParserConstant.LOG_FOR_ENTRY_FOR_CONVERTCSVFORMAT_METHOD);
			
			convertToMultipleCSV.convertToCSV(files);
			return new ResponseEntity<>(new SingletonMap("successMessage",ResumeParserConstant.SUCCESS_MSG_FOR_CSV_CREATE), HttpStatus.OK);
		} catch (ResumeParsingException e) {
			return new ResponseEntity(new SingletonMap("errorMessage",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	

	@GetMapping(value = ("/getCandidateDetails"))
	public ResponseEntity<List<CandidateDetails>> getCandidateDetailsnew() throws IOException {
		try {
			logger.info(ResumeParserConstant.LOG_FOR_ENTRY_FOR_GETCANDIDATEDETAILS_METHOD);
			List<CandidateDetails> candidateDetailsFromCSVFile = csvReader.getCandidateDetailsFromCSVFile();
			return new ResponseEntity<>(candidateDetailsFromCSVFile, HttpStatus.OK);
		} catch (ResumeParsingException e) {
			return new ResponseEntity(new SingletonMap("errorMessage",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}