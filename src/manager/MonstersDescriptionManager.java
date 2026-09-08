package manager;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MonstersDescriptionManager {
    private static Map<Integer, String> descricoesMonstros; 
	
    private MonstersDescriptionManager(){
    }

    public static void carregarDescrições(){
        try {
            Path caminho = Paths.get("data", "description", "monsters_pt.yaml");
            
            Yaml yaml = new Yaml();
            descricoesMonstros = new HashMap<>();
			
            try (InputStream in = Files.newInputStream(caminho)){
                List<Map<String, Object>> listaMonstros = yaml.load(in);

                if (listaMonstros != null){
                    for (Map<String, Object> monstroMap : listaMonstros){
                        int id = ((Number) monstroMap.get("id")).intValue();
                        String descricao = (String) monstroMap.get("descricao");
                        
                        descricoesMonstros.put(id, descricao);
                    }
                }
            }

            System.out.println(">>Descrições carregadas: " + descricoesMonstros.size());
        }catch (Exception e){
            System.out.println("Erro ao carregar descrições dos monstros: " + e.getMessage());
        }
    }
	
    public static String getDescrição(int idMonstro){
        return descricoesMonstros.get(idMonstro);
    }
	
	//===
}