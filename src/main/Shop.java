package main;

import bestiary.Monsters;
import bestiary.MonstersManager;

import asciiPanel.AsciiPanel;
import java.util.ArrayList;

public final class Shop {
	private static class ItemLoja{
        final int idMonstro;
        final int preco;
		
        ItemLoja(int idMonstro, int preco){
            this.idMonstro = idMonstro;
            this.preco = preco;
        }
    }
	
	private Shop(){
	}
	
	private static ArrayList<ItemLoja> estoque;
	
    public static void inicializarLoja(){
		estoque = new ArrayList<>();
        estoque.add(new ItemLoja(1, 0));  
        estoque.add(new ItemLoja(2, 0));  
        estoque.add(new ItemLoja(3, 0)); 
    }
	
    public static void desenhaLoja(){
        Grapchics.limpaTela();
		
		Grapchics.desenhaCentro("Loja",0, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("E: Voltar", 0, 1, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Q: Comprar", 0, 2, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Enter: Colocar/Remover do carrinho", 0, 3, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Shift: Ver detalhes", 0, 4, AsciiPanel.brightBlack);
		Grapchics.desenhaTela("Ouro: "+Battle.getOuro(), 0, 5, AsciiPanel.brightWhite);
		Grapchics.desenhaTela("____________________",0,6, AsciiPanel.brightWhite);
        
		if (Terminal.cursorY == -1) Terminal.cursorY = estoque.size()-1;
		if (Terminal.cursorY > estoque.size()-1) Terminal.cursorY = 0;
		
		int linhaItem = 8;
        for (int i = 0; i < estoque.size(); i++){
            ItemLoja item = estoque.get(i);
            
            Monsters infoMonstro = MonstersManager.getMonstro(item.idMonstro);
            if (infoMonstro == null) continue;
				
			boolean selecionado = (i == Terminal.cursorY);
	
            if (selecionado){
                Grapchics.desenhaTela(infoMonstro.getNomeMonstro()+" - Preco: "+item.preco, 0, linhaItem++, AsciiPanel.brightYellow, 
				AsciiPanel.brightBlack);
            }else{
                Grapchics.desenhaTela(infoMonstro.getNomeMonstro()+" - Preco: "+item.preco, 0, linhaItem++, AsciiPanel.brightWhite);
            }
        }
		Grapchics.desenhaTela("____________________",0,linhaItem, AsciiPanel.brightWhite);
		
        Grapchics.atualizarTela();
    }
	
    public static void comprarMonstro(){
            ItemLoja item = estoque.get(Terminal.cursorY);
			if (item == null) return;
			
            if (Battle.getOuro() >= item.preco){
                Battle.setOuro(Battle.getOuro() - item.preco);
                Battle.adicionarMonstroInventário(item.idMonstro);
				Monsters infoMonstro = MonstersManager.getMonstro(item.idMonstro);
				System.out.println("Monstro comprado: "+infoMonstro.getNomeMonstro());
            }
    }
	
    public static int getTamanhoEstoque(){
        return estoque.size();
    }
	
	//===
}