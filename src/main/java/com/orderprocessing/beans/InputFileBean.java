package com.orderprocessing.beans;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.util.Date;

/**
 * It represents the header of a valid input CSV file.
 *
 * @author Gabor Tavali
 */
@Data
public class InputFileBean {

    @CsvBindByName(column = "LineNumber")
    private int lineNumber;

    @CsvBindByName(column = "OrderItemId")
    private long orderItemId;

    @CsvBindByName(column = "OrderId")
    private long orderId;

    @CsvBindByName(column = "BuyerName")
    private String buyerName;

    @CsvBindByName(column = "BuyerEmail")
    private String buyerEmail;

    @CsvBindByName(column = "Address")
    private String address;

    @CsvBindByName(column = "Postcode")
    private int postcode;

    @CsvBindByName(column = "SalePrice")
    private double salePrice;

    @CsvBindByName(column = "ShippingPrice")
    private double shippingPrice;

    @CsvBindByName(column = "SKU")
    private String sku;

    @CsvBindByName(column = "Status")
    private String status;

    @CsvBindByName(column = "OrderDate")
    @CsvDate(value = "yyyy-mm-dd")
    private Date orderDate;

}
