package main;

import java.util.ArrayList;

import model.Pedido;
import processing.core.PApplet;
import processing.core.PImage;

public class PeerWonka extends PApplet implements OnMessageListener {
	
	
	UDPConnection udp;
	
	//imagenes
		PImage cereal;
		PImage hotdog;
		PImage icecream;
		PImage sandwich;
		
		ArrayList<Pedido> pedidos;

		//Controlo la variable de Y para hacer la lista de los pedidos
		int posY,posX;
		
		
		//con esto controlo el numero de los pedidos que lleguen
		int numeroPedido;
		
		//Pedido terminado
		boolean pedidoTerminado;
		
		//Contador de los nuevos pedidos que lleguen
		int turno;
		
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("main.PeerWonka");
	}
	
	public void settings() {
		size(800,600);
		
	}
	
	
	
	public void setup() {
		//Todo lo de UDP
		udp=new UDPConnection();
		udp.setObserver(this);
        udp.start();
        
        //imagenes
        cereal=loadImage("img/cereal.jpg");
		hotdog=loadImage("img/hotdog.jpg");
		icecream=loadImage("img/icecream.jpg");
		sandwich=loadImage("img/sandwich.jpg");
		
		//El arraylist donde agrero los pedidos
		pedidos = new ArrayList<>();
		
		
		posX=0;
		posY=150;
		
		//Los turnos comienzan en 0 
		turno=0;
		

	}
	
	
	
	public void draw() {
		background(255);
		
		fill(0);
		
		for(int i=0; i<pedidos.size();i++) {
	
			Pedido pedidoN= pedidos.get(i);
			
			numeroPedido=i;
		
			//Controlo que en pantalla no se vean mas de 4 pedidos para que no se salgan de la pantalla
			
			if(numeroPedido<4) {
				
			//Si el pedido es cereal
			if(pedidoN.getPedido().equals("cereal")) {
				
				image(cereal,posX,posY*i,150,150);
				textSize(25);
				text("Pedido #"+ pedidoN.getTurno(),posX+180,posY*i+50);
				text(pedidoN.getHora(),posX+180,posY*i+80);
			}
			
			
			//Si el pedido es helado
			if(pedidoN.getPedido().equals("helado")) {
				
				image(icecream,posX,posY*i,150,150);
				textSize(25);
				text("Pedido #"+ pedidoN.getTurno(),posX+180,posY*i+50);
				text(pedidoN.getHora(),posX+180,posY*i+80);
			}
			
			
			//Si el pedido es sandwich
			if(pedidoN.getPedido().equals("sandwich")) {
				
				image(sandwich,posX,posY*i,150,150);
				textSize(25);
				text("Pedido #"+pedidoN.getTurno(),posX+180,posY*i+50);
				text(pedidoN.getHora(),posX+180,posY*i+80);
			}
			
			
			//Si el pedido es hotdog
			if(pedidoN.getPedido().equals("hotdog")) {
				
				image(hotdog,posX,posY*i,150,150);
				textSize(25);
				text("Pedido #"+pedidoN.getTurno(),posX+180,posY*i+50);
				text(pedidoN.getHora(),posX+180,posY*i+80);
			}
		}
		}
		}
	
		
	

	
	
	
	
	public void onMessage(Pedido pedido, String ip) {
	
	
	pedido.setIp(ip);
	
	//Añado los pedidos que lleguen a mi arraylist
	pedidos.add(pedido);
	
	
	//Cuando llegue un pedido, le sumo al contador de turno
	turno+=1;
	pedido.setTurno(turno);
	
	System.out.println(pedido.getTurno());
	
	}
	
	
	
	
	public void mousePressed() {
		
		
		for(int i=0; i<pedidos.size();i++) {
			Pedido pedidoN= pedidos.get(i);
			
		//Quito el pedido cuando el trabajador de wonka le de a la zona sensible del pedido
		if ((mouseX>posX && mouseX<800)&&(mouseY> posY*i && mouseY <posY*i+150)) {
			pedidos.remove(i);
			
			//Envio la notificacion
			udp.enviar("terminado",pedidoN.getIp());
			
		}
		}
		
	}
}
