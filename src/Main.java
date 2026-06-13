import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import javax.swing.*;
import java.awt.*;

/*
* @author Carlos S. Rehem
*/

public class Main {
    public static void main(String[] args) {
		boolean rodandoJogo = true;
		String VERSION = "0.0.7";
		String TITLE = "Madoka - "+VERSION;
		
        Terminal terminal = new Terminal(TITLE);
		Battle battle = new Battle();
		
		terminal.setarJogo();
		terminal.carregarMapa("Template");
		Battle.carregarDadosBatalha();
		
		Thread jogo = new Thread(new Runnable(){
			public void run(){
				// 60 FPS == 16.66666 MS.
				final long TARGET_TIME = 1000000000 / 60; 
				
				long tempoUltimoCalculoFPS = System.nanoTime();
				long tempoInicioFrame = 0;
				int contadorFrames = 0;

				try {
					while(rodandoJogo == true){
						tempoInicioFrame = System.nanoTime();
						
						terminal.desenhaEstado();

						//Contador de FPS.
						contadorFrames++;
						if (System.nanoTime() - tempoUltimoCalculoFPS >= 1000000000){
							terminal.mostrarDebug(contadorFrames);
							contadorFrames = 0;
							tempoUltimoCalculoFPS = System.nanoTime();
						}
						
						// Quanto tempo o processamento acima demorou.
						long tempoGasto = System.nanoTime() - tempoInicioFrame;
						
						// Calcula quanto tempo resta para completar o frame de 60 FPS.
						long tempoRestanteSleep = TARGET_TIME - tempoGasto;
						
						// Se ainda sobrou tempo, a thread dorme apenas o necessário.
						if (tempoRestanteSleep > 0) {
							// Converte nanosegundos de volta para milissegundos e nanosegundos residuais.
							Thread.sleep(tempoRestanteSleep / 1000000, (int) (tempoRestanteSleep % 1000000));
						}
					}
				}catch (InterruptedException e){
					Thread.currentThread().interrupt();
					System.out.println("Thread interrompida.");
				}
			}
		});
		jogo.start();
		
		//===
    }
}