package fases;
import modelo.Comida;
import modelo.Perssonagem;
import jplay.GameImage;
import jplay.Keyboard;
import jplay.Window;
import service.Data.Action;
import service.GameCliente;


public class Fase1 implements Runnable{
	
	private Window janela;
	private GameImage imagem;
	private Keyboard teclado;
	
	private long horaAtual;
    private long horaUltima;
    private long deltaTime;
    
    long lastTime = System.currentTimeMillis();
	int frames = 0;
	
	public Fase1(String nick, int porta){
		this.janela = new Window(800, 600);
		this.imagem = new GameImage("res/fundo.jpg");
		this.teclado = this.janela.getKeyboard();
		
		//new GameServidor(60000);
		new GameCliente(porta);
		
		//String nome = JOptionPane.showInputDialog("Qual seu nome?");
		GameCliente.jogadores.add(new Perssonagem("res/bola2.png", 1, 200, 200));
		GameCliente.jogadores.add(new Perssonagem("res/bola.png", 1, 100, 100, nick));
		GameCliente.jogadores.get(1).data.setAction(Action.CADASTRAR);
		GameCliente.enviarPacote(GameCliente.jogadores.get(1).data);

		horaAtual = System.currentTimeMillis();
		horaUltima = System.currentTimeMillis();
		deltaTime = 0;
		
	}
	
	public Fase1(String nick, String ip, int porta){
		this.janela = new Window(800, 600);
		this.imagem = new GameImage("res/fundo.jpg");
		this.teclado = this.janela.getKeyboard();
		
		//new GameServidor(60000);
		new GameCliente(ip, porta);
		
		//String nome = JOptionPane.showInputDialog("Qual seu nome?");
		GameCliente.jogadores.add(new Perssonagem("res/bola2.png", 1, 200, 200));
		GameCliente.jogadores.add(new Perssonagem("res/bola.png", 1, 100, 100, nick));
		GameCliente.jogadores.get(1).data.setAction(Action.CADASTRAR);
		GameCliente.enviarPacote(GameCliente.jogadores.get(1).data);

		horaAtual = System.currentTimeMillis();
		horaUltima = System.currentTimeMillis();
		deltaTime = 0;
	}
	
	public void run(){
		
		while(true){
			
			GameCliente.jogadores.get(1).setMovimento(getDeltaTime());
			
			this.imagem.draw();
				
			GameCliente.jogadores.get(1).mover(this.janela, this.teclado);
			GameCliente.jogadores.get(1).draw();
			GameCliente.jogadores.get(0).draw();
			
			for (Comida comida : GameCliente.comidas) {
				comida.draw();
				comida.colisao(GameCliente.jogadores.get(1));
			}
			
			if(teclado.keyDown(Keyboard.LEFT_KEY) || teclado.keyDown(Keyboard.RIGHT_KEY) || 
			   teclado.keyDown(Keyboard.DOWN_KEY) || teclado.keyDown(Keyboard.UP_KEY) ){
				
				GameCliente.jogadores.get(1).data.setAction(Action.MOVER);
				GameCliente.enviarPacote(GameCliente.jogadores.get(1).data);
				
			}
			
			this.janela.update();
			//janela.delay(1);
			
			fps();
			
		}
	}
	
	public void fps(){
		frames++;
		if (System.currentTimeMillis() - lastTime > 1000) 
		{
			lastTime += 1000;
			this.janela.setTitle("FPS: "+ frames);
			frames = 0;
		}
	}
	
	public double getDeltaTime(){
		
		horaAtual = System.currentTimeMillis();
		deltaTime = horaAtual - horaUltima;
		horaUltima = horaAtual;
		
		return deltaTime;
	}
	
}
