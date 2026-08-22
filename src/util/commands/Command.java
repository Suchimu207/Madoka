package util.commands;

public interface Command {
    void executar(String[] args) throws Exception;
    String getUso();
    String getDescricao();
}