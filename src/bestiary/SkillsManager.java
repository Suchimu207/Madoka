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
	private static Monsters skillCarregada;
	private static JSONObject skills;
	private static JSONArray skillsArray;
	private static JSONArray elementosArray;
	
	public SkillsManager(){
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
				
				Skills skillCarregada = new Skills(
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
				skillsExistentes.put(skillCarregada.getIdHabilidade(), skillCarregada);
				
				/*
				System.out.println("-------------------");
				System.out.println("ID: " + skillCarregada.getIdHabilidade());
				System.out.println("Nome: " + skillCarregada.getNomeHabilidade());
				System.out.println("Elemento: " + skillCarregada.getElementoHabilidade());
				System.out.println("Tipo: " + skillCarregada.getTipoHabilidade());
				System.out.println("Alvo: " + skillCarregada.getAlvoHabilidade());
				System.out.println("Poder: " + skillCarregada.getPoderHabilidade());
				System.out.println("Precisão base: " + skillCarregada.getPrecisaoBase());
				System.out.println("Energia: " + skillCarregada.getEnergiaHabilidade());
				System.out.println("Recarga: " + skillCarregada.getRecargaHabilidade());
				System.out.println("-------------------");
				System.out.println("");
				*/
			}
        
        System.out.println(">>Habilidades carregadas: " +skillsExistentes.size());
		System.out.println("");
		}catch (IOException | IllegalArgumentException e){ 
			System.out.println("Erro ao carregar habilidades: "+e.getMessage());
		}
	}

	//===
}