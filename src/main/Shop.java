package main;

import bestiary.Monsters;
import bestiary.MonstersManager;

import util.Grapchics;
import util.Input;

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
    // ==================== ATRIBUTOS ====================
	
	private static ArrayList<ItemLoja> estoque, carrinho;
    private static int linhaItem, idEstanteAtual, totalPaginas, paginaAtual, 
    inicioLista, fimLista, tamanhoLoja, tamanhoRecibo, ouroGasto;
    
    private static String indicadorPagina;
	
    private Shop(){
    }
    
	// ==================== INICIALIZAÇÃO ====================
	
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
	
	// ==================== DESENHO ====================
	
    protected static void desenhaLoja(){
        Grapchics.limpaTela();
		
		int total;
		tamanhoLoja = estoque.size();
        inicioLista = (paginaAtual - 1) * 24;
        fimLista = Math.min(inicioLista + 24, tamanhoLoja);
		totalPaginas = Math.max(1, (int) Math.ceil(tamanhoLoja / 24.0));
		indicadorPagina = "Página " + paginaAtual+(char)45+totalPaginas;
		
		if (Input.getCursorY() < inicioLista) Input.setCursorY(fimLista-1);
		if (Input.getCursorY() >= fimLista) Input.setCursorY(inicioLista);
		
        Grapchics.desenhaCentroTTF("Loja - "+indicadorPagina, 0, Grapchics.BRANCO_CLARO);
        Grapchics.desenhaTTF("E: Sair", 0, 1, Grapchics.PRETO_CLARO);
        Grapchics.desenhaTTF("Q: Comprar", 0, 2, Grapchics.PRETO_CLARO);
        Grapchics.desenhaTTF("ENTER: Colocar/Remover do carrinho", 0, 3, Grapchics.PRETO_CLARO);
        Grapchics.desenhaTTF("Ouro: "+Player.getOuro(), 0, 4, Grapchics.BRANCO_CLARO);
		
		if (carrinho != null && !carrinho.isEmpty()){
			total = 0;
            for (int i = 0; i < carrinho.size(); i++){
                ItemLoja item = carrinho.get(i);
                if (item == null) continue;
                total += item.preco;
            }
			
			if (Player.getOuro() > total){
				Grapchics.desenhaTTF("Total: "+total, 0, 5, Grapchics.VERDE_CLARO);
			}else if (Player.getOuro() == total){
				Grapchics.desenhaTTF("Total: "+total, 0, 5, Grapchics.AMARELO_CLARO);
			}else if (Player.getOuro() < total){
				Grapchics.desenhaTTF("Total: "+total, 0, 5, Grapchics.VERMELHO_CLARO);
			}
			
			linhaItem = 6;
		}else linhaItem = 5;
		
		Grapchics.desenhaTela("____________________", 0, linhaItem++, Grapchics.PRETO_CLARO);
		desenhaListaLoja();
        Grapchics.desenhaTela("____________________", 0, linhaItem, Grapchics.PRETO_CLARO);
		
        Grapchics.atualizarTela();
    }
	
	private static void desenhaListaLoja(){
		for (int i = inicioLista; i < fimLista; i++){
            ItemLoja item = estoque.get(i);
            Monsters infoMonstro = MonstersManager.getMonstro(item.idMonstro); 
            if (infoMonstro == null) continue;

            int textoMarcado = item.isItemCarrinho() ? 67 : 0;
			
            if (Input.getCursorY() == i){
                Grapchics.desenhaHibrido(infoMonstro.getNomeMonstro()+" Nv"+infoMonstro.getNivelBase()+" - Preço: "+item.preco,textoMarcado, 0, 
				linhaItem++, Grapchics.AMARELO_CLARO);
            }else{
                Grapchics.desenhaHibrido(infoMonstro.getNomeMonstro()+" Nv"+infoMonstro.getNivelBase()+" - Preço: "+item.preco,textoMarcado, 0, 
				linhaItem++, Grapchics.BRANCO_CLARO);
            }
        }
	}
	
	protected static void desenhaLojaRecibo(){
        Grapchics.limpaTela();
        
        tamanhoRecibo = carrinho.size();
		inicioLista = (paginaAtual - 1) * 24;
        fimLista = Math.min(inicioLista + 24, tamanhoRecibo);
        totalPaginas = Math.max(1, (int) Math.ceil(tamanhoRecibo / 24.0));
		
        indicadorPagina = "Página " + paginaAtual+(char)45+totalPaginas;
		
        Grapchics.desenhaCentroTTF("Recibo - " + indicadorPagina, 0, Grapchics.BRANCO_CLARO);
        Grapchics.desenhaTTF("ESC: Sair", 0, 1, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("E: Abrir inventário", 0, 2, Grapchics.PRETO_CLARO);
        Grapchics.desenhaTTF("Q: Continuar comprando", 0, 3, Grapchics.PRETO_CLARO);
		Grapchics.desenhaTTF("Ouro atual: " + Player.getOuro(), 0, 4, Grapchics.BRANCO_CLARO);
        Grapchics.desenhaTTF("Ouro gasto:", 0, 5, Grapchics.BRANCO_CLARO);
		Grapchics.desenhaTTF(" " + ouroGasto,11,5, Grapchics.AMARELO_CLARO);
        Grapchics.desenhaTela("____________________", 0, 6, Grapchics.PRETO_CLARO);
        linhaItem = 7;
        
        desenhaListaRecibo();
        Grapchics.desenhaTela("____________________", 0, linhaItem, Grapchics.PRETO_CLARO);
        
        Grapchics.atualizarTela();
    }
	
    private static void desenhaListaRecibo(){
        for (int i = inicioLista; i < fimLista; i++){
            ItemLoja item = carrinho.get(i);
            Monsters infoMonstro = MonstersManager.getMonstro(item.idMonstro);
            if (infoMonstro == null) continue;
            
            Grapchics.desenhaTTF(infoMonstro.getNomeMonstro()+" Nv"+infoMonstro.getNivelBase(), 0, linhaItem++, Grapchics.BRANCO_CLARO);
        }
    }
	
	// ==================== AÇÕES DO JOGADOR ====================
	
    protected static boolean comprarMonstro(){
        if (carrinho != null && !carrinho.isEmpty()){
            int total = 0;
            for (int i = 0; i < carrinho.size(); i++){
                ItemLoja item = carrinho.get(i);
                if (item == null) continue;
                total += item.preco;
            }
            
            if (Player.getOuro() >= total){
				Player.perderOuro(total);
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
        if (Input.getCursorY() >= 0 && Input.getCursorY() < estoque.size()){
            ItemLoja item = estoque.get(Input.getCursorY());
            if (item.isItemCarrinho()){
                item.setItemCarrinho(false);
                carrinho.remove(item);
            }else{
                item.setItemCarrinho(true);
                carrinho.add(item);
            }
        }
    }
	
	// ==================== MÉTODOS AUXILIARES ====================
	
	protected static void limparCarrinho(){
        if (carrinho != null){
            for (ItemLoja item : carrinho){
                item.setItemCarrinho(false);
            }
            carrinho.clear();
        }
        paginaAtual = 1;
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