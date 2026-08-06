package combat.status;

import bestiary.Monsters;

import combat.status.strategies.*;

import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.nio.file.*;

import org.json.JSONArray;
import org.json.JSONObject;

public final class StatusManager {
    private static String conteudoJson;
    private static Path caminho;

    private static Map<Integer, StatusData> statusDataExistentes;
    private static StatusData statusCarregado;
    private static JSONObject status;
    private static JSONArray statusArray;

    private StatusManager(){
    }

    public final static void carregarStatus(){
        try {
            caminho = Paths.get("data", "system", "status.json");
            conteudoJson = Files.readString(caminho);
			
            statusDataExistentes = new HashMap<Integer, StatusData>();
            statusArray = new JSONArray(conteudoJson);

            for (int i = 0; i < statusArray.length(); i++){
                status = statusArray.getJSONObject(i);

                int id = status.getInt("id");
                String nome = status.getString("nome");
                String tipo = status.getString("tipo");
                String subtipo = status.getString("subtipo");
				
                statusCarregado = new StatusData(id, nome, tipo, subtipo);
                statusDataExistentes.put(statusCarregado.getId(), statusCarregado);
            }
			
            System.out.println(">>Status carregados: " + statusDataExistentes.size());
        }catch (IOException | IllegalArgumentException e){
            System.out.println("Erro ao carregar status: "+e.getMessage());
        }
    }
	
	public static StatusBase getStatusPorId(int id){
		StatusData dados = statusDataExistentes.get(id);
        if (dados == null) return null;
		
        switch (id){
            case 0: return new StatusShield(dados);
			case 1: return new StatusVeneno(dados);
            case 2: return new StatusCombus(dados);
            case 3: return new StatusTont(dados);
            case 4: return new StatusAtord(dados);
			case 5: return new StatusCeg(dados);
			case 6: return new StatusPes(dados);
            case 7: return new StatusImunAtord(dados);
            case 8: return new StatusRegen(dados);
			case 9: return new StatusImunTont(dados);
			case 10: return new StatusDoubleLife(dados);
			case 11: return new StatusProvo(dados);
			case 12: return new StatusLent(dados);
			case 13: return new StatusRegenSta(dados);
			case 14: return new StatusFraquezaElemental(dados, Monsters.Elementos.FOGO);
			case 15: return new StatusFraquezaElemental(dados, Monsters.Elementos.NATUREZA);
			case 16: return new StatusFraquezaElemental(dados, Monsters.Elementos.MAGIA);
			case 17: return new StatusFraquezaElemental(dados, Monsters.Elementos.METAL);
			case 18: return new StatusFraquezaElemental(dados, Monsters.Elementos.VENTO);
			case 19: return new StatusFraquezaElemental(dados, Monsters.Elementos.LUZ);
			case 20: return new StatusFraquezaElemental(dados, Monsters.Elementos.TREVAS);
			case 21: return new StatusFraquezaElemental(dados, Monsters.Elementos.TERRA);
			case 22: return new StatusFraquezaElemental(dados, Monsters.Elementos.TROVAO);
			case 23: return new StatusFraquezaElemental(dados, Monsters.Elementos.AGUA);
			case 24: return new StatusFraquezaElemental(dados, Monsters.Elementos.FISICO);
			case 25: return new StatusRap(dados);
            default: return null;
        }
	}
	
    //===
}