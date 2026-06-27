package bestiary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.io.FileReader;
import java.io.IOException;

import java.nio.file.*;   

import org.json.JSONArray;
import org.json.JSONObject;

public final class MonstersManager {
	private static String conteudoJson;
	private static Path caminho;
	
	private static Map<Integer, Monsters> monstrosExistentes; 
	private static Monsters monstroCarregado;
	private static JSONObject monstros;
	private static JSONArray monstrosArray;
	private static JSONArray elementosArray;
	private static JSONArray habilidadesArray;
	private static JSONArray traçosArray;
	
	private MonstersManager(){
	}
	
	public final static void carregarMonstros(){
		try{
			caminho = Paths.get("data", "system", "monsters.json");
			conteudoJson = Files.readString(caminho);
			
			monstrosExistentes = new HashMap<Integer, Monsters>();
			monstrosArray = new JSONArray(conteudoJson);
			Map<Integer, Skills> skillsExistentes = SkillsManager.getSkillsExistentes();
			
			for (int i = 0; i < monstrosArray.length(); i++){
				monstros = monstrosArray.getJSONObject(i);
				Monsters.Classes classeConvertida = Monsters.Classes.valueOf(monstros.getString("classe"));
				
				elementosArray = monstros.getJSONArray("elementos");
				Monsters.Elementos[] elementosConvertidos = new Monsters.Elementos[elementosArray.length()];
				for (int e = 0; e < elementosArray.length(); e++){
					elementosConvertidos[e] = Monsters.Elementos.valueOf(elementosArray.getString(e));
				}
				Monsters.Raridades raridadeConvertida = Monsters.Raridades.valueOf(monstros.getString("raridade"));
				
				traçosArray = monstros.getJSONArray("tracos");
                int[] traçosIds = new int[traçosArray.length()];
                for (int j = 0; j < traçosArray.length(); j++){
                    traçosIds[j] = traçosArray.getInt(j);
                }
				
				Monsters monstroCarregado = new Monsters(
					monstros.getInt("id"),
					monstros.getString("nome"),
					classeConvertida,
					elementosConvertidos,
					raridadeConvertida,
					monstros.getInt("nivelBase"),
					monstros.getInt("forcaBase"),
					monstros.getInt("vidaBase"),
					monstros.getInt("velocidadeBase"),
					monstros.getInt("estaminaBase"),
					traçosIds
				);
				
				Map<Integer, Skills> skillsTree = new HashMap<Integer, Skills>();
				habilidadesArray = monstros.getJSONArray("habilidades");
				Skills skillEncontrada = null;
				for (int j = 0; j < habilidadesArray.length(); j++){
					JSONObject habInfo = habilidadesArray.getJSONObject(j);
					int idHabilidade = habInfo.getInt("id");
					int nivelAprendivel = habInfo.getInt("nivel");
					Skills habilidadeEncontrada = SkillsManager.getSkillsExistentes().get(idHabilidade);
					
					Skills skillAtual = new Skills(habilidadeEncontrada);
					
					if (habilidadeEncontrada != null){
						skillAtual.setNivelNecessario(nivelAprendivel);
						monstroCarregado.adicionarHabilidadeArvore(nivelAprendivel, skillAtual);
					}
				}
				
				monstrosExistentes.put(monstroCarregado.getIdMonstro(), monstroCarregado);
			}
        
        System.out.println(">>Monstros carregados: " +monstrosExistentes.size());
		}catch (IOException | IllegalArgumentException e){ 
			System.out.println("Erro ao carregar monstros: "+e.getMessage());
		}
	}
	
	public static Monsters getMonstro(int idMonstro){
		monstroCarregado = monstrosExistentes.get(idMonstro);
		
		return monstroCarregado;
	}
	
	//===
}