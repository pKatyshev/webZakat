package com.katyshev.webZakat.utils;

import com.katyshev.webZakat.exceptions.FileNotFoundException;
import com.katyshev.webZakat.exceptions.ImporterException;
import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.models.UnikoLecItem;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


@Component
@Log
public class Importer {

    private final MyFileManager myFileManager;

    @Autowired
    public Importer(MyFileManager myFileManager) {
        this.myFileManager = myFileManager;
    }

    public List<UnikoLecItem> importUnikoQuery() {
        String path = myFileManager.getUnikoQueryPath();
        Charset charset = Charset.forName("windows-1251");
        List<UnikoLecItem> unikoLecItems = new ArrayList<>();

        if(!Files.isRegularFile(Path.of(path))) {
            log.warning("Request file was NOT FOUND");
            throw new FileNotFoundException("Uniko-query file was not found");
        }

        try (DBFReader reader = new DBFReader(new FileInputStream(path), charset))
        {
            int numberOfFields = reader.getFieldCount();
            for (int i = 0; i < numberOfFields; i++) {
                DBFField field = reader.getField(i);
            }
            Object[] rowObj;
            while ((rowObj = reader.nextRecord()) != null) {
                int group = Integer.parseInt(rowObj[1].toString());
                int tmc = Integer.parseInt(rowObj[2].toString());
                float quantity = Float.parseFloat(rowObj[3].toString());
                String name = rowObj[4].toString();
                String factory = rowObj[5].toString();

                unikoLecItems.add(new UnikoLecItem(group, tmc, quantity, name, factory));
            }
            log.info(String.format("Request file contains %s positions", unikoLecItems.size()));
        } catch (Exception e) {
            log.warning("Error reading request file");
            throw new ImporterException(String.format("file cannot be read. File: %s", path.toString()));
        }
        myFileManager.moveToStorage(path);

        Collections.sort(unikoLecItems);

        return unikoLecItems;
    }

    public List<PriceItem> importAllPrices() {
        List<PriceItem> priceItems = new ArrayList<>();
        String pricesDirectory = myFileManager.getPriceDirectory();
        for(File price : MyFileManager.getFileList(pricesDirectory)) {
            priceItems.addAll(Objects.requireNonNull(importPrice(price.toString())));
        }
        return priceItems;
    }

    public List<PriceItem> importPrice(String path) {
        Charset charset = Charset.forName("Cp866");
        List<PriceItem> priceItems = new ArrayList<>();
        if(!Files.isRegularFile(Path.of(path))) {
            return null;
        }

        try (DBFReader reader = new DBFReader(new FileInputStream(path), charset)) {
            Object[] rowObj;
            while ((rowObj = reader.nextRecord()) != null) {
                priceItems.add(readPriceItem(rowObj, path));
            }
        } catch (Exception e) {
            log.warning(String.format("price was NOT read completely. File: %s", path));
            e.printStackTrace();
        }

        log.info(String.format("File %s was read successfully. File size: %s positions", path, priceItems.size()));
        return priceItems;
    }

    private static PriceItem readPriceItem(Object[] rowObj, String pricePath) {
        PriceItem item = new PriceItem();
        int lastIndex = pricePath.lastIndexOf("\\");
        String fileName = pricePath.substring(lastIndex+1).replaceAll("(\\d)|(_)", "");
        item.setDist(fileName);
        item.setCodePst(rowObj[0].toString());
        item.setName(rowObj[1].toString());
        item.setMnn(rowObj[2].toString());
        item.setCountry(rowObj[3].toString());
        item.setManufacturer(rowObj[4].toString());
        item.setQntPack(Integer.parseInt(rowObj[5].toString()));
        item.setEan13(rowObj[6].toString());
        item.setNds(Double.parseDouble(rowObj[7].toString()));
        try {
            item.setGDate(rowObj[8].toString());
        } catch (NullPointerException e) {
            item.setGDate("unavailable date");
        }
        String quantityToInt = rowObj[9].toString();
        String newString = quantityToInt.replaceAll("\\.00", "");
        try {
            item.setQuantity(Integer.parseInt(newString));
        } catch (Exception e) {
            item.setQuantity(Integer.parseInt(quantityToInt.replaceAll("\\.0", "")));
        }
        item.setPrice(new BigDecimal(rowObj[11].toString()));
        item.setMark(Integer.parseInt(rowObj[12].toString()));

        return item;
    }

}
