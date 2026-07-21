package combat;

import main.Inventory;
import bestiary.Monsters;
import bestiary.Skills;
import bestiary.Troop;

import util.Grapchics;
import util.Input;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public final class BattlePreparation {
	private Skills skillMostrada;
	private Monsters[] monstroSlotsAtivos;
	
	private int linhaAtual, linhaInicial, linhaMax,
	posiçãoLinhaX, posiçãoLinhaY, posiçãoLinhaBatalha;
	
	protected BattlePreparation(){
	}
	
	public void desenhaTelaPreparo(){
		Grapchics.limpaTela();
		
		Battle.setMonstroMostrado(null);
		
		Troop tropaCarregada = Battle.getTropaCarregada();
		Monsters[] monstroSlotsAtivos = Battle.getMonstroSlotsAtivos();
		
		if (tropaCarregada == null || monstroSlotsAtivos == null) return;
		
		int maxSlotsAtivos = monstroSlotsAtivos.length;
		int linhaAtual = 1;
		
		Grapchics.desenhaCentro("Batalha",0, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("E: Voltar", 0,linhaAtual++, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("Q: Iniciar batalha",0,linhaAtual++, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTela("Enter: Escolher integrante",0,linhaAtual++, Grapchics.PRETO_CLARO);		
		Grapchics.desenhaTela("Oponente: ",0,linhaAtual++, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		
		for (Monsters monstro : tropaCarregada.getMonstros()){
			Grapchics.desenhaTela(monstro.getNomeMonstro()+" Nv"+
			monstro.getNivelAtual()+" ("+monstro.getElementosAtuais()+")"
			,0,linhaAtual++, Grapchics.BRANCO_CLARO);
		}
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		linhaAtual += 1;
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		linhaInicial = linhaAtual;
		for (int i = 0; i <= maxSlotsAtivos-1; i++){
			Monsters monstro = monstroSlotsAtivos[i];
			if(monstro == null){
				if (Input.getCursorY() == linhaAtual){
					Grapchics.desenhaTela("["+(i+1)+"]", 0, linhaAtual++, Grapchics.AMARELO_CLARO);
					Battle.setMonstroMostrado(null);
				}else{
					Grapchics.desenhaTela("["+(i+1)+"]", 0, linhaAtual++, Grapchics.PRETO_CLARO);
				}
			}else{
				if (Input.getCursorY() == linhaAtual){
					Grapchics.desenhaTela(monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual(), 0, linhaAtual++, 
					Grapchics.AMARELO_CLARO);
					Battle.setMonstroMostrado(monstro);
				}else{
					Grapchics.desenhaTela(monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual(), 0, linhaAtual++, 
					Grapchics.BRANCO_CLARO);
				}
			}
		}
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		linhaAtual += 1;
		
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, Grapchics.PRETO_CLARO);
		List<Monsters> equipe = Inventory.getEquipeLista();
		for (Monsters monstroEquipe : equipe){
			String nomeMonstroExibido = "";
			
			if (monstroEquipe != null){
				if (Input.getCursorY() == linhaAtual){
					Battle.setMonstroMostrado(null);
					nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
					Grapchics.desenhaTela(nomeMonstroExibido, 0, linhaAtual++, Grapchics.AMARELO_CLARO);
					Battle.setMonstroMostrado(monstroEquipe);
				}else{
					nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
					Grapchics.desenhaTela(nomeMonstroExibido, 0, linhaAtual++, Grapchics.BRANCO_CLARO);
				}
			}else{
				if (Input.getCursorY() == linhaAtual){
					Grapchics.desenhaTela("[Vazio]", 0, linhaAtual++, Grapchics.AMARELO_CLARO);
					Battle.setMonstroMostrado(null);
				}else{
					Grapchics.desenhaTela("[Vazio]", 0, linhaAtual++, Grapchics.PRETO_CLARO);
				}
			}
		}
		linhaMax = linhaAtual;
		Grapchics.desenhaTela("____________________",0,linhaAtual, Grapchics.PRETO_CLARO);
		
		if (Input.getCursorY() > linhaMax-1){
			Input.setCursorY(linhaInicial);
		}else if (Input.getCursorY() < linhaInicial){
			Input.setCursorY(linhaMax-1);
		}
		
		Grapchics.atualizarTela();
	}
	
	//===
}