package com.qwerty.mazeagentgame.evolution;



import com.qwerty.mazeagentgame.model.Maze;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StandardGA implements GeneticAlgorithm {
    private final Maze maze;
    private List<Individual> population;
    private final int popSize;
    private final int generations;
    private final Random rand;
    private final int tournamentSize;
    private final double crossoverRate;
    private final double mutationRate;

    public StandardGA(Maze maze, Random rand, int popSize, int generations, int tournamentSize,
                      double crossoverRate, double mutationRate) {
        this.maze = maze;
        this.rand = rand;
        this.popSize = popSize;
        this.generations = generations;
        this.tournamentSize = tournamentSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.population = IntStream.range(0, popSize)
                .mapToObj(i -> new Individual(rand))
                .collect(Collectors.toList());
    }

    @Override
    public Individual run(Runnable updateCallback) {
        population.forEach(ind -> ind.evaluate(maze)); // Инициализация фитнеса

        for (int gen = 0; gen < generations; gen++) {
            List<Individual> newPopulation = new ArrayList<>();
            Individual best = population.stream()
                    .max(Comparator.comparingDouble(Individual::getFitness))
                    .orElseThrow();
            newPopulation.add(best.copy());

            // Генерируем остальную популяцию через селекцию, кроссовер и мутацию
            while (newPopulation.size() < popSize) {
                Individual parent1 = tournamentSelection();
                Individual parent2 = tournamentSelection();
                Individual[] children = parent1.crossover(parent2);
                children[0].mutate(mutationRate);
                children[1].mutate(mutationRate);
                newPopulation.add(children[0]);
                if (newPopulation.size() < popSize) {
                    newPopulation.add(children[1]);
                }
            }
            population = newPopulation;
            population.forEach(ind -> ind.evaluate(maze)); // Переоцениваем фитнес

            if (updateCallback != null) {
                updateCallback.run();
            }
        }

        return population.stream()
                .max(Comparator.comparingDouble(Individual::getFitness))
                .orElseThrow();
    }

    private Individual tournamentSelection() {
        return IntStream.range(0, tournamentSize)
                .mapToObj(i -> population.get(rand.nextInt(population.size())))
                .max(Comparator.comparingDouble(Individual::getFitness))
                .orElseThrow();
    }
}