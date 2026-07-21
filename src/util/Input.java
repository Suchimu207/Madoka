package util;

public final class Input {
	private static int cursorX, cursorY;
	private static int cursorY_Anterior;
	
	private Input(){
	}
	
	public static int getCursorX(){
		return cursorX;
	}

	public static int getCursorY(){
		return cursorY;
	}
	
	public static int getCursorAnteriorY(){
		return cursorY_Anterior;
	}
	
	public static void setCursorX(int cursorX){
		if (cursorX < 0 || cursorX >= Grapchics.getTileSizeX()) cursorX = 0;
		Input.cursorX = cursorX;
	}

	public static void setCursorY(int cursorY){
		if (cursorY < 0 || cursorY >= Grapchics.getTileSizeY()) cursorY = 0;
		Input.cursorY = cursorY;
	}
	
	public static void setCursorAnteriorY(int cursorAnteriorY){
		if (cursorAnteriorY < 0 || cursorAnteriorY >= Grapchics.getTileSizeY()) cursorAnteriorY = 0;
		Input.cursorY_Anterior = cursorAnteriorY;
	}

	public static void decrementarCursorX(){
		Input.cursorX--;
		if (Input.cursorX < 0) Input.cursorX = 0;
	}
	
	public static void incrementarCursorX(){
		Input.cursorX++;
		if (Input.cursorX >= Grapchics.getTileSizeX()) Input.cursorX = 0;
	}
	
	public static void decrementarCursorY(){
		Input.cursorY--;
		if (Input.cursorY < 0) Input.cursorY = 0;
	}
	
	public static void incrementarCursorY(){
		Input.cursorY++;
		if (Input.cursorY >= Grapchics.getTileSizeY()) Input.cursorY = 0;
	}
	
	public static void resetarCursor(){
		Input.cursorX = 0;
		Input.cursorY = 0;
	}

	//===
}