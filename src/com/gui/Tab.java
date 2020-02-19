package com.gui;

import com.estaticas.Helper;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tab extends RTextScrollPane {

    private String Ext;
    private String Path;
    private String FileName;

    public Tab(RSyntaxTextArea text_area, String path) {
        super(text_area);
        String[] PathComponents = Helper.ObtenerComponentesPath(path);
        if (PathComponents != null) {
            this.Path = PathComponents[0];
            this.FileName = PathComponents[1];
            this.Ext = PathComponents[2];
        }
    }

    public Tab(RSyntaxTextArea text_area, String file_name, String ext) {
        super(text_area);
        try {
            this.Path = new File(Tab.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException ex) {
            Logger.getLogger(Tab.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.Ext = ext;
        this.FileName = file_name;
    }

    public String getPath() {
        return Path;
    }

    public String getFileName() {
        return FileName;
    }

    public String ObtenerNombreCompletoArchivo() {
        return FileName + "." + Ext;
    }

}