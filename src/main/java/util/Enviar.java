/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author euler
 */
public class Enviar implements Runnable {

    private final Socket envia;

    public Enviar(Socket envia) {
        this.envia = envia;
    }

    @Override
    public void run() {
        try {

            Scanner y = new Scanner(System.in);
            PrintStream saida = new PrintStream(envia.getOutputStream());

            while (y.hasNextLine()) {
                if (envia.isConnected()) {
                    String x = y.nextLine();
                    saida.println(x);
                    if (x.equalsIgnoreCase("sair")) {
                        break;
                    }
                } else {
                    System.out.println("");
                    break;
                }

            }
            saida.close();
            y.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
