package com.babas.utilities;

import com.babas.controllers.*;
import com.babas.controllers.Colors;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.views.frames.FPrincipal;
import com.moreno.Notify;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Excel {
    private File file;
    private String[] sheets;

    public Excel() {
        Sex sex=Sexs.getByName("Unisex");
        if(sex==null){
            sex=new Sex("Unisex");
            sex.setActive(true);
            sex.save();
            FPrincipal.sexs.add(sex);
            FPrincipal.sexsWithAll.add(sex);
        }
        sheets = new String[]{
                "categorias",
                "estilos",
                "generos",
                "tallas",
                "colores",
                "marcas",
                "dimensiones",
                "estados",
                "productos",
                "presentaciones",
                "precios"};
    }

    public boolean initialize(boolean impor) {
        if(impor){
            JFileChooser selectFile = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("*.excel", "xlsx","xls", "slxs");
            selectFile.addChoosableFileFilter(filter);
            int result = selectFile.showOpenDialog(Utilities.getJFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                file = selectFile.getSelectedFile();
                return true;
            }
        }else{
            JFileChooser selectFile = new JFileChooser();
            int result = selectFile.showSaveDialog(Utilities.getJFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                file = selectFile.getSelectedFile();
                return true;
            }
        }
        return false;
    }

    public void loadData() {
        Utilities.getLblIzquierda().setText("Importando productos...");
        try {
            FileInputStream inputStream = new FileInputStream(file);
            HSSFWorkbook book = new HSSFWorkbook(inputStream);
            for (String nameSheet : sheets) {
                Sheet sheet = book.getSheet(nameSheet.toUpperCase());
                if (sheet != null) {
                    Iterator<Row> iterator = sheet.iterator();
                    iterator.next();
                    while (iterator.hasNext()) {
                        Row row = iterator.next();
                        Utilities.getLblIzquierda().setText("Importando registros...");
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
                            case "generos":
                                Sex sex = Sexs.getByName(row.getCell(1).getStringCellValue().trim());
                                if (sex == null) {
                                    sex = new Sex();
                                    sex.setName(row.getCell(1).getStringCellValue().trim());
                                    sex.save();
                                    FPrincipal.sexs.add(sex);
                                    FPrincipal.sexsWithAll.add(sex);
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
                                Size size;
                                try {
                                    size = Sizes.getByName(row.getCell(1).getStringCellValue().trim());
                                } catch (IllegalStateException e) {
                                    size = Sizes.getByName(String.valueOf(row.getCell(1).getNumericCellValue()));
                                }
                                if (size == null) {
                                    size = new Size();
                                    try {
                                        size.setName(row.getCell(1).getStringCellValue().trim());
                                        System.out.println(row.getCell(1).getStringCellValue().trim());
                                    } catch (IllegalStateException e) {
                                        size.setName(String.valueOf(row.getCell(1).getNumericCellValue()));
                                        System.out.println(row.getCell(1).getNumericCellValue());
                                    }
                                    size.setActive(true);
                                    size.save();
                                    FPrincipal.sizes.add(size);
                                    FPrincipal.sizesWithAll.add(size);
                                }
                                break;
                            case "colores":
                                Color color = Colors.getByName(row.getCell(1).getStringCellValue().trim());
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
                                Brand brand = Brands.getByName(row.getCell(1).getStringCellValue().trim());
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
                            case "estados":
                                Stade stade = Stades.getByName(row.getCell(1).getStringCellValue().trim());
                                if (stade == null) {
                                    stade = new Stade();
                                    stade.setName(row.getCell(1).getStringCellValue().trim());
                                    stade.save();
                                    FPrincipal.stades.add(stade);
                                    FPrincipal.stadesWithAll.add(stade);
                                }
                                break;
                            case "productos":
                                Product product = new Product();
                                style = Styles.get(row.getCell(1).getStringCellValue().trim());
                                product.setStyle(style);
                                try {
                                    size = Sizes.getByName(row.getCell(1).getStringCellValue().trim());
                                } catch (IllegalStateException e) {
                                    size = Sizes.getByName(String.valueOf(row.getCell(1).getNumericCellValue()));
                                }
                                product.setSize(size);
                                color = Colors.getByName(row.getCell(3).getStringCellValue().trim());
                                product.setColor(color);
                                sex = Sexs.getByName(row.getCell(4).getStringCellValue().trim());
                                product.setSex(sex);
                                brand = Brands.getByName(row.getCell(5).getStringCellValue().trim());
                                product.setBrand(brand);
                                Cell cell=row.getCell(6);
                                if(cell!=null){
                                    String valueDimension=cell.getStringCellValue();
                                    dimention = Dimentions.get(valueDimension.trim());
                                    product.setDimention(dimention);
                                }
                                cell=row.getCell(7);
                                if(cell!=null){
                                    String valueStade=cell.getStringCellValue();
                                    stade = Stades.getByName(valueStade.trim());
                                    product.setStade(stade);
                                }
                                try {
                                    product.setBarcode(row.getCell(8).getStringCellValue().trim());
                                } catch (IllegalStateException e) {
                                    product.setBarcode(String.valueOf((int)row.getCell(8).getNumericCellValue()));
                                }
                                product.setSex(sex);
                                product.getStyle().getProducts().add(product);
                                product.save();
                                FPrincipal.products.add(product);
                                break;
                            case "presentaciones":
                                product = Products.get((int) row.getCell(1).getNumericCellValue());
                                if (product != null) {
                                    Presentation presentation= new Presentation(product);
                                    presentation.setName(row.getCell(2).getStringCellValue().trim());
                                    presentation.setQuantity((int)row.getCell(3).getNumericCellValue());
                                    presentation.setDefault(row.getCell(4).getBooleanCellValue());
                                    if(presentation.isDefault()){
                                        product.setPresentationDefault(presentation);
                                    }
                                    product.getPresentations().add(presentation);
                                    product.save();
                                    presentation.save();
                                }
                                break;
                            case "precios":
                                Presentation presentation = Presentations.get((int) row.getCell(1).getNumericCellValue());
                                if (presentation != null) {
                                    Price price= new Price(presentation);
                                    price.setPrice(row.getCell(2).getNumericCellValue());
                                    price.setDefault(row.getCell(3).getBooleanCellValue());
                                    if(price.isDefault()){
                                        presentation.setPriceDefault(price);
                                    }
                                    presentation.save();
                                    price.save();
                                    presentation.getPrices().add(price);
                                }
                                break;
                        }
                    }
                }
            }
            inputStream.close();
            book.close();
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Datos importados");
            Utilities.getLblIzquierda().setText("Éxito: Registros cargados");
            Utilities.getTabbedPane().updateTab();
        } catch (IOException e) {
            Utilities.getLblIzquierda().setText("Error al importar registros");
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Datos incorrectos");
            throw new RuntimeException(e);
        }
    }

    public void exportData(Branch branch){
        Utilities.getLblIzquierda().setText("Exportando productos...");
        HSSFWorkbook book = new HSSFWorkbook();
        CellStyle headerStyle = book.createCellStyle();
        Font font = book.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        HSSFSheet categorias = book.createSheet("categorias");
        HSSFSheet estilos = book.createSheet("estilos");
        HSSFSheet tallas = book.createSheet("tallas");
        HSSFSheet colores = book.createSheet("colores");
        HSSFSheet marcas = book.createSheet("marcas");
        HSSFSheet dimensiones = book.createSheet("dimensiones");
        HSSFSheet estados = book.createSheet("estados");
        HSSFSheet generos = book.createSheet("generos");
        HSSFSheet productos = book.createSheet("productos");
        HSSFSheet presentaciones = book.createSheet("presentaciones");
        HSSFSheet precios = book.createSheet("precios");

        List<Category> categories=new ArrayList<>();
        List<Style> styles=new ArrayList<>();
        List<Size> sizes=new ArrayList<>();
        List<Color> colors=new ArrayList<>();
        List<Brand> brands=new ArrayList<>();
        List<Dimention> dimentions=new ArrayList<>();
        List<Stade> stades=new ArrayList<>();
        List<Sex> sexes=new ArrayList<>();
        List<Product> products=new ArrayList<>();

        HSSFRow productRow = productos.createRow(productos.getLastRowNum()+1);
        HSSFCell hssfCell;
        hssfCell=productRow.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=productRow.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ESTILO");
        hssfCell=productRow.createCell(2);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("TALLA");
        hssfCell=productRow.createCell(3);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("COLOR");
        hssfCell=productRow.createCell(4);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("GENERO");
        hssfCell=productRow.createCell(5);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("MARCA");
        hssfCell=productRow.createCell(6);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("DIMENSION");
        hssfCell=productRow.createCell(7);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ESTADO");
        hssfCell=productRow.createCell(8);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("CODIGO DE BARRAS");

        HSSFRow presentacionesRow = presentaciones.createRow(presentaciones.getLastRowNum()+1);
        hssfCell=presentacionesRow.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=presentacionesRow.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID-PRODUCTO");
        hssfCell=presentacionesRow.createCell(2);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        hssfCell=presentacionesRow.createCell(3);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("CANTIDAD");
        hssfCell=presentacionesRow.createCell(4);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("DEFECTO");

        HSSFRow pricesRow = precios.createRow(precios.getLastRowNum()+1);
        hssfCell=pricesRow.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=pricesRow.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID-PRESENTACION");
        hssfCell=pricesRow.createCell(2);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("PRECIO");
        hssfCell=pricesRow.createCell(3);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("DEFECTO");

        if(branch.getId()!=null){
            branch.getStocks().forEach(stock->{
                categories.remove(stock.getProduct().getStyle().getCategory());
                categories.add(stock.getProduct().getStyle().getCategory());
                styles.remove(stock.getProduct().getStyle());
                styles.add(stock.getProduct().getStyle());
                sizes.remove(stock.getProduct().getSize());
                sizes.add(stock.getProduct().getSize());
                colors.remove(stock.getProduct().getColor());
                colors.add(stock.getProduct().getColor());
                brands.remove(stock.getProduct().getBrand());
                brands.add(stock.getProduct().getBrand());
                if(stock.getProduct().getDimention()!=null){
                    dimentions.remove(stock.getProduct().getDimention());
                    dimentions.add(stock.getProduct().getDimention());
                }
                if(stock.getProduct().getStade()!=null){
                    stades.remove(stock.getProduct().getStade());
                    stades.add(stock.getProduct().getStade());
                }
                sexes.remove(stock.getProduct().getSex());
                sexes.add(stock.getProduct().getSex());
                products.add(stock.getProduct());
            });
        }else{
            categories.addAll(FPrincipal.categories);
            styles.addAll(FPrincipal.styles);
            sizes.addAll(FPrincipal.sizes);
            colors.addAll(FPrincipal.colors);
            brands.addAll(FPrincipal.brands);
            dimentions.addAll(FPrincipal.dimentions);
            stades.addAll(FPrincipal.stades);
            sexes.addAll(FPrincipal.sexs);
            products.addAll(FPrincipal.products);
        }

        products.forEach(product -> {
            if(product!=null){
                HSSFRow productosRow = productos.createRow(productos.getLastRowNum()+1);
                productosRow.createCell(0).setCellValue(product.getId());
                productosRow.createCell(1).setCellValue(product.getStyle().getName());
                productosRow.createCell(2).setCellValue(product.getSize().getName());
                productosRow.createCell(3).setCellValue(product.getColor().getName());
                productosRow.createCell(4).setCellValue(product.getSex().getName());
                productosRow.createCell(5).setCellValue(product.getBrand().getName());
                if(product.getDimention()!=null){
                    productosRow.createCell(6).setCellValue(product.getDimention().getName());
                }
                if(product.getStade()!=null){
                    productosRow.createCell(7).setCellValue(product.getStade().getName());
                }
                productosRow.createCell(8).setCellValue(product.getBarcode());
                product.getPresentations().forEach(presentation -> {
                    HSSFRow presentacionesRow1=presentaciones.createRow(presentaciones.getLastRowNum()+1);
                    presentacionesRow1.createCell(0).setCellValue(presentation.getId());
                    presentacionesRow1.createCell(1).setCellValue(presentation.getProduct().getId());
                    presentacionesRow1.createCell(2).setCellValue(presentation.getName());
                    presentacionesRow1.createCell(3).setCellValue(presentation.getQuantity());
                    presentacionesRow1.createCell(4).setCellValue(presentation.isDefault());
                    presentation.getPrices().forEach(price -> {
                        if(price!=null){
                            HSSFRow pricesRow1=precios.createRow(precios.getLastRowNum()+1);
                            pricesRow1.createCell(0).setCellValue(price.getId());
                            pricesRow1.createCell(1).setCellValue(price.getPresentation().getId());
                            pricesRow1.createCell(2).setCellValue(price.getPrice());
                            pricesRow1.createCell(3).setCellValue(price.isDefault());
                        }
                    });
                });
            }
        });

        HSSFRow header = categorias.createRow(categorias.getLastRowNum()+1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=header.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        categories.forEach(category -> {
            HSSFRow row = categorias.createRow(categorias.getLastRowNum()+1);
            row.createCell(0).setCellValue(category.getId());
            row.createCell(1).setCellValue(category.getName());
        });

        header = estilos.createRow(estilos.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=header.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        hssfCell=header.createCell(2);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("CATEGORIA");
        styles.forEach(style -> {
            HSSFRow row = estilos.createRow(estilos.getLastRowNum()+1);
            row.createCell(0).setCellValue(style.getId());
            row.createCell(1).setCellValue(style.getName());
            row.createCell(2).setCellValue(style.getCategory().getName());
        });

        header = tallas.createRow(tallas.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=header.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        sizes.forEach(size -> {
            HSSFRow row = tallas.createRow(tallas.getLastRowNum()+1);
            row.createCell(0).setCellValue(size.getId());
            row.createCell(1).setCellValue(size.getName());
        });

        header = colores.createRow(colores.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=header.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        colors.forEach(color -> {
            HSSFRow row = colores.createRow(colores.getLastRowNum()+1);
            row.createCell(0).setCellValue(color.getId());
            row.createCell(1).setCellValue(color.getName());
        });

        header = marcas.createRow(marcas.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=header.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        brands.forEach(brand -> {
            HSSFRow row = marcas.createRow(marcas.getLastRowNum()+1);
            row.createCell(0).setCellValue(brand.getId());
            row.createCell(1).setCellValue(brand.getName());
        });

        header = dimensiones.createRow(dimensiones.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=header.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        dimentions.forEach(dimention -> {
            HSSFRow row = dimensiones.createRow(dimensiones.getLastRowNum()+1);
            row.createCell(0).setCellValue(dimention.getId());
            row.createCell(1).setCellValue(dimention.getName());
        });

        header = estados.createRow(estados.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=header.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        System.out.println(stades.size());
        stades.forEach(stade -> {
            HSSFRow row = estados.createRow(estados.getLastRowNum()+1);
            row.createCell(0).setCellValue(stade.getId());
            row.createCell(1).setCellValue(stade.getName());
        });

        header = generos.createRow(generos.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("ID");
        hssfCell=header.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        sexes.forEach(sex -> {
            HSSFRow row = generos.createRow(generos.getLastRowNum()+1);
            row.createCell(0).setCellValue(sex.getId());
            row.createCell(1).setCellValue(sex.getName());
        });
        try {
            String destiny=file.getAbsolutePath();
            FileOutputStream fileOutputStream = new FileOutputStream(destiny+".xls");
            book.write(fileOutputStream);
            fileOutputStream.close();
            book.close();
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Datos exportados");
            Utilities.getLblIzquierda().setText("Éxito: Registros exportados");
            Utilities.getTabbedPane().updateTab();
        } catch (IOException e) {
            Utilities.getLblIzquierda().setText("Error al exportar registros");
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Ocurrió un error");
        }

    }
}
