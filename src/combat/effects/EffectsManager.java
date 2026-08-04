package combat.effects;

import combat.effects.strategies.*;

import java.util.HashMap;
import java.util.Map;

public final class EffectsManager {
    private static final Map<String, EffectsStrategy> efeitos = new HashMap<>();

    static{
		efeitos.put("HEALING", new EffectsHealing());
		efeitos.put("GAIN_STA", new EffectsGainSta());
		efeitos.put("APPLY_STATUS", new EffectsApplyStatus());
		efeitos.put("APPLY_SHIELD", new EffectsApplyShield());
		efeitos.put("REMOVE_STA", new EffectsRemoveSta());
		efeitos.put("REMOVE_NEGATIVE_STATUS", new EffectsRemoveNegativeStatus());
		efeitos.put("REMOVE_POSITIVE_STATUS", new EffectsRemovePositiveStatus());
		efeitos.put("ACTION_ADVANCE", new EffectsActionAdvance());
		/*
        efeitos.put("REMOVE_CONTINUOUS_DAMAGE", new EfeitoRemoverDanoContinuo());
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