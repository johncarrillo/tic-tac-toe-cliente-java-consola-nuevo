/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ufps.tic_tac_toc_consola.negocio;

/**
 *
 * @author john
 */
public class FachadaSocket {
    private char[][] tablero;
    private boolean movimientoValido;
    private boolean miTurno;
    private String ganador;

    public FachadaSocket() {
        tablero = new char[3][3];
    }

    public void interpretarEstadoPartidaSencillo (String estado) {
        /*
            mensaje[0] -> formato de tablero (X,X,X,O,X,O,O,O,X)
            mensaje[0] -> ultimo movimiento hecho es valido o no (true o  false)
            mensaje[0] -> es mi turno o no (true o  false)
            mensaje[0] -> gano el usuario (perdio: -1, empato: 0, gano: 1)
        */
        String[] mensaje = estado.split("\n");
        this.armarTablero(mensaje[0]);
        if (mensaje.length > 1) {
            if (mensaje[1].equals("1")) {
                this.ganador = "GANADO";
            } else if (mensaje[1].equals("0")) {
                this.ganador = "EMPATADO";
            } else if (mensaje[1].equals("-1")) {
                this.ganador = "PERDIDO";
            }
        } else {
            this.ganador = null;
        }
    }

    public void interpretarEstadoPartida (String estado) {
        /*
            mensaje[0] -> formato de tablero (X,X,X,O,X,O,O,O,X)
            mensaje[0] -> ultimo movimiento hecho es valido o no (true o  false)
            mensaje[0] -> es mi turno o no (true o  false)
            mensaje[0] -> gano el usuario (perdio: -1, empato: 0, gano: 1)
        */
        String[] mensaje = estado.split("\n");
        this.armarTablero(mensaje[0]);
        this.movimientoValido = Boolean.parseBoolean(mensaje[1]);
        this.miTurno = Boolean.parseBoolean(mensaje[2]);
        if (mensaje.length > 3) {
            if (mensaje[3].equals("1")) {
                this.ganador = "GANADO";
            } else if (mensaje[3].equals("0")) {
                this.ganador = "EMPATADO";
            } else if (mensaje[3].equals("-1")) {
                this.ganador = "PERDIDO";
            }
        } else {
            this.ganador = null;
        }
    }

    public void armarTablero (String tablero) {
        String[] mensaje = tablero.split(",");
        this.tablero[0][0] = mensaje[0].charAt(0);
        this.tablero[0][1] = mensaje[1].charAt(0);
        this.tablero[0][2] = mensaje[2].charAt(0);
        this.tablero[1][0] = mensaje[3].charAt(0);
        this.tablero[1][1] = mensaje[4].charAt(0);
        this.tablero[1][2] = mensaje[5].charAt(0);
        this.tablero[2][0] = mensaje[6].charAt(0);
        this.tablero[2][1] = mensaje[7].charAt(0);
        this.tablero[2][2] = mensaje[8].charAt(0);
    }

    public String mostrarTablero() {
        String cadena = "   0   1   2\n";
        int i = 0;
        for (char[] x: this.tablero) {
            cadena += i + " ";
            for (char y: x ) {
                if (y == 0) {
                    y = ' ';
                }
                cadena += "[" + y + "] ";
            }
            cadena += "\n";
            i++;
        }
        return cadena;
    }

    public boolean isMovimientoValido() {
        return movimientoValido;
    }

    public void setMovimientoValido(boolean movimientoValido) {
        this.movimientoValido = movimientoValido;
    }

    public boolean isMiTurno() {
        return miTurno;
    }

    public void setMiTurno(boolean miTurno) {
        this.miTurno = miTurno;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

}
