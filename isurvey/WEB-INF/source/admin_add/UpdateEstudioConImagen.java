/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_add;

import dinamica.GenericTableManager;
import dinamica.Recordset;
import dinamica.StringUtil;
import dinamica.xml.Element;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;

import tokens_participantes.TokenGenerator;
import utils.ImageToGif;

/**
 *
 * @author Randy
 */
public class UpdateEstudioConImagen extends GenericTableManager {

    private long maxsize = 10000000;
    private String imagespath = "/images/";
    private String imagespathreal = "";
    private String prefix = "banner_";
    private String dinamicprefix = null;
    private String saveext = "png";
    private String[] validext = {"jpg,gif,png,bmp"};
    private boolean forcescale = true;
    private boolean aspectradio = true;
    private boolean createthumb = true;
    private int maximgwidth = 189;
    private int maximgheight = 189;
    private int maxthumbheight = 50;
    private int maxthumbwidth = 50;
    private String imgfield[] = null;
    String ext = "";
    BufferedImage imagen = null;
    String filenames[] = null;

    @Override
    public int service(Recordset inputParams) throws Throwable {

        Element conf = getConfig().getDocument().getElement("maxsize");
        if (conf != null) {
            maxsize = Long.parseLong(conf.getValue());
        }

        conf = getConfig().getDocument().getElement("imagespath");
        if (conf != null) {
            imagespathreal = this.getContext().getRealPath(conf.getString());
            imagespath = conf.getString();
        } else {
            imagespathreal = this.getContext().getRealPath(imagespath);
        }

        conf = getConfig().getDocument().getElement("prefix");
        if (conf != null) {
            prefix = conf.getValue();
        }

        conf = getConfig().getDocument().getElement("dinamicprefix");
        if (conf != null) {
            dinamicprefix = inputParams.getString(conf.getValue());
        }
        
        conf = getConfig().getDocument().getElement("saveext");
        if (conf != null) {
            saveext = conf.getValue();
        }

        conf = getConfig().getDocument().getElement("validext");
        if (conf != null) {
            validext = conf.getValue().split(",");
        }

        conf = getConfig().getDocument().getElement("forcescale");
        if (conf != null) {
            forcescale = Boolean.parseBoolean(conf.getValue());
        }

        conf = getConfig().getDocument().getElement("aspectradio");
        if (conf != null) {
            aspectradio = Boolean.parseBoolean(conf.getValue());
        }

        conf = getConfig().getDocument().getElement("createthumb");
        if (conf != null) {
            createthumb = Boolean.parseBoolean(conf.getValue());
        }

        conf = getConfig().getDocument().getElement("maximgwidth");
        if (conf != null) {
            maximgwidth = Integer.parseInt(conf.getValue());
        }

        conf = getConfig().getDocument().getElement("maximgheight");
        if (conf != null) {
            maximgheight = Integer.parseInt(conf.getValue());
        }

        conf = getConfig().getDocument().getElement("maxthumbwidth");
        if (conf != null) {
            maxthumbwidth = Integer.parseInt(conf.getValue());
        }

        conf = getConfig().getDocument().getElement("maxthumbheight");
        if (conf != null) {
            maxthumbheight = Integer.parseInt(conf.getValue());
        }

        conf = getConfig().getDocument().getElement("imgfield");
        if (conf != null) {
            imgfield = conf.getValue().split(",");
            for (int i = 0; i < imgfield.length; i++) {
                if (inputParams.isNull(imgfield[i] + ".filename")) {
                    throw new Throwable("El campo " + imgfield[i] + " de la imagen no existe, " +
                            "revise el formulario o el validator.xml");
                }
            }
            filenames = new String[imgfield.length];
            for (int i = 0; i < imgfield.length; i++) {
                filenames[i] = inputParams.getString(imgfield[i] + ".filename");
            }
        } else {
            throw new Throwable("No esta configurado el parametro imgfield");
        }
        String file = null;
        Recordset newInputParams = inputParams.copyStructure();
        for (int i = 0; i < filenames.length; i++) {
            file = inputParams.getString(imgfield[i]);
            processFile(new File(file), i);
            inputParams.setValue(imgfield[i] + ".filename",
                    prefix + (dinamicprefix != null ? dinamicprefix : "") + "_" 
                    + filenames[i].substring(0,filenames[i].indexOf("."))+"."+saveext);
            inputParams.setValue(imgfield[i],
                    imagespath + "/" + prefix + (dinamicprefix != null ? dinamicprefix : "") + "_" 
                    + filenames[i].substring(0,filenames[i].indexOf("."))+"."+saveext);
            newInputParams.append("thumb_"+imgfield[i], java.sql.Types.VARCHAR);
            newInputParams.append("thumb_"+imgfield[i]+".filename", java.sql.Types.VARCHAR);
        }
        
        newInputParams.addNew();
        inputParams.copyValues(newInputParams);
        
        for (int i = 0; i < filenames.length; i++) {
            newInputParams.setValue("thumb_"+imgfield[i] + ".filename",
                    "thumb_" + prefix + (dinamicprefix != null ? dinamicprefix : "") + "_" 
                    + filenames[i].substring(0,filenames[i].indexOf("."))+"."+saveext);
             newInputParams.setValue("thumb_"+imgfield[i],
                    imagespath + "/" + "thumb_"+ prefix + (dinamicprefix != null ? dinamicprefix : "") + "_" 
                    + filenames[i].substring(0,filenames[i].indexOf("."))+"."+saveext);
        }
        //System.out.println(newInputParams);      

        ///////////////////////////////////////////////

        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String nombre = ((String[]) parametros.get("nombre_estudio"))[0];
        String tipo = ((String[]) parametros.get("tipo"))[0];
        String idEmpresa = ((String[]) parametros.get("id_empresa"))[0];
        String idEstudio = ((String[]) parametros.get("id"))[0];
        TokenGenerator tg = new TokenGenerator();
        Recordset estudio = getEstudio(idEstudio);
        estudio.top();
        String tipoOld = "";
        while (estudio.next()){
            tipoOld = estudio.getString("tipo");
        }
        //Si se esta cambiando de cualquier tipo a abierto-identificado
        if (!tipoOld.equals("Abierto-Identificado") && tipo.equals("Abierto-Identificado")){
            String sql1 = StringUtil.replace(getResource("insert-lista.sql"), "{{nombre_lista}}", nombre);
            sql1 = StringUtil.replace(sql1, "{{id_empresa}}", idEmpresa);
            getDb().exec(sql1);
            String idLista = getIdListaParticipantes(nombre);
            Recordset participantes = getParticipantes(idEmpresa);
            Recordset instrumentos = getInstrumentos(idEstudio);
            participantes.top();
            while (participantes.next()){
                String sql2 = StringUtil.replace(getResource("insert-int-lista-participante.sql"), "{{id_participante}}", participantes.getString("id_participante"));
                sql2 = StringUtil.replace(sql2, "{{id_empresa_participante}}", idEmpresa);
                sql2 = StringUtil.replace(sql2, "{{id_lista_participantes}}", idLista);
                getDb().exec(sql2);
                instrumentos.top();
                while (instrumentos.next()){
                    String token = tg.generarToken(participantes.getString("id_participante"), instrumentos.getString("id_instrumento"));
                    String sql3 = StringUtil.replace(getResource("insert-lime.sql"), "{{id_encuesta}}", instrumentos.getString("id_instrumento"));
                    sql3 = StringUtil.replace(sql3, "{{firstname}}", participantes.getString("nombre_participante"));
                    sql3 = StringUtil.replace(sql3, "{{lastname}}", participantes.getString("apellido_participante"));
                    sql3 = StringUtil.replace(sql3, "{{email}}", participantes.getString("email_participante"));
                    sql3 = StringUtil.replace(sql3, "{{token}}", token);
                    this.getDb().exec(sql3);
                    
                    String sql4 = StringUtil.replace(getResource("insert-lime-respuestas.sql"), "{{id_encuesta}}", instrumentos.getString("id_instrumento"));
                    sql4 = StringUtil.replace(sql4, "{{token}}", token);
                    this.getDb().exec(sql4);
                    
                    String sql5 = StringUtil.replace(getResource("insert-token.sql"), "{{id_participante}}", participantes.getString("id_participante"));
                    sql5 = StringUtil.replace(sql5, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
                    sql5 = StringUtil.replace(sql5, "{{token}}", token);
                    this.getDb().exec(sql5);
                }
            }

            
            String sql4 = StringUtil.replace(getResource("insert-int-lista-estudio.sql"), "{{id_lista}}", idLista);
            getDb().exec(sql4);
        }
        //Si se esta dejando el tipo abierto-identificado igual
        if (tipoOld.equals("Abierto-Identificado") && tipo.equals("Abierto-Identificado")) {

        }
        //Si se esta pasando de abierto-identificado a cualquier otro tipo
        if (tipoOld.equals("Abierto-Identificado") && !tipo.equals("Abierto-Identificado")){

            
            String sql2 = StringUtil.replace(getResource("delete-lista.sql"), "{{nombre_lista}}", nombre);
            getDb().exec(sql2); 
            
            Recordset participantes = getParticipantes(idEmpresa);
            participantes.top();
            Recordset instrumentos = getInstrumentos(idEstudio);
            while (participantes.next()){
                instrumentos.top();
                while (instrumentos.next()){
                    String token = tg.generarToken(participantes.getString("id_participante"), instrumentos.getString("id_instrumento"));
                    String sql3 = StringUtil.replace(getResource("delete-lime-respuestas.sql"), "{{token}}", token);
                    sql3 = StringUtil.replace(sql3, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
                    getDb().exec(sql3);
                    
                    String sql4 = StringUtil.replace(getResource("delete-lime-token.sql"), "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
                    sql4 = StringUtil.replace(sql4, "{{token}}", token);
                    getDb().exec(sql4);
                    
                    String sql5 = StringUtil.replace(getResource("delete-token.sql"), "{{token_participante}}", token);
                    getDb().exec(sql5);
                }
            }
        }

        ///////////////////////////////////////////////
        return super.service(newInputParams);
    }

    /**
     * 
     * @param archivo
     * @return
     */
    public boolean processFile(File archivo, int i) throws Throwable {
        if (archivo.length() > maxsize) {
            //error
            throw new Throwable("el tama�o de la imagen supera el tama�o maximo permitido");
        }
        StringTokenizer st = new StringTokenizer(filenames[i], ".");
        while (st.hasMoreTokens()) {
            ext = st.nextToken();
        }
        ext = ext.toLowerCase();
        List lista = Arrays.asList(validext);

        if (!lista.contains(ext) && !lista.contains("*")) {
            throw new Throwable("la imagen no tiene una extension valida");
        }

        imagen = ImageIO.read(new FileInputStream(archivo));

        if (forcescale) {
            scale(maximgwidth, maximgheight);
            //System.out.println(maximgwidth+" aa "+maximgheight);
        }

        if (createthumb) {
            createAndSaveThumb(i);
        }

        saveFile(i);

        // todo fino
        return true;
    }

    /**
     * 
     * @return
     */
    public String saveFile(int i) throws Throwable {
        if (saveext.equals("gif")) {
            File outputFile = new File(imagespathreal + "/" + prefix + (dinamicprefix != null ? dinamicprefix : "") + "_" 
                    + filenames[i].substring(0,filenames[i].indexOf("."))+"."+saveext );
            FileOutputStream fos = new FileOutputStream(outputFile);
            ImageToGif gif = new ImageToGif(imagen, fos);
            gif.startProc();
            fos.close();
            imagen = null;
            return imagespathreal + "/" + prefix + (dinamicprefix != null ? dinamicprefix : "") + "_" 
                    + filenames[i].substring(0,filenames[i].indexOf("."))+"."+saveext;
        } else {
            ImageIO.write(imagen, saveext, new File(imagespathreal + "/" + prefix + (dinamicprefix != null ? dinamicprefix : "") + "_" 
                    + filenames[i].substring(0,filenames[i].indexOf("."))+"."+saveext));
            imagen = null;
            return imagespathreal + "/" + prefix + (dinamicprefix != null ? dinamicprefix : "") + "_" 
                    + filenames[i].substring(0,filenames[i].indexOf("."))+"."+saveext;
        }
    }

    /**
     * 
     * @param destWidth
     * @param destHeight
     * @throws java.io.IOException
     */
    public void scale(int destWidth, int destHeight) throws Throwable {
        if (imagen != null) {
            double newWidth = destWidth;
            double newHeight = destHeight;
            if (aspectradio) {
                if (imagen.getWidth() >= newWidth || imagen.getHeight() >= newHeight) {
                    if (imagen.getWidth() < imagen.getHeight()) {
                        newWidth = (imagen.getWidth() * newHeight) / imagen.getHeight();
                    } else {
                        newHeight = (imagen.getHeight() * newWidth) / imagen.getWidth();
                    }
                    BufferedImage dest = new BufferedImage(new Double(newWidth).intValue(), new Double(newHeight).intValue(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = dest.createGraphics();
                    AffineTransform at = AffineTransform.getScaleInstance((double) newWidth / imagen.getWidth(), (double) newHeight / imagen.getHeight());
                    g.drawRenderedImage(imagen, at);
                    imagen = dest;
                }
            }
        } else {
            throw new Throwable("No se puede escalar la imagen porque el archivo no es una imagen");
        }
    }

    /**
     * 
     */
    public void createAndSaveThumb(int i) throws Throwable {
        if (imagen != null) {
            double newWidth = maxthumbwidth;
            double newHeight = maxthumbheight;
            if (aspectradio) {
                if (imagen.getWidth() >= newWidth || imagen.getHeight() >= newHeight) {
                    if (imagen.getWidth() < imagen.getHeight()) {
                        newWidth = (imagen.getWidth() * newHeight) / imagen.getHeight();
                    } else {
                        newHeight = (imagen.getHeight() * newWidth) / imagen.getWidth();
                    }
                } else {
                    newWidth = imagen.getWidth();
                    newHeight = imagen.getHeight();
                }
            }
            BufferedImage dest = new BufferedImage(new Double(newWidth).intValue(), new Double(newHeight).intValue(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = dest.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance((double) newWidth / imagen.getWidth(), (double) newHeight / imagen.getHeight());
            g.drawRenderedImage(imagen, at);

            if (saveext.equals("gif")) {
                File outputFile = new File(imagespathreal + "/" + "thumb_" + prefix + (dinamicprefix != null ? dinamicprefix : "") + "_" 
                        + filenames[i].substring(0,filenames[i].indexOf("."))+"."+saveext);
                FileOutputStream fos = new FileOutputStream(outputFile);
                ImageToGif gif = new ImageToGif(dest, fos);
                gif.startProc();
                fos.close();
            } else {
                ImageIO.write(dest, saveext, new File(imagespathreal + "/" + "thumb_" + prefix + (dinamicprefix != null ? dinamicprefix : "") + "_" 
                        + filenames[i].substring(0,filenames[i].indexOf("."))+"."+saveext));
            }
        } else {
            throw new Throwable("No se le puede crear thumbnail porque el archivo no es una imagen");
        }
    }

    ///////////////////////////////////////////

        Recordset getInstrumentos (String idEstudio) throws Throwable{
        String query = "select * from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio;
        return this.getDb().get(query);
    }
    
    Recordset getEstudio (String id) throws Throwable{
        String query = "select * from ajvieira_isurvey_app.estudio where id_estudio = " + id;
        return this.getDb().get(query);
    }
    
    String getIdEstudio (String nombre) throws Throwable{
        String query = "select * from ajvieira_isurvey_app.estudio where nombre_estudio = '" + nombre + "'";
        Recordset lista = this.getDb().get(query);
        lista.top();
        String id = "";
        while (lista.next()){
            id = lista.getString("id_estudio");
        }
        return id;
    }
    
    String getIdListaParticipantes (String nombre) throws Throwable{
        String query = "select * from ajvieira_isurvey_app.lista_participantes where nombre_lista_participantes = '" + nombre + "' and oculta = 'Si'";
        Recordset lista = this.getDb().get(query);
        lista.top();
        String id = "";
        while (lista.next()){
            id = lista.getString("id_lista_participantes");
        }
        return id;
    }
    
    Recordset getParticipantes(String idEmpresa) throws Throwable{
        String query = "select id_participante, nombre_participante, apellido_participante, email_participante" +
                " from ajvieira_isurvey_app.participante where id_empresa = " + idEmpresa;
        return this.getDb().get(query);
    }

    ///////////////////////////////////////////
}
