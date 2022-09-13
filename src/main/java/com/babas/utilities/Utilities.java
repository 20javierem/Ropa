package com.babas.utilities;

import ch.swaechter.smbjwrapper.SmbConnection;
import ch.swaechter.smbjwrapper.SmbDirectory;
import ch.swaechter.smbjwrapper.SmbFile;
import com.babas.custom.TabbedPane;
import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatToggleButton;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.*;
import com.hierynomus.smbj.auth.AuthenticationContext;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.*;

public class Utilities {
    public static DateFormat formatoFecha=new SimpleDateFormat("dd-MM-yyyy");
    public static DateFormat formatoFechaHora=new SimpleDateFormat("dd/MM/yyyy: h:mm a");
    public static DateFormat formatoHora=new SimpleDateFormat("HH:mm a");
    public static DateFormat año=new SimpleDateFormat("yyyy");
    public static NumberFormat moneda = NumberFormat.getCurrencyInstance();
    public static NumberFormat numberFormat=new DecimalFormat("###,###,###.##");
    public static String getFormatoFecha(){
        return "dd-MM-yyyy";
    }
    private static JFrame jFrame;
    private static TabbedPane tabbedPane;
    public static Propiedades propiedades;
    public static SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
    public static List<JTable> tables=new ArrayList<>();
    public static JLabel lblIzquiera;
    public static JButton actions=new JButton();

    public static void setActionsdOfDialog(ActionListener action){
        actions=new JButton();
        actions.addActionListener(action);
    }

    public static void updateDialog(){
        actions.doClick();
    }

    public static JSpinner.NumberEditor getEditorPrice(FlatSpinner spinner) {
        return new JSpinner.NumberEditor(spinner, "###,###,###.##");
    }

    public static boolean newImage(InputStream imageImput,String imageName){
        AuthenticationContext auth = new AuthenticationContext("javier", "ernestomoreno".toCharArray(), "localhost");
        try (SmbConnection smbConnection = new SmbConnection("192.168.0.119", "clothes", auth)) {
            SmbDirectory dirProducts = new SmbDirectory(smbConnection, "products/");
            SmbFile file = new SmbFile(smbConnection,dirProducts.getPath()+imageName);
            OutputStream out = file.getOutputStream();
            IOUtils.copy(imageImput, out);
            imageImput.close();
            out.close();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Image getImage(String imageName){
        AuthenticationContext auth = new AuthenticationContext("javier", "ernestomoreno".toCharArray(), "localhost");
        try (SmbConnection smbConnection = new SmbConnection("192.168.0.119", "clothes", auth)) {
            SmbDirectory dirProducts = new SmbDirectory(smbConnection, "products/");
            SmbFile file = new SmbFile(smbConnection,dirProducts.getPath()+imageName);
            return ImageIO.read(file.getInputStream());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean downloadImage(String imageName){
        AuthenticationContext auth = new AuthenticationContext("javier", "ernestomoreno".toCharArray(), "localhost");
        try (SmbConnection smbConnection = new SmbConnection("192.168.0.119", "clothes", auth)) {
            SmbDirectory dirProducts = new SmbDirectory(smbConnection, "products/");
            SmbFile file = new SmbFile(smbConnection,dirProducts.getPath()+"miimagen.png");
            OutputStream out = new FileOutputStream("F:\\"+imageName);
            IOUtils.copy(file.getInputStream(), out);
            out.close();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void loadTheme(){
        UIManager.getDefaults().put("defaultFont",propiedades.getFont());
        FlatLaf.registerCustomDefaultsSource("com.babas.themes");
        switch (propiedades.getTema()){
            case "Ligth":
                FlatIntelliJLaf.setup();
                break;
            case "Oscuro":
                FlatDarkLaf.setup();
                break;
            case "Claro":
                FlatLightLaf.setup();
                break;
            case "Darcula":
                FlatDarculaLaf.setup();
                break;
            case "Arc":
                FlatArcIJTheme.setup();
                break;
            case "Arc - Orange":
                FlatArcOrangeIJTheme.setup();
                break;
            case "Arc Dark":
                FlatArcDarkIJTheme.setup();
                break;
            case "Arc Dark - Orange":
                FlatArcDarkOrangeIJTheme.setup();
                break;
            case "Carbon":
                FlatCarbonIJTheme.setup();
                break;
            case "Cobalt 2":
                FlatCobalt2IJTheme.setup();
                break;
            case "Cyan light":
                FlatCyanLightIJTheme.setup();
                break;
            case "Dark Flat":
                FlatDarkFlatIJTheme.setup();
                break;
            case "Dark purple":
                FlatDarkPurpleIJTheme.setup();
                break;
            case "Dracula":
                FlatDraculaIJTheme.setup();
                break;
            case "Gradianto Dark Fuchsia":
                FlatGradiantoDarkFuchsiaIJTheme.setup();
                break;
            case "Gradianto Deep Ocean":
                FlatGradiantoDeepOceanIJTheme.setup();
                break;
            case "Gradianto Midnight Blue":
                FlatGradiantoMidnightBlueIJTheme.setup();
                break;
            case "Gradianto Nature Green":
                FlatGradiantoNatureGreenIJTheme.setup();
                break;
            case "Gray":
                FlatGrayIJTheme.setup();
                break;
            case "Gruvbox Dark Hard":
                FlatGruvboxDarkHardIJTheme.setup();
                break;
            case "Gruvbox Dark Medium":
                FlatGruvboxDarkMediumIJTheme.setup();
                break;
            case "Gruvbox Dark Soft":
                FlatGruvboxDarkSoftIJTheme.setup();
                break;
            case "Hiberbee Dark":
                FlatHiberbeeDarkIJTheme.setup();
                break;
            case "High contrast":
                FlatHighContrastIJTheme.setup();
                break;
            case "Light Flat":
                FlatLightFlatIJTheme.setup();
                break;
            case "Material Design Dark":
                FlatMaterialDesignDarkIJTheme.setup();
                break;
            case "Monocai":
                FlatMonocaiIJTheme.setup();
                break;
            case "Monokai Pro":
                FlatMonokaiProIJTheme.setup();
                break;
            case "Nord":
                FlatNordIJTheme.setup();
                break;
            case "One Dark":
                FlatOneDarkIJTheme.setup();
                break;
            case "Solarized Dark":
                FlatSolarizedDarkIJTheme.setup();
                break;
            case "Solarized Light":
                FlatSolarizedLightIJTheme.setup();
                break;
            case "Spacegray":
                FlatSpacegrayIJTheme.setup();
                break;
            case "Vuesion":
                FlatVuesionIJTheme.setup();
                break;
            case "Xcode-Dark":
                FlatXcodeDarkIJTheme.setup();
                break;
            case "Arc Dark (Material)":
                FlatArcDarkIJTheme.setup();
                break;
            case "Arc Dark Contrast (Material)":
                FlatArcDarkContrastIJTheme.setup();
                break;
            case "Atom One Dark (Material)":
                FlatAtomOneDarkIJTheme.setup();
                break;
            case "Atom One Dark Contrast (Material)":
                FlatAtomOneDarkContrastIJTheme.setup();
                break;
            case "Atom One Light (Material)":
                FlatAtomOneLightIJTheme.setup();
                break;
            case "Atom One Light Contrast (Material)":
                FlatAtomOneLightContrastIJTheme.setup();
                break;
            case "Dracula (Material)":
                FlatDraculaIJTheme.setup();
                break;
            case "Dracula Contrast (Material)":
                FlatDraculaContrastIJTheme.setup();
                break;
            case "GitHub (Material)":
                FlatGitHubIJTheme.setup();
                break;
            case "GitHub Contrast (Material)":
                FlatGitHubContrastIJTheme.setup();
                break;
            case "GitHub Dark (Material)":
                FlatGitHubDarkIJTheme.setup();
                break;
            case "GitHub Dark Contrast (Material)":
                FlatGitHubDarkContrastIJTheme.setup();
                break;
            case "Light Owl (Material)":
                FlatLightOwlIJTheme.setup();
                break;
            case "Light Owl Contrast (Material)":
                FlatLightOwlContrastIJTheme.setup();
                break;
            case "Material Darker (Material)":
                FlatMaterialDarkerIJTheme.setup();
                break;
            case "Material Darker Contrast (Material)":
                FlatMaterialDarkerContrastIJTheme.setup();
                break;
            case "Material Deep Ocean (Material)":
                FlatMaterialDeepOceanIJTheme.setup();
                break;
            case "Material Deep Ocean Contrast (Material)":
                FlatMaterialDeepOceanContrastIJTheme.setup();
                break;
            case "Material Lighter (Material)":
                FlatMaterialLighterIJTheme.setup();
                break;
            case "Material Lighter Contrast (Material)":
                FlatMaterialLighterContrastIJTheme.setup();
                break;
            case "Material Oceanic (Material)":
                FlatMaterialOceanicIJTheme.setup();
                break;
            case "Material Oceanic Contrast (Material)":
                FlatMaterialOceanicContrastIJTheme.setup();
                break;
            case "Material Palenight (Material)":
                FlatMaterialPalenightIJTheme.setup();
                break;
            case "Material Palenight Contrast (Material)":
                FlatMaterialPalenightContrastIJTheme.setup();
                break;
            case "Monokai Pro (Material)":
                FlatMonokaiProIJTheme.setup();
                break;
            case "Monokai Pro Contrast (Material)":
                FlatMonokaiProContrastIJTheme.setup();
                break;
            case "Moonlight (Material)":
                FlatMoonlightIJTheme.setup();
                break;
            case "Moonlight Contrast (Material)":
                FlatMoonlightContrastIJTheme.setup();
                break;
            case "Night Owl (Material)":
                FlatNightOwlIJTheme.setup();
                break;
            case "Night Owl Contrast (Material)":
                FlatNightOwlContrastIJTheme.setup();
                break;
            case "Solarized Dark (Material)":
                FlatSolarizedDarkIJTheme.setup();
                break;
            case "Solarized Dark Contrast (Material)":
                FlatSolarizedDarkContrastIJTheme.setup();
                break;
            case "Solarized Light (Material)":
                FlatSolarizedLightIJTheme.setup();
                break;
            case "Solarized Light Contrast (Material)":
                FlatSolarizedLightContrastIJTheme.setup();
                break;

        }
    }

    public static void setJFrame(JFrame jFrame){
        Utilities.jFrame=jFrame;
    }

    public static JFrame getJFrame(){
        return jFrame;
    }

    public static TabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public static void setTabbedPane(TabbedPane tabbedPane) {
        Utilities.tabbedPane = tabbedPane;
    }

    public static void updateUI(){
        FlatAnimatedLafChange.showSnapshot();
        FlatLaf.updateUI();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static Date getDateStart(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,-1);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return calendar.getTime();
    }

    public static Date getDateEnd(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return calendar.getTime();
    }

    public static Date getDateGreaterThan(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,00);
        calendar.add(Calendar.SECOND,-1);
        return calendar.getTime();
    }
    public static Date getDateLessThan(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,00);
        return calendar.getTime();
    }

    public static boolean precioEsValido(KeyEvent e, String precio){
        int caracter = e.getKeyChar();
        if(caracter==46){
            if(precio.lastIndexOf('.')==-1){
                if(precio.length()>0){
                    return true;
                }
                return false;
            }else{
                return false;
            }
        }else{
            if(caracter >= 48 && caracter <= 57){
                return true;
            }else{
                return false;
            }
        }
    }

    public static void buttonSelected(FlatToggleButton boton){
        boton.setSelected(true);
    }

    public static void despintarButton(FlatToggleButton boton){
        boton.setSelected(false);
    }

    public static Date convertLocalTimeToDate(LocalTime time) {
        Instant instant = time.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        return toDate(instant);
    }

    private static Date toDate(Instant instant) {
        BigInteger millis = BigInteger.valueOf(instant.getEpochSecond()).multiply(
                BigInteger.valueOf(1000));
        millis = millis.add(BigInteger.valueOf(instant.getNano()).divide(
                BigInteger.valueOf(1_000_000)));
        return new Date(millis.longValue());
    }

    public static Date localDateToDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static LocalDate dateToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static String getCodeOfName(String nameProduct){
        nameProduct=nameProduct.trim();
        boolean stade=true;
        String code="";
        if(!nameProduct.isEmpty()){
            int aux;
            do {
                code+=nameProduct.substring(0,1);
                aux=nameProduct.indexOf(" ");
                if(aux!=-1){
                    nameProduct=nameProduct.substring(aux+1);
                }else{
                    stade=false;
                }
            }while (stade);
        }
        return code;
    }

    public static Vector invertirVector(Vector vector){
        Object ventaAUX;
        for(int i=0;i<vector.size()/2;i++){
            ventaAUX=vector.get(i);
            vector.set(i, vector.get(vector.size() - i-1));
            vector.set((vector.size()-i-1), ventaAUX);
        }
        return vector;
    }

    public static Integer calcularaños(Date fecha){
        Calendar hoy=Calendar.getInstance();
        Calendar nacimiento=Calendar.getInstance();
        nacimiento.setTime(fecha);
        int años= hoy.get(Calendar.YEAR)-nacimiento.get(Calendar.YEAR);
        int meses= hoy.get(Calendar.MONTH)-nacimiento.get(Calendar.MONTH);
        int dias= hoy.get(Calendar.DAY_OF_MONTH)-nacimiento.get(Calendar.DAY_OF_MONTH);
        switch (meses){
            case 0:
                if(dias<0){
                    años-=1;
                }
                break;
            default:
                if(meses<0){
                    años-=1;
                }
        }
        return años<0?0:años;
    }

    public static SecretKeySpec getSecretKey(){
        Propiedades propiedades=new Propiedades();
        byte[] salt = "12345678".getBytes();
        int iterationCount = 40000;
        int keyLength = 128;
        try {
            return createSecretKey(propiedades.getKey().toCharArray(),salt,iterationCount,keyLength);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }
    public static String encriptar(String contraseña){
        return encrypt(contraseña,getSecretKey());
    }
    public static String desencriptar(String contraseña){
        return decrypt(contraseña,getSecretKey());
    }
    public static String encrypt(String dataToEncrypt, SecretKeySpec key) {
        try {
            Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key);
            AlgorithmParameters parameters = pbeCipher.getParameters();
            IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
            byte[] cryptoText = pbeCipher.doFinal(dataToEncrypt.getBytes("UTF-8"));
            byte[] iv = ivParameterSpec.getIV();
            return base64Encode(iv) + ":" + base64Encode(cryptoText);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error, notifique al administrador");
        }
        return null;
    }
    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String string, SecretKeySpec key) {
        try {
            String iv = string.split(":")[0];
            String property = string.split(":")[1];
            Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
            return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<JTable> getTables(){
        return tables;
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }

    public static void setPropiedades(Propiedades propiedades) {
        Utilities.propiedades=propiedades;
    }
    public static Propiedades getPropiedades(){
        return propiedades;
    }
}
