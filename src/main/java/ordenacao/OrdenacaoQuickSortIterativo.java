package ordenacao;

public class OrdenacaoQuickSortIterativo extends OrdenacaoAbstract{

    @Override
    public void ordenar() {
        if (getInfo() == null || getInfo().length <= 1) {
            return;
        }
        quickSortIterativo(0, getInfo().length - 1);

    }

    private void quickSortIterativo(int inicio, int fim) {
        int[] pilha = new int[fim - inicio + 1];
        int topo = -1;

        pilha[++topo] = inicio;
        pilha[++topo] = fim;

        while (topo >= 0) {

            fim = pilha[topo--];
            inicio = pilha[topo--];

            int idxPivo = particionar(inicio,fim);

            // Se houver elementos à esquerda do pivô, empilha o intervalo esquerdo
            if (idxPivo - 1 > inicio) {
                pilha[++topo] = inicio;
                pilha[++topo] = idxPivo - 1;
            }

            // Se houver elementos à direita do pivô, empilha o intervalo direito
            if (idxPivo + 1 < fim) {
                pilha[++topo] = idxPivo + 1;
                pilha[++topo] = fim;
            }
        }
    }

    private int particionar(int inicio, int fim) {

        int meio = inicio + (fim - inicio) / 2;
        trocar(getInfo(), inicio, meio);

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
