package bestiary.traits;

import bestiary.traits.strategies.*;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

import java.io.FileReader;
import java.io.IOException;

import java.nio.file.*;   

import org.json.JSONArray;
import org.json.JSONObject;

public final class TraitsManager {
	private static String conteudoJson;
	private static Path caminho;
	
	private static Map<Integer, Traits> traçosExistentes;
    private static Traits traçoCarregado;
    private static JSONObject traços;
    private static JSONArray traçosArray;
	
	private TraitsManager(){
	}
	
	public final static void carregarTraços(){
        try {
            caminho = Paths.get("data", "system", "traits.json");
            conteudoJson = Files.readString(caminho);
            
            traçosExistentes = new HashMap<Integer, Traits>();
            traçosArray = new JSONArray(conteudoJson);
            
            for (int i = 0; i < traçosArray.length(); i++){
                traços = traçosArray.getJSONObject(i);
                
                int id = traços.getInt("id");
                String nome = traços.getString("nome");
                String descricao = traços.getString("descricao");
                
				List<TraitEffect> efeitosTraço = new ArrayList<TraitEffect>();
				
                JSONArray efeitosArray = traços.optJSONArray("efeitos");
				if (efeitosArray != null){
					for (int j = 0; j < efeitosArray.length(); j++){
						JSONObject efeitoObj = efeitosArray.getJSONObject(j);
						Traits.EfeitosTraço tipoEfeito = Traits.EfeitosTraço.valueOf(efeitoObj.getString("efeito"));
						int valor = efeitoObj.optInt("valor", 0);
						int atributo = efeitoObj.optInt("atributo", -1);
						
						TraitEffect efeito = criarEfeitoTraço(tipoEfeito, valor, atributo, nome);
						if (efeito != null) efeitosTraço.add(efeito);
					}
				}
				
				traçoCarregado = new Traits(id, nome, descricao, efeitosTraço);
                traçosExistentes.put(traçoCarregado.getIdTraço(), traçoCarregado);
            }
            
            System.out.println(">>Traços carregados: "+traçosExistentes.size());
        }catch (IOException e){
            System.out.println("Erro ao carregar traços: "+e.getMessage());
        }
    }
	
	private static TraitEffect criarEfeitoTraço(Traits.EfeitosTraço tipo, int valor, int atributo, String nome){
        switch (tipo){
            case Traits.EfeitosTraço.IMMUNITY:
                return new TraitImmunity(valor);
            case Traits.EfeitosTraço.ATTUNNED:
                return new TraitAttuned(valor);
            case Traits.EfeitosTraço.BONUS_ATTRIBUTE:
                return new TraitBonusAttribute(valor, atributo);
            case Traits.EfeitosTraço.STATUS_ARMOR:
                return new TraitStatusArmor(valor);
            default:
				return null;
        }
    }
	
	public static Traits getTraço(int id){
        return traçosExistentes.get(id);
    }
   
    public static Map<Integer, Traits> getTraçosExistentes(){
        return traçosExistentes;
    }
	
	//===
}