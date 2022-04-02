package com.wicc.personalassistant.utils;
import com.wicc.personalassistant.dto.transaction.TransactionDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExcelServiceUtil {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<TransactionDto> transactionDtos;

    public ExcelServiceUtil(List<TransactionDto> transactionDtos) {
        workbook = new XSSFWorkbook();
        this.transactionDtos = transactionDtos;
        sheet = workbook.createSheet();
    }

    private void writeHeading(){
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Date");
        cell = row.createCell(1);
        cell.setCellValue("Title");
        cell = row.createCell(2);
        cell.setCellValue("Amount");
        cell = row.createCell(3);
        cell.setCellValue("Category");
        cell = row.createCell(4);
        cell.setCellValue("Description");
    }

    private void writeData(){
        int rowCount = 1;
        for(TransactionDto transactionDto : transactionDtos){
            Row row = sheet.createRow(rowCount);
            Cell cell = row.createCell(0);
            cell.setCellValue(transactionDto.getTransactionDate().toString());
            cell = row.createCell(1);
            cell.setCellValue(transactionDto.getTitle());
            cell = row.createCell(2);
            cell.setCellValue(transactionDto.getAmount());
            cell = row.createCell(3);
            cell.setCellValue(transactionDto.getCategory().getName());
            cell = row.createCell(4);
            cell.setCellValue(transactionDto.getDescription());
            rowCount++;
        }

    }
    public void export(HttpServletResponse response) throws IOException {
        writeHeading();
        writeData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
