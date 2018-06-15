import random
import matplotlib.pyplot as plt

n = 100  # Number of population
p_crossover = [i / 10 for i in range(0, 11, 1)]  # Array of Possibilities of crossover
p_mutation = 0.001  # Possibility of mutation
chromo_length = 100  # Length of a chromosomes


def fitness(chromosome):  # Fitness function
    return chromosome.count("1")


def roulette(population):  # Roulette Function
    max = sum([fitness(c) for c in population])
    pick = random.uniform(0, max)
    current = 0
    index = 0
    for chromosome in population:
        current += fitness(chromosome)
        if current > pick:
            return chromosome, index
        index += 1


def generateAChromosome():  # Generate a chromosome
    chromosome = ""
    for _ in range(0, chromo_length):
        chromosome += str(random.randint(0, 1))
    return chromosome


def generateFirstPopulation():  # Generate the first population
    population = []
    for _ in range(0, n):
        population.append(generateAChromosome())
    return population


def mutateChromo(chromosome):  # Mutation on a chromosome
    index = random.randint(0, chromo_length - 1)
    chromosome = list(chromosome)
    if int(chromosome[index]) == 1:
        chromosome[index] = "0"
    else:
        chromosome[index] = "1"
    return "".join(chromosome)


def mutatePopulation(population):  # Mutation on chromosomes of a population
    for i in range(40):
        if random.random() < p_mutation:
            population[i] = mutateChromo(population[i])
    return population


def crossOver(parent1, parent2, possibility):  # Crossover if random greater than possibility then return the parents
    if random.random() < possibility:
        index = random.randint(0, chromo_length)
        child1 = str(parent1[:index]) + str(parent2[index:])
        child2 = str(parent2[:index]) + str(parent1[index:])
        return child1, child2
    else:
        return parent1, parent2


def plotStatistics(statistics):  # Plot the results
    print(statistics)
    plt.bar(range(len(statistics)), statistics, align='center', alpha=0.5)
    plt.xticks(range(len(statistics)), p_crossover)
    plt.ylabel('Mean Generations')
    plt.xlabel('Crossover Possibility')

    plt.show()


def checkForBest(best, i):  # Check if the best chromosomes have 100 points in fitness function
    for p in best:
        if p == 100:
            print("Found it in generation " + str(i))
            return False
    return True


if __name__ == "__main__":
    statistics = []
    for possibility in p_crossover:  # Run for all the possibilities of crossover
        generation = []
        for _ in range(1):  # Run the experiment for 20 times
            best = []
            population = generateFirstPopulation()
            population.sort(key=fitness, reverse=True)
            best.append(fitness(population[0]))
            g = 1 # The number of generations
            while checkForBest(best, g):
                nextPopulation = []
                i = 0
                while i < 20:  # Create 40 children
                    parent1, index1 = roulette(population)
                    parent2, index2 = roulette(population)
                    if index1 != index2:
                        child1, child2 = crossOver(parent1, parent2, possibility)
                        nextPopulation.append(child1)
                        nextPopulation.append(child2)
                        i += 1
                population.sort(key=fitness)
                for _ in range(40):  # Remove 40 worst chromosomes from present population
                    population.pop(0)
                population.extend(mutatePopulation(nextPopulation))  # Add the children in the population after the possible mutation
                population.sort(key=fitness, reverse=True)
                best.append(fitness(population[0]))
                g += 1
            generation.append(g - 1)
        print(
            "Mean Generations: " + str(int(sum(generation) / 1)) + " with crossover possibility: " + str(possibility))
        statistics.append(int(sum(generation) / 1))
    plotStatistics(statistics)
