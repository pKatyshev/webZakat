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
import java.util.List;

@Component
@Log
public class DbfExporter implements Exporter{
    private final MyFileManager myFileManager;

    public DbfExporter(MyFileManager myFileManager) {
        this.myFileManager = myFileManager;
    }

    @Override
    public void exportOrder(List<PriceItem> order) {
        Charset charset = Charset.forName("Cp866");
        DBFField[] fields = createDbfTable();

        try (DBFWriter writer = new DBFWriter(new FileOutputStream(myFileManager.getNewOutputFile()), charset)){
            writer.setFields(fields);
            Object[] rowData;
            for (PriceItem item : order) {
                rowData = new Object[14];
                rowData[0] = item.getCodePst();
                rowData[1] = item.getName();
                rowData[2] = item.getMnn();
                rowData[3] = item.getCountry();
                rowData[4] = item.getManufacturer();
                rowData[5] = item.getQntPack();
                rowData[6] = item.getEan13();
                rowData[7] = item.getNds();
                rowData[8] = item.getGDate();
                rowData[9] = item.getQuantity();
                rowData[10] = 1;
                rowData[11] = item.getPrice();
                rowData[12] = item.getMark();
                rowData[13] = item.getInOrder();

                writer.addRecord(rowData);
            }
            log.info("order was export successfully");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private DBFField[] createDbfTable() {
        DBFField[] fields = new DBFField[14];

        fields[0] = new DBFField();
        fields[0].setName("CODEPST");
        fields[0].setType(DBFDataType.CHARACTER);
        fields[0].setLength(12);

        fields[1] = new DBFField();
        fields[1].setName("NAME");
        fields[1].setType(DBFDataType.CHARACTER);
        fields[1].setLength(80);

        fields[2] = new DBFField();
        fields[2].setName("MNN");
        fields[2].setType(DBFDataType.CHARACTER);
        fields[2].setLength(80);

        fields[3] = new DBFField();
        fields[3].setName("CNTR");
        fields[3].setType(DBFDataType.CHARACTER);
        fields[3].setLength(15);

        fields[4] = new DBFField();
        fields[4].setName("FIRM");
        fields[4].setType(DBFDataType.CHARACTER);
        fields[4].setLength(40);

        fields[5] = new DBFField();
        fields[5].setName("QNTPACK");
        fields[5].setType(DBFDataType.NUMERIC);
        fields[5].setLength(8);

        fields[6] = new DBFField();
        fields[6].setName("EAN13");
        fields[6].setType(DBFDataType.CHARACTER);
        fields[6].setLength(13);

        fields[7] = new DBFField();
        fields[7].setName("NDS");
        fields[7].setType(DBFDataType.NUMERIC);
        fields[7].setLength(19);
        fields[7].setDecimalCount(2);

        fields[8] = new DBFField();
        fields[8].setName("GDATE");
        fields[8].setType(DBFDataType.DATE);
        fields[8].setLength(8);

        fields[9] = new DBFField();
        fields[9].setName("QNT");
        fields[9].setType(DBFDataType.NUMERIC);
        fields[9].setLength(9);
        fields[9].setDecimalCount(2);

        fields[10] = new DBFField();
        fields[10].setName("GNVLS");
        fields[10].setType(DBFDataType.NUMERIC);
        fields[10].setLength(1);

        fields[11] = new DBFField();
        fields[11].setName("PRICE");
        fields[11].setType(DBFDataType.NUMERIC);
        fields[11].setLength(9);
        fields[11].setDecimalCount(2);

        fields[12] = new DBFField();
        fields[12].setName("MARK");
        fields[12].setType(DBFDataType.NUMERIC);
        fields[12].setLength(2);

        fields[13] = new DBFField();
        fields[13].setName("ORDER");
        fields[13].setType(DBFDataType.NUMERIC);
        fields[13].setLength(9);

        return fields;
    }
}
