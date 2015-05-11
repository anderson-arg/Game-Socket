package service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import bean.Comida;
import bean.Perssonagem;

public class GameCliente{

	// lista de perssonagem e comida
	public static List<Perssonagem> jogadores = new ArrayList<Perssonagem>();
	public static List<Comida> comidas = new ArrayList<Comida>();
	
	// UDP
	private static DatagramSocket dataGramSocket;
	
	// Pacote para enviar via UDP 
	private static DatagramPacket enviarPacote;
	private DatagramPacket receberPacote;
	
	// guardando IP e definindo bytes para receber pacote
	private static InetAddress enderecoIP;
	private static byte[] enviarDados;
	private byte[] receberDados;
	
	// declaração para converção de objeto para byte
	private static ByteArrayOutputStream byteArray; 
	private static ObjectOutputStream output;
	
	// porta
	static int porta;
	
	
	public GameCliente(int port){
		
		// adicionando comida
			comidas.add(new Comida("res/bola.png", 400, 400));
			comidas.add(new Comida("res/bola.png", 400, 200));
			comidas.add(new Comida("res/bola.png", 200, 400));
			comidas.add(new Comida("res/bola.png", 100, 100));
			comidas.add(new Comida("res/bola.png", 500, 300));
		
		try {
			enderecoIP = InetAddress.getByName("localhost");
			porta = port;
			dataGramSocket = new DatagramSocket();
			new Thread(new ListenerSocket()).start();
			
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}	
	}
	
	public GameCliente(String ip, int port){
		
		// adicionando comida
			comidas.add(new Comida("res/bola.png", 400, 400));
			comidas.add(new Comida("res/bola.png", 400, 200));
			comidas.add(new Comida("res/bola.png", 200, 400));
			comidas.add(new Comida("res/bola.png", 100, 100));
			comidas.add(new Comida("res/bola.png", 500, 300));
		
		try {
			enderecoIP = InetAddress.getByName(ip);
			porta = port;
			dataGramSocket = new DatagramSocket();
			new Thread(new ListenerSocket()).start();
			
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}	
	}
	
private class ListenerSocket implements Runnable{

	public void run(){

		while(true){
			receberDados = new byte[1024];
			receberPacote = new DatagramPacket(receberDados, receberDados.length);

			try {
				System.out.println("Aguardando resposta do servidor...");
				dataGramSocket.receive(receberPacote);
				System.out.println("Pacote do servidor recebido no cliente!");
			} catch (IOException e) {
				e.printStackTrace();
			}

			//convertendo bytes para objeto
			enviarDados = new byte[1024];
			enviarDados = receberPacote.getData();
			Data data = deSerializar(enviarDados);
			
			renderPlayer(data);

		}

	}

}

	public void renderPlayer(Data data){
		// condição para o usuario controlar um objeto
		if(!data.getNome().equals(jogadores.get(1).getNome())){
			jogadores.get(0).setX(data.getX());
			jogadores.get(0).setY(data.getY());
		}
		
	}

	public static void enviarPacote(Data jogador){
		
		enviarDados = new byte[1024];
		enviarDados = Serializar(jogador);
		
		try {	
			enviarPacote = new DatagramPacket(enviarDados, enviarDados.length, enderecoIP, porta);
			dataGramSocket.send(enviarPacote);
			System.out.println("enviou o pacote para o servidor!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static byte[] Serializar(Data jogador){
		
		byteArray = new ByteArrayOutputStream(); 
		
		try {
			output = new ObjectOutputStream (byteArray);
			output.writeObject(jogador);
			output.close();
			enviarDados = byteArray.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return enviarDados;
	}
	
	public Data deSerializar(byte[] enviarDados){
		
		Data joga = null;
		
		ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(enviarDados);
		
		try {
			ObjectInputStream input = new ObjectInputStream(byteArrayInput);
			joga =  (Data) input.readObject();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return joga;	
	}
	
}
