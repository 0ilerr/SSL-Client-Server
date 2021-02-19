package server;

import util.ServerDistribuir;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;

/**
 * @author euler
 */
public class SSLServer {


    private int porta;
    private static ArrayList<PrintStream> list;

    public SSLServer(int porta) {
        this.porta = porta;
        this.list = new ArrayList<PrintStream>();
    }

    public void distribuiMensg(String msg, Socket socket) throws IOException {
        int v = 0;
        for (PrintStream printStream : list) {
            if (v < 1) {
                System.out.println("Cliente " + socket.getPort() + ": " + msg);
                v++;
            }
            PrintStream ps1 = new PrintStream(socket.getOutputStream());
            if (msg.equalsIgnoreCase("sair")) {
                ps1.println("Conexão Fechada!");
                socket.close();
                break;
            } else {
                printStream.println(socket.getPort() + "-;-" + msg);
            }

        }
    }

    public void executa() {

        try {
            // Get the keystore
            System.setProperty("javax.net.debug", "all");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            String password = "asdfgh";
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("server/certificate-server.p12");
            keyStore.load(inputStream, password.toCharArray());

            // TrustManagerFactory
            String password2 = "asdfgh";
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
            InputStream inputStream1 = ClassLoader.getSystemClassLoader().getResourceAsStream("client/certificate-client.p12");
            trustStore.load(inputStream1, password2.toCharArray());
            trustManagerFactory.init(trustStore);
            X509TrustManager x509TrustManager = null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                    break;
                }
            }

            if (x509TrustManager == null) throw new NullPointerException();


            // KeyManagerFactory ()
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
            keyManagerFactory.init(keyStore, password.toCharArray());
            X509KeyManager x509KeyManager = null;
            for (KeyManager keyManager : keyManagerFactory.getKeyManagers()) {
                if (keyManager instanceof X509KeyManager) {
                    x509KeyManager = (X509KeyManager) keyManager;
                    break;
                }
            }
            if (x509KeyManager == null) throw new NullPointerException();

            // set up the SSL Context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(new KeyManager[]{x509KeyManager}, new TrustManager[]{x509TrustManager}, null);

            SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(porta);
            System.out.println("----Iniciando Server----\n" + porta);
            serverSocket.setNeedClientAuth(true);
            serverSocket.setEnabledProtocols(new String[]{"TLSv1.2"});
            SSLSocket socket = (SSLSocket) serverSocket.accept();

            System.out.println("Nova Conexão com o Cliente: " + socket.getPort());

            PrintStream ps = new PrintStream(socket.getOutputStream());
            list.add(ps);

            ServerDistribuir tc = new ServerDistribuir(ps, this, socket);
            new Thread(tc).start();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SSLServer servidorThreadSSL = new SSLServer(54321);
        servidorThreadSSL.executa();
    }
}
