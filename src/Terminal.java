import asciiPanel.AsciiPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Terminal implements KeyListener {
	private final AsciiPanel tela;
    private final JFrame frame;
	private final Grapchics gráfico;
	
	private int cursorX, cursorY = 0;
	private int lastX, lastY = 0;
	private String os;
	private boolean movimentoJogador;
	
	public Terminal(String TITLE){
		os = System.getProperty("os.name").toLowerCase();
		
		frame = new JFrame(TITLE);
		gráfico = new Grapchics();
		
		tela = gráfico.getTela();
		
        frame.add(tela);
        frame.setResizable(false);  
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setLocationRelativeTo(null);
		
		frame.addKeyListener(this);
        frame.setFocusable(true);     
        frame.toFront();
		frame.requestFocus();
	}
	
	public void iniciarJogo(){
		limpaPrompt();
		gráfico.desenhaTela('@', 0, 0, AsciiPanel.brightWhite);
		frame.setVisible(true);
	}
	
	public void limpaTela(){
		gráfico.atualizarTela();
	}
	
	public void limpaPrompt(){
        try {
            if (os.contains("win")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }else if (os.contains("linux") || os.contains("unix")){
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        }catch (Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
        }
      //===
    }
	
	public void mostrarDebug(){
		if (movimentoJogador){
			limpaPrompt();
			System.out.println("Jogador_X: "+cursorX);
			System.out.println("Jogador_Y: "+cursorY);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		lastX = cursorX; 
		lastY = cursorY;
		
		movimentoJogador = true;
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
		}
		
		if (cursorX >= 0 && cursorX <= gráfico.getTileSizeX()-1 && 
			cursorY >= 0 && cursorY <= gráfico.getTileSizeY()-1){
			gráfico.desenhaTela(' ', lastX, lastY);
			gráfico.desenhaTela('@', cursorX, cursorY, AsciiPanel.brightWhite);
			
		}else{
			cursorX = lastX;
			cursorY = lastY;
		}
		movimentoJogador = false;
	}
	
	@Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
	
	//===
}