package mecanografia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TecladoCompletoLayoutAbsoluto {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AplicacionMecanografia();
        });
    }
}

class AplicacionMecanografia {

    private JTextArea areaEscritura;
    private JLabel etiquetaFrase;
    private ArrayList<String> listaPangramas = new ArrayList<>();
    private String pangramaAleatorio;
    private int pulsacionesCorrectas = 0;
    private int pulsacionesIncorrectas = 0;
    private HashMap<Character, Integer> teclasProblematicas = new HashMap<>();

    public AplicacionMecanografia() {
        cargarPangramas();
        JFrame marco = new JFrame("TUTOR DE MECANOGRAFÍA");
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(750, 500);
        marco.getContentPane().setBackground(Color.LIGHT_GRAY);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.PINK);
        
        etiquetaFrase = new JLabel();
        etiquetaFrase.setBounds(80, 10, 800, 60);

        etiquetaFrase.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaFrase.setForeground(Color.DARK_GRAY);
        panel.add(etiquetaFrase);

        areaEscritura = new JTextArea(1, 1);
        areaEscritura.setEditable(false);
        JScrollPane panelDesplazable = new JScrollPane(areaEscritura);
        panelDesplazable.setBounds(100, 100, 600, 50);
        panel.add(panelDesplazable);

        String[][] letrasNumeros = {
                {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
                {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
                {"A", "S", "D", "F", "G", "H", "J", "K", "L"},
                {"Z", "X", "C", "V", "B", "N", "M"}
        };

        for (int i = 0; i < letrasNumeros.length; i++) {
            agregarBotones(panel, letrasNumeros[i], 100, 160 + i * 60);
        }

        agregarBoton(panel, "BORRAR", 550, 340);
        agregarBoton(panel, "ESPACIO", 150, 400);
        marco.add(panel);
        marco.setLocationRelativeTo(null);
        marco.setResizable(false);
        marco.setVisible(true);
        mostrarPangramaAleatorio();
    }

    private void cargarPangramas() {
        try {
            BufferedReader lector = new BufferedReader(new FileReader("pangramas.txt"));
            String linea;
            while ((linea = lector.readLine()) != null) {
                listaPangramas.add(linea);
            }
            lector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void agregarBotones(JPanel panel, String[] etiquetas, int inicioX, int y) {
        int x = inicioX;
        for (String etiqueta : etiquetas) {
            agregarBoton(panel, etiqueta, x, y);
            x += 60;
        }
    }

    private void mostrarPangramaAleatorio() {
        Random aleatorio = new Random();
        int indice = aleatorio.nextInt(listaPangramas.size());
        pangramaAleatorio = listaPangramas.get(indice);
        System.out.println(pangramaAleatorio);
        etiquetaFrase.setText("<html><body style='width: 500px'>" + pangramaAleatorio + "</body></html>");
    }

    private void agregarBoton(JPanel panel, String etiqueta, int x, int y) {
        JButton boton = new JButton(etiqueta);
        if (etiqueta.equals("ESPACIO")) {
            boton.setPreferredSize(new Dimension(350, 50));
        } else if (etiqueta.equals("BORRAR")) {
            boton.setPreferredSize(new Dimension(90, 50));
        } else {
            boton.setPreferredSize(new Dimension(50, 50));
        }
        boton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (etiqueta.equals("BORRAR")) {
                    String textoActual = areaEscritura.getText();
                    if (!textoActual.isEmpty()) {
                        textoActual = textoActual.substring(0, textoActual.length() - 1);
                        areaEscritura.setText(textoActual);
                    }
                } else if (etiqueta.equals("ESPACIO")) {
                    areaEscritura.append(" ");
                } else {
                    char teclaEsperada = pangramaAleatorio.charAt(areaEscritura.getText().length());
                    if (etiqueta.charAt(0) == teclaEsperada) {
                        pulsacionesCorrectas++;
                    } else {
                        pulsacionesIncorrectas++;
                        teclasProblematicas.put(teclaEsperada, teclasProblematicas.getOrDefault(teclaEsperada, 0) + 1);
                    }
                    areaEscritura.append(etiqueta);
                }
                actualizarEtiquetaEstadisticas();
            }
        });
        boton.setBounds(x, y, boton.getPreferredSize().width, boton.getPreferredSize().height);
        panel.add(boton);
    }

    private void actualizarEtiquetaEstadisticas() {
        String estadisticas = "Correctas: " + pulsacionesCorrectas + "\nIncorrectas: " + pulsacionesIncorrectas
                + "\nTeclas problemáticas: " + teclasProblematicas.toString();
        JOptionPane.showMessageDialog(null, estadisticas, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }
}
