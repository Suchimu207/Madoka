package combat;

import bestiary.Monsters;
import bestiary.Skills;

import combat.effects.Effects;
import combat.effects.EffectsManager;
import combat.effects.EffectsStrategy;

import combat.status.StatusBase;
import combat.status.strategies.StatusFraquezaElemental;

import java.util.List;
import java.util.ArrayList;

import java.util.Random;

public final class BattleAction {
	private static final int CONSTANTE = 10;
	private static final Random random = new Random();
	
	private BattleAction(){
	}
	
	protected static void recarregarEnergia(Monsters usuario){
		int maxEstamina = usuario.getEstaminaAtual();
		int regneração = (int) Math.ceil(maxEstamina * 0.5); 
		int novaEstamina = regneração + usuario.getEstaminaAtualCombate();
		
		usuario.ganharEstamina(novaEstamina);
	}
	
	protected static boolean verificarCustoHabilidade(Monsters usuario, Skills habilidade){
		int estaminaAtualCombate = usuario.getEstaminaAtualCombate();
		int energiaHabilidade = habilidade.getEnergiaHabilidade();
		
		if (estaminaAtualCombate >= energiaHabilidade){
			return true;
		}
		
		return false;
	}
	
	private static boolean verificarPrecisao(Monsters usuario, List<Monsters> alvos, Skills habilidade){
		if (habilidade.getTipoHabilidade() == Skills.TipoHabilidade.ESPECIAL || 
		habilidade.getTipoHabilidade() == Skills.TipoHabilidade.DEFENSIVA) return true;
		
		int precisaoBase = habilidade.getPrecisaoBase();
		int precisaoAtual = habilidade.getPrecisaoAtual();
		
		if (precisaoBase <= 0 || precisaoAtual <= 0) return false;
		
		int roll = random.nextInt(100) + 1;
        return roll <= precisaoAtual;
	}
	
    protected static BattleActionResult executarHabilidade(Monsters usuario, List<Monsters> alvos, Skills habilidade){
		int estaminaAtualCombate = usuario.getEstaminaAtualCombate();
		int energiaHabilidade = habilidade.getEnergiaHabilidade();
		int danoResultado = 0;
		
        usuario.setEstaminaAtualCombate(estaminaAtualCombate - energiaHabilidade);
        
		if (!verificarPrecisao(usuario, alvos, habilidade)){
			return new BattleActionResult(danoResultado, false);
		}			
		
        if (habilidade.getPoderHabilidade() > 0) danoResultado = calcularDano(usuario, alvos, habilidade);
		
		aplicarEfeitos(usuario, alvos, habilidade);
		
		carregarEspecialMonstros(usuario, alvos, habilidade);
		
		habilidade.ativarRecarga();
		return new BattleActionResult(danoResultado, true);
    }
	
	private static void carregarEspecialMonstros(Monsters usuario, List<Monsters> alvos, Skills habilidade){
		Skills.TipoAlvo alvo = habilidade.getAlvoHabilidadeValor();
		
		switch(alvo){
			case Skills.TipoAlvo.USUARIO:
				usuario.carregarEspecial(2);
			break;
			default:
				usuario.carregarEspecial(5);
				for (Monsters monstro : alvos){
					monstro.carregarEspecial(2);
				}
			break;
		}
		
		if (habilidade.isTipoEspecial(habilidade.getTipoHabilidade())){
			usuario.zerarEspecial();
		}
	}
	
	private static int calcularDano(Monsters usuario, List<Monsters> alvos, Skills habilidade){
		int forçaMonstro = usuario.getForcaAtualCombate();
		int poderHabilidade = habilidade.getPoderHabilidade();
		
		Monsters.Elementos elementoAtaque = habilidade.getElementoHabilidadeTipo();
		
        double danoBase = Math.ceil((forçaMonstro / 1000.0) * (poderHabilidade) * CONSTANTE);
		int danoSomado = 0;
		
		for (Monsters monstro : alvos){
			double multiplicadorElemental = 1.0;
			
			for (Monsters.Elementos elementoDefensor : monstro.getElementosAtuaisValores()){
				multiplicadorElemental *= elementoAtaque.getMultiplicadorDano(elementoDefensor);
			}
			
			for (StatusBase status : monstro.getStatusAtuais()){
				if (status instanceof StatusFraquezaElemental fraq && fraq.isAtivo()){
					if (fraq.getElementoFraqueza() == elementoAtaque){
						multiplicadorElemental += 0.5;
					}
				}
			}
			
			int danoFinal = (int) (danoBase * multiplicadorElemental);
			danoSomado += danoFinal;
			monstro.perderVida(danoFinal);
			
			if (habilidade.getLifeSteal() > 0){
				int cura = (int) Math.ceil(danoFinal * (habilidade.getLifeSteal() / 100));
				usuario.ganharVida(cura);
			}
		}
		return danoSomado;
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
	
	// ==================== MÉTODOS AUXILIARES ====================
	
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
		}else if (alvoEfeito == Effects.ALIADO_UNICO || alvoEfeito == Effects.INIMIGO_UNICO){
			return alvosHabilidade.isEmpty() ? alvos : alvosHabilidade;
		}else if (alvoEfeito == Effects.ALIADO_AREA){
			alvos.addAll(aliadosVivos);
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