import asciiPanel.AsciiPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class Terminal implements KeyListener {
	private enum EstadosJogo{
		TITULO("Título"),
		MAPA("Mapa");
		
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
	private int lastJogador_X, lastJogador_Y;
	private int cursorX, cursorY;
	private final String TITLE;
	private String os, mapaAtual, mapaInicial;
	private boolean ativaDebug;
	
	protected Terminal(String TITLE, String mapaInicial){
		this.TITLE = TITLE;
		this.mapaInicial = mapaInicial;
		os = System.getProperty("os.name").toLowerCase();
		frame = new JFrame(TITLE);
	}
	
	protected void setarJogo(){
		estadoAtual = EstadosJogo.TITULO;
		mapaAtual = mapaInicial;
		lastJogador_X = 0;
		lastJogador_Y = 0;
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
		}
	}
	
	protected void desenhaEstado(){
		switch (estadoAtual){
			case TITULO:
				desenhaTítulo();
				break;
			case MAPA:
				Maps.desenhaMapa(mapaAtual, jogadorX, jogadorY);
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
		
		Grapchics.atualizarTela();
	}
	
	private void teclaEsquerda(){
		if (estadoAtual == EstadosJogo.MAPA) jogadorX--;
	}
	
	private void teclaDireita(){
		if (estadoAtual == EstadosJogo.MAPA) jogadorX++;
	}
	
	private void teclaCima(){
		if (estadoAtual == EstadosJogo.TITULO){
			Grapchics.limpaTela();
			cursorY--;
		}					
		if (estadoAtual == EstadosJogo.MAPA) jogadorY--;
	}
	
	private void teclaBaixo(){
		if (estadoAtual == EstadosJogo.TITULO){
			Grapchics.limpaTela();
			cursorY++;
		}					
		if (estadoAtual == EstadosJogo.MAPA) jogadorY++;
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
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		lastJogador_X = jogadorX; 
		lastJogador_Y = jogadorY;
		
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
			case KeyEvent.VK_F3:
			case KeyEvent.VK_ALT:
				teclaDebug();
				break;
			case KeyEvent.VK_ENTER:
				teclaEnter();
				break;
			case KeyEvent.VK_ESCAPE:
				Grapchics.limpaTela();
				if (estadoAtual == EstadosJogo.MAPA) estadoAtual = EstadosJogo.TITULO;
				break;
		}
		
		if (jogadorX >= 0 && jogadorX <= Grapchics.getTileSizeX()-1 && 
			jogadorY >= 0 && jogadorY <= Grapchics.getTileSizeY()-1){
		}else{
			jogadorX = lastJogador_X;
			jogadorY = lastJogador_Y;
		}
	}
	
	@Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
	
	//===
}