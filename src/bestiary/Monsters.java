package bestiary;

import java.util.Arrays;
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
		AGUA("Água"),
		FISICO("Físico");
		
		private String nomeElemento;
		
		Elementos(String nomeElemento){
			this.nomeElemento = nomeElemento;
		}
		
		public String getElementoNome(){
			return nomeElemento;
		}
	}
	
	private int idMonstro;
	private String nomeMonstro;
	private int nivelBase, nivelAtual;
	private int expAtual;
	private boolean monstroEquipado;
	
	private int forçaBase, forçaAtual, forçaAtualCombate;
	private int vidaBase, vidaAtual, vidaAtualCombate;
	private int speedBase, speedAtual, speedAtualCombate;
	private int estaminaBase, estaminaAtual, estaminaAtualCombate;
	private int[] traçosIds;
	
	private Classes classeAtual;
	private Elementos[] elementosAtuais;
	
	public Monsters(int idMonstro, String nomeMonstro, Classes classeAtual, Elementos[] elementosAtuais,
	int nivelBase, int expAtual, int forçaBase, int vidaBase, int speedBase, int estaminaBase, int[] traçosIds){
		try{
			if (idMonstro <= 0){
				throw new IllegalArgumentException("ID deve ser maior do que 0.");
			}
			this.idMonstro = idMonstro;
			
			this.nomeMonstro = nomeMonstro;
			this.classeAtual = classeAtual;
			
			if (elementosAtuais.length >= 3){
				throw new IllegalArgumentException("O monstro não pode ter mais de dois elementos.");
			}
			this.elementosAtuais = elementosAtuais;
			
			if (nivelBase <= 0){
				throw new IllegalArgumentException("Nivel base deve ser maior do que 0.");
			}
			this.nivelBase = nivelBase;
			this.nivelAtual = nivelBase;
			
			if (expAtual != 0){
				throw new IllegalArgumentException("ExpAtual deve ser igual a 0.");
			}
			this.expAtual = expAtual;
			
			if (forçaBase <= 0 || vidaBase <= 0 || speedBase <= 0 || estaminaBase <= 0){
				throw new IllegalArgumentException("Atributos base devem ser maiores do que 0.");
			}
			this.forçaBase = forçaBase;
			this.vidaBase = vidaBase;
			this.speedBase = speedBase;
			this.estaminaBase = estaminaBase;
			
			this.traçosIds = traçosIds;
			this.monstroEquipado = false;
		}catch(IllegalArgumentException e){
			System.out.println("Erro ao criar monstro ID_"+idMonstro+": "+e.getMessage());
			System.exit(1);
		}
	}
	
   public Monsters(Monsters monstroRequerido){
	   this.idMonstro = monstroRequerido.getIdMonstro();
	   this.nomeMonstro = monstroRequerido.getNomeMonstro();
	   this.classeAtual = monstroRequerido.getClasseAtual();
	   this.elementosAtuais = monstroRequerido.getElementosAtuaisValores();
	   this.nivelBase = monstroRequerido.getNivelBase();
	   this.nivelAtual = monstroRequerido.getNivelAtual();
	   this.expAtual = monstroRequerido.getExpAtual();
	   this.forçaBase = monstroRequerido.getForcaBase();
	   this.vidaBase = monstroRequerido.getVidaBase();
	   this.speedBase = monstroRequerido.getSpeedBase();
	   this.estaminaBase = monstroRequerido.getEstaminaBase();
	   this.traçosIds = monstroRequerido.getTracosIds();
	   this.monstroEquipado = false;
	} 

	public int getIdMonstro(){
		return idMonstro;
	}

	public String getNomeMonstro(){
		return nomeMonstro;
	}
	
	public Classes getClasseAtual(){
		return classeAtual;
	}
	
	public Elementos[] getElementosAtuaisValores(){
		return elementosAtuais;
	}
	
	public String getElementosAtuais(){
		return Arrays.toString(elementosAtuais);
	}
	
	public int getNivelBase(){
		return nivelBase;
	}
	
	public int getNivelAtual(){
		return nivelAtual;
	}
	
	public int getExpAtual(){
		return expAtual;
	}
	
	public int getForcaBase(){
		return forçaBase;
	}

	public int getVidaBase(){
		return vidaBase;
	}

	public int getSpeedBase(){
		return speedBase;
	}

	public int getEstaminaBase(){
		return estaminaBase;
	}

	public int[] getTracosIds(){
		return traçosIds;
	}
	
	public boolean isMonstroEquipado(){ 
		return monstroEquipado;
	}
	
	public void setMonstroEquipado(boolean monstroEquipado){
		this.monstroEquipado = monstroEquipado;
	}
	
	//===
}