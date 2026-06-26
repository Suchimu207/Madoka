package main;

import bestiary.Monsters;
import bestiary.MonstersManager;
import asciiPanel.AsciiPanel;
import java.util.ArrayList;

public final class Shop {
    private static class ItemLoja {
        private final int idMonstro;
        private final int preco;
        private final int idEstante;
        private boolean carrinhoItem; 
        
        ItemLoja(int idMonstro, int preco, int idEstante){
            if (idMonstro <= 0){
                this.idMonstro = 1;
            }else{
                this.idMonstro = idMonstro;
            }
            
            if (preco <= -1){
                this.preco = 0;
            }else{
                this.preco = preco;
            }
            
            this.idEstante = idEstante;
            this.carrinhoItem = false;
        }
        
        public boolean isItemCarrinho(){ 
            return carrinhoItem;
        }
        
        public void setItemCarrinho(boolean carrinhoAtivo){
            this.carrinhoItem = carrinhoAtivo;
        }
    }
    
    private Shop() {
    }
    
    private static ArrayList<ItemLoja> estoque, carrinho;
    private static int linhaItem, idEstanteAtual, totalPaginas, paginaAtual, 
    inicioLista, fimLista, tamanhoLoja, tamanhoRecibo, ouroGasto;
    
    private static String indicadorPagina;

    protected static void inicializarLoja(){
        estoque = new ArrayList<>();
        carrinho = new ArrayList<>();
        idEstanteAtual = 1;
        paginaAtual = 1;
        ouroGasto = 0;
		indicadorPagina = "";
        
        for (int i = 1; i <= 20; i++){
            estoque.add(new ItemLoja(i++, 150, idEstanteAtual++));
        }
    }

    protected static void limparCarrinho(){
        if (carrinho != null){
            for (ItemLoja item : carrinho){
                item.setItemCarrinho(false);
            }
            carrinho.clear();
        }
        paginaAtual = 1;
    }
	
    protected static void desenhaLoja(){
        Grapchics.limpaTela();
		
		int total;
		tamanhoLoja = estoque.size();
        inicioLista = (paginaAtual - 1) * 24;
        fimLista = Math.min(inicioLista + 24, tamanhoLoja);
		totalPaginas = Math.max(1, (int) Math.ceil(tamanhoLoja / 24.0));
		indicadorPagina = "Pagina " + paginaAtual + "/" + totalPaginas;
		
		if (Terminal.cursorY < inicioLista) Terminal.cursorY = fimLista-1;
		if (Terminal.cursorY >= fimLista) Terminal.cursorY = inicioLista;
		
        Grapchics.desenhaCentro("Loja - "+indicadorPagina, 0, AsciiPanel.brightWhite);
        Grapchics.desenhaTela("E: Sair", 0, 1, AsciiPanel.brightBlack);
        Grapchics.desenhaTela("Q: Comprar", 0, 2, AsciiPanel.brightBlack);
        Grapchics.desenhaTela("ENTER: Colocar/Remover do carrinho", 0, 3, AsciiPanel.brightBlack);
        Grapchics.desenhaTela("Ouro: "+Player.getOuro(), 0, 4, AsciiPanel.brightWhite);
		
		if (carrinho != null && !carrinho.isEmpty()){
			total = 0;
            for (int i = 0; i < carrinho.size(); i++){
                ItemLoja item = carrinho.get(i);
                if (item == null) continue;
                total += item.preco;
            }
			
			if (Player.getOuro() > total){
				Grapchics.desenhaTela("Total: "+total, 0, 5, AsciiPanel.brightGreen);
			}else if (Player.getOuro() == total){
				Grapchics.desenhaTela("Total: "+total, 0, 5, AsciiPanel.brightYellow);
			}else if (Player.getOuro() < total){
				Grapchics.desenhaTela("Total: "+total, 0, 5, AsciiPanel.brightRed);
			}
			
			linhaItem = 6;
		}else linhaItem = 5;
		
		Grapchics.desenhaTela("____________________", 0, linhaItem++, AsciiPanel.brightWhite);
		desenhaListaLoja();
        Grapchics.desenhaTela("____________________", 0, linhaItem, AsciiPanel.brightWhite);
		
        Grapchics.atualizarTela();
    }
	
	private static void desenhaListaLoja(){
		for (int i = inicioLista; i < fimLista; i++){
            ItemLoja item = estoque.get(i);
            Monsters infoMonstro = MonstersManager.getMonstro(item.idMonstro); 
            if (infoMonstro == null) continue;

            String textoMarcado = item.isItemCarrinho() ? " [C]" : "";

            if (Terminal.cursorY == i){
                Grapchics.desenhaTela(infoMonstro.getNomeMonstro()+" Nv"+infoMonstro.getNivelBase()+" - Preco: "+item.preco+textoMarcado, 0, 
				linhaItem++, AsciiPanel.brightYellow, AsciiPanel.brightBlack);
            }else{
                Grapchics.desenhaTela(infoMonstro.getNomeMonstro()+" Nv"+infoMonstro.getNivelBase()+" - Preco: "+item.preco+textoMarcado, 0, 
				linhaItem++, AsciiPanel.brightWhite);
            }
        }
	}
	
    protected static boolean comprarMonstro(){
        if (carrinho != null && !carrinho.isEmpty()){
            int total = 0;
            for (int i = 0; i < carrinho.size(); i++){
                ItemLoja item = carrinho.get(i);
                if (item == null) continue;
                total += item.preco;
            }
            
            if (Player.getOuro() >= total){
                Player.setOuro(Player.getOuro() - total);
                ouroGasto = total;
                
                for (ItemLoja item : carrinho){
                    Inventory.adicionarMonstroInventário(item.idMonstro);
                }
                paginaAtual = 1;
                return true;
            }
        }
        return false;
    }

	protected static void alternarItemCarrinho(){
        if (Terminal.cursorY >= 0 && Terminal.cursorY < estoque.size()){
            ItemLoja item = estoque.get(Terminal.cursorY);
            if (item.isItemCarrinho()){
                item.setItemCarrinho(false);
                carrinho.remove(item);
            }else{
                item.setItemCarrinho(true);
                carrinho.add(item);
            }
        }
    }

    protected static void desenhaLojaRecibo(){
        Grapchics.limpaTela();
        
        tamanhoRecibo = carrinho.size();
		inicioLista = (paginaAtual - 1) * 24;
        fimLista = Math.min(inicioLista + 24, tamanhoRecibo);
        totalPaginas = Math.max(1, (int) Math.ceil(tamanhoRecibo / 24.0));
        indicadorPagina = "Pagina " + paginaAtual + "/" + totalPaginas;
		
        Grapchics.desenhaCentro("Recibo - " + indicadorPagina, 0, AsciiPanel.brightWhite);
        Grapchics.desenhaTela("ESC: Sair", 0, 1, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("E: Abrir inventario", 0, 2, AsciiPanel.brightBlack);
        Grapchics.desenhaTela("Q: Continuar comprando", 0, 3, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Ouro atual: " + Player.getOuro(), 0, 4, AsciiPanel.brightWhite);
        Grapchics.desenhaTela("Ouro gasto:", 0, 5, AsciiPanel.brightWhite);
		Grapchics.desenhaTela(" " + ouroGasto,11,5, AsciiPanel.brightYellow);
        Grapchics.desenhaTela("____________________", 0, 6, AsciiPanel.brightWhite);
        linhaItem = 7;
        
        desenhaListaRecibo();
        Grapchics.desenhaTela("____________________", 0, linhaItem, AsciiPanel.brightWhite);
        
        Grapchics.atualizarTela();
    }
	
    private static void desenhaListaRecibo(){
        for (int i = inicioLista; i < fimLista; i++){
            ItemLoja item = carrinho.get(i);
            Monsters infoMonstro = MonstersManager.getMonstro(item.idMonstro);
            if (infoMonstro == null) continue;
            
            Grapchics.desenhaTela(infoMonstro.getNomeMonstro()+" Nv"+infoMonstro.getNivelBase(), 0, linhaItem++, AsciiPanel.brightWhite);
        }
    }

    protected static void alternarPagina(boolean avançar){
        if (avançar){
            paginaAtual++;
            if (paginaAtual >= totalPaginas){
                paginaAtual = 1;
            }
        }else{
            paginaAtual--;
            if (paginaAtual < 1){
                paginaAtual = totalPaginas;
            }
        }
    }
    
    public static int getTamanhoEstoque(){
        return estoque.size();
    }
	
  //===	
}