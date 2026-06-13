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
	private static JSONArray traçosArray;
	
	public MonstersManager(){
	}
	
	public static void carregarMonstros(){
		try{
			caminho = Paths.get("data", "system", "monsters.json");
			conteudoJson = Files.readString(caminho);
			
			monstrosExistentes = new HashMap<Integer, Monsters>();
			monstrosArray = new JSONArray(conteudoJson);
			
			for (int i = 0; i < monstrosArray.length(); i++){
				monstros = monstrosArray.getJSONObject(i);
				Monsters.Classes classeConvertida = Monsters.Classes.valueOf(monstros.getString("classe"));
				
				elementosArray = monstros.getJSONArray("elementos");
				Monsters.Elementos[] elementosConvertidos = new Monsters.Elementos[elementosArray.length()];
				for (int e = 0; e < elementosArray.length(); e++) {
					elementosConvertidos[e] = Monsters.Elementos.valueOf(elementosArray.getString(e));
				}
				
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
					monstros.getInt("forcaBase"),
					monstros.getInt("vidaBase"),
					monstros.getInt("velocidadeBase"),
					monstros.getInt("estaminaBase"),
					traçosIds
				);
				monstrosExistentes.put(monstroCarregado.getIdMonstro(), monstroCarregado);
				
				System.out.println("ID: " + monstroCarregado.getIdMonstro());
				System.out.println("Nome: " + monstroCarregado.getNomeMonstro());
				System.out.println("Classe: " + monstroCarregado.getClasseAtual());
				System.out.println("Elementos: " + monstroCarregado.getElementosAtuais());
				System.out.println("Força base: " + monstroCarregado.getForcaBase());
				System.out.println("Vida base: " + monstroCarregado.getVidaBase());
				System.out.println("Velocidade base: " + monstroCarregado.getSpeedBase());
				System.out.println("Estamina base: " + monstroCarregado.getEstaminaBase());
				System.out.println("Traços: " + Arrays.toString(monstroCarregado.getTracosIds()));
			}
        
		System.out.println("");
        System.out.println("Monstros carregados: " +monstrosExistentes.size());
		System.out.println("");
		}catch (IOException | IllegalArgumentException e){ 
			System.out.println("Erro ao carregar monstros: "+e.getMessage());
		}
	}
	
	//===
}