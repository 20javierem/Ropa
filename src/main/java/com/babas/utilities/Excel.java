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
                "CATEGORIAS",
                "ESTILOS",
                "GENEROS",
                "TALLAS",
                "COLORES",
                "MARCAS",
                "DIMENSIONES",
                "ESTADOS",
                "PRODUCTOS",
                "PRESENTACIONES",
                "PRECIOS",
                "STOCKS"};
    }
    public void loadData() {
        JFileChooser selectFile = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("*.excel", "xlsx","xls", "slxs");
        selectFile.addChoosableFileFilter(filter);
        int result = selectFile.showOpenDialog(Utilities.getJFrame());
        if (result != JFileChooser.APPROVE_OPTION) return;
        Utilities.getLblIzquierda().setText("Importando productos...");
        try {
            FileInputStream inputStream = new FileInputStream(selectFile.getSelectedFile());
            HSSFWorkbook book = new HSSFWorkbook(inputStream);
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = book.getCreationHelper().createFormulaEvaluator();
            for (String nameSheet : sheets) {
                Sheet sheet = book.getSheet(nameSheet.toUpperCase());
                if (sheet != null) {
                    Iterator<Row> iterator = sheet.iterator();
                    iterator.next();
                    while (iterator.hasNext()) {
                        Row row = iterator.next();
                        Utilities.getLblIzquierda().setText("Importando registros...");
                        switch (nameSheet) {
                            case "CATEGORIAS":
                                String data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                if (data.isEmpty()) continue;
                                Category category = Categorys.get(data);
                                if (category == null) {
                                    category = new Category();
                                    category.setName(data);
                                    category.save();
                                    FPrincipal.categories.add(category);
                                    FPrincipal.categoriesWithAll.add(category);
                                }
                                break;
                            case "GENEROS":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                if (data.isEmpty()) continue;
                                Sex sex = Sexs.getByName(data);
                                if (sex == null) {
                                    sex = new Sex();
                                    sex.setName(data);
                                    sex.save();
                                    FPrincipal.sexs.add(sex);
                                    FPrincipal.sexsWithAll.add(sex);
                                }
                                break;
                            case "ESTILOS":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                if (data.isEmpty()) continue;
                                category = Categorys.get(formatter.formatCellValue(row.getCell(1), evaluator).trim());
                                Style style = Styles.get(data);
                                if (style == null) {
                                    style = new Style();
                                    style.setName(data);
                                    style.setCategory(category);
                                    style.save();
                                    FPrincipal.styles.add(style);
                                }
                                break;
                            case "TALLAS":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                if (data.isEmpty()) continue;
                                Size size = Sizes.getByName(data);
                                if (size == null) {
                                    size = new Size();
                                    size.setName(data);
                                    size.setActive(true);
                                    size.save();
                                    FPrincipal.sizes.add(size);
                                    FPrincipal.sizesWithAll.add(size);
                                }
                                break;
                            case "COLORES":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                if (data.isEmpty()) continue;
                                Color color = Colors.getByName(data);
                                if (color == null) {
                                    color = new Color();
                                    color.setName(data);
                                    color.setActive(true);
                                    color.save();
                                    FPrincipal.colors.add(color);
                                    FPrincipal.colorsWithAll.add(color);
                                }
                                break;
                            case "MARCAS":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                if (data.isEmpty()) continue;
                                Brand brand = Brands.getByName(data);
                                if (brand == null) {
                                    brand = new Brand();
                                    brand.setName(data);
                                    brand.setActive(true);
                                    brand.save();
                                    FPrincipal.brands.add(brand);
                                    FPrincipal.brandsWithAll.add(brand);
                                }
                                break;
                            case "DIMENSIONES":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                if (data.isEmpty()) continue;
                                Dimention dimention = Dimentions.get(data);
                                if (dimention == null) {
                                    dimention = new Dimention();
                                    dimention.setName(data);
                                    dimention.setActive(true);
                                    dimention.save();
                                    FPrincipal.dimentions.add(dimention);
                                    FPrincipal.dimentionsWithAll.add(dimention);
                                }
                                break;
                            case "ESTADOS":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                if (data.isEmpty()) continue;
                                Stade stade = Stades.getByName(data);
                                if (stade == null) {
                                    stade = new Stade();
                                    stade.setName(data);
                                    stade.save();
                                    FPrincipal.stades.add(stade);
                                    FPrincipal.stadesWithAll.add(stade);
                                }
                                break;
                            case "PRODUCTOS":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                Product product=Products.getByUniqueCode(data);
                                if(product==null){
                                    product = new Product();
                                    product.setUniqueCode(data);
                                    style = Styles.get(formatter.formatCellValue(row.getCell(1), evaluator).trim());
                                    product.setStyle(style);
                                    size = Sizes.getByName(formatter.formatCellValue(row.getCell(2), evaluator).trim());
                                    product.setSize(size);
                                    color = Colors.getByName(formatter.formatCellValue(row.getCell(3), evaluator).trim());
                                    product.setColor(color);
                                    sex = Sexs.getByName(formatter.formatCellValue(row.getCell(4), evaluator).trim());
                                    product.setSex(sex);
                                    brand = Brands.getByName(formatter.formatCellValue(row.getCell(5), evaluator).trim());
                                    product.setBrand(brand);
                                    data = formatter.formatCellValue(row.getCell(6), evaluator).trim();
                                    if(!data.isEmpty()){
                                        dimention = Dimentions.get(data);
                                        product.setDimention(dimention);
                                    }
                                    data = formatter.formatCellValue(row.getCell(7), evaluator).trim();
                                    if(!data.isEmpty()){
                                        stade = Stades.getByName(data);
                                        product.setStade(stade);
                                    }
                                    product.setBarcode(formatter.formatCellValue(row.getCell(8), evaluator).trim());
                                    product.setSex(sex);
                                    product.getStyle().getProducts().add(product);
                                    product.save();
                                    FPrincipal.products.add(product);
                                }
                                break;
                            case "PRESENTACIONES":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                Presentation presentation=Presentations.getByUniqueCode(data);
                                data = formatter.formatCellValue(row.getCell(1), evaluator).trim();
                                product = Products.getByUniqueCode(data);
                                if (presentation==null&&product != null) {
                                    presentation= new Presentation(product);
                                    presentation.setUniqueCode(formatter.formatCellValue(row.getCell(0), evaluator).trim());
                                    presentation.setName(formatter.formatCellValue(row.getCell(2), evaluator).trim());
                                    presentation.setQuantity((int)row.getCell(3).getNumericCellValue());
                                    presentation.setDefault(row.getCell(4).getStringCellValue().trim().equalsIgnoreCase("true"));
                                    if(presentation.isDefault()){
                                        product.setPresentationDefault(presentation);
                                    }
                                    product.getPresentations().add(presentation);
                                    product.save();
                                    presentation.save();
                                }
                                break;
                            case "PRECIOS":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                presentation = Presentations.getByUniqueCode(data);
                                if (presentation != null) {
                                    data = formatter.formatCellValue(row.getCell(1), evaluator).trim();
                                    if(!data.isEmpty()){
                                        Price price=Prices.getByUniqueCode(presentation.getUniqueCode(), Double.parseDouble(data));
                                        if(price==null){
                                            price= new Price(presentation);
                                            price.setPrice(row.getCell(1).getNumericCellValue());
                                            price.setDefault(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("true"));
                                            if(price.isDefault()){
                                                presentation.setPriceDefault(price);
                                            }
                                            presentation.save();
                                            price.save();
                                            presentation.getPrices().add(price);
                                        }
                                    }

                                }
                                break;
                            case "STOCKS":
                                data = formatter.formatCellValue(row.getCell(0), evaluator).trim();
                                product=Products.getByUniqueCode(data);
                                data = formatter.formatCellValue(row.getCell(1), evaluator).trim();
                                Branch branch=Branchs.getByName(data);
                                if(product!=null&&branch!=null){
                                    Stock stock=Stocks.getStock(branch,product);
                                    if(stock==null){
                                        stock=new Stock();
                                        stock.setBranch(branch);
                                        stock.setProduct(product);
                                        product.getStocks().add(stock);
                                        branch.getStocks().add(stock);
                                    }
                                    stock.setQuantity((int)row.getCell(2).getNumericCellValue());
                                    stock.setOnRental((int)row.getCell(3).getNumericCellValue());
                                    stock.setOnStock((int)row.getCell(4).getNumericCellValue());
                                    stock.setTimesRented((int)row.getCell(5).getNumericCellValue());
                                    stock.setOnReserve((int)row.getCell(6).getNumericCellValue());
                                    stock.save();
                                }
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
        JFileChooser selectFile = new JFileChooser();
        int result = selectFile.showSaveDialog(Utilities.getJFrame());
        if (result != JFileChooser.APPROVE_OPTION) return;

        Utilities.getLblIzquierda().setText("Exportando productos...");
        HSSFWorkbook book = new HSSFWorkbook();
        CellStyle headerStyle = book.createCellStyle();
        Font font = book.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        HSSFSheet categorias = book.createSheet("CATEGORIAS");
        HSSFSheet estilos = book.createSheet("ESTILOS");
        HSSFSheet tallas = book.createSheet("TALLAS");
        HSSFSheet colores = book.createSheet("COLORES");
        HSSFSheet marcas = book.createSheet("MARCAS");
        HSSFSheet dimensiones = book.createSheet("DIMENSIONES");
        HSSFSheet estados = book.createSheet("ESTADOS");
        HSSFSheet generos = book.createSheet("GENEROS");
        HSSFSheet productos = book.createSheet("PRODUCTOS");
        HSSFSheet presentaciones = book.createSheet("PRESENTACIONES");
        HSSFSheet precios = book.createSheet("PRECIOS");
        HSSFSheet stocks = book.createSheet("STOCKS");

        List<Category> categories=new ArrayList<>();
        List<Style> styles=new ArrayList<>();
        List<Size> sizes=new ArrayList<>();
        List<Color> colors=new ArrayList<>();
        List<Brand> brands=new ArrayList<>();
        List<Dimention> dimentions=new ArrayList<>();
        List<Stade> stades=new ArrayList<>();
        List<Sex> sexes=new ArrayList<>();
        List<Product> products=new ArrayList<>();
        List<Stock> stocks1=new ArrayList<>();

        HSSFCell hssfCell;

        HSSFRow productRow = productos.createRow(productos.getLastRowNum()+1);
        hssfCell=productRow.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("IDENTIFICADOR");
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
        hssfCell.setCellValue("IDENTIFICADOR");
        hssfCell=presentacionesRow.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("IDENTIFICADOR-PRODUCTO");
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
        hssfCell.setCellValue("IDENTIFICADOR-PRESENTACION");
        hssfCell=pricesRow.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("PRECIO");
        hssfCell=pricesRow.createCell(2);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("DEFECTO");

        if(branch.getId()!=null){
            stocks1.addAll(branch.getStocks());
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
            stocks1.addAll(Stocks.getActives());
        }

        products.forEach(product -> {
            if(product!=null){
                HSSFRow productosRow = productos.createRow(productos.getLastRowNum()+1);
                productosRow.createCell(0).setCellValue(product.getUniqueCode());
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
                    presentacionesRow1.createCell(0).setCellValue(presentation.getUniqueCode());
                    presentacionesRow1.createCell(1).setCellValue(presentation.getProduct().getUniqueCode());
                    presentacionesRow1.createCell(2).setCellValue(presentation.getName());
                    presentacionesRow1.createCell(3).setCellValue(presentation.getQuantity());
                    presentacionesRow1.createCell(4).setCellValue(String.valueOf(presentation.isDefault()));
                    presentation.getPrices().forEach(price -> {
                        if(price!=null){
                            HSSFRow pricesRow1=precios.createRow(precios.getLastRowNum()+1);
                            pricesRow1.createCell(0).setCellValue(price.getPresentation().getUniqueCode());
                            pricesRow1.createCell(1).setCellValue(price.getPrice());
                            pricesRow1.createCell(2).setCellValue(String.valueOf(price.isDefault()));
                        }
                    });
                });
            }
        });
        HSSFRow stocksRow = stocks.createRow(stocks.getLastRowNum()+1);
        hssfCell=stocksRow.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("IDENTIFICADOR-PRODUCTO");
        hssfCell=stocksRow.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE-SUCURSAL");
        hssfCell=stocksRow.createCell(2);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("CANTIDAD");
        hssfCell=stocksRow.createCell(3);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("EN ALQUILER");
        hssfCell=stocksRow.createCell(4);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("EN STOCK");
        hssfCell=stocksRow.createCell(5);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("VECES ALQUILADO");
        hssfCell=stocksRow.createCell(6);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("EN RESERVA");
        stocks1.forEach(stock -> {
            HSSFRow row = stocks.createRow(stocks.getLastRowNum()+1);
            row.createCell(0).setCellValue(stock.getProduct().getUniqueCode());
            row.createCell(1).setCellValue(stock.getBranch().getName());
            row.createCell(2).setCellValue(stock.getQuantity());
            row.createCell(3).setCellValue(stock.getOnRental());
            row.createCell(4).setCellValue(stock.getOnStock());
            row.createCell(5).setCellValue(stock.getTimesRented());
            row.createCell(6).setCellValue(stock.getOnReserve());
        });

        HSSFRow header = categorias.createRow(categorias.getLastRowNum()+1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        categories.forEach(category -> {
            HSSFRow row = categorias.createRow(categorias.getLastRowNum()+1);
            row.createCell(0).setCellValue(category.getName());
        });

        header = estilos.createRow(estilos.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        hssfCell=header.createCell(1);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("CATEGORIA");
        styles.forEach(style -> {
            HSSFRow row = estilos.createRow(estilos.getLastRowNum()+1);
            row.createCell(0).setCellValue(style.getName());
            row.createCell(1).setCellValue(style.getCategory().getName());
        });

        header = tallas.createRow(tallas.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        sizes.forEach(size -> {
            HSSFRow row = tallas.createRow(tallas.getLastRowNum()+1);
            row.createCell(0).setCellValue(size.getName());
        });

        header = colores.createRow(colores.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        colors.forEach(color -> {
            HSSFRow row = colores.createRow(colores.getLastRowNum()+1);
            row.createCell(0).setCellValue(color.getName());
        });

        header = marcas.createRow(marcas.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        brands.forEach(brand -> {
            HSSFRow row = marcas.createRow(marcas.getLastRowNum()+1);
            row.createCell(0).setCellValue(brand.getName());
        });

        header = dimensiones.createRow(dimensiones.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        dimentions.forEach(dimention -> {
            HSSFRow row = dimensiones.createRow(dimensiones.getLastRowNum()+1);
            row.createCell(0).setCellValue(dimention.getName());
        });

        header = estados.createRow(estados.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        System.out.println(stades.size());
        stades.forEach(stade -> {
            HSSFRow row = estados.createRow(estados.getLastRowNum()+1);
            row.createCell(0).setCellValue(stade.getName());
        });

        header = generos.createRow(generos.getLastRowNum() + 1);
        hssfCell=header.createCell(0);
        hssfCell.setCellStyle(headerStyle);
        hssfCell.setCellValue("NOMBRE");
        sexes.forEach(sex -> {
            HSSFRow row = generos.createRow(generos.getLastRowNum()+1);
            row.createCell(0).setCellValue(sex.getName());
        });
        try {
            String destiny=selectFile.getSelectedFile().getAbsolutePath();
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
