package com.orderprocessing.services;

import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.orderprocessing.beans.InputFileBean;
import com.orderprocessing.beans.ResponseFileBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Service class to process the CSV files.
 *
 * @author Gabor Tavali
 */
@Service
@Slf4j
public class CsvService {

    public List<InputFileBean> readInputFile(String path) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(path));
        CsvToBean<InputFileBean> csvToBean = new CsvToBeanBuilder(reader)
                .withType(InputFileBean.class)
                .withSeparator(';')
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return csvToBean.parse();
    }

    public void writeResponseFile(List<ResponseFileBean> response, String fileName) {
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("./" + fileName));

            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder<>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(';')
                    .build();

            beanToCsv.write(response);
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException ex) {
            log.error("Failed to write response.", ex);
        }

    }

}
