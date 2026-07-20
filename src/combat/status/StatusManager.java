package combat.status;

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
	private static Map<Integer, StatusBase> statusExistentes;
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
			
			inicializarStatus();
            System.out.println(">>Status carregados: " + statusExistentes.size());

        } catch (IOException | IllegalArgumentException e){
            System.out.println("Erro ao carregar status: "+e.getMessage());
        }
    }
	
	private static void inicializarStatus(){
		statusExistentes = new HashMap<>();
		StatusData dadosRegeneração = statusDataExistentes.get(8);
		StatusBase statusRegeneração = new StatusRegen(dadosRegeneração);
		
		statusExistentes.put(dadosRegeneração.getId(), statusRegeneração);	
	}
	
	public static StatusBase getStatusPorId(int id){
		return statusExistentes.get(id);
	}
	
    //===
}