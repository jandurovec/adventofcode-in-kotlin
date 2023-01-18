package aoc2015

import AdventDay
import TestCase

class Day15 : AdventDay<List<Day15.Ingredient>, Int, Int>(2015, 15) {
    companion object {
        val INPUT_REGEX = Regex("""(.*): capacity (.*), durability (.*), flavor (.*), texture (.*), calories (.*)""")
    }

    class Ingredient(
        val name: String, val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int
    )

    override fun parseInput(stringInput: List<String>) = stringInput.map { line ->
        val values = INPUT_REGEX.matchEntire(line)!!.groupValues.drop(1)
        val (capacity, durability, flavor, texture, calories) = values.drop(1).map { it.toInt() }
        Ingredient(values.first(), capacity, durability, flavor, texture, calories)
    }

    private fun maxScore(ingredients: List<Ingredient>, recipeFilter: (Map<String, Int>) -> Boolean = { true }): Int {
        fun generateIngredients(
            ingredient: Int = 0, total: Int = 100, inventory: Map<String, Int> = mapOf()
        ): Sequence<Map<String, Int>> = sequence {
            val name = ingredients[ingredient].name
            if (ingredient == ingredients.lastIndex) {
                yield(inventory.plus(name to total))
            } else {
                for (count in 1..total - (ingredients.lastIndex - ingredient)) {
                    yieldAll(generateIngredients(ingredient + 1, total - count, inventory.plus(name to count)))
                }
            }
        }
        return generateIngredients().filter(recipeFilter).maxOf { recipe ->
            val totalCapacity = maxOf(ingredients.sumOf { it.capacity * recipe[it.name]!! }, 0)
            val totalDurability = maxOf(ingredients.sumOf { it.durability * recipe[it.name]!! }, 0)
            val totalFlavor = maxOf(ingredients.sumOf { it.flavor * recipe[it.name]!! }, 0)
            val totalTexture = maxOf(ingredients.sumOf { it.texture * recipe[it.name]!! }, 0)
            totalCapacity * totalDurability * totalFlavor * totalTexture
        }
    }

    override fun part1(input: List<Ingredient>, testArg: Any?) = maxScore(input)
    override fun part2(input: List<Ingredient>, testArg: Any?) = maxScore(input) { recipe ->
        input.sumOf { it.calories * recipe[it.name]!! } == 500
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 62842880))
    override fun testCases2() = listOf(TestCase(getTestInput(), 57600000))
}

fun main() {
    Day15().main()
}
