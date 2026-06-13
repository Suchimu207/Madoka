package bestiary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Monsters {
	protected enum Classes{
		ATACANTE("Atacante"),
		SUPORTE("Suporte"),
		GENERALISTA("Generalista"),
		SABOTADOR("Sabotador"),
		TANQUE("Tanque");
		
		private String nomeClasse;
		
		Classes(String nomeClasse){
			this.nomeClasse = nomeClasse;
		}
		
		public String getClasseNome(){
			return nomeClasse;
		}
	}
	
	protected enum Elementos{
		FOGO("Fogo"),
		NATUREZA("Natureza"),
		MAGIA("Magia"),
		METAL("Metal"),
		VENTO("Vento"),
		LUZ("Luz"),
		TREVAS("Trevas"),
		TERRA("Terra"),
		TROVAO("Trovão"),
		AGUA("Água");
		
		private String nomeElemento;
		
		Elementos(String nomeElemento){
			this.nomeElemento = nomeElemento;
		}
		
		public String getElementoNome(){
			return nomeElemento;
		}
	}
	
	private String nomeMonstro;
	private int forçaBase, forçaAtual, forçaAtualCombate;
	private int vidaBase, vidaAtual, vidaAtualCombate;
	private int speedBase, speedAtual, speedAtualCombate;
	private int estaminaBase, estaminaAtual, estaminaAtualCombate;
	private int nivelBase, nivelAtual;
	private int expAtual;
	
	private Classes classeAtual;
	private Elementos elementosAtuais;
	private Map<Integer, Traits> traçosExistentes; 
	
	public Monsters(String nomeMonstro, Classes classeAtual, Elementos elementosAtuais,
	int forçaBase, int vidaBase, int speedBase, int estaminaBase){
		this.nomeMonstro = nomeMonstro;
		this.classeAtual = classeAtual;
		this.elementosAtuais = elementosAtuais;
		this.forçaBase = forçaBase;
		this.vidaBase = vidaBase;
		this.speedBase = speedBase;
		this.estaminaBase = estaminaBase;
		//this.tracosAtuais.add(traço);
	}
	
	//===
}