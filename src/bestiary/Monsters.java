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
	
	private int forçaBase, forçaAtual, forçaAtualCombate;
	private int vidaBase, vidaAtual, vidaAtualCombate;
	private int speedBase, speedAtual, speedAtualCombate;
	private int estaminaBase, estaminaAtual, estaminaAtualCombate;
	private int[] traçosIds;
	
	private Classes classeAtual;
	private Elementos[] elementosAtuais;
	
	private Map<Integer, Traits> traçosExistentes; 
	
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
		}catch(IllegalArgumentException e){
			System.out.println("Erro ao criar monstro ID_"+idMonstro+": "+e.getMessage());
			System.exit(1);
		}
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
	
	public String getElementosAtuais(){
		return Arrays.toString(elementosAtuais);
	}
	
	public int getNivelBase(){
		return nivelBase;
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
	
	//===
}