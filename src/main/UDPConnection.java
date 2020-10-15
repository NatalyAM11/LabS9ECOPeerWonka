package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.google.gson.Gson;

import model.Pedido;

public class UDPConnection extends Thread {

	private DatagramSocket socket;
	
	//primer variable para obtner el ip del usuario
	String[] ipYa;
	
	//variable definitiva para obtener el ip del usuario, para devolverle la notificacion correspondiente
		String ipDef;
	
	//Patron observer
	private OnMessageListener observer;
	
	public void setObserver(OnMessageListener observer) {
		this.observer=observer;
	}
	

	
	//Hilo recepcion
	public void run() {
		
		try {
			
			socket= new DatagramSocket(6000);
			
			
			//Recepción mensajes
			
			while(true) {
				byte []buffer= new byte[50];
				DatagramPacket packet= new DatagramPacket(buffer,buffer.length);
				
				System.out.println("Esperando datagrama");
				socket.receive(packet);

				
				//Convierto los bytes a String
                String mensaje= new String(packet.getData()).trim();
                
                Gson gson= new Gson();
                Pedido objeto= gson.fromJson(mensaje, Pedido.class);
                
                
      
                //obtengo la direccion del usuario
                SocketAddress socketAddress=packet.getSocketAddress();
                //La convierto en string
                String addressString= socketAddress.toString();
                //La separo por "/"
                String[] ipp= addressString.split("/");
                //
                String addressOk=ipp[1];
                //Vuelvo y lo separo, pero ahora con ":"
                ipYa=addressOk.split(":");
                //Esta es la ip del usuario
                ipDef=ipYa[0];
                
              
               //observer
                observer.onMessage(objeto,ipDef);
                
			}
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	//Metodo envio de datagramas
    public void enviar(String mensaje, String ipDef){

        new Thread(
                ()->{

                    try {
                    	
                        InetAddress ip= InetAddress.getByName(ipDef);
                       
                        DatagramPacket packet= new DatagramPacket(mensaje.getBytes(),mensaje.getBytes().length,ip,5000);
                        socket.send(packet);
                        
                       System.out.println(ip);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

}
	
	
	




