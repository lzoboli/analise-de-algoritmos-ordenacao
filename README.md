# Análise Experimental de Algoritmos de Ordenação

Implementação e análise comparativa de desempenho entre os algoritmos **Quick Sort Recursivo**, **Quick Sort Iterativo** e **Merge Sort**, desenvolvida para a disciplina de **Análise de Algoritmos** (Ciência da Computação — FURB).

## 📋 Sobre o Projeto

O objetivo deste repositório é armazenar o código-fonte das implementações, o motor de execução dos experimentos e os dados brutos coletados (`.csv`) para avaliação estatística de tempo de execução (cálculo de tamanho amostral, intervalos de confiança e análise de variância).

### Algoritmos Implementados
- **Quick Sort Recursivo:** Particionamento bidirecional (Hoare) com escolha de pivô no primeiro elemento e chamadas recursivas.
- **Quick Sort Iterativo:** Mesma lógica de particionamento do modelo recursivo, substituindo a pilha de chamadas da JVM por uma estrutura de pilha explícita.
- **Merge Sort:** Paradigma de divisão e conquista clássico com intercalação em vetores auxiliares.

## 🔄 Origem e Adaptação do Código Base

As implementações base dos algoritmos de ordenação (arquitetura de herança, particionamento no Quick Sort e intercalação do Merge Sort) foram reaproveitadas e adaptadas de atividades desenvolvidas anteriormente na disciplina de **Algoritmos e Estruturas de Dados** (3ª fase do curso de Ciência da Computação — FURB).

- **Desenvolvimento da variante iterativa:** Criação do `OrdenacaoQuickSortIterativo` utilizando pilha explícita em array primitivo, mantendo exatamente a mesma lógica de particionamento da versão recursiva.

## 🛠️ Tecnologias Utilizadas

- **Java 17 (LTS)**
- **Maven** (gerenciamento do projeto)

## 📂 Estrutura do Repositório

```text
├── src/                  # Código-fonte das implementações e executor de benchmarks
├── resultados/           # Arquivos .csv com os tempos brutos coletados nos experimentos
└── README.md             # Documentação do repositório
