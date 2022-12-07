package proyU;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class IntroServidor {
    public static void main(String[] args) {
        Servidor miserver = new Servidor();
    }
}

class Servidor implements Runnable{

    public Servidor(){
        Thread servidor1=new Thread(this);
        servidor1.start();
    }

    @Override
    public void run() {
        System.out.println("Iniciando servidor...");
        try {
            ServerSocket ss = new ServerSocket(7780);
            String nombre, mensaje;
            Integer ip, destino;
            Paquete paquete_recibido;
            while(true) {
                Socket s = ss.accept();
                ObjectInputStream paquete_datos =new ObjectInputStream(s.getInputStream());
                paquete_recibido= (Paquete) paquete_datos.readObject();
                /*
                Socket s = ss.accept();
                DataInputStream dis = new DataInputStream(s.getInputStream());
                System.out.print(">> ");
                peek = (String) dis.readUTF();
                System.out.println(peek);
                chat(peek);
                s.close();*/
                ip=paquete_recibido.getIp();
                destino=paquete_recibido.getDestino();
                nombre=paquete_recibido.getNombre();
                mensaje=paquete_recibido.getMensaje();
                System.out.print("IP>> ");
                System.out.println(ip+"("+nombre+") para ("+destino+")"+mensaje);
                try {
                    chat(paquete_recibido);
                }catch (Exception e){
                    System.out.println(e);
                    avisar(paquete_recibido);
                }
                s.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    static void chat(Paquete paquete_recibido) throws IOException {

        Socket s = new Socket("localhost", paquete_recibido.getDestino());

        Paquete datos = new Paquete();

        datos.setIp(paquete_recibido.getIp());
        datos.setNombre(paquete_recibido.getNombre());
        datos.setMensaje(paquete_recibido.getMensaje());
        datos.setDestino(paquete_recibido.getDestino());

        ObjectOutputStream paquete_datos = new ObjectOutputStream(s.getOutputStream());
        paquete_datos.writeObject(datos);

        /*DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        dout.writeUTF(cad);*/
        paquete_datos.flush();
        paquete_datos.close();
        s.close();
    }
    static void avisar(Paquete paquete_recibido) throws IOException {

        Socket s = new Socket("localhost", paquete_recibido.getIp());

        Paquete datos = new Paquete();

        datos.setIp(7780);
        datos.setNombre("SERVIDOR");
        datos.setMensaje("No se pudo conectar con ("+paquete_recibido.getDestino()+") :c");
        datos.setDestino(paquete_recibido.getDestino());

        ObjectOutputStream paquete_datos = new ObjectOutputStream(s.getOutputStream());
        paquete_datos.writeObject(datos);

        /*DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        dout.writeUTF(cad);*/
        paquete_datos.flush();
        paquete_datos.close();
        s.close();
    }
    /*
    static void chat(String cad) throws IOException {
        Socket s = new Socket("localhost", 7888);
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

//        Ejemplo 1
//        dout.writeUTF("Cliente -  " + (int)(Math.random()*100));

//        Ejemplo 2

        //System.out.print("Mensaje: ");
        dout.writeUTF(cad);
        dout.flush();
        dout.close();
        s.close();
    }*/
}