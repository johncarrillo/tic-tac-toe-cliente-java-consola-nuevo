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
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author john
 */
public class Cliente {
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException {
        FachadaSocket fachada = new FachadaSocket();
        Socket cliente = new Socket("localhost",8090);
        DataOutputStream out = new DataOutputStream(cliente.getOutputStream());
        DataInputStream in = new DataInputStream(cliente.getInputStream());
        Scanner sc = new Scanner(System.in);
        String mensaje =  in.readUTF();
        /*
            Mensaje de confirmacion
        */
        System.out.println(mensaje);
        while (true) {
            /*
                Pedira que envie el nombre de usuario
            */
            out.writeUTF(sc.nextLine());
            /*
                Retornara el puntaje que lleva del usuario con el nombre enviado o
                un false si el usuario ya inicio sesion en otro pc
            */
            String puntaje = in.readUTF();
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
            fachada.interpretarEstadoPartidaSencillo(in.readUTF());
            System.out.println(fachada.mostrarTablero());
            if (fachada.getGanador() != null) {
                System.out.println("Usted ha " + fachada.getGanador() + " esta Partida");
            }
            /*
                Esperando a que me den el turno
            */
            System.out.println(in.readUTF());
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
                    movimiento += "," +fila;
                    filaValida = false;
                } else {
                    System.out.println("Valor INVALIDO, intentelo de nuevo...");
                }
            }
            /*
                Envia al servidor la coordenada que va a tachar
            */
            out.writeUTF(movimiento);
            /*
                Muestra el estado de la partida
            */
            fachada.interpretarEstadoPartida(in.readUTF());
            System.out.println(fachada.mostrarTablero());
            if (fachada.getGanador() != null) {
                System.out.println("Usted ha " + fachada.getGanador() + " esta Partida");
            } else if (!fachada.isMovimientoValido()) {
                System.out.println("Fue un movimiento INVALIDO, intentelo de nuevo");
            }
        }
    }
}
