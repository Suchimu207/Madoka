package combat;

import bestiary.Monsters;
import bestiary.Skills;

import combat.effects.Effects;
import combat.effects.EffectsManager;
import combat.effects.EffectsStrategy;

import java.util.List;
import java.util.ArrayList;

public final class BattleAction {
	private static final int CONSTANTE = 10;
	
	private BattleAction(){
	}
	
	protected static void recarregarEnergia(Monsters usuario){
		int maxEstamina = usuario.getEstaminaAtual();
		int regneração = (int) Math.ceil(maxEstamina * 0.5); 
		int novaEstamina = regneração + usuario.getEstaminaAtualCombate();
		
		if (novaEstamina >= maxEstamina){
			novaEstamina = maxEstamina;
		}
		
		usuario.setEstaminaAtualCombate(novaEstamina);
	}
	
	protected static boolean verificarCustoHabilidade(Monsters usuario, Skills habilidade){
		int estaminaAtualCombate = usuario.getEstaminaAtualCombate();
		int energiaHabilidade = habilidade.getEnergiaHabilidade();
		
		if (estaminaAtualCombate >= energiaHabilidade){
			return true;
		}
		
		return false;
	}
	
    protected static void executarHabilidade(Monsters usuario, List<Monsters> alvos, Skills habilidade){
		int estaminaAtualCombate = usuario.getEstaminaAtualCombate();
		int energiaHabilidade = habilidade.getEnergiaHabilidade();
		
        usuario.setEstaminaAtualCombate(estaminaAtualCombate - energiaHabilidade);
        
		aplicarEfeitos(usuario, alvos, habilidade);
		
        if (habilidade.getPoderHabilidade() > 0) calcularDano(usuario, alvos, habilidade);
		
		usuario.carregarEspecial(5);
		for (Monsters monstro : alvos){
			monstro.carregarEspecial(2);
		}
		
		habilidade.ativarRecarga();
    }
	
	private static void calcularDano(Monsters usuario, List<Monsters> alvos, Skills habilidade){
		int forçaMonstro = usuario.getForcaAtualCombate();
		int poderHabilidade = habilidade.getPoderHabilidade();
		
        double danoBase = Math.ceil((forçaMonstro / 1000.0) * (poderHabilidade) * CONSTANTE);
		
		for (Monsters monstro : alvos){
			int danoFinal = (int) (danoBase);
			monstro.perderVida(danoFinal);
		}
    }
	
	private static void aplicarEfeitos(Monsters usuario, List<Monsters> alvosHabilidade, Skills habilidade){
		for (Effects dados : habilidade.getEfeitos()){
			EffectsStrategy efeito = EffectsManager.getEfeito(dados.getTipo());
			if (efeito != null){
				List<Monsters> alvosDoEfeito = determinarAlvosEfeito(usuario, alvosHabilidade, dados.getAlvo());
				
				for (Monsters monstro : alvosDoEfeito){
					if (monstro != null && monstro.getVidaAtualCombate() > 0){
						efeito.aplicar(usuario, monstro, habilidade, dados);
					}
				}
			}
		}
	}
	
	private static List<Monsters> determinarAlvosEfeito(Monsters usuario, List<Monsters> alvosHabilidade, int alvoEfeito){
		List<Monsters> alvos = new ArrayList<>();
		
		List<Monsters> aliadosVivos = new ArrayList<>();
		List<Monsters> inimigosVivos = new ArrayList<>();
    
		for (BattleUnit unidade : BattleTurn.getUnidades()){
			Monsters monstro = unidade.getMonstro();
			if (monstro != null && monstro.getVidaAtualCombate() > 0){
				if (unidade.isAliado()){
					aliadosVivos.add(monstro);
				}else{
					inimigosVivos.add(monstro);
				}
			}
		}
    
		if (alvoEfeito == Effects.MESMO_ALVO){
			return alvosHabilidade;
		}else if (alvoEfeito == Effects.ALIADO_UNICO){
			if (!aliadosVivos.isEmpty()){
			alvos.add(aliadosVivos.get(0));
			}
		}else if (alvoEfeito == Effects.ALIADO_AREA){
			alvos.addAll(aliadosVivos);
		}else if (alvoEfeito == Effects.INIMIGO_UNICO){
			if (!inimigosVivos.isEmpty()){
                alvos.add(inimigosVivos.get(0));
            }
		}else if (alvoEfeito == Effects.INIMIGO_AREA){
			alvos.addAll(inimigosVivos);
		}else if (alvoEfeito == Effects.USUARIO){
			alvos.add(usuario);
		}else{
			alvos.addAll(alvosHabilidade);
		}		
		return alvos;
	}

	//===
}