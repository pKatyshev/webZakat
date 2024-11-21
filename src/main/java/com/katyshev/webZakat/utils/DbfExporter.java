package com.katyshev.webZakat.utils;

import com.katyshev.webZakat.models.PriceItem;
import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

@Component
@Log
public class DbfExporter implements Exporter{
    private final FileManager fileManager;

    public DbfExporter(FileManager fileManager) {
        this.fileManager = fileManager;
    }


    @Override
    public void exportOrder(List<PriceItem> order) {
    }

    @Override
    public void exportOrder(List<PriceItem> order, List<String> distributors) {
        for (String dist : distributors) {
            exportOrderByDistributor(order, dist);
        }
    }

    private void exportOrderByDistributor(List<PriceItem> order, String dist) {
        Path path = fileManager.getOrderFileByDistributor(dist);
        Charset charset = Charset.forName("Cp866");
        DBFField[] fields = createDbfTableFourFields();

        try (DBFWriter writer = new DBFWriter(new FileOutputStream(path.toFile()), charset)){
            writer.setFields(fields);
            Object[] rowData;
            for (PriceItem item : order) {
                rowData = new Object[4];
                rowData[0] = 314151;
                rowData[1] = item.getCodePst();
                rowData[2] = item.getPrice();
                rowData[3] = item.getInOrder();

                writer.addRecord(rowData);
            }
            log.info("order was export successfully");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private DBFField[] createDbfTableFourFields() {
        DBFField[] fields = new DBFField[4];

        fields[0] = new DBFField();
        fields[0].setName("NUMZ");
        fields[0].setType(DBFDataType.NUMERIC);
        fields[0].setLength(12);

        fields[1] = new DBFField();
        fields[1].setName("CODEPST");
        fields[1].setType(DBFDataType.CHARACTER);
        fields[1].setLength(12);

        fields[2] = new DBFField();
        fields[2].setName("PRICE");
        fields[2].setType(DBFDataType.NUMERIC);
        fields[2].setLength(9);
        fields[2].setDecimalCount(2);

        fields[3] = new DBFField();
        fields[3].setName("QNT");
        fields[3].setType(DBFDataType.NUMERIC);
        fields[3].setLength(9);
        fields[3].setDecimalCount(2);
        return fields;
    }
}

