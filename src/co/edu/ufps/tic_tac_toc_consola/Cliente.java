/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ufps.tic_tac_toc_consola;

import co.edu.ufps.tic_tac_toc_consola.negocio.FachadaSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author john
 */
public class Cliente {
    private static String recibido;
    private static char[] cbuf;
    /**
     * @param args the command line arguments
     */
    public static String recibirSocket (InputStreamReader in) throws IOException {
        cbuf = new char[512];
        recibido = "";
        in.read(cbuf);
        for (char c : cbuf) {
            recibido += c;
            if (c == 00) {
                break;
            }
        }
        return recibido;
    }
    
    public static void main(String[] args) throws IOException {
        FachadaSocket fachada = new FachadaSocket();
        Socket cliente = new Socket("127.0.0.1",8090);
        OutputStreamWriter out = new OutputStreamWriter(cliente.getOutputStream());
        InputStreamReader in = new InputStreamReader(cliente.getInputStream(), "UTF8");
        Scanner sc = new Scanner(System.in);
        String mensaje = recibirSocket(in);
        /*
            Mensaje de confirmacion
        */
        System.out.println(mensaje);
        while (true) {
            /*
                Pedira que envie el nombre de usuario
            */
            out.write(sc.nextLine());
            out.flush();
            /*
                Retornara el puntaje que lleva del usuario con el nombre enviado o
                un false si el usuario ya inicio sesion en otro pc
            */
            String puntaje = recibirSocket(in);
            if (puntaje.equalsIgnoreCase("false")) {
                System.out.println("Este usuario ya inicio sesion en otro pc\n"
                        + "intentelo de nuevo");
            } else {
                System.out.println(puntaje);
                break;
            }
        }
        while (true) {
            /*
                Cuando todo este listo retornara un estado de la partida
            */
            fachada.interpretarEstadoPartidaSencillo(recibirSocket(in));
            System.out.println(fachada.mostrarTablero());
            if (fachada.getGanador() != null) {
                System.out.println("Usted ha " + fachada.getGanador() + " esta Partida");
            }
            /*
                Esperando a que me den el turno
            */
            System.out.println(recibirSocket(in));
            boolean columnaValida = true;
            String columna = "";
            String fila = "";
            String movimiento = "";
            while (columnaValida) {
                System.out.println("Ingrese el valor de la COLUMNA (0, 1, 2) "
                        + "la cual quiere marcar...");
                columna = sc.nextLine();
                if (Pattern.matches("(0|1|2)", columna)) {
                    movimiento = columna;
                    columnaValida = false;
                } else {
                    System.out.println("Valor INVALIDO, intentelo de nuevo...");
                }
            }
            boolean filaValida = true;
            while (filaValida) {
                System.out.println("Ingrese el valor de la FILA (0, 1, 2) "
                        + "la cual quiere marcar...");
                fila = sc.nextLine();
                if (Pattern.matches("(0|1|2)", fila)) {
                    movimiento += "-" + fila;
                    filaValida = false;
                } else {
                    System.out.println("Valor INVALIDO, intentelo de nuevo...");
                }
            }
            /*
                Envia al servidor la coordenada que va a tachar
            */
            out.write(movimiento);
            out.flush();
            /*
                Muestra el estado de la partida
            */
            fachada.interpretarEstadoPartida(recibirSocket(in));
            System.out.println(fachada.mostrarTablero());
            if (fachada.getGanador() != null) {
                System.out.println("Usted ha " + fachada.getGanador() + " esta Partida");
            } else if (!fachada.isMovimientoValido()) {
                System.out.println("Fue un movimiento INVALIDO, intentelo de nuevo");
            }
        }
    }
}
