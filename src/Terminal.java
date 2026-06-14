import asciiPanel.AsciiPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.nio.file.Files;   
import java.nio.file.Paths;

import java.io.IOException;

public class Terminal implements KeyListener {
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
	private final Grapchics gráfico;
	private EstadosJogo estadoAtual;
	
	private int jogadorX, jogadorY = 0;
	private int lastJogador_X, lastJogador_Y = 0;
	private int cursorX, cursorY;
	private int iLinha, jColuna = 0;
	private String os, mapaAtual, TITLE, mapaNomeFormatado;
	private boolean ativaDebug;
	
	public Terminal(String TITLE){
		this.TITLE = TITLE;
		os = System.getProperty("os.name").toLowerCase();
		
		frame = new JFrame(this.TITLE);
		gráfico = new Grapchics();
	}
	
	public void setarJogo(){
		estadoAtual = EstadosJogo.TITULO;
		cursorY = 1; // A posição inicial é "Novo jogo".
	}
	
	public void setarJanela(){
		frame.add(gráfico.getTela());
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
	
	public void mostrarDebug(int contadorFrames){
		if (ativaDebug){
			limpaPrompt();
			System.out.println("FPS Atual: " + contadorFrames);
			System.out.println("Jogador_X: "+jogadorX);
			System.out.println("Jogador_Y: "+jogadorY);
		}
	}
	
	public void desenhaEstado(){
		switch (estadoAtual){
			case TITULO:
				desenhaTítulo();
				break;
			case MAPA:
				desenhaMapa();
				break;
		}
	}
	
	private void desenhaTítulo(){
		gráfico.desenhaCentro(TITLE, 14, AsciiPanel.brightWhite);
		
		if (cursorY >= 4){
			cursorY = 1;
		} else if (cursorY <= 0) cursorY = 3;
		
		if (cursorY == 1){
			gráfico.desenhaCentro("Novo jogo", 18, AsciiPanel.brightYellow, 
			AsciiPanel.brightBlack);
		}else{
			gráfico.desenhaCentro("Novo jogo", 18, AsciiPanel.brightWhite);
		}
		
		if (cursorY == 2){
			gráfico.desenhaCentro("Continuar", 20, AsciiPanel.brightYellow, 
			AsciiPanel.brightBlack);
		}else{
			gráfico.desenhaCentro("Continuar", 20, AsciiPanel.brightWhite);
		}
		
		if (cursorY == 3){
			gráfico.desenhaCentro("Sair     ", 22, AsciiPanel.brightYellow,
			AsciiPanel.brightBlack);
		}else{
			gráfico.desenhaCentro("Sair     ", 22, AsciiPanel.brightWhite);
		}
		
		gráfico.atualizarTela();
	}
	
	public void carregarMapa(String mapaNome){
		try {
			mapaAtual = Files.readString(Paths.get("data", "maps", mapaNome + ".txt"));
		} catch(IOException e) {
			System.out.println("Erro ao carregar mapa: "+e.getMessage());
		}
	}
	
	private void desenhaMapa(){
		String[] linhas = mapaAtual.split("\\R");
		
		for (iLinha = 0; iLinha < linhas.length; iLinha++){
			char[] caracteres = linhas[iLinha].toCharArray();
			for (jColuna = 0; jColuna < caracteres.length; jColuna++){
				char tile = linhas[iLinha].charAt(jColuna);
				if (jColuna == jogadorX && iLinha == jogadorY){
					gráfico.desenhaTela('@', jogadorX, jogadorY, AsciiPanel.brightWhite);
				}else{
					switch(tile){
					case '#':
					gráfico.desenhaTela('#', jColuna, iLinha, AsciiPanel.brightBlack);
					break;
					case '.':
					gráfico.desenhaTela('.', jColuna, iLinha, AsciiPanel.brightWhite);
					break;
					case '_':
					gráfico.desenhaTela('_', jColuna, iLinha, AsciiPanel.brightWhite);
					break;
					}
				}
			}
		}
		gráfico.atualizarTela();
    }
	
	private void teclaEsquerda(){
		if (estadoAtual == EstadosJogo.MAPA) jogadorX--;
	}
	
	private void teclaDireita(){
		if (estadoAtual == EstadosJogo.MAPA) jogadorX++;
	}
	
	private void teclaCima(){
		if (estadoAtual == EstadosJogo.TITULO){
			gráfico.limpaTela();
			cursorY--;
		}					
		if (estadoAtual == EstadosJogo.MAPA) jogadorY--;
	}
	
	private void teclaBaixo(){
		if (estadoAtual == EstadosJogo.TITULO){
			gráfico.limpaTela();
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
				gráfico.limpaTela();
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
				gráfico.limpaTela();
				if (estadoAtual == EstadosJogo.MAPA) estadoAtual = EstadosJogo.TITULO;
				break;
		}
		
		if (jogadorX >= 0 && jogadorX <= gráfico.getTileSizeX()-1 && 
			jogadorY >= 0 && jogadorY <= gráfico.getTileSizeY()-1){
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