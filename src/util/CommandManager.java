package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class CommandManager {
	/*
    private static final Map<String, Command> comandos = new HashMap<>();
    private static boolean ativo = false;
    private static Thread threadComandos;
    */
	
    private CommandManager() {}
    
    public static void iniciar(){
		/*
        if (ativo) return;
        ativo = true;
        
        registrarComandos();
        
        threadComandos = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (ativo) {
                System.out.print("\n> ");
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) continue;
                
                String[] partes = input.split(" ");
                String comando = partes[0].toLowerCase();
                String[] args = new String[partes.length - 1];
                System.arraycopy(partes, 1, args, 0, args.length);
                
                executarComando(comando, args);
            }
        });
        
        threadComandos.setDaemon(true);
        threadComandos.start();
        System.out.println(">>Sistema de comandos iniciado. Digite 'help' para ajuda.");
		*/
    }
    
    private static void registrarComandos() {
        // comandos.put("help", new HelpCommand());
        // comandos.put("player", new PlayerCommand());
        // comandos.put("battle", new BattleCommand());
        // comandos.put("monster", new MonsterCommand());
        // comandos.put("troop", new TroopCommand());
        // comandos.put("status", new StatusCommand());
        // comandos.put("kill", new KillCommand());
        // comandos.put("heal", new HealCommand());
        // comandos.put("clear", new ClearCommand());
        // comandos.put("exit", new ExitCommand());
    }
    
    private static void executarComando(String comando, String[] args){
		/*
        Command cmd = comandos.get(comando);
        if (cmd == null){
            System.out.println("Comando desconhecido. Digite 'help' para ver a lista.");
            return;
        }
        
        try{
            cmd.executar(args);
        }catch (Exception e){
            System.out.println("Erro ao executar comando: "+e.getMessage());
        }
		*/
    }
    
    public static void parar(){
		/*
        ativo = false;
        if (threadComandos != null){
            threadComandos.interrupt();
        }
		*/
    }
	
	//===
}