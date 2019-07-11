package com.hsi.parsing.parsers;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public abstract class AbstractParser {

  private static final String NAME = "^[\\p{L} .'-]+$";
  private static final String SKILLS = "\\b(C|C++|Java|PHP|Python|Testing|Automation|Perl|.Net|Unix|Windows)\\b";
  private static final String EXPERIENCE = "\\b(\\d{1,2}\\.{0,1}\\d{0,2}\\+{0,1})\\s{0,1}(years|Years|Yrs)\\b?";  
  private static final String EMAIL = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
  private static final String EDUCATION = "\\b(Diploma|BSc|MSc|BE|Bachelor|Engineer(ing?)|BTech|Master|MTech|MBA|MCA|BCA|BCom|PhD)\\b";
  private static final String PHONE_NUMBER = "(?:(?:\\+|0{0,2})91(\\s*[\\- ]\\s*)?|[0 ]?)?[789]\\d{9}|(\\d[ -]?){10}\\d";

  public abstract String convert(MultipartFile file) throws IOException;

  public void handleName(Set<String> list, String[] fileData, int i) {
    if (list.size() <= 0) {
      String probableName = findPattern(fileData[i],
          Pattern.compile(NAME, Pattern.CASE_INSENSITIVE));
      if (probableName != null && probableName.length() < 20) {
        writeToList(list, probableName);
      }
    }
  }

  public void handlePhone(Set<String> list, String[] fileData, int i) {
    writeToList(list, findPattern(fileData[i],
        Pattern.compile(PHONE_NUMBER, Pattern.MULTILINE | Pattern.DOTALL)));
  }

  public void handleMail(Set<String> list, String[] fileData, int i) {
    writeToList(list, findPattern(fileData[i], Pattern.compile(EMAIL, Pattern.MULTILINE)));
  }

  public void handleEducation(Set<String> list, String[] fileData, int i) {
    writeToList(list, findPattern(fileData[i], Pattern.compile(EDUCATION, Pattern.MULTILINE)));
  }

  //public void handleExperience(Set<String> list, String[] fileData, int i) {
  //  writeToList(list,
  //      findPattern(fileData[i], Pattern.compile(EXPERIENCE, Pattern.CASE_INSENSITIVE)));
  //}
  
  public void handleExperience(Set<String> list, String[] fileData, int i) {
		if (list.size() <= 0) {
			writeToList(list,
					findPattern(fileData[i], Pattern.compile(EXPERIENCE, Pattern.MULTILINE | Pattern.DOTALL)));
		}
	}

  public void handleSkills(Set<String> list, String[] fileData, int i) {
    writeToList(list, findPattern(fileData[i], Pattern.compile(SKILLS, Pattern.CASE_INSENSITIVE)));
  }

  private void writeToList(Set<String> list, String var) {
    if (var != null && !"[]".equals(var) && !"".equals(var) && !" ".equals(var)) {
      list.add(var);
    }
  }

  private String findPattern(String line, Pattern pattern) {

    Matcher matcher = pattern.matcher(line);
    if (matcher.find()) {
      String group = matcher.group();
      group=group.replaceAll("  ", "");
      group=group.replaceAll("  ", "");
      return group;
    }
    return null;
  }

}
