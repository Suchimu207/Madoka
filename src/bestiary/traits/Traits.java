package bestiary.traits;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class Traits {
	public enum EfeitosTraço{
		IMMUNITY("Imunidade"),
		ATTUNNED("Harmonizado"),
		BONUS_ATTRIBUTE("Atributo bônus"),
		STATUS_ARMOR("Reforçado");
		
		private final String nome;
		
		EfeitosTraço(String nome){
			this.nome = nome;
		}
		
		public String getNome(){
			return nome;
		}
	}
	private int idTraço;
	private String nomeTraço;
	private String descriçãoTraço;
	private boolean traçoHabilitado;
	
	private List<TraitEffect> efeitosTraço;
	
	public Traits(int idTraço, String nomeTraço, String descriçãoTraço, List<TraitEffect> efeitosTraço){
		try{
			if (idTraço <= 0){
				throw new IllegalArgumentException("ID deve ser maior do que 0.");
			}
			this.idTraço = idTraço;
			
			this.nomeTraço = nomeTraço;
			this.descriçãoTraço = descriçãoTraço;
			this.efeitosTraço = efeitosTraço;
			this.traçoHabilitado = true;
		}catch(IllegalArgumentException e){
			System.out.println("Erro ao criar traço ID_"+idTraço+": "+e.getMessage());
			System.exit(1);
		}
	}
	
	public int getIdTraço(){ 
		return idTraço; 
	}
	
	public String getNomeTraço(){ 
		return nomeTraço; 
	}
	
	public String getDescriçãoTraço(){ 
		return descriçãoTraço; 
	}
	
	public boolean isTraçoHabilitado(){ 
		return traçoHabilitado; 
	}
	
	public List<TraitEffect> getEfeitosTraço(){
		return efeitosTraço;
	}
	
	//===
}