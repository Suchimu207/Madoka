package main;

import combat.Battle;

import util.Debug;
import util.Grapchics;
import util.Input;
import util.Utils;

import asciiPanel.AsciiTTFFont;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.HashSet;
import java.util.Set;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;

public final class Terminal implements KeyListener {
	private enum EstadosJogo{
		TITULO("Título"),
		MAPA("Mapa"),
		BATALHA("Batalha"),
		INVENTARIO("Inventário"),
		MONSTRO_DETALHES("Monstro_Detalhes"),
		MONSTRO_HABILIDADES("Monstro_Habilidades"),
		LOJA("Loja"),
		LOJA_RECIBO("Loja_Recibo");
		
		private final String nome;
		
		EstadosJogo(String nome){
			this.nome = nome;
		}
		
		public String getEstadoNome(){
			return nome;
		}
	}

    private final JFrame frame;
	private EstadosJogo estadoAtual;
	private final String TITLE;
	private String mapaAtual, mapaInicial;
	private boolean mostraEquipe;
	
	private Set<Integer> teclasPressionadas = new HashSet<>();
	
	protected Terminal(String TITLE, String mapaInicial){
		this.TITLE = TITLE;
		this.mapaInicial = mapaInicial;
		frame = new JFrame(TITLE);
	}
	
	// ==================== INICIALIZAÇÃO ====================
	
	protected void setarFonte(){
		Font fontePadrao;
		Font fonteItalico;
		
		try{
			Path caminho = Paths.get("data", "font", "mac's Minecraft.ttf");
			
			AsciiTTFFont ttf = AsciiTTFFont.loadFromFile(caminho.toString(), 0, 0);
			
			fontePadrao = ttf.getFont();
			fontePadrao = new Font("mac's Minecraft", Font.BOLD, 14);
			
			fonteItalico = fontePadrao.deriveFont(Font.ITALIC, 14);
			System.out.println(">>Fonte carregada com sucesso.");
			System.out.println("");
		}catch (Exception e){
			System.err.println(">>Erro ao carregar fonte TTF: " + e.getMessage());
			System.out.println("");
			System.err.println(">>Usando fonte fallback (Monospaced).");
			
			fontePadrao = new Font("Monospaced", Font.PLAIN, 14);
			fonteItalico = new Font("Monospaced", Font.ITALIC, 14);
		}
		
		Grapchics.setFontePadrao(fontePadrao);
		Grapchics.setFonteItalico(fonteItalico);
	}
	
	protected void setarJogo(){
		estadoAtual = EstadosJogo.TITULO;
		Title.setTITLE_NAME(TITLE);
		
		mapaAtual = mapaInicial;
		mostraEquipe = false;
		Shop.inicializarLoja();
		
		Input.setCursorY(1); // A posição inicial é "Novo jogo".
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
	
	// ==================== DESENHO DO ESTADO ====================
	
	protected void desenhaEstado(){
		if (estadoAtual == EstadosJogo.BATALHA){
			Battle.desenhaEstadoBatalha();
			return;
		}
		
		switch (estadoAtual){
			case TITULO:
				Title.desenhaTítulo();
				break;
			case MAPA:
				Maps.desenhaMapa(mapaAtual, Player.getJogadorX(), Player.getJogadorY());
				if (mostraEquipe){
					Inventory.desenhaInfoEquipe();
				}else desenhaInfo();
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
		Grapchics.desenhaTTF("ESC: Título",0,35, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("E: Inventário",0,36, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Shift: Mostrar equipe",0,37, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Enter: Interagir",0,38, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Ouro: "+Player.getOuro(),0,39, Grapchics.BRANCO_CLARO);	
		
		Grapchics.atualizarTela();
	}
	
	// ==================== TECLAS ====================
	
	@Override
	public void keyPressed(KeyEvent e){
		int tecla = e.getKeyCode();
		teclasPressionadas.add(tecla);	
		if (estadoAtual == EstadosJogo.BATALHA){
			if(Battle.recebeComandosBatalha(tecla, teclasPressionadas)){
				Grapchics.limpaTela();
				estadoAtual = EstadosJogo.MAPA;
			}
			return;
		}
		
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
    public void keyReleased(KeyEvent e){
		int tecla = e.getKeyCode();
        teclasPressionadas.remove(tecla);
	}
    
    @Override
    public void keyTyped(KeyEvent e) {}
	
	private void teclaEsquerda(){
		switch (estadoAtual){
        case MAPA:
            if (!Maps.ehParede(mapaAtual, Player.getJogadorX() - 1, Player.getJogadorY())){
				Player.setJogadorX(Player.getJogadorX()-1);
            }
            break;
        case MONSTRO_DETALHES:
		case MONSTRO_HABILIDADES:
			Input.decrementarCursorX();
            break;
        case INVENTARIO:
            Inventory.alternarPagina(false);
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
            if (!Maps.ehParede(mapaAtual, Player.getJogadorX() + 1, Player.getJogadorY())){
				Player.setJogadorX(Player.getJogadorX()+1);
            }
            break;
        case MONSTRO_DETALHES:
		case MONSTRO_HABILIDADES:
			Input.incrementarCursorX();
            break;
        case INVENTARIO:
            Inventory.alternarPagina(true);
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
			Input.decrementarCursorY();
            break;
        case INVENTARIO:
			Input.decrementarCursorY();
            break;
        case MAPA:
            if (!Maps.ehParede(mapaAtual, Player.getJogadorX(), Player.getJogadorY() - 1)){
				Player.setJogadorY(Player.getJogadorY()-1);
            }
            break;
        case LOJA:
            Input.decrementarCursorY();
            break;
		case MONSTRO_HABILIDADES:
			Input.decrementarCursorY();
			break;
		}
	}
	
	private void teclaBaixo(){
		switch (estadoAtual){
        case TITULO:
			Input.incrementarCursorY();
            break;
        case INVENTARIO:
			Input.incrementarCursorY();
            break;
        case MAPA:
            if (!Maps.ehParede(mapaAtual, Player.getJogadorX(), Player.getJogadorY() + 1)){
                Player.setJogadorY(Player.getJogadorY()+1);
            }
            break;
        case LOJA:
            Input.incrementarCursorY();
            break;
		case MONSTRO_HABILIDADES:
			Input.incrementarCursorY();
			break;
		}
	}
	
	private void teclaDebug(){
		if (Debug.isAtivaDebug()){
			Debug.setAtivaDebug(false);
			Utils.limpaPrompt();
		}else Debug.setAtivaDebug(true);
	}
	
	private void teclaEnter(){
		switch (estadoAtual) {
        case TITULO:
            if (Input.getCursorY() == 1 || Input.getCursorY() == 2){
                Grapchics.limpaTela();
				Input.resetarCursor();
                estadoAtual = EstadosJogo.MAPA;
            }
            if (Input.getCursorY() == 3) System.exit(0); // Provisório.
            break;
        case INVENTARIO:
            Inventory.alternarMonstroTabela(Input.getCursorY());
            break;
        case MONSTRO_DETALHES:
            Inventory.alternarMonstroFavorito(Input.getCursorY());
            break;
		case MONSTRO_HABILIDADES:
			Inventory.alternarHabilidadeAtiva();
			break;
        case MAPA:
            if (Maps.ehEvento(mapaAtual, Player.getJogadorX(), Player.getJogadorY()) == '$'){
                Input.resetarCursor();
                Shop.limparCarrinho();
                estadoAtual = EstadosJogo.LOJA;
            }
			if (Maps.ehEvento(mapaAtual, Player.getJogadorX(), Player.getJogadorY()) == '!'){
				Battle.setarBatalha();
				estadoAtual = EstadosJogo.BATALHA;
            }
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
			Input.setCursorX(Input.getCursorY());
            estadoAtual = EstadosJogo.MONSTRO_DETALHES;
            break;
		case MONSTRO_DETALHES:
			Grapchics.limpaTela();
			Input.setCursorAnteriorY(Input.getCursorY());
			estadoAtual = EstadosJogo.MONSTRO_HABILIDADES;
			break;
		}
	}
	
	private void teclaInventário(){
		switch (estadoAtual){
        case MAPA:
        case LOJA_RECIBO:
            Grapchics.limpaTela();
			Input.setCursorX(1);
			Input.setCursorY(1);
            estadoAtual = EstadosJogo.INVENTARIO;
            break;
        case INVENTARIO:
            Grapchics.limpaTela();
            Input.setCursorX(1);
			Input.setCursorY(1);
            estadoAtual = EstadosJogo.MAPA;
            break;
        case MONSTRO_DETALHES:
            Grapchics.limpaTela();
            estadoAtual = EstadosJogo.INVENTARIO;
            break;
		case MONSTRO_HABILIDADES:
			Grapchics.limpaTela();
			Input.setCursorY(Input.getCursorAnteriorY());
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
        case LOJA:
            if (Shop.comprarMonstro()){
				Grapchics.limpaTela();
				Input.resetarCursor();
				estadoAtual = EstadosJogo.LOJA_RECIBO;
			}else if (estadoAtual == EstadosJogo.LOJA_RECIBO){
				Grapchics.limpaTela();
				Shop.limparCarrinho(); 
				Input.resetarCursor();
				estadoAtual = EstadosJogo.LOJA;
			}
            break;
        case LOJA_RECIBO:
            Grapchics.limpaTela();
            Shop.limparCarrinho();
			Input.resetarCursor();
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
	
	// ==================== OUTROS ====================
	
	public String getEstadoAtual(){
		if (this.estadoAtual == null){
			return EstadosJogo.TITULO.getEstadoNome();
		}
		return this.estadoAtual.getEstadoNome();
	}
	
	//===
}