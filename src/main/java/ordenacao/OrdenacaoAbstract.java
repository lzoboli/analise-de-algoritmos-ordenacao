package ordenacao;

public abstract class OrdenacaoAbstract {
    private int[] info;

    public abstract void ordenar();

    public void trocar(int[] infoParam, int pos1, int pos2) {
        int aux = infoParam[pos1];
        infoParam[pos1] = infoParam[pos2];
        infoParam[pos2] = aux;
    }

    public int[] getInfo() {
        return info;
    }

    public void setInfo(int[] info) {
        this.info = info;
    }
}