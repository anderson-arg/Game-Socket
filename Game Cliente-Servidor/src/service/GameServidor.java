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
import java.util.HashMap;
import java.util.Map;
import service.Data.Action;


public class GameServidor{
	
	private Map<String, DatagramPacket> posicao = new HashMap<String, DatagramPacket>();
	
	private DatagramSocket servidorSocket;
	private DatagramPacket pacoteRecebido;
	private DatagramPacket pacoteEnviado;
	
	private InetAddress enderecoIP;
	private int porta;
	
	private byte[] receberDados;
	private byte[] enviarDados;
	
	//converter objeto em bytes
	private ByteArrayInputStream byteArrayInput;
	private ObjectInputStream input;
	
	public GameServidor(int porta){
		
		try {
			servidorSocket = new DatagramSocket(porta);
			System.out.println("Porta criado com sucesso!");

			new Thread(new ListenerSocket()).start();
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
public class ListenerSocket implements Runnable{

	private Data data;

	@Override
	public void run(){
		System.out.println("entrou no listener do servidor!");
		while(true){

			receberDados = new byte[1024];
			pacoteRecebido = new DatagramPacket(receberDados, receberDados.length);

			try {
				servidorSocket.receive(pacoteRecebido);
				System.out.println("Pacote recebido com sucesso!");

				enderecoIP = pacoteRecebido.getAddress();
				porta = pacoteRecebido.getPort();
				System.out.println("Endereço IP cliente: "+enderecoIP+" Porta cliente: "+porta);

			} catch (IOException e) {
				e.printStackTrace();
			}

			//convertendo bytes para objeto
			enviarDados = new byte[1024];
			enviarDados = pacoteRecebido.getData();
			data = deSerializar(enviarDados);
			System.out.println("Dados deSerializado!");	

			Action action = data.getAction();
			
			if(action.equals(Action.CADASTRAR)){		
				// adicionar usuario
				addUsuario(data);
				data.setAction(null);
			}else if(action.equals(Action.MOVER)){
				// enviar para todos
				enviarParaAll(enviarDados);				
			}
			
			System.out.println("Pacote enviado para o cliente!");
		}

	}

}

	public void enviarParaAll(byte[] enviarDados){
		
		for(Map.Entry<String, DatagramPacket> kv : posicao.entrySet()){
			enviarPacote(enviarDados, kv.getValue().getAddress(), kv.getValue().getPort());
		}
		
	}

	public void addUsuario(Data data){
		int cont=0;
		
		for(Map.Entry<String, DatagramPacket> kv : posicao.entrySet()){
			if(kv.getKey().equals(data.getNome())){
				cont++;
				System.out.println("Ja existe este nome no servidor");	
			}
		}
		if(cont == 0){						
			posicao.put(data.getNome(), pacoteRecebido);
			cont = 0;
			System.out.println("Incluio no servidor: "+data.getNome());
		}
	}
	
	public void enviarPacote(byte[] enviarDados, InetAddress enderecoIP, int porta){
		
		pacoteEnviado = new DatagramPacket(enviarDados, enviarDados.length, enderecoIP, porta);
		
		try {
			servidorSocket.send(pacoteEnviado);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public byte[] serializar(Data jogador){
		
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream(); 
		
		try {
			ObjectOutputStream output = new ObjectOutputStream (byteArray);
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
		
		byteArrayInput = new ByteArrayInputStream(enviarDados);
		
		try {
			input = new ObjectInputStream(byteArrayInput);
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
