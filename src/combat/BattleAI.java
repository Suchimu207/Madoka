package combat;

import bestiary.Monsters;
import bestiary.Skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BattleAI {
	// ==================== ATRIBUTOS ====================
	
	private static final Random random = new Random();
	private static BattleUnit unidadeAlvo;
	
	private BattleAI(){
	}
	
	protected static void turnoInimigo(){
		BattleUnit unidadeAtual = BattleTurn.getUnidadeAtual();
        if (unidadeAtual == null) return;
        
        Monsters monstroInimigo = unidadeAtual.getMonstro();
		if (monstroInimigo == null) return;
		
		if (monstroInimigo.getEstaminaAtualCombate() <= 0){
			recarregarEnergiaUsuário(monstroInimigo);
			return;
		}
		
		Skills habilidade = getHabilidades(monstroInimigo);
		
		if (habilidade != null){
			List<Monsters> alvos = getAlvosHabilidade(monstroInimigo, habilidade);
			
			if (!alvos.isEmpty()){
				for (Monsters alvo : alvos){
					unidadeAlvo = BattleTurn.getUnidadePorMonstro(alvo);
					if (unidadeAlvo != null) unidadeAlvo.setAlvo(true);
				}
				
				int danoRealizado = BattleAction.executarHabilidade(monstroInimigo, alvos, habilidade);
				String nomeMonstro = monstroInimigo.getNomeMonstro()+" usou ";
				Skills ultimaSkill = habilidade;
				
				String dano = null;
				if (danoRealizado > 0) dano = ">>Causou "+danoRealizado+" de dano.";
				
				Battle.exibirMensagemInimigo(nomeMonstro, ultimaSkill, dano);
				alvos.clear();
				return;
			}
		}
		
		recarregarEnergiaUsuário(monstroInimigo);
    }
	
	private static void recarregarEnergiaUsuário(Monsters monstro){
		BattleAction.recarregarEnergia(monstro);
		String frase = monstro.getNomeMonstro()+ " recarrega.";
		Battle.exibirMensagemInimigo(frase,null,null);
	}
	
	// ==================== MÉTODOS AUXILIARES ====================
	
	private static Skills getHabilidades(Monsters monstro){
        List<Skills> habilidadesValidas = new ArrayList<>();
        
        for (int i = 0; i < monstro.getQuantidadeMaxSlotsHabilidade(); i++){
            Skills skill = monstro.getHabilidadeAtiva(i);
            
            if (skill != null && BattleAction.verificarCustoHabilidade(monstro, skill)){
                habilidadesValidas.add(skill);
            }
        }
        
        if (habilidadesValidas.isEmpty()) return null;
        
        return habilidadesValidas.get(random.nextInt(habilidadesValidas.size()));
    }
	
	private static List<Monsters> getAlvosHabilidade(Monsters usuario, Skills habilidade){
		List<Monsters> alvos = new ArrayList<>();
		
		List<Monsters> timeJogador = new ArrayList<>();
		List<Monsters> timeInimigo = new ArrayList<>(); 
		
		for (BattleUnit u : BattleTurn.getUnidades()){
			if (u.getMonstro() != null && u.getMonstro().getVidaAtualCombate() > 0){
				if (u.isAliado()){
					timeJogador.add(u.getMonstro());
				}else{
					timeInimigo.add(u.getMonstro());
				}
			}
		}
    
		Skills.TipoAlvo tipoAlvo = habilidade.getAlvoHabilidadeTipo();
    
		switch (tipoAlvo){
			case INIMIGO_UNICO:
				if (!timeJogador.isEmpty()) alvos.add(timeJogador.get(random.nextInt(timeJogador.size())));
				break;
			case ALIADO_UNICO:
				if (!timeInimigo.isEmpty()) alvos.add(timeInimigo.get(random.nextInt(timeInimigo.size())));
				break;
			case USUARIO:
				alvos.add(usuario);
				break;
			case INIMIGO_AREA:
				alvos.addAll(timeJogador);
				break;
			case ALIADO_AREA:
				alvos.addAll(timeInimigo);
				break;
			case CAMPO:
				alvos.addAll(timeJogador);
				alvos.addAll(timeInimigo);
				break;
		}
		return alvos;
	}
	
    //===
}