package bestiary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.io.FileReader;
import java.io.IOException;

import java.nio.file.*;   

import org.json.JSONArray;
import org.json.JSONObject;

public final class TroopManager {
	private static String conteudoJson;
    private static Path caminho;

    private static Map<Integer, Troop> tropasExistentes;
    private static Troop troopCarregada;
	private static Monsters monstroCarregado;
    private static JSONObject troop;
    private static JSONArray troopsArray;
    private static JSONArray inimigosArray;

    private TroopManager(){
    }

    public final static void carregarTropas(){
        try {
            caminho = Paths.get("data", "system", "troop.json");
            conteudoJson = Files.readString(caminho);

            tropasExistentes = new HashMap<Integer, Troop>();
            troopsArray = new JSONArray(conteudoJson);

            for (int i = 0; i < troopsArray.length(); i++){
                troop = troopsArray.getJSONObject(i);

                int id = troop.getInt("id");
                int exp = troop.getInt("exp");
                int ouro = troop.getInt("ouro");

                inimigosArray = troop.getJSONArray("inimigos");
                java.util.List<Troop.Inimigo> listaInimigos = new java.util.ArrayList<>();

                for (int j = 0; j < inimigosArray.length(); j++){
                    JSONObject inimigoObj = inimigosArray.getJSONObject(j);
                    int idMonstro = inimigoObj.getInt("idMonstro");
                    int nivel = inimigoObj.getInt("nivel");
       			
					Monsters monstroRequerido = MonstersManager.getMonstro(idMonstro);
					if (monstroRequerido == null) throw new IllegalArgumentException("Monstro_"+idMonstro+" é nulo.");
					monstroCarregado = new Monsters(monstroRequerido);
					
                    Troop.Inimigo inimigo = new Troop.Inimigo(monstroCarregado, nivel);
                    listaInimigos.add(inimigo);
                }

                troopCarregada = new Troop(id, listaInimigos, exp, ouro);
                tropasExistentes.put(troopCarregada.getId(), troopCarregada);
            }

            System.out.println(">>Tropas carregadas: "+tropasExistentes.size());
        }catch (IOException | IllegalArgumentException e){
            System.out.println("Erro ao carregar tropas: "+e.getMessage());
        }
    }

    protected static Map<Integer, Troop> getTropasExistentes(){
        return tropasExistentes;
    }

    public static Troop getTroop(int id){
        return tropasExistentes.get(id);
    }
	
	//===
}