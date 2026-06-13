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
		this.idTraço = idTraço;
		this.nomeTraço = nomeTraço;
		this.descriçãoTraço = descriçãoTraço;
		this.traçoHabilitado = traçoHabilitado;
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