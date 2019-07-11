package com.hsi.parsing.parsers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hsi.parsing.exception.ResumeParsingException;

@Service("docParser")
public class DocParser extends AbstractParser {
	@Value("${src_path}")
	private String PATH;

	@Value("${property_value_to_save_csv}")
	private String CSV;

	@Override
	public String convert(MultipartFile file) throws IOException {

		System.out.println("converting *****");

		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			FileWriter fw = null;
			WordExtractor extractor = null;
			HWPFDocument document = new HWPFDocument(file.getResource().getInputStream());
			extractor = new WordExtractor(document);
			String[] fileData = extractor.getParagraphText();
			extractor.close();
			String fileName = file.getOriginalFilename();
			String csvfileName = fileName.replace(".doc", ".csv");
			File csvPath = new File(PATH + "//" + CSV);
			if (!csvPath.exists()) {
				csvPath.mkdirs();
			}

			File csvFile = new File(PATH + "//" + CSV + "//" + csvfileName);

			if (csvFile.exists()) {
				fw = new FileWriter(csvFile, true);
			} else {
				csvFile.createNewFile();
				fw = new FileWriter(csvFile);
			}

			Set<String> nameS = new HashSet<>();
			Set<String> emailS = new HashSet<>();
			Set<String> phoneS = new HashSet<>();
			Set<String> experienceS = new HashSet<>();
			Set<String> educationS = new HashSet<>();
			Set<String> skillsS = new HashSet<>();

			for (int i = 0; i < fileData.length; i++) {
				/* extract Name */
				if (fileData[i] != null) {
					/* extract Name */
					handleName(nameS, fileData, i);
					/* extract phone numbers */
					handlePhone(phoneS, fileData, i);
					/* extract email */
					handleMail(emailS, fileData, i);
					/* Years of experience */
					handleExperience(experienceS, fileData, i);
					/* extract education */
					handleEducation(educationS, fileData, i);
					/* extract skills */
					handleSkills(skillsS, fileData, i);

				}

			}

			StringBuilder mainB = new StringBuilder();

			writeToSB(nameS, mainB);
			writeToSB(phoneS, mainB);
			writeToSB(emailS, mainB);
			writeToSB(skillsS, mainB);
			writeToSB(educationS, mainB);
			writeToSB(experienceS, mainB);
			writeToSB(Collections.singleton(fileName), mainB);
			writeToSB(Collections.singleton(sdf.format(date)), mainB);
			mainB.deleteCharAt(mainB.length() - 1);
			fw.write(mainB.toString());

			fw.close();

		} catch (Exception exep) {
			exep.printStackTrace();
			throw new ResumeParsingException(exep.getMessage());
		}
		return "Writing successfull";
	}

	private void writeToSB(Set<String> nameS, StringBuilder mainB) {

		for (String str : nameS) {
			mainB.append(str.replace(",", "|")).append("|");
		}

		if (nameS.size() > 0) {
			mainB.deleteCharAt(mainB.length() - 1);
		}

		mainB.append(",");
	}

}
