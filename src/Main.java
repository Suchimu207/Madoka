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
		String VERSION = "0.0.1";
		String TITLE = "Banco Madoka - "+VERSION;
		
        Terminal terminal = new Terminal(TITLE);
		terminal.iniciarJogo();
		
		while(rodandoJogo == true){
			terminal.limpaTela();
			terminal.mostrarDebug();
		}
		
		//===
    }
}