package combat;

import bestiary.Monsters;
import bestiary.Skills;

import java.util.*;

public final class BattleTurn {
	private static List<BattleUnit> unidades = new ArrayList<>();
	private static BattleUnit unidadeAtual, unidadeJogadorAtual;
	private static int avAtual;
	private static boolean turnoJogador, aguardandoTurno;
	
	private BattleTurn(){
	}
	
	// avançarTurno() → Jogador/IA age → finalizarTurno() → turnoRealizado() → [Começo]
	
	protected static void ordenarActionValue(List<BattleUnit> unidades){
        Collections.sort(unidades, (u1, u2) ->{
            int compare = Integer.compare(u1.getActionValue(), u2.getActionValue());
            if (compare == 0){
                // Desempate: aliados primeiro, depois inimigos.
                if (u1.isAliado() != u2.isAliado()){
                    return u1.isAliado() ? -1 : 1;
                }
                return Integer.compare(u1.getId(), u2.getId());
            }
            return compare;
        });
    }
	
    protected static void avançarTurno(){
        if (unidades.isEmpty()) return;
        
        ordenarActionValue(unidades);
        
        unidadeAtual = unidades.get(0);
        avAtual = unidadeAtual.getActionValue();
		
		Monsters monstro = unidadeAtual.getMonstro();
		monstro.checarStatus();
		
        turnoJogador = unidadeAtual.isAliado();
        
		if (turnoJogador){
			unidadeJogadorAtual = unidadeAtual;
            aguardandoTurno = true;
        }else{
			aguardandoTurno = false;
		}
    }
	
	protected static void finalizarTurno(){
		Monsters monstro = unidadeAtual.getMonstro();
		
		monstro.reduzirDuraçãoStatus();
		
		unidades.removeIf(u -> u.getMonstro().getVidaAtualCombate() <= 0);
		
		controlarRecargaHabilidade();
		
		for (BattleUnit u : unidades){
			u.setAlvo(false);
		}
		
		turnoRealizado();
        avançarTurno();
    }
	
	private static void turnoRealizado(){
		for (BattleUnit user : unidades){
			if (user != unidadeAtual){
				user.setActionValueAtual(user.getActionValue() - avAtual);
			}
		}
		unidadeAtual.setActionValueAtual(unidadeAtual.calcularActionValue());
	}
	
	private static void controlarRecargaHabilidade(){
		if (unidadeAtual != null){
			Monsters monstro = unidadeAtual.getMonstro();
			monstro.reduzirRecargaHabilidades();
		}
	}
	
	protected static List<BattleUnit> getUnidades(){
		return unidades;
	}	
	
	protected static BattleUnit getUnidadeAtual(){
		return unidadeAtual;
	}
	
	protected static BattleUnit getUnidadeJogadorAtual(){
		return unidadeJogadorAtual;
	}
	
	protected static BattleUnit getUnidadePorMonstro(Monsters monstro){
		for (BattleUnit unidade : unidades){
			if (unidade.getMonstro() == monstro){
				return unidade;
			}
		}
		return null;
	}
	
	protected static boolean isTurnoJogador(){
		return turnoJogador;
	}
	
	protected static boolean isAguardandoTurno(){
		return aguardandoTurno;
	}
	
	protected static void setAguardandoTurno(Boolean aguardandoTurno){
		BattleTurn.aguardandoTurno = aguardandoTurno;
	}
	
	//===
}