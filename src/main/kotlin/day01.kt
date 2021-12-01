fun day01_puzzle1() {
    val input = listOf(
        199,
        200,
        208,
        210,
        200,
        207,
        240,
        269,
        260,
        263
    )
    val increments = input.windowed(2).filter { it[0] < it[1] }.size
    println("Day  1, Puzzle 1: $increments increments")
}
