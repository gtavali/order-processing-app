package com.orderprocessing.beans;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * It represents the response CSV file.
 *
 * @author Gabor Tavali
 */
@Data
@AllArgsConstructor
public class ResponseFileBean {

    @CsvBindByName(column = "LineNumber")
    private int lineNumber;

    @CsvBindByName(column = "Status")
    private String status;

    @CsvBindByName(column = "Message")
    private String message;

}
