package service;

import java.io.Serializable;
import java.util.Set;


public class Data implements Serializable
{
	
	private int x, y;
	private String nome;
	private Set<String> Online;
	private Action action;
	
	public Data(String nome, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getX() 
	{
		return x;
	}
	
	public int getY() 
	{
		return y;
	}
	
	public void setAll(int id, int x, int y, String nick)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setXYN(String nome, int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Set<String> getOnline() {
		return Online;
	}

	public void setOnline(Set<String> online) {
		Online = online;
	}
	
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	public enum Action{
		ENVIAR_ALL, CADASTRAR, MOVER
	}
}
