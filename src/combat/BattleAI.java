package combat;

import bestiary.Monsters;
import bestiary.Skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BattleAI {
	private static final Random random = new Random();
	
	private BattleAI(){
	}
	
	protected static void turnoInimigo(){
		BattleUnit unidadeAtual = BattleTurn.getUnidadeAtual();
        if (unidadeAtual == null) return;
        
        Monsters monstroInimigo = unidadeAtual.getMonstro();
		if (monstroInimigo.getEstaminaAtualCombate() <= 0){
			String frase = "Sem energia...";
			Battle.exibirMensagemInimigo(frase);
			return;
		}
		
		List<Monsters> possiveisAlvos = getAlvos();
		if (possiveisAlvos.isEmpty()){
            BattleTurn.finalizarTurno();
            return;
        }
		
		Monsters alvo = possiveisAlvos.get(random.nextInt(possiveisAlvos.size()));
		
        Skills habilidade = getHabilidades(monstroInimigo, alvo);
        
        if (alvo != null && habilidade != null){
			BattleAction.executarHabilidade(monstroInimigo, alvo, habilidade);
			String frase = monstroInimigo.getNomeMonstro()+" usou "+habilidade.getNomeHabilidade() + "!";
			Battle.exibirMensagemInimigo(frase);
        }
    }
	
	private static List<Monsters> getAlvos(){
        List<Monsters> aliadosVivos = new ArrayList<>();
        
        for (BattleUnit u : BattleTurn.getUnidades()){
            if (u.isAliado() && u.getMonstro() != null && u.getMonstro().getVidaAtualCombate() > 0){
                aliadosVivos.add(u.getMonstro());
            }
        }
        return aliadosVivos;
    }
	
	private static Skills getHabilidades(Monsters monstro, Monsters alvo){
        List<Skills> habilidadesValidas = new ArrayList<>();
        
        for (int i = 0; i < monstro.getQuantidadeMaxSlotsHabilidade(); i++){
            Skills skill = monstro.getHabilidadeAtiva(i);
            
            if (skill != null && BattleAction.verificarCustoHabilidade(monstro, alvo, skill)){
                habilidadesValidas.add(skill);
            }
        }
        
        if (habilidadesValidas.isEmpty()) return null;
        
        return habilidadesValidas.get(random.nextInt(habilidadesValidas.size()));
    }
	
    //===
}