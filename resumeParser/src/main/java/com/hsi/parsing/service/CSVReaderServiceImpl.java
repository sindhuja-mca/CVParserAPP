package com.hsi.parsing.service;

import com.hsi.parsing.exception.ResumeParsingException;
import com.hsi.parsing.model.CandidateDetails;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CSVReaderServiceImpl implements CSVReaderService {
	@Value("${src_path}")
	private String PATH;
	@Value("${property_value_to_save_csv}")
	private String CSV;
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	@Override
	public List<CandidateDetails> getCandidateDetailsFromCSVFile() throws ResumeParsingException, IOException {

		File dir = new File(PATH + "//" + CSV);
		File[] listFiles = dir.listFiles();

		if (listFiles == null) {
			throw new ResumeParsingException("No files in this path!");
		}

		CandidateDetails candidateDetails = null;
		List<CandidateDetails> candidateDetailsList = new ArrayList<CandidateDetails>();
		for (File f : listFiles) {
			if (f.isDirectory()) {
				continue;
			}
			candidateDetails = getCandidateDetails(Paths.get(f.getAbsolutePath()));
			if (candidateDetails != null) {
				candidateDetailsList.add(candidateDetails);
			}
		}
		return candidateDetailsList;
	}

	private CandidateDetails getCandidateDetails(Path p) throws IOException {
		System.out.println(p);
		try (Reader reader = Files.newBufferedReader(p);
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);) {
			List<CSVRecord> records = csvParser.getRecords();
			CandidateDetails candidateDetail = new CandidateDetails();

			if (records == null || records.size() == 0) {
				System.err.println("No records in this csv. returning!");
				return null;
			}

			CSVRecord csvRecord = records.get(0);
			String name = "";
			String email = "";

			String phone = "";
			String skills = "";

			String education = "";
			String experience = "";
			String fileName = "";
			String date = "";

			if (csvRecord.size() > 0)
				name = csvRecord.get(0);
			if (csvRecord.size() > 1)
				phone = csvRecord.get(1);
			if (csvRecord.size() > 2)
				email = csvRecord.get(2);
			if (csvRecord.size() > 3)
				skills = csvRecord.get(3);
			if (csvRecord.size() > 4)
				education = csvRecord.get(4);
			if (csvRecord.size() > 5)
				experience = csvRecord.get(5);
			if (csvRecord.size() > 6)
				fileName = csvRecord.get(6);
			if (csvRecord.size() > 7)
				date = csvRecord.get(7);

			System.out.println("---------------");
			System.out.println("Name : " + name);
			System.out.println("Email : " + email);
			System.out.println("Phone : " + phone);
			System.out.println("skills : " + skills);
			System.out.println("Education : " + education);
			System.out.println("Experience : " + experience);
			System.out.println("uploadedDate :" + date);
			System.out.println("FileName :" + fileName);

			System.out.println("---------------\n\n");
			candidateDetail.setUploadedFileName(fileName);
			candidateDetail.setCandidateName(name);
			candidateDetail.setCandidateEmail(email);
			candidateDetail.setCandidatePhone(phone);
			candidateDetail.setSkills(skills);
			candidateDetail.setQualification(education);
			candidateDetail.setExperience(experience);

			try {
				candidateDetail.setUploadedDate(sdf.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return candidateDetail;
		}
	}
}