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

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Log
@Component
public class DBFImporter implements Importer{
    private final FileManager fileManager;

    @Autowired
    public DBFImporter(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public List<UnikoLecItem> importUnikoQuery() {
        String path = fileManager.getUnikoQueryFile();
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

        Collections.sort(unikoLecItems);

        return unikoLecItems;
    }

    @Override
    public List<PriceItem> importAllPrices() {
        List<PriceItem> priceItems = new ArrayList<>();
        List<Path> pricesPaths = fileManager.getPricesPaths();

        for (Path path : pricesPaths) {
            priceItems.addAll(Objects.requireNonNull(importPrice(path)));
        }

        return priceItems;
    }

    private List<PriceItem> importPrice(Path path) {
        Charset charset = Charset.forName("Cp866");
        List<PriceItem> priceItems = new ArrayList<>();

        try (DBFReader reader = new DBFReader(new FileInputStream(path.toFile()), charset)) {
            Map<String, Integer> fieldMap = getFieldMap(reader);
            Object[] rowObj;
            while ((rowObj = reader.nextRecord()) != null) {
                priceItems.add(readPriceItemByFieldMap(rowObj, path, fieldMap));
            }
        } catch (Exception e) {
            log.warning(String.format("price was NOT read completely. File: %s", path));
            e.printStackTrace();
        }

        log.info(String.format("File %s was read successfully. File size: %s positions", path, priceItems.size()));
        return priceItems;
    }

    private PriceItem readPriceItemByFieldMap(Object[] rowObj, Path pricePath, Map<String, Integer> fieldMap) {
        PriceItem item = new PriceItem();

        item.setDist(pricePath.getName(2).toString());
        item.setCodePst(rowObj[fieldMap.get("CODEPST")].toString());
        item.setName(rowObj[fieldMap.get("NAME")].toString());
        item.setCountry(rowObj[fieldMap.get("CNTR")].toString());
        item.setManufacturer(rowObj[fieldMap.get("FIRM")].toString());
        item.setQntPack(Integer.parseInt(rowObj[fieldMap.get("QNTPACK")].toString()));
        item.setEan13(rowObj[fieldMap.get("EAN13")].toString());
        item.setNds(Double.parseDouble(rowObj[fieldMap.get("NDS")].toString()));
        item.setGDate((Date) rowObj[fieldMap.get("GDATE")]);
        String quantityToInt = rowObj[fieldMap.get("QNT")].toString();
        String newString = quantityToInt.replaceAll("\\.00", "");
        try {
            item.setQuantity(Integer.parseInt(newString));
        } catch (Exception e) {
            item.setQuantity(Integer.parseInt(quantityToInt.replaceAll("\\.0", "")));
        }
        item.setGnvlp(new BigDecimal(rowObj[fieldMap.get("GNVLS")].toString()).intValue() == 1);
        item.setPrice(new BigDecimal(rowObj[fieldMap.get("PRICE1")].toString()));


        return item;
    }

    private Map<String, Integer> getFieldMap(DBFReader reader) {
        Map<String, Integer> map = new HashMap<>();
        int fieldCount = reader.getFieldCount();
        for (int i = 0; i < fieldCount; i++) {
            map.put(reader.getField(i).getName(), i);
        }
        return map;
    }

    @Deprecated
    private PriceItem readPriceItem(Object[] rowObj, Path pricePath) {
        PriceItem item = new PriceItem();

        item.setDist(pricePath.getName(2).toString());
        item.setCodePst(rowObj[0].toString());
        item.setName(rowObj[1].toString());
        item.setCountry(rowObj[2].toString());
        item.setManufacturer(rowObj[3].toString());
        item.setQntPack(Integer.parseInt(rowObj[4].toString()));
        item.setEan13(rowObj[5].toString());
        item.setNds(Double.parseDouble(rowObj[6].toString()));
        item.setGDate((Date) rowObj[7]);
        String quantityToInt = rowObj[8].toString();
        String newString = quantityToInt.replaceAll("\\.00", "");
        try {
            item.setQuantity(Integer.parseInt(newString));
        } catch (Exception e) {
            item.setQuantity(Integer.parseInt(quantityToInt.replaceAll("\\.0", "")));
        }
        item.setGnvlp(new BigDecimal(rowObj[10].toString()).intValue() == 1);
        item.setPrice(new BigDecimal(rowObj[11].toString()));

        return item;
    }
}


