package combat.effects;

import combat.effects.strategies.*;

import java.util.HashMap;
import java.util.Map;

public final class EffectsManager {
    private static final Map<String, EffectsStrategy> efeitos = new HashMap<>();

    static{
		efeitos.put("HEALING", new EffectsHealing());
		efeitos.put("APPLY_STATUS", new EffectsApplyStatus());
		/*
        efeitos.put("REMOVE_NEGATIVE_STATUS", new EfeitoRemoverNegativos());
		efeitos.put("REMOVE_POSITIVE_STATUS", new EfeitoRemoverNegativos());
        efeitos.put("REMOVE_CONTINUOUS_DAMAGE", new EfeitoRemoverDanoContinuo());
		efeitos.put("REMOVE_CONTROL_STATUS", new EfeitoRemoverDanoContinuo());
		efeitos.put("REMOVE_STATUS", new EfeitoAplicarStatus());
		*/
    }
	
    private EffectsManager() {}

    public static EffectsStrategy getEfeito(String nome){
        return efeitos.get(nome);
    }

    public static boolean existeEfeito(String nome){
        return efeitos.containsKey(nome);
    }
	
	//===
}