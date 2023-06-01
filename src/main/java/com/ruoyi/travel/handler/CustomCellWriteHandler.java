package com.ruoyi.travel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.AbstractCellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class CustomCellWriteHandler extends AbstractCellWriteHandler {
    //开始行
    private int firstRowIndex;
    //结束行
    private int lastRowIndex;
    //开始列
    private int columnIndex;
    //结束列
    private int lastCellIndex;

    public CustomCellWriteHandler(int firstRowIndex, int lastRowIndex, int columnIndex, int lastCellIndex) {
        //列合并，columnIndex为开始合并的列，lastCellIndex为结束合并的列
        this.firstRowIndex = firstRowIndex;
        this.lastRowIndex = lastRowIndex;
        this.columnIndex = columnIndex;
        this.lastCellIndex = lastCellIndex;
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (isHead != null && !isHead && cell.getRowIndex() >= firstRowIndex &&
            cell.getRowIndex() <= lastRowIndex
        && cell.getColumnIndex() == columnIndex) {
            System.out.println(cell.getRowIndex() + " " + cell.getColumnIndex());
            Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
            Sheet sheet = writeSheetHolder.getSheet();

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), columnIndex, lastCellIndex);
            System.out.println(firstRowIndex+" "+lastRowIndex+" "+columnIndex+" "+lastCellIndex);
            sheet.addMergedRegion(cellRangeAddress);

            for (int i = firstRowIndex; i <= lastRowIndex; i++) {
                Row row = sheet.getRow(i);
                //空指针异常处理
                if (row == null) {
                    row = sheet.createRow(i);
                }
                for (int j = columnIndex; j <= lastCellIndex; j++) {
                    Cell c = row.getCell(j);
                    //空指针异常，创建单元格
                    if (c == null) {
                        c = row.createCell(j);
                    }
                    c.setCellStyle(cellStyle);
                }
            }
        }
    }
}
