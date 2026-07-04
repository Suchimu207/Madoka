package combat.effects;

import bestiary.Monsters;
import bestiary.Skills;

public interface EffectsStrategy {
    void aplicar(Monsters usuario, Monsters alvo, Skills habilidade, Effects efeito);
    String getNome();
}