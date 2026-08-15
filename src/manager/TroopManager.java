package manager;

import bestiary.Monsters;
import bestiary.Troop;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import java.io.FileReader;
import java.io.IOException;

import java.nio.file.*;   

import org.json.JSONArray;
import org.json.JSONObject;

public final class TroopManager {
	private static String conteudoJson;
    private static Path caminho;

    private static Map<Integer, Troop> tropasExistentes;
	private static Monsters monstroCarregado;
    private static JSONObject troop;
    private static JSONArray troopsArray;
    private static JSONArray inimigosArray;

    private TroopManager(){
    }

    protected final static void carregarTropas(){
		if (tropasExistentes != null){
			return;
		}
		
        try {
            caminho = Paths.get("data", "system", "troop.json");
            conteudoJson = Files.readString(caminho);

            tropasExistentes = new LinkedHashMap<Integer, Troop>();
            troopsArray = new JSONArray(conteudoJson);

            for (int i = 0; i < troopsArray.length(); i++){
                troop = troopsArray.getJSONObject(i);

                int id = troop.getInt("id");
				String nome = troop.optString("nome","Inimigo Desconhecido");
                int exp = troop.optInt("exp", 0);
                int ouro = troop.optInt("ouro", 0);
				
                inimigosArray = troop.getJSONArray("inimigos");
                List<Troop.Inimigo> listaInimigos = new java.util.ArrayList<>();
				
                for (int j = 0; j < inimigosArray.length(); j++){
                    JSONObject inimigoObj = inimigosArray.getJSONObject(j);
                    int idMonstro = inimigoObj.getInt("idMonstro");
                    int nivel = inimigoObj.getInt("nivel");
       			
					Monsters monstroRequerido = MonstersManager.getMonstro(idMonstro);
					if (monstroRequerido == null) throw new IllegalArgumentException("Monstro_"+idMonstro+" é nulo.");
					monstroCarregado = new Monsters(monstroRequerido);
					
                    Troop.Inimigo inimigo = new Troop.Inimigo(monstroCarregado, nivel, false);
                    listaInimigos.add(inimigo);
                }
				
                Troop troopRequerida = new Troop(id, nome, listaInimigos, exp, ouro);
				Troop troopCarregada = new Troop(troopRequerida); 
				
                tropasExistentes.put(troopCarregada.getId(), troopCarregada);
            }

            System.out.println(">>Tropas carregadas: "+tropasExistentes.size());
        }catch (IOException | IllegalArgumentException e){
            System.out.println("Erro ao carregar tropas: "+e.getMessage());
        }
    }

    public static Map<Integer, Troop> getTropasExistentes(){
        return tropasExistentes;
    }

    public static Troop getTroop(int id){
        return tropasExistentes.get(id);
    }
	
	//===
}