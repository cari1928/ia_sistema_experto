package Clases;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author AlphaGo
 */
public class Principal {

    /**
     * Contenido del menú principal
     */
    public static final String MSG1 = "Seleccione una opción:"
            + "\n1) Añadir Meta"
            + "\n2) Modificar Meta"
            + "\n3) Eliminar Meta"
            + "\n4) Encadenamiento hacia Adelante"
            + "\n5) Encadenamiento hacia Atrás"
            + "\n6) Salir";

    /**
     * Contenido del menu Enc. hacia adelante
     */
    public static final String MSG2 = "Seleccione:\n1)Con meta\n2)Sin meta";

    /**
     * Inicio de todo el programa
     *
     * @param args
     */
    public static void main(String[] args) {
        GestionArchivo manager_file = new GestionArchivo(); //última versión funcional        
        boolean flag = true;
        int opt;

        try {
            //para modificar el archivo maestro con las nuevas reglas
            if (!manager_file.escribir()) {
                JOptionPane.showMessageDialog(null, "ERROR, la sintáxis de las reglas es incorrecta");
            } else {
                while (flag) {
                    switch (opciones(MSG1, 0, 7)) {
                        case 1: //añadir meta
                            añadir_regla();
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4: //enc. hacia adelante
                            opt = opciones(MSG2, 0, 3); //muestra otro menú de opciones y valida la opción seleccionada
                            MotorInferencias mi_p = new MotorInferencias(manager_file.leerMaestro(), pedirDatos(opt));
                            JOptionPane.showMessageDialog(null, mi_p.encadenamientoAdelante());
                            break;
                        case 5:
                            break;
                        case 6: //salida
                            flag = false;
                            JOptionPane.showMessageDialog(null, "Cerrando el programa...");
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ingreso de los hechos iniciales y el hecho meta
     *
     * @param type int Indica si se requiere hecho meta o no
     * @return BaseHechos Objeto que almacena los hechos iniciales y el hecho
     * meta
     */
    public static BaseHechos pedirDatos(int type) {
        boolean flag = true;
        String meta;
        ArrayList<String> hechos_iniciales = new ArrayList<>(0);

        JOptionPane.showMessageDialog(null, "Se solicitarán los Hechos Iniciales");
        while (flag) {
            String dato = JOptionPane.showInputDialog("Ingresa un hecho o escribe 0 para iniciar el proceso");

            try {
                int opcion = Integer.parseInt(dato);
                if (opcion == 0) {
                    flag = false;
                } else {
                    JOptionPane.showMessageDialog(null, "Valor no admitido");
                }

            } catch (Exception e) {
                if (!dato.equals("")) {
                    hechos_iniciales.add(dato.toLowerCase());
                }
            }
        }

        meta = "Sin meta"; //para mostrarlo en consola
        if (type == 1) { //con meta
            meta = JOptionPane.showInputDialog("Ingresa el hecho meta").toLowerCase();
        }

        JOptionPane.showMessageDialog(null, "Iniciando Proceso");
        System.out.println("\nBase de hechos: " + hechos_iniciales);
        System.out.println("Hecho meta: " + meta + "\n");
        System.out.println("Encadenamiento Hacia Adelante:");

        if (type == 2) { //sin meta
            meta = null;
        }
        return new BaseHechos(hechos_iniciales, meta);
    }

    /**
     * Objetivo: Ahorrar código. \nPermite mostrar los menús de forma más
     * simple. \nLos límites definen cuántas y cuáles opciones serán válidas.
     * Ej: 0-2 opciones válidas: 1
     *
     * @param msg Texto a mostrar dentro del JOPtionPane
     * @param lim_inf Límite inferior para un ciclo
     * @param lim_sup Límite superior para un ciclo
     * @return
     */
    public static Integer opciones(String msg, int lim_inf, int lim_sup) {
        while (true) {
            try {
                String tipo = JOptionPane.showInputDialog(msg);
                int prueba = Integer.parseInt(tipo);
                if (prueba > lim_inf && prueba < lim_sup) {
                    return prueba;
                } else {
                    JOptionPane.showMessageDialog(null, "ERROR, ingrese datos válidos");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR, ingrese datos válidos");
            }
        }
    }

    /**
     * Permite añadir una nueva regla al archivo reglas.txt y archivo maestro
     *
     * @throws IOException En caso de que no se pueda realizar la escritura en
     * el archivo maestro
     */
    public static void añadir_regla() throws IOException {
        ArrayList<String> tmp_antecedentes = new ArrayList<>();
        ArrayList<BaseConocimientos> tmp_reglas;
        String consecuente = "", antecedente = "";
        boolean flag = true;

        while (flag) {
            try {
                antecedente = JOptionPane.showInputDialog("Ingrese un antecedente, presione 0 para terminar");
                int opt = Integer.parseInt(antecedente);

                if (tmp_antecedentes.size() != 0) {
                    consecuente = JOptionPane.showInputDialog("Ingrese el consecuente");
                    flag = false;
                } else {
                    JOptionPane.showMessageDialog(null, "!No ha ingresado antecedentes!");
                }

            } catch (Exception e) {
                tmp_antecedentes.add(antecedente);
            }
        }

        tmp_reglas = new ArrayList<>();
        BaseConocimientos bc = new BaseConocimientos();
        bc.setAntecedentes(tmp_antecedentes);
        bc.setConsecuente(consecuente);
        tmp_reglas.add(bc);

        //escribir nueva regla en el archivo .txt 
        GestionArchivo ga = new GestionArchivo();
        ga.escrbirRegla(tmp_reglas);

        //escribir nueva regla en archivo maestro
        ga.escribir();
    }
}
