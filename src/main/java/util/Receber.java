/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author euler
 */
public class Receber implements Runnable {

    private Socket receber;

    public Receber(Socket servidor) {

        this.receber = servidor;
    }

    @Override
    public void run() {

        try {
            Scanner s = new Scanner(this.receber.getInputStream());

            while (s.hasNextLine() && receber.isConnected()) {
                if (receber.isConnected()) {
                    String msg = s.nextLine();
                    String[] textoSeparado = msg.split("-;-");
                    int port = Integer.parseInt(textoSeparado[0]);
                    int port2 = receber.getLocalPort();

                    System.out.println("Resposta: " + textoSeparado[1]);


                } else {

                    break;
                }
            }
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(Receber.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Socket getReceber() {

        return receber;
    }

    public void setReceber(Socket receber) {

        this.receber = receber;
    }

}
