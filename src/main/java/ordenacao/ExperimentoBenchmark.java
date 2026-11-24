package ordenacao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ExperimentoBenchmark {

    private static final int AMOSTRA_INICIAL = 30;
    private static final int RODADAS_WARMUP = 15;
    private static final double Z_95 = 1.96;      // Nível de confiança de 95%
    private static final double ERRO_MAX = 0.05;  // Erro relativo máximo de 5%

    public static void main(String[] args) {
        File diretorioResultados = new File("resultados");
        if (!diretorioResultados.exists()) {
            diretorioResultados.mkdirs();
        }

        int[] tamanhos = {10_000, 50_000, 100_000};

        OrdenacaoAbstract[] algoritmos = {
                new OrdenacaoQuickSortRecursivo(),
                new OrdenacaoQuickSortIterativo(),
                new OrdenacaoMergeSort()
        };

        String arquivoCsv = "resultados/tempos_execucao.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivoCsv))) {
            // Cabeçalho do CSV (padrão internacional com ponto decimal)
            writer.println("Cenario,Tamanho,Algoritmo,Execucao,Tempo_ms");

            for (int tamanho : tamanhos) {
                // Gera as mesmas massas de dados para todos os algoritmos
                int[] baseAleatoria = gerarVetorAleatorio(tamanho, 42L);
                int[] baseOrdenada = gerarVetorOrdenado(tamanho);
                int[] baseDecrescente = gerarVetorDecrescente(tamanho);

                executarCenario("Aleatorio", tamanho, baseAleatoria, algoritmos, writer);
                executarCenario("Ordenado", tamanho, baseOrdenada, algoritmos, writer);
                executarCenario("Decrescente", tamanho, baseDecrescente, algoritmos, writer);
            }

            System.out.println("\nExperimentos concluídos! Dados brutos exportados para: " + arquivoCsv);

        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo CSV: " + e.getMessage());
        }
    }

    private static void executarCenario(String nomeCenario, int tamanho, int[] vetorBase,
                                        OrdenacaoAbstract[] algoritmos, PrintWriter writer) {
        System.out.println("\n=========================================================================");
        System.out.printf("CENÁRIO: %s | TAMANHO: %d elementos%n", nomeCenario, tamanho);
        System.out.println("=========================================================================");

        for (OrdenacaoAbstract algoritmo : algoritmos) {
            String nomeAlgoritmo = algoritmo.getClass().getSimpleName();
            List<Double> temposMs = new ArrayList<>();

            try {
                // 1. Aquecimento da JVM (Warm-up) para estabilizar compilação JIT
                for (int w = 0; w < RODADAS_WARMUP; w++) {
                    int[] copia = Arrays.copyOf(vetorBase, vetorBase.length);
                    algoritmo.setInfo(copia);
                    algoritmo.ordenar();
                }

                // 2. Amostragem inicial (n0 = 30)
                for (int i = 0; i < AMOSTRA_INICIAL; i++) {
                    temposMs.add(medirExecucaoMs(algoritmo, vetorBase));
                }

                // 3. Cálculo do tamanho amostral necessário (erro 5%, confiança 95%)
                double mediaInicial = calcularMedia(temposMs);
                double desvioInicial = calcularDesvioPadrao(temposMs, mediaInicial);
                int nNecessario = calcularTamanhoAmostra(mediaInicial, desvioInicial);

                // Garante no mínimo 30 execuções para validade assintótica
                int totalExecucoes = Math.max(AMOSTRA_INICIAL, nNecessario);

                // 4. Executa rodadas complementares se Necessario > 30
                for (int i = AMOSTRA_INICIAL; i < totalExecucoes; i++) {
                    temposMs.add(medirExecucaoMs(algoritmo, vetorBase));
                }

                // 5. Estatísticas finais e Intervalo de Confiança (95%)
                double mediaFinal = calcularMedia(temposMs);
                double desvioFinal = calcularDesvioPadrao(temposMs, mediaFinal);
                double margemErro = Z_95 * (desvioFinal / Math.sqrt(totalExecucoes));
                double icInferior = mediaFinal - margemErro;
                double icSuperior = mediaFinal + margemErro;

                System.out.printf(Locale.US,
                        "%-28s | n_calc: %4d | n_usado: %4d | Média: %8.4f ms | IC 95%%: [%8.4f, %8.4f]%n",
                        nomeAlgoritmo, nNecessario, totalExecucoes, mediaFinal, icInferior, icSuperior);

                // 6. Grava cada execução individual no CSV
                for (int i = 0; i < temposMs.size(); i++) {
                    writer.printf(Locale.US, "%s,%d,%s,%d,%.6f%n",
                            nomeCenario, tamanho, nomeAlgoritmo, (i + 1), temposMs.get(i));
                }

            } catch (StackOverflowError e) {
                System.out.printf("%-28s | ERRO: StackOverflowError (Estouro de pilha da JVM)%n", nomeAlgoritmo);
                writer.printf(Locale.US, "%s,%d,%s,1,STACK_OVERFLOW%n", nomeCenario, tamanho, nomeAlgoritmo);
            }
        }
    }

    private static double medirExecucaoMs(OrdenacaoAbstract algoritmo, int[] vetorBase) {
        // Clona o vetor FORA da medição de tempo para garantir a mesma entrada
        int[] copia = Arrays.copyOf(vetorBase, vetorBase.length);
        algoritmo.setInfo(copia);

        long inicio = System.nanoTime();
        algoritmo.ordenar();
        long fim = System.nanoTime();

        return (fim - inicio) / 1_000_000.0; // Converte nanossegundos para milissegundos
    }

    private static int calcularTamanhoAmostra(double media, double desvioPadrao) {
        if (media == 0) return AMOSTRA_INICIAL;
        double numerador = Z_95 * desvioPadrao;
        double denominador = ERRO_MAX * media;
        return (int) Math.ceil(Math.pow(numerador / denominador, 2));
    }

    private static double calcularMedia(List<Double> valores) {
        double soma = 0.0;
        for (double v : valores) {
            soma += v;
        }
        return soma / valores.size();
    }

    private static double calcularDesvioPadrao(List<Double> valores, double media) {
        double somaDiferencasQuadrado = 0.0;
        for (double v : valores) {
            somaDiferencasQuadrado += Math.pow(v - media, 2);
        }
        return Math.sqrt(somaDiferencasQuadrado / (valores.size() - 1));
    }

    private static int[] gerarVetorAleatorio(int tamanho, long seed) {
        Random random = new Random(seed);
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = random.nextInt(tamanho * 10);
        }
        return vetor;
    }

    private static int[] gerarVetorOrdenado(int tamanho) {
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = i;
        }
        return vetor;
    }

    private static int[] gerarVetorDecrescente(int tamanho) {
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = tamanho - i;
        }
        return vetor;
    }
}