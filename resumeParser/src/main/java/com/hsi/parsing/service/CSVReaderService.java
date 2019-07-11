package com.hsi.parsing.service;

import com.hsi.parsing.exception.ResumeParsingException;
import com.hsi.parsing.model.CandidateDetails;

import java.io.IOException;
import java.util.List;

/**
 * Created by sindhuja on 6/18/2019.
 */
interface CSVReaderService {
    List<CandidateDetails> getCandidateDetailsFromCSVFile() throws ResumeParsingException,IOException;}
