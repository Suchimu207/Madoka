package bestiary;

import java.util.HashMap;
import java.util.Map;

import java.io.FileReader;
import java.io.IOException;

import java.nio.file.*;   

import org.json.JSONArray;
import org.json.JSONObject;

public final class TraitsManager {
	private static String conteudoJson;
	private static Path caminho;
	
	private static Map<Integer, Traits> traçosExistentes; 
	private static Traits traçoCarregado;
	private static JSONObject traços;
	private static JSONArray traçosArray;
	
	public TraitsManager(){
	}
	
	public static void carregarTraços(){
		try{
			caminho = Paths.get("data", "system", "traits.json");
			conteudoJson = Files.readString(caminho);
			
			traçosExistentes = new HashMap<Integer, Traits>();
			traçosArray = new JSONArray(conteudoJson);
			
			for (int i = 0; i < traçosArray.length(); i++){
				traços = traçosArray.getJSONObject(i);
            
				Traits traçoCarregado = new Traits(
					traços.getInt("id"), 
					traços.getString("nome"), 
					traços.getString("descricao"), 
					traços.getBoolean("habilitado")
				);
				traçosExistentes.put(traçoCarregado.getIdTraço(), traçoCarregado);
				
				System.out.println("ID: " + traçoCarregado.getIdTraço());
				System.out.println("Nome: " + traçoCarregado.getNomeTraço());
				System.out.println("Descrição: " + traçoCarregado.getDescriçãoTraço());
				System.out.println("Habilitado: " + traçoCarregado.isTraçoHabilitado());
			}
        
		System.out.println("");
        System.out.println("Traços carregados: " +traçosExistentes.size());
		System.out.println("");
		}catch (IOException e){ 
			System.out.println("Erro ao carregar traços: "+e.getMessage());
		}
	}
	
	//===
}