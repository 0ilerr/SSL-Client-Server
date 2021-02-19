/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import server.SSLServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author euler
 */
public class ServerDistribuir implements Runnable {

    private SSLServer servidorThread;
    private InputStream cliente;
    private PrintStream output;
    private Socket socket;

    public ServerDistribuir(PrintStream output, SSLServer servidorThread, Socket socket) throws IOException {
        this.servidorThread = servidorThread;
        this.output = output;
        this.cliente = socket.getInputStream();
        this.socket = socket;
    }


    @Override
    public void run() {
        Scanner s = new Scanner(this.cliente);
        while (s.hasNextLine()) {
            try {
                servidorThread.distribuiMensg(s.nextLine(), socket);
            } catch (IOException ex) {
                Logger.getLogger(ServerDistribuir.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        s.close();

    }
}
