package aoc2015

import AdventDay
import java.util.PriorityQueue

class Day22 : AdventDay<Day22.Boss, Int, Int>(2015, 22) {
    enum class Spell(val cost: Int) { MAGIC_MISSILE(53), DRAIN(73), SHIELD(113), POISON(173), RECHARGE(229) }
    data class Boss(val hp: Int, val damage: Int)

    override fun parseInput(stringInput: List<String>) =
        stringInput.map { it.split(Regex(": ")) }.map { it[1].toInt() }.let { (hp, damage) -> Boss(hp, damage) }

    data class GameState(
        val bossHp: Int,
        val playerHp: Int,
        val playerMana: Int = 500,
        val manaSpent: Int = 0,
        val effects: Map<Spell, Int> = emptyMap()
    ) : Comparable<GameState> {
        override fun compareTo(other: GameState): Int = this.manaSpent.compareTo(other.manaSpent)
    }

    private fun findMinMana(startingHp: Int, boss: Boss, autoDamageToPlayer: Int = 0): Int {
        val toExplore = PriorityQueue(listOf(GameState(boss.hp, startingHp)))
        while (toExplore.isNotEmpty()) {
            val cur = toExplore.remove()
            Spell.values().filter { it.cost <= cur.playerMana && !cur.effects.containsKey(it) }.forEach { spell ->
                val newManaSpent = cur.manaSpent + spell.cost
                var newMana = cur.playerMana - spell.cost
                var newBossHp = cur.bossHp
                var newPlayerHp = cur.playerHp
                var effects = cur.effects
                // Cast spell
                when (spell) {
                    Spell.MAGIC_MISSILE -> newBossHp -= 4
                    Spell.DRAIN -> {
                        newBossHp -= 2
                        newPlayerHp += 2
                    }

                    Spell.SHIELD -> effects = effects + (Spell.SHIELD to 6)
                    Spell.POISON -> effects = effects + (Spell.POISON to 6)
                    Spell.RECHARGE -> effects = effects + (Spell.RECHARGE to 5)
                }
                fun Map<Spell, Int>.apply(): Map<Spell, Int> = mapNotNull { (s, timer) ->
                    when (s) {
                        Spell.POISON -> newBossHp -= 3
                        Spell.RECHARGE -> newMana += 101
                        else -> {}
                    }
                    if (timer > 1) s to timer - 1 else null
                }.associate { it }
                // Boss turn
                effects = effects.apply()
                if (newBossHp <= 0) {
                    return newManaSpent
                }
                val magicArmor = if (effects.containsKey(Spell.SHIELD)) 7 else 0
                newPlayerHp -= maxOf(1, boss.damage - magicArmor)
                // Player turn (until action)
                newPlayerHp -= autoDamageToPlayer
                if (newPlayerHp > 0) {
                    effects = effects.apply()
                    if (newBossHp <= 0) {
                        return newManaSpent
                    }
                    toExplore.offer(GameState(newBossHp, newPlayerHp, newMana, newManaSpent, effects))
                }
            }
        }
        error("Player cannot win")
    }

    override fun part1(input: Boss, testArg: Any?) = findMinMana(50, input)
    override fun part2(input: Boss, testArg: Any?) = findMinMana(49, input, 1)
}

fun main() {
    Day22().main()
}
