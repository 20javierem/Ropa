package com.babas.utilities;

import com.babas.controllers.*;
import com.babas.controllers.Colors;
import com.babas.models.*;
import com.babas.views.frames.FPrincipal;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class Excel {
    private File file;
    private String[] sheets;
    private Sex sex;
    public Excel() {
        sex = new Sex();
        sex.setName("Unisex");
        sex.setActive(true);
        sex.save();
        sheets = new String[]{
                "categorias",
                "estilos",
                "tallas",
                "colores",
                "marcas",
                "dimensiones",
                "productos"};
    }
    public boolean initialize() {
        JFileChooser selectFile = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("*.excel", "xlsx","xls", "slxs");
        selectFile.addChoosableFileFilter(filter);
        int result = selectFile.showOpenDialog(Utilities.getJFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            file = selectFile.getSelectedFile();
            return true;
        }
        return false;
    }
    public void loadData() {
        try {
            XSSFWorkbook book = new XSSFWorkbook(file);
            for (String nameSheet : sheets) {
                Sheet sheet = book.getSheet(nameSheet.toUpperCase());
                if (sheet != null) {
                    Iterator<Row> iterator = sheet.iterator();
                    iterator.next();
                    while (iterator.hasNext()) {
                        Row row = iterator.next();
                        switch (nameSheet) {
                            case "categorias":
                                Category category = Categorys.get(row.getCell(1).getStringCellValue().trim());
                                if (category == null) {
                                    category = new Category();
                                    category.setName(row.getCell(1).getStringCellValue().trim());
                                    category.save();
                                    FPrincipal.categories.add(category);
                                    FPrincipal.categoriesWithAll.add(category);
                                }
                                break;
                            case "estilos":
                                category = Categorys.get(row.getCell(2).getStringCellValue().trim());
                                Style style = Styles.get(row.getCell(1).getStringCellValue().trim());
                                if (style == null) {
                                    style = new Style();
                                    style.setName(row.getCell(1).getStringCellValue().trim());
                                    style.setCategory(category);
                                    style.save();
                                    FPrincipal.styles.add(style);
                                }
                                break;
                            case "tallas":
                                Size size = Sizes.get(row.getCell(1).getStringCellValue().trim());
                                if (size == null) {
                                    size = new Size();
                                    size.setName(row.getCell(1).getStringCellValue().trim());
                                    size.setActive(true);
                                    size.save();
                                    FPrincipal.sizes.add(size);
                                    FPrincipal.sizesWithAll.add(size);
                                }
                                break;
                            case "colores":
                                Color color = Colors.get(row.getCell(1).getStringCellValue().trim());
                                if (color == null) {
                                    color = new Color();
                                    color.setName(row.getCell(1).getStringCellValue().trim());
                                    color.setActive(true);
                                    color.save();
                                    FPrincipal.colors.add(color);
                                    FPrincipal.colorsWithAll.add(color);
                                }
                                break;
                            case "marcas":
                                Brand brand = Brands.get(row.getCell(1).getStringCellValue().trim());
                                if (brand == null) {
                                    brand = new Brand();
                                    brand.setName(row.getCell(1).getStringCellValue().trim());
                                    brand.setActive(true);
                                    brand.save();
                                    FPrincipal.brands.add(brand);
                                    FPrincipal.brandsWithAll.add(brand);
                                }
                                break;
                            case "dimensiones":
                                Dimention dimention = Dimentions.get(row.getCell(1).getStringCellValue().trim());
                                if (dimention == null) {
                                    dimention = new Dimention();
                                    dimention.setName(row.getCell(1).getStringCellValue().trim());
                                    dimention.setActive(true);
                                    dimention.save();
                                    FPrincipal.dimentions.add(dimention);
                                    FPrincipal.dimentionsWithAll.add(dimention);
                                }
                                break;
                            default:
                                Product product = new Product();
                                style = Styles.get(row.getCell(1).getStringCellValue().trim());
                                product.setStyle(style);
                                size = Sizes.get(row.getCell(2).getStringCellValue().trim());
                                product.setSize(size);
                                color = Colors.get(row.getCell(3).getStringCellValue().trim());
                                System.out.println(row.getCell(3).getStringCellValue());
                                System.out.println(color);
                                product.setColor(color);
                                brand = Brands.get(row.getCell(4).getStringCellValue().trim());
                                product.setBrand(brand);
                                Cell cell=row.getCell(5);
                                if(cell!=null){
                                    String valueDimension=cell.getStringCellValue();
                                    dimention = Dimentions.get(valueDimension.trim());
                                    product.setDimention(dimention);
                                }

                                try {
                                    product.setBarcode(row.getCell(6).getStringCellValue().trim());
                                } catch (IllegalStateException e) {
                                    product.setBarcode(String.valueOf(row.getCell(6).getNumericCellValue()));
                                }
                                cell=row.getCell(7);
                                String value;
                                if(cell!=null){
                                    value = String.valueOf(cell.getNumericCellValue());
                                    Presentation presentationSale = new Presentation(product);
                                    Price priceSale = new Price(presentationSale);
                                    priceSale.setDefault(true);
                                    priceSale.setPrice(Double.parseDouble(value));
                                    presentationSale.setName("Unitario");
                                    presentationSale.setDefault(true);
                                    presentationSale.setQuantity(1);
                                    presentationSale.setPriceDefault(priceSale);
                                    presentationSale.getPrices().add(priceSale);
                                    product.getPresentations().add(presentationSale);
                                }
                                cell=row.getCell(8);
                                if(cell!=null){
                                    value = String.valueOf(cell.getNumericCellValue());
                                    Presentation presentationRental = new Presentation(product);
                                    Price priceRental = new Price(presentationRental);
                                    priceRental.setDefault(true);
                                    priceRental.setPrice(Double.parseDouble(value));
                                    presentationRental.setName("Alquiler");
                                    presentationRental.setDefault(true);
                                    presentationRental.setQuantity(1);
                                    presentationRental.setPriceDefault(priceRental);
                                    presentationRental.getPrices().add(priceRental);
                                    product.getPresentations().add(presentationRental);
                                }
                                product.setSex(sex);
                                product.getStyle().getProducts().add(product);
                                product.save();
                                FPrincipal.products.add(product);
                        }
                    }
                }
            }
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
