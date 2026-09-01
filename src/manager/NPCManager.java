package manager;

import world.NPC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NPCManager {
    private static String conteudoJson;
    private static Path caminho;
    
    private static Map<String, List<NPC>> npcsPorMapa;
    
    private NPCManager(){
    }
    
    protected final static void carregarNPCs(){
        try{
            caminho = Paths.get("data", "system", "npcs.json");
            conteudoJson = Files.readString(caminho);
            
            npcsPorMapa = new HashMap<String, List<NPC>>();
            JSONArray mapasArray = new JSONArray(conteudoJson);
            
            for (int i = 0; i < mapasArray.length(); i++){
                JSONObject mapaObj = mapasArray.getJSONObject(i);
                String nomeMapa = mapaObj.getString("nomeMapa");
                
                List<NPC> npcsDoMapa = new ArrayList<>();
                JSONArray npcsArray = mapaObj.getJSONArray("npcs");
                
                for (int j = 0; j < npcsArray.length(); j++){
                    JSONObject npcObj = npcsArray.getJSONObject(j);
                    
                    NPC npc = new NPC(
                        npcObj.getString("nome"),
                        npcObj.getInt("npcX"),
                        npcObj.getInt("npcY"),
                        npcObj.optBoolean("batalha", false),
                        npcObj.optInt("idBatalha", -1)
                    );
                    
                    JSONArray dialogoArray = npcObj.getJSONArray("dialogo");
                    String[] dialogo = new String[dialogoArray.length()];
                    for (int k = 0; k < dialogoArray.length(); k++){
                        dialogo[k] = dialogoArray.getString(k);
                    }
                    npc.setDialogo(dialogo);
                    
                    npcsDoMapa.add(npc);
                }
                
                npcsPorMapa.put(nomeMapa, npcsDoMapa);
            }
            
			System.out.println("");
            System.out.println(">>NPCs carregados para "+ npcsPorMapa.size()+" mapa(s).");
            for (Map.Entry<String, List<NPC>> entry : npcsPorMapa.entrySet()){
                System.out.println(entry.getKey() + ": " + entry.getValue().size() + " NPC(s)");
            }
            
        }catch (IOException | IllegalArgumentException e){ 
            System.out.println("Erro ao carregar NPCs: "+e.getMessage());
        }
    }
    
    public static List<NPC> getNPCs(String nomeMapa){
        if (npcsPorMapa == null) return new ArrayList<>();
        return npcsPorMapa.getOrDefault(nomeMapa, new ArrayList<>());
    }
    
    public static NPC getNPC(String nomeMapa, int x, int y){
        List<NPC> npcs = getNPCs(nomeMapa);
        for (NPC npc : npcs){
            if (npc.getNpcX() == x && npc.getNpcY() == y){
                return npc;
            }
        }
        return null;
    }
    
    //===
}