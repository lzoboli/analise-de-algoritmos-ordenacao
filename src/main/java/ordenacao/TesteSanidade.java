package ordenacao;

import java.util.Arrays;
import java.util.Random;

public class TesteSanidade {
    public static void main(String[] args) {
        int[][] casosDeTeste = {
                {},                                     // Vazio
                {42},                                   // 1 elemento
                {5, 4, 3, 2, 1},                        // Ordem decrescente
                {1, 2, 3, 4, 5},                        // Já ordenado
                {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5},      // Com elementos duplicados
                gerarVetorAleatorio(10_000, 42)         // Vetor maior (10 mil elementos)
        };

        OrdenacaoAbstract[] algoritmos = {
                new OrdenacaoQuickSortRecursivo(),
                new OrdenacaoQuickSortIterativo(),
                new OrdenacaoMergeSort()
        };

        for (int i = 0; i < casosDeTeste.length; i++) {
            int[] original = casosDeTeste[i];
            int[] esperado = Arrays.copyOf(original, original.length);
            Arrays.sort(esperado);

            for (OrdenacaoAbstract algoritmo : algoritmos) {
                int[] copiaParaOrdenar = Arrays.copyOf(original, original.length);
                algoritmo.setInfo(copiaParaOrdenar);
                algoritmo.ordenar();

                if (!Arrays.equals(copiaParaOrdenar, esperado)) {
                    System.err.println("FALHA no algoritmo " + algoritmo.getClass().getSimpleName()
                            + " no caso de teste " + i);
                    return;
                }
            }
        }

        System.out.println("SUCESSO! Todos os 3 algoritmos ordenaram perfeitamente todos os casos de teste.");
    }

    private static int[] gerarVetorAleatorio(int tamanho, long seed) {
        Random random = new Random(seed);
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = random.nextInt(100_000);
        }
        return vetor;
    }
}
