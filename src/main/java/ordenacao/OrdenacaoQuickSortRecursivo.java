package ordenacao;

public class OrdenacaoQuickSortRecursivo extends OrdenacaoAbstract {

    @Override
    public void ordenar() {
        int n = getInfo().length - 1;
        quickSort(0, n);
    }

    private void quickSort(int inicio, int fim) {
        if (inicio < fim) {
            int idxPivo = particionar(inicio, fim);
            quickSort(inicio,idxPivo-1);
            quickSort(idxPivo+1, fim);
        }
    }

    private int particionar(int inicio, int fim) {
        int a = inicio;
        int b = fim + 1;
        int pivo = getInfo()[inicio];

        while (true) {
            do {
                a += 1;
            } while ((a <= fim) && (getInfo()[a] < pivo));

            do {
                b -= 1;
            } while ((b >= inicio) && (getInfo()[b] > pivo));

            if (a >= b) break;
            trocar(getInfo(), a, b);
        }
        trocar(getInfo() , b, inicio);
        return b;
    }
}