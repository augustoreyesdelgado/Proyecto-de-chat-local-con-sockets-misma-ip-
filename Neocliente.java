package proyU;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.Serializable;

public class Neocliente implements Runnable{
    static int puerto;
    static String nombre;
    Neocliente(){
        Thread servidor1=new Thread(this);
        servidor1.start();
    }

    static boolean prueba(){
        Scanner sn0 = new Scanner(System.in);
        int puerto1=sn0.nextInt();
        Socket Skt;
        String host = "localhost";
        try {
            Skt = new Socket(host, puerto1);
            System.out.println(puerto1+" Ya esta en uso");
            return true;
        }
        catch (IOException e) {
        }
        puerto=puerto1;
        return false;
    }

    static void prueba2(){

        Socket Skt;
        String host = "localhost";
        for (int i = 8500; i < 8511; i++) {
            try {
                System.out.println("Puerto: "+ i);
                Skt = new Socket(host, i);
                System.out.println("Puerto: " + i + " en uso");
            }
            catch (IOException e) {
            }
        }
    }


    public static void main(String[] args) throws IOException {
        /*
        boolean prue=true;
        do{
            System.out.println("Elige tu puerto: ");
            prue=prueba();
        }while (prue==true);*/
        System.out.println("Elige tu puerto: ");
        Scanner sn0 = new Scanner(System.in);
        puerto=sn0.nextInt();
        System.out.println("¿Nombre?: ");
        Scanner sn2 = new Scanner(System.in);
        nombre= sn2.nextLine();

        Scanner sn = new Scanner(System.in);
        boolean salir = false;
        int opcion;
        Neocliente neo1=new Neocliente();
        do{
            System.out.println("1. Chatear");
            System.out.println("4. Salir");
            System.out.println("Escribe una de las opciones");
            opcion = sn.nextInt();
            switch(opcion){
                case 1:
                    int destinatario=0;
                    System.out.print("Destinatario? (PUERTO): ");
                    //System.out.print("*** PARA CHAT GLOBAL USAR EL PUERTO: 7777 *** ");
                    Scanner sn1 = new Scanner(System.in);
                    destinatario = Integer.parseInt(sn1.nextLine());
                    System.out.println("Para terminar este chat usa: /LEAVE");
                    while (true) {
                        System.out.print(">>");
                        String cad = sn1.nextLine();
                        if(cad.equals("/LEAVE")){
                            break;
                        }
                        try {
                            chat(cad, destinatario);
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                case 4:
                    break;
                default:
                    System.out.println("Solo números entre 1 y 4");
                    break;
            }
        }
        while(opcion!=4);
        System.out.println("Adiós");
        System.exit(0);
    }
    static void chat(String cad, Integer destinatario) throws IOException {

        Socket s = new Socket("localhost", 7780);

        Paquete datos = new Paquete();

        datos.setIp(puerto);
        datos.setNombre(nombre);
        datos.setMensaje(cad);
        datos.setDestino(destinatario);

        ObjectOutputStream paquete_datos = new ObjectOutputStream(s.getOutputStream());
        paquete_datos.writeObject(datos);

        /*DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        dout.writeUTF(cad);*/
        paquete_datos.flush();
        paquete_datos.close();
        s.close();
    }

    @Override
    public void run() {

        try {
            ServerSocket ss = new ServerSocket(puerto);
            String nombre, mensaje;
            Integer ip, destino;
            Paquete paquete_recibido;
            while(true) {
                Socket s = ss.accept();
                ObjectInputStream paquete_datos =new ObjectInputStream(s.getInputStream());
                paquete_recibido= (Paquete) paquete_datos.readObject();
                ip=paquete_recibido.getIp();
                destino=paquete_recibido.getDestino();
                nombre=paquete_recibido.getNombre();
                mensaje=paquete_recibido.getMensaje();
                System.out.print(nombre+"("+ip+"): "+mensaje);
                System.out.println("");
                System.out.print(">>");
                s.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        /*
        try {
            ServerSocket ss = new ServerSocket(puerto);
            String peek;
            while(true) {
                Socket s = ss.accept();
                DataInputStream dis = new DataInputStream(s.getInputStream());
                peek = (String) dis.readUTF();
                System.out.print("Recivido: "+peek);
                System.out.println("");
                System.out.print(">>");
                s.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}


class Paquete implements Serializable {

    String nombre, mensaje;
    Integer ip, destino;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getIp() {
        return ip;
    }

    public void setIp(Integer ip) {
        this.ip = ip;
    }

    public Integer getDestino() {
        return destino;
    }

    public void setDestino(Integer destino) {
        this.destino = destino;
    }
}