package combat;

import asciiPanel.AsciiPanel;

import main.Inventory;
import bestiary.Monsters;
import bestiary.Skills;
import bestiary.Troop;

import util.Grapchics;

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
		
		Grapchics.desenhaCentro("Batalha",0, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("E: Voltar", 0,linhaAtual++, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Q: Iniciar batalha",0,linhaAtual++, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Escolher integrante",0,linhaAtual++, AsciiPanel.brightBlack);	
		Grapchics.desenhaTela("Oponente: ",0,linhaAtual++, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		
		for (Monsters monstro : tropaCarregada.getMonstros()){
			Grapchics.desenhaTela(monstro.getNomeMonstro()+" Nv"+
			monstro.getNivelAtual()+" ("+monstro.getElementosAtuais()+")"
			,0,linhaAtual++, AsciiPanel.brightWhite);
		}
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		linhaAtual += 1;
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		linhaInicial = linhaAtual;
		for (int i = 0; i <= maxSlotsAtivos-1; i++){
			Monsters monstro = monstroSlotsAtivos[i];
			if(monstro == null){
				if (Battle.getCursorY() == linhaAtual){
					Grapchics.desenhaTela("["+(i+1)+"]", 0, linhaAtual++, AsciiPanel.brightYellow, 
					AsciiPanel.brightBlack);
					Battle.setMonstroMostrado(null);
				}else{
					Grapchics.desenhaTela("["+(i+1)+"]", 0, linhaAtual++, AsciiPanel.brightBlack);
				}
			}else{
				if (Battle.getCursorY() == linhaAtual){
					Grapchics.desenhaTela(monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual(), 0, linhaAtual++, 
					AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					Battle.setMonstroMostrado(monstro);
				}else{
					Grapchics.desenhaTela(monstro.getNomeMonstro()+" Nv"+monstro.getNivelAtual(), 0, linhaAtual++, 
					AsciiPanel.brightWhite);
				}
			}
		}
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		linhaAtual += 1;
		
		
		Grapchics.desenhaTela("____________________",0,linhaAtual++, AsciiPanel.brightWhite);
		List<Monsters> equipe = Inventory.getEquipeLista();
		for (Monsters monstroEquipe : equipe){
			String nomeMonstroExibido = "";
			
			if (monstroEquipe != null){
				if (Battle.getCursorY() == linhaAtual){
					Battle.setMonstroMostrado(null);
					nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
					Grapchics.desenhaTela(nomeMonstroExibido, 0, linhaAtual++, AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					Battle.setMonstroMostrado(monstroEquipe);
				}else{
					nomeMonstroExibido = monstroEquipe.getNomeMonstro()+" Nv"+monstroEquipe.getNivelAtual();
					Grapchics.desenhaTela(nomeMonstroExibido, 0, linhaAtual++, AsciiPanel.brightWhite);
				}
			}else{
				if (Battle.getCursorY() == linhaAtual){
					Grapchics.desenhaTela("[Vazio]", 0, linhaAtual++, AsciiPanel.brightYellow, AsciiPanel.brightBlack);
					Battle.setMonstroMostrado(null);
				}else{
					Grapchics.desenhaTela("[Vazio]", 0, linhaAtual++, AsciiPanel.brightBlack);
				}
			}
		}
		linhaMax = linhaAtual;
		Grapchics.desenhaTela("____________________",0,linhaAtual, AsciiPanel.brightWhite);
		
		if (Battle.getCursorY() > linhaMax-1){
			Battle.setCursorY(linhaInicial);
		}else if (Battle.getCursorY() < linhaInicial){
			Battle.setCursorY(linhaMax-1);
		}
		
		Grapchics.atualizarTela();
	}
	
	//===
}