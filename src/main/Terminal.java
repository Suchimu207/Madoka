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
		BATALHA_PREPARO("Batalha_Preparo"),
		BATALHA("Batalha"),
		INVENTARIO("Inventário"),
		MONSTRO_DETALHES("Monstro_Detalhes"),
		MONSTRO_HABILIDADES("Monstro_Habilidades"),
		LOJA("Loja"),
		LOJA_RECIBO("Loja_Recibo");
		
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
	private static int cursorY_Anterior;
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
		Title.setTITLE_NAME(TITLE);
		
		mapaAtual = mapaInicial;
		mostraEquipe = false;
		Shop.inicializarLoja();
		
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
			System.out.println("Cursor_X: "+cursorX);
			System.out.println("Cursor_Y: "+cursorY);
			System.out.println("EstadoAtual: "+estadoAtual);
		}
	}
	
	protected void desenhaEstado(){
		switch (estadoAtual){
			case TITULO:
				Title.desenhaTítulo();
				break;
			case MAPA:
				Maps.desenhaMapa(mapaAtual, jogadorX, jogadorY);
				if (mostraEquipe){
					Battle.desenhaInfoEquipe();
				}else desenhaInfo();
				break;
			case BATALHA_PREPARO:
				Battle.desenhaTelaPreparo();
				break;
			case BATALHA:
				Battle.desenhaBatalha();
				break;
			case INVENTARIO:
				Inventory.desenhaInventário();
				break;
			case MONSTRO_DETALHES:
				Inventory.desenhaMonstroDetalhes();
				break;
			case MONSTRO_HABILIDADES:
				Inventory.desenhaHabilidadeDetalhes();
				break;
			case LOJA:
				Shop.desenhaLoja();
				break;
			case LOJA_RECIBO:
				Shop.desenhaLojaRecibo();
				break;
		}
	}
	
	private void desenhaInfo(){		
		Grapchics.desenhaTela("ESC: Titulo",0,35, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("E: Inventario",0,36, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Shift: Mostrar equipe",0,37, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Interagir",0,38, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Ouro: "+Player.getOuro(),0,39, AsciiPanel.brightWhite);	
		
		Grapchics.atualizarTela();
	}
	
	private void teclaEsquerda(){
		switch (estadoAtual){
        case MAPA:
            if (!Maps.ehParede(mapaAtual, jogadorX - 1, jogadorY)){
                jogadorX--;
            }
            break;
		case BATALHA:
        case MONSTRO_DETALHES:
		case MONSTRO_HABILIDADES:
            cursorX--;
            break;
        case INVENTARIO:
            Battle.alternarPagina(false);
            break;
        case LOJA:
		case LOJA_RECIBO:
            Shop.alternarPagina(false);
            break;
		}
	}
	
	private void teclaDireita(){
		switch (estadoAtual){
        case MAPA:
            if (!Maps.ehParede(mapaAtual, jogadorX + 1, jogadorY)){
                jogadorX++;
            }
            break;
		case BATALHA:
        case MONSTRO_DETALHES:
		case MONSTRO_HABILIDADES:
            cursorX++;
            break;
        case INVENTARIO:
            Battle.alternarPagina(true);
            break;
        case LOJA:
		case LOJA_RECIBO:
            Shop.alternarPagina(true);
            break;
		}
	}
	
	private void teclaCima(){
		switch (estadoAtual){
        case TITULO:
            cursorY--;
            break;
        case INVENTARIO:
            cursorY--;
            break;
        case MAPA:
            if (!Maps.ehParede(mapaAtual, jogadorX, jogadorY - 1)){
                jogadorY--;
            }
            break;
		case BATALHA:
		case BATALHA_PREPARO:
        case LOJA:
            cursorY--;
            break;
		case MONSTRO_HABILIDADES:
			cursorY--;
			break;
		}
	}
	
	private void teclaBaixo(){
		switch (estadoAtual){
        case TITULO:
            cursorY++;
            break;
        case INVENTARIO:
            cursorY++;
            break;
        case MAPA:
            if (!Maps.ehParede(mapaAtual, jogadorX, jogadorY + 1)){
                jogadorY++;
            }
            break;
		case BATALHA:
		case BATALHA_PREPARO:
        case LOJA:
            cursorY++;
            break;
		case MONSTRO_HABILIDADES:
			cursorY++;
			break;
		}
	}
	
	private void teclaDebug(){
		if (ativaDebug){
			ativaDebug = false;
			limpaPrompt();
		}else ativaDebug = true;
	}
	
	private void teclaEnter(){
		switch (estadoAtual) {
        case TITULO:
            if (cursorY == 1 || cursorY == 2){
                Grapchics.limpaTela();
                estadoAtual = EstadosJogo.MAPA;
            }
            if (cursorY == 3) System.exit(0); // Provisório.
            break;
        case INVENTARIO:
            Inventory.alternarMonstroTabela(cursorY);
            break;
        case MONSTRO_DETALHES:
            Inventory.alternarMonstroFavorito(cursorY);
            break;
		case MONSTRO_HABILIDADES:
			Inventory.alternarHabilidadeAtiva();
			break;
        case MAPA:
            if (Maps.ehEvento(mapaAtual, jogadorX, jogadorY) == '$'){
                cursorY = 0;
                cursorX = 0;
                Shop.limparCarrinho();
                estadoAtual = EstadosJogo.LOJA;
            }
			if (Maps.ehEvento(mapaAtual, jogadorX, jogadorY) == '!'){
				
				estadoAtual = EstadosJogo.BATALHA_PREPARO;
            }
            break;
		case BATALHA_PREPARO:
			Battle.alternarMonstroSlotsAtivos();
			break;
		case BATALHA:
			Battle.selecionarComandoBatalha();
			break;
        case LOJA:
			Shop.alternarItemCarrinho();
            break;
		}
	}
	
	private void teclaShift(){
		switch (estadoAtual){
        case MAPA:
            Grapchics.limpaTela();
            mostraEquipe = !mostraEquipe;
            break;
        case INVENTARIO:
            Grapchics.limpaTela();
			cursorX = cursorY;
            estadoAtual = EstadosJogo.MONSTRO_DETALHES;
            break;
		case MONSTRO_DETALHES:
			Grapchics.limpaTela();
			cursorY_Anterior = cursorY;
			estadoAtual = EstadosJogo.MONSTRO_HABILIDADES;
			break;
		}
	}
	
	private void teclaInventário(){
		switch (estadoAtual){
        case MAPA:
        case LOJA_RECIBO:
            Grapchics.limpaTela();
            cursorX = 1;
            cursorY = 1;
            estadoAtual = EstadosJogo.INVENTARIO;
            break;
		case BATALHA_PREPARO:
			Battle.resetarSlotsAtivos();
        case INVENTARIO:
            Grapchics.limpaTela();
            cursorX = 1;
            cursorY = 1;
            estadoAtual = EstadosJogo.MAPA;
            break;
        case MONSTRO_DETALHES:
            Grapchics.limpaTela();
            estadoAtual = EstadosJogo.INVENTARIO;
            break;
		case MONSTRO_HABILIDADES:
			Grapchics.limpaTela();
			cursorY = cursorY_Anterior;
			estadoAtual = EstadosJogo.MONSTRO_DETALHES;
			break;
        case LOJA:
            Grapchics.limpaTela();
            estadoAtual = EstadosJogo.MAPA;
            break;
		}
	}
	
	private void teclaComprar(){
		switch (estadoAtual){
		case BATALHA_PREPARO:
			if (Battle.setarBatalha()){
				cursorX = 1;
				cursorY = 1;
				estadoAtual = EstadosJogo.BATALHA;
			}
			break;
		case BATALHA:
			Battle.voltarComandoBatalha();
			break;
        case LOJA:
            if (Shop.comprarMonstro()){
				Grapchics.limpaTela();
				cursorY = 0;
				estadoAtual = EstadosJogo.LOJA_RECIBO;
			}else if (estadoAtual == EstadosJogo.LOJA_RECIBO){
				Grapchics.limpaTela();
				Shop.limparCarrinho(); 
				cursorY = 0;
				estadoAtual = EstadosJogo.LOJA;
			}
            break;
        case LOJA_RECIBO:
            Grapchics.limpaTela();
            Shop.limparCarrinho();
			cursorY = 0;
            estadoAtual = EstadosJogo.LOJA;
            break;
		}
	}
	
	private void teclaEsc(){
		switch (estadoAtual){
        case MAPA:
            Grapchics.limpaTela();
            estadoAtual = EstadosJogo.TITULO;
            break;
        case LOJA_RECIBO:
            Grapchics.limpaTela();
            Shop.limparCarrinho();
            estadoAtual = EstadosJogo.MAPA;
            break;
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
			case KeyEvent.VK_Q:
				teclaComprar();
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