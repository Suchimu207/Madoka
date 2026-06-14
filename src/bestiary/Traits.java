package bestiary;

import java.util.HashMap;
import java.util.Map;

public class Traits {
	private int idTraço;
	private String nomeTraço;
	private String descriçãoTraço;
	private boolean traçoHabilitado;
	
	private static Map<Integer, Traits> traçosExistentes; 
	private static Traits traçoAtual;
	
	public Traits(int idTraço, String nomeTraço, String descriçãoTraço, boolean traçoHabilitado){
		try{
			if (idTraço <= 0){
				throw new IllegalArgumentException("ID deve ser maior do que 0.");
			}
			this.idTraço = idTraço;
			
			this.nomeTraço = nomeTraço;
			this.descriçãoTraço = descriçãoTraço;
			this.traçoHabilitado = traçoHabilitado;
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
	
	//===
}