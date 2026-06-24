package bestiary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.io.FileReader;
import java.io.IOException;

import java.nio.file.*;   

import org.json.JSONArray;
import org.json.JSONObject;

public final class SkillsManager {
	private static String conteudoJson;
	private static Path caminho;
	
	private static Map<Integer, Skills> skillsExistentes; 
	private static Skills skillCarregada;
	private static JSONObject skills;
	private static JSONArray skillsArray;
	private static JSONArray elementosArray;
	private static JSONArray efeitosArray;
	
	private SkillsManager(){
	}
	
	public static void carregarHabilidades(){
		try{
			caminho = Paths.get("data", "system", "skills.json");
			conteudoJson = Files.readString(caminho);
			
			skillsExistentes = new HashMap<Integer, Skills>();
			skillsArray = new JSONArray(conteudoJson);
			
			for (int i = 0; i < skillsArray.length(); i++){
				skills = skillsArray.getJSONObject(i);
				
				Monsters.Elementos elementoConvertido = Monsters.Elementos.valueOf(skills.getString("elemento"));
				Skills.TipoHabilidade tipoConvertido = Skills.TipoHabilidade.valueOf(skills.getString("tipo"));
				Skills.TipoAlvo alvoConvertido = Skills.TipoAlvo.valueOf(skills.getString("alvo"));
				
				skillCarregada = new Skills(
					skills.getInt("id"),
					skills.getString("nome"),
					elementoConvertido,
					tipoConvertido,
					alvoConvertido,
					skills.getInt("poder"),
					skills.getInt("precisaoBase"),
					skills.getInt("energia"),
					skills.getInt("recarga")
				);
				
				efeitosArray = skills.optJSONArray("efeitos");
				if (efeitosArray != null){
					for (int e = 0; e < efeitosArray.length(); e++){
						JSONObject efeitoObj = efeitosArray.getJSONObject(e);
						
						String tipoStr = efeitoObj.getString("efeito");
						Skills.EfeitoHabilidade tipo = Skills.EfeitoHabilidade.valueOf(tipoStr);
						int alvo = efeitoObj.getInt("alvo");
						int valor = efeitoObj.getInt("valor");
						int chance = efeitoObj.getInt("chance");
						
						Skills.Efeito efeito = new Skills.Efeito(tipo, alvo, valor, chance);
						skillCarregada.adicionarEfeito(efeito);
					}
				}
				
				skillsExistentes.put(skillCarregada.getIdHabilidade(), skillCarregada);
			}
        
        System.out.println(">>Habilidades carregadas: " +skillsExistentes.size());
		System.out.println("");
		}catch (IOException | IllegalArgumentException e){ 
			System.out.println("Erro ao carregar habilidades: "+e.getMessage());
		}
	}
	
	public static Map<Integer, Skills> getSkillsExistentes(){
		return skillsExistentes;
	}
	
	//===
}