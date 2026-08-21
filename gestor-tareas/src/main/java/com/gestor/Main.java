package com.gestor;

import com.gestor.gui.MainFrame;
import com.gestor.gui.PersonasPanel;
import com.gestor.gui.EquiposPanel;
import com.gestor.gui.TareasPanel;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            PersonasPanel personasPanel = new PersonasPanel(frame);
            EquiposPanel equiposPanel = new EquiposPanel(frame);
            TareasPanel tareasPanel = new TareasPanel(frame);
            frame.setPanel("personas", personasPanel);
            frame.setPanel("equipos", equiposPanel);
            frame.setPanel("tareas", tareasPanel);
            frame.setVisible(true);
        });
    }
}