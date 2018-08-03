package com.orderprocessing.services;

import com.orderprocessing.beans.InputFileBean;
import com.orderprocessing.beans.ResponseFileBean;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Test for {@link CsvService}
 *
 * @author Gabor Tavali
 */
public class CsvServiceTest {

    CsvService csvService = new CsvService();

    @Test
    public void testReadInputFile() throws URISyntaxException, IOException {
        File file = new File(getClass().getClassLoader().getResource("sample-valid.csv").toURI());

        List<InputFileBean> input = csvService.readInputFile(file.toPath().toString());

        InputFileBean firstLine = input.get(0);
        InputFileBean secondLine = input.get(1);

        Assert.assertEquals(4, input.size());

        Assert.assertEquals(1, firstLine.getLineNumber());
        Assert.assertEquals(1l, firstLine.getOrderItemId());
        Assert.assertEquals(1l, firstLine.getOrderId());
        Assert.assertEquals("John Doe", firstLine.getBuyerName());
        Assert.assertEquals("john@doe.com", firstLine.getBuyerEmail());
        Assert.assertEquals("Boston", firstLine.getAddress());
        Assert.assertEquals(12345, firstLine.getPostcode());
        Assert.assertEquals(Double.valueOf(100.20d), Double.valueOf(firstLine.getSalePrice()));
        Assert.assertEquals(Double.valueOf(10d), Double.valueOf(firstLine.getShippingPrice()));
        Assert.assertEquals("sku", firstLine.getSku());
        Assert.assertEquals("IN_STOCK", firstLine.getStatus());
        Assert.assertEquals("2018-08-02", firstLine.getOrderDate());

        Assert.assertEquals(2, secondLine.getLineNumber());
        Assert.assertEquals(2l, secondLine.getOrderItemId());
        Assert.assertEquals(1l, secondLine.getOrderId());
        Assert.assertEquals("John Doe", secondLine.getBuyerName());
        Assert.assertEquals("john@doe.com", secondLine.getBuyerEmail());
        Assert.assertEquals("Boston", secondLine.getAddress());
        Assert.assertEquals(12345, secondLine.getPostcode());
        Assert.assertEquals(Double.valueOf(50d), Double.valueOf(secondLine.getSalePrice()));
        Assert.assertEquals(Double.valueOf(10.3d), Double.valueOf(secondLine.getShippingPrice()));
        Assert.assertEquals("sku", secondLine.getSku());
        Assert.assertEquals("IN_STOCK", secondLine.getStatus());
        Assert.assertEquals("2018-08-02", secondLine.getOrderDate());
    }

    @Test
    public void testWriteResponseFile() throws IOException {
        List<ResponseFileBean> response = Arrays.asList(new ResponseFileBean(1, ValidatorService.ValidationStatus.OK.name(), ""));
        csvService.writeResponseFile(response, "./test.csv");

        InputStream inputStream = new FileInputStream(new File("./test.csv"));
        StringBuilder resultStringBuilder = new StringBuilder();

        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }

        String result = resultStringBuilder.toString();

        Assert.assertTrue(result.contains("LINENUMBER"));
        Assert.assertTrue(result.contains("MESSAGE"));
        Assert.assertTrue(result.contains("STATUS"));
        Assert.assertTrue(result.contains("OK"));
    }
}
