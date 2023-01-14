package aoc2015

import AdventDay
import kotlin.math.ceil

class Day21 : AdventDay<Day21.RpgCharacter, Int, Int>(2015, 21) {
    companion object {
        val PLAYER = RpgCharacter(100, 0, 0)
        val WEAPONS = listOf(
            Item("Dagger", 8, 4, 0),
            Item("Shortsword", 10, 5, 0),
            Item("Warhammer", 25, 6, 0),
            Item("Longsword", 40, 7, 0),
            Item("Greataxe", 74, 8, 0)
        )
        val ARMORS = listOf(
            Item("Leather", 13, 0, 1),
            Item("Chainmail", 31, 0, 2),
            Item("Splintmail", 53, 0, 3),
            Item("Bandedmail", 75, 0, 4),
            Item("Platemail", 102, 0, 5)
        )
        val RINGS = listOf(
            Item("Damage +1", 25, 1, 0),
            Item("Damage +2", 50, 2, 0),
            Item("Damage +3", 100, 3, 0),
            Item("Defense +1", 20, 0, 1),
            Item("Defense +2", 40, 0, 2),
            Item("Defense +3", 80, 0, 3)
        )
    }

    data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int)
    data class RpgCharacter(val hp: Int, val damage: Int, val armor: Int, val inv: List<Item> = emptyList()) {
        fun add(items: List<Item>) = RpgCharacter(hp, damage, armor, inv + items)
        fun totalDamage() = damage + inv.sumOf { it.damage }
        fun totalArmor() = armor + inv.sumOf { it.armor }
    }

    override fun parseInput(stringInput: List<String>) = stringInput.map { it.split(Regex(": ")) }.map { it[1].toInt() }
        .let { (hp, damage, armor) -> RpgCharacter(hp, damage, armor) }

    private fun List<Item>.pickItems(count: Int, fromIndex: Int = 0, inv: List<Item> = emptyList()): Sequence<List<Item>> =
        sequence {
            if (size - fromIndex >= count) {
                if (count > 0) {
                    yieldAll(pickItems(count - 1, fromIndex + 1, inv + this@pickItems[fromIndex]))
                    yieldAll(pickItems(count, fromIndex + 1, inv))
                } else {
                    yield(inv)
                }
            }
        }

    private fun itemSets(): Sequence<List<Item>> = sequence {
        WEAPONS.pickItems(1).forEach { weapon ->
            (0..1).asSequence().flatMap { ARMORS.pickItems(it) }.forEach { armor ->
                (0..2).asSequence().flatMap { RINGS.pickItems(it) }.forEach { rings ->
                    yield(weapon + armor + rings)
                }
            }
        }
    }

    private fun losingTurn(character: RpgCharacter, damageAgainst: Int): Int {
        val dps = maxOf(1, damageAgainst - character.totalArmor())
        return ceil(character.hp * 1.0 / dps).toInt()
    }

    override fun part1(input: RpgCharacter, testArg: Any?): Int {
        return itemSets().map { PLAYER.add(it) }.filter {
            losingTurn(it, input.totalDamage()) >= losingTurn(input, it.totalDamage())
        }.minOf { p -> p.inv.sumOf { it.cost } }
    }

    override fun part2(input: RpgCharacter, testArg: Any?): Int {
        return itemSets().map { PLAYER.add(it) }.filter {
            losingTurn(it, input.totalDamage()) < losingTurn(input, it.totalDamage())
        }.maxOf { p -> p.inv.sumOf { it.cost } }
    }
}

fun main() {
    Day21().main()
}
