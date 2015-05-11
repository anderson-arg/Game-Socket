package bean;
import java.io.Serializable;

import jplay.Keyboard;
import jplay.Sprite;
import jplay.Window;
import service.Data;


public class Perssonagem extends Sprite implements Serializable{
	
	private int direcao;
	private double movimento;
	private boolean isMover;

	private String nome;
	
	public static Data data;

	public Perssonagem(String fileName, int num, int x, int y) {
		super(fileName, num);
		
		this.direcao = 5;
		
		this.x = x;
		this.y = y;
		
	}
	
	public Perssonagem(String fileName, int num, int x, int y, String nome) {
		super(fileName, num);
		
		this.direcao = 5;
		
		this.x = x;
		this.y = y;
		this.nome = nome;
		
		data = new Data(nome, x, y);
		
	}
	
	public void mover(Window janela, Keyboard teclado){
		
		if(teclado.keyDown(Keyboard.LEFT_KEY)){
			if(this.x > 0)
				this.x -= movimento;
			
			if(direcao != 1){
				//setCurrFrame(1);
				direcao = 1;
			}
			
		}
		
		if(teclado.keyDown(Keyboard.RIGHT_KEY)){
			if(this.x < janela.getWidth() - 50)
				this.x += movimento;
			
			if(direcao != 2){
				//setCurrFrame(0);
				direcao = 2;
			}
			
		}
		
		data.setXYN(nome, (int)x, (int)y);
		
		if(teclado.keyDown(Keyboard.UP_KEY)){
			if(this.y > 0)
				this.y -= movimento;
			
			if(direcao != 3){
				//setCurrFrame(0);
				direcao = 3;
			}
			
		}
		
		if(teclado.keyDown(Keyboard.DOWN_KEY)){
			if(this.y < janela.getHeight() - 50)
				this.y += movimento;
			
			if(direcao != 4){
				//setCurrFrame(1);
				direcao = 4;
			}
			
		}
		
	}

	public int getDirecao() {
		return direcao;
	}

	public void setDirecao(int direcao) {
		this.direcao = direcao;
	}

	public double getMovimento() {
		return movimento;
	}

	public void setMovimento(double movimento) {
		this.movimento = 0.15*movimento;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
