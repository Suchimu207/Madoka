package util;

public final class Utils {
	private static String os;
	
	private Utils(){
	}
	
	public static void limpaPrompt(){
		os = System.getProperty("os.name").toLowerCase();
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
	
	//===
}