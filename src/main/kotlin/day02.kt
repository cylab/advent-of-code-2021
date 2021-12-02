import java.lang.ClassLoader.getSystemResource
import java.lang.Integer.parseInt

private val day2Sample get() = parse("day02_sample.txt")
private val day2Input get() = parse("day02_input.txt")


fun day02Puzzle1() {
    println("Day  2, Puzzle 1: ${day2Input.combinedPosAndDepth()} combined steering")
}

fun day02Puzzle2() {
    println("Day  2, Puzzle 2: ${day2Input.combinedPosAndAim()} combined steering")
}


private fun List<Pair<String, Int>>.combinedPosAndDepth() = this
    .fold(listOf(0, 0)) { (pos, depth), (dir, amount) ->
        when(dir) {
            "down" -> listOf(pos, depth + amount)
            "up" -> listOf(pos, depth - amount)
            "forward" -> listOf(pos + amount, depth)
            else -> listOf(pos, depth) // ignore unknown directions
        }
    }
    .let { (pos, depth) -> pos * depth }


private fun List<Pair<String, Int>>.combinedPosAndAim() = this
    .fold(listOf(0, 0, 0)) { (pos, depth, aim), (dir, amount) ->
        when(dir) {
            "down" -> listOf(pos, depth, aim + amount)
            "up" -> listOf(pos, depth, aim - amount)
            "forward" -> listOf(pos + amount, depth + (aim * amount), aim)
            else -> listOf(pos, depth, aim) // ignore unknown directions
        }
    }
    .let { (pos, depth) -> pos * depth }


private fun parse(resource: String) = getSystemResource(resource)
    .readText()
    .lines()
    .filter { it.isNotBlank() }
    .map { it.split(" ") }
    .map { (dir, amount) -> dir to parseInt(amount) }

