package main;

import asciiPanel.AsciiPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class Terminal implements KeyListener {
	private enum EstadosJogo{
		TITULO("Título"),
		MAPA("Mapa"),
		INVENTARIO("Inventário"),
		MONSTRO_DETALHES("Monstro_Detalhes"),
		LOJA("Loja");
		
		private String nome;
		
		EstadosJogo(String nome){
			this.nome = nome;
		}
		
		public String getEstadoNome(){
			return nome;
		}
	}

    private final JFrame frame;
	private EstadosJogo estadoAtual;
	
	private int jogadorX, jogadorY;
	protected static int cursorX, cursorY; // Provisório.
	private final String TITLE;
	private String os, mapaAtual, mapaInicial;
	private boolean ativaDebug, mostraEquipe;
	
	protected Terminal(String TITLE, String mapaInicial){
		this.TITLE = TITLE;
		this.mapaInicial = mapaInicial;
		os = System.getProperty("os.name").toLowerCase();
		frame = new JFrame(TITLE);
	}
	
	protected void setarJogo(){
		estadoAtual = EstadosJogo.TITULO;
		mapaAtual = mapaInicial;
		mostraEquipe = false;
		jogadorX = 19;
		jogadorY = 9;
		cursorY = 1; // A posição inicial é "Novo jogo".
	}
	
	protected void setarJanela(){
		frame.add(Grapchics.getTela());
        frame.setResizable(false);  
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setLocationRelativeTo(null);
		
		frame.addKeyListener(this);
        frame.setFocusable(true);     
        frame.toFront();
		frame.requestFocus();
		
		frame.setVisible(true);
	}
	
	private void limpaPrompt(){
        try {
            if (os.contains("win")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }else if (os.contains("linux") || os.contains("unix")){
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        }catch (Exception e){
			System.out.println("Erro ao limpar prompt: "+e.getMessage());
			System.exit(1);
        }
      //===
    }
	
	protected void mostrarDebug(int contadorFrames){
		if (ativaDebug){
			limpaPrompt();
			System.out.println("FPS Atual: " + contadorFrames);
			System.out.println("Jogador_X: "+jogadorX);
			System.out.println("Jogador_Y: "+jogadorY);
			System.out.println("EstadoAtual: "+estadoAtual);
		}
	}
	
	protected void desenhaEstado(){
		switch (estadoAtual){
			case TITULO:
				desenhaTítulo();
				break;
			case MAPA:
				Maps.desenhaMapa(mapaAtual, jogadorX, jogadorY);
				if (mostraEquipe){
					Battle.desenhaInfoEquipe();
				}else desenhaInfo();
				break;
			case INVENTARIO:
				Battle.desenhaInventário();
				break;
			case MONSTRO_DETALHES:
				Battle.desenhaMonstroDetalhes();
				break;
			case LOJA:
				Shop.desenhaLoja();
				break;
		}
	}
	
	private void desenhaTítulo(){
		Grapchics.desenhaCentro(TITLE, 14, AsciiPanel.brightWhite);
		
		if (cursorY >= 4){
			cursorY = 1;
		} else if (cursorY <= 0) cursorY = 3;
		
		if (cursorY == 1){
			Grapchics.desenhaCentro("Novo jogo", 18, AsciiPanel.brightYellow, 
			AsciiPanel.brightBlack);
		}else{
			Grapchics.desenhaCentro("Novo jogo", 18, AsciiPanel.brightWhite);
		}
		
		if (cursorY == 2){
			Grapchics.desenhaCentro("Continuar", 20, AsciiPanel.brightYellow, 
			AsciiPanel.brightBlack);
		}else{
			Grapchics.desenhaCentro("Continuar", 20, AsciiPanel.brightWhite);
		}
		
		if (cursorY == 3){
			Grapchics.desenhaCentro("Sair     ", 22, AsciiPanel.brightYellow,
			AsciiPanel.brightBlack);
		}else{
			Grapchics.desenhaCentro("Sair     ", 22, AsciiPanel.brightWhite);
		}
		
		Grapchics.desenhaTela("Desenvolvido por Carlos S. Rehem.",0,39, AsciiPanel.brightWhite);
		Grapchics.atualizarTela();
	}
	
	private void desenhaInfo(){		
		Grapchics.desenhaTela("ESC: Titulo",0,35, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("E: Inventario",0,36, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Shift: Mostrar equipe",0,37, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Interagir",0,38, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Ouro: "+Battle.getOuro(),0,39, AsciiPanel.brightWhite);	
		
		Grapchics.atualizarTela();
	}
	
	private void teclaEsquerda(){
		if (estadoAtual == EstadosJogo.MAPA){
			if (!Maps.ehParede(mapaAtual, jogadorX-1, jogadorY)){
				jogadorX--;
			}
		}
		if (estadoAtual == EstadosJogo.MONSTRO_DETALHES){
			cursorY--;
		}
		if (estadoAtual == EstadosJogo.INVENTARIO){
			Battle.alternarPagina(false);
		}
	}
	
	private void teclaDireita(){
		if (estadoAtual == EstadosJogo.MAPA){
			if (!Maps.ehParede(mapaAtual, jogadorX+1, jogadorY)){
				jogadorX++;
			}
		}
		if (estadoAtual == EstadosJogo.MONSTRO_DETALHES){
			cursorY++;
		}
		if (estadoAtual == EstadosJogo.INVENTARIO){
			Battle.alternarPagina(true);
		}
	}
	
	private void teclaCima(){
		if (estadoAtual == EstadosJogo.TITULO){
			cursorY--;
		}
		if (estadoAtual == EstadosJogo.INVENTARIO){
			cursorY--;
		}
		if (estadoAtual == EstadosJogo.MAPA){
			if (!Maps.ehParede(mapaAtual, jogadorX, jogadorY-1)){
				jogadorY--;
			}
		}
		if (estadoAtual == EstadosJogo.LOJA){
			cursorY--;
		}
	}
	
	private void teclaBaixo(){
		if (estadoAtual == EstadosJogo.TITULO){
			cursorY++;
		}
		if (estadoAtual == EstadosJogo.INVENTARIO){
			cursorY++;
		}		
		if (estadoAtual == EstadosJogo.MAPA){
			if (!Maps.ehParede(mapaAtual, jogadorX, jogadorY+1)){
				jogadorY++;
			}
		}
		if (estadoAtual == EstadosJogo.LOJA){
			cursorY++;
		}
	}
	
	private void teclaDebug(){
		if (ativaDebug){
			ativaDebug = false;
			limpaPrompt();
		}else ativaDebug = true;
	}
	
	private void teclaEnter(){
		if (estadoAtual == EstadosJogo.TITULO){
			if (cursorY == 1 || cursorY == 2){
				Grapchics.limpaTela();
				estadoAtual = EstadosJogo.MAPA;
			}
		if (cursorY == 3) System.exit(0); // Provisório.
		}else if (estadoAtual == EstadosJogo.INVENTARIO){
			Battle.alternarMonstroTabela(cursorY);
		}else if (estadoAtual == EstadosJogo.MONSTRO_DETALHES){
			Battle.alternarMonstroFavorito(cursorY);
		}else if (estadoAtual == EstadosJogo.MAPA){
			if(Maps.ehEvento(mapaAtual, jogadorX, jogadorY) == '$'){
				cursorY = 0;
				cursorX = 0;
				Shop.inicializarLoja();
				estadoAtual = EstadosJogo.LOJA;
			}
		}else if (estadoAtual == EstadosJogo.LOJA){
			Shop.comprarMonstro();
		}
	}
	
	private void teclaShift(){		
		if(estadoAtual == EstadosJogo.MAPA){
			Grapchics.limpaTela();
			if (mostraEquipe){
				mostraEquipe = false;
			}else mostraEquipe = true;
		}
		if (estadoAtual == EstadosJogo.INVENTARIO){
			Grapchics.limpaTela();
			estadoAtual = EstadosJogo.MONSTRO_DETALHES;
		}
	}
	
	private void teclaInventário(){
		if (estadoAtual == EstadosJogo.MAPA){
			Grapchics.limpaTela();
			cursorX = 1;
			cursorY = 1;
			estadoAtual = EstadosJogo.INVENTARIO;
		}else if (estadoAtual == EstadosJogo.INVENTARIO){
			Grapchics.limpaTela();
			cursorX = 1;
			cursorY = 1;
			estadoAtual = EstadosJogo.MAPA;
		}else if (estadoAtual == EstadosJogo.MONSTRO_DETALHES){
			Grapchics.limpaTela();
			estadoAtual = EstadosJogo.INVENTARIO;
		}else if (estadoAtual == EstadosJogo.LOJA){
			Grapchics.limpaTela();
			estadoAtual = EstadosJogo.MAPA;
		}
	}
	
	private void teclaEsc(){
		if (estadoAtual == EstadosJogo.MAPA){
			Grapchics.limpaTela();
			estadoAtual = EstadosJogo.TITULO;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		switch (e.getKeyCode()){
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				teclaEsquerda();
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				teclaDireita();
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				teclaCima();
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				teclaBaixo();
				break;
			case KeyEvent.VK_ENTER:
				teclaEnter();
				break;
			case KeyEvent.VK_SHIFT:
				teclaShift();
				break;
			case KeyEvent.VK_E:
				teclaInventário();
				break;	
			case KeyEvent.VK_F3:
			case KeyEvent.VK_ALT:
				teclaDebug();
				break;
			case KeyEvent.VK_ESCAPE:
				teclaEsc();
				break;
		}
	}
	
	@Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
	
	//===
}