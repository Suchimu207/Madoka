package util;

import java.util.Set;

public interface GameState {
    /**
    * Lógica de renderização do estado atual.
    */
    void desenhaEstado();
	
    /**
    * Processa a entrada de teclado específica deste estado.
    */
    void recebeComando(int tecla, Set<Integer> teclasPressionadas);
}