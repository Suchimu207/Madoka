import asciiPanel.AsciiPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.nio.file.Files;   
import java.nio.file.Paths;

import java.io.IOException;

public class Terminal implements KeyListener {
    private final JFrame frame;
	private final Grapchics gráfico;
	
	private int cursorX, cursorY = 0;
	private int lastX, lastY = 0;
	private int iLinha, jColuna = 0;
	private String os, mapaAtual, TITLE, mapaNomeFormatado;
	private boolean ativaDebug;
	
	public Terminal(String TITLE){
		this.TITLE = TITLE;
		os = System.getProperty("os.name").toLowerCase();
		
		frame = new JFrame(this.TITLE);
		gráfico = new Grapchics();
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
			System.out.println("Jogador_X: "+cursorX);
			System.out.println("Jogador_Y: "+cursorY);
		}
	}
	
	public void carregarMapa(String mapaNome){
		try {
			mapaAtual = Files.readString(Paths.get("data", "maps", mapaNome + ".txt"));
		} catch(IOException e) {
			System.out.println("Erro ao carregar mapa: "+e.getMessage());
		}
	}
	
	public void desenhaMapa(){
			
			/*
			gráfico.desenhaCentro("============================================", 1, 
			AsciiPanel.brightBlack);
			mapaNomeFormatado = mapaNome.toUpperCase().replaceAll("[_0-9]", "");
			gráfico.desenhaCentro
			
			gráfico.desenhaCentro("============================================", 3, 
			AsciiPanel.brightBlack);
			
			System.out.println(AMARELO+">>"+mapaNomeFormatado+RESETA);
			System.out.println("");
			*/
			
		String[] linhas = mapaAtual.split("\\R");
		
		for (iLinha = 0; iLinha < linhas.length; iLinha++){
			char[] caracteres = linhas[iLinha].toCharArray();
			for (jColuna = 0; jColuna < caracteres.length; jColuna++){
				char tile = linhas[iLinha].charAt(jColuna);
				if (jColuna == cursorX && iLinha == cursorY){
					gráfico.desenhaTela('@', cursorX, cursorY, AsciiPanel.brightWhite);
				}else{
					switch(tile){
					case '#':
					gráfico.desenhaTela('#', jColuna, iLinha, AsciiPanel.brightBlack);
					break;
					case '.':
					gráfico.desenhaTela('.', jColuna, iLinha, AsciiPanel.brightWhite);
					break;
					case '~':
					gráfico.desenhaTela('~', jColuna, iLinha, AsciiPanel.blue);
					break;
					case '&':
					gráfico.desenhaTela('&', jColuna, iLinha, AsciiPanel.brightRed);
					break;
					case '$':
					gráfico.desenhaTela('$', jColuna, iLinha, AsciiPanel.brightYellow);
					break;
					case 'H':
					gráfico.desenhaTela('H', jColuna, iLinha, AsciiPanel.brightYellow);
					break;
					case '_':
					gráfico.desenhaTela('_', jColuna, iLinha, AsciiPanel.brightWhite);
					break;
					case '*':
					gráfico.desenhaTela('*', jColuna, iLinha, AsciiPanel.brightYellow);
					break;
					case '¨':
					gráfico.desenhaTela('^', jColuna, iLinha, AsciiPanel.brightGreen);
					break;
					case '[':
					gráfico.desenhaTela('[', jColuna, iLinha, AsciiPanel.brightWhite);
					break;
					case ']':
					gráfico.desenhaTela(']', jColuna, iLinha, AsciiPanel.brightWhite);
					break;
					}
				}
			}
		}
		gráfico.atualizarTela();
    }
	
	@Override
	public void keyPressed(KeyEvent e){
		lastX = cursorX; 
		lastY = cursorY;
		
		switch (e.getKeyCode()){
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				cursorX--;
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				cursorX++;
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				cursorY--;
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				cursorY++;
				break;
			case KeyEvent.VK_F3:
			case KeyEvent.VK_ALT:
				if (ativaDebug){
					ativaDebug = false;
					limpaPrompt();
				}else ativaDebug = true;
				break;
		}
		
		if (cursorX >= 0 && cursorX <= gráfico.getTileSizeX()-1 && 
			cursorY >= 0 && cursorY <= gráfico.getTileSizeY()-1){
		}else{
			cursorX = lastX;
			cursorY = lastY;
		}
	}
	
	@Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
	
	//===
}