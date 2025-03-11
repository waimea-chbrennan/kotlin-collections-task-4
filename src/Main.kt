import kotlin.math.max

/**
 * Kotlin Collections Task 4- Automation
 *
 *              OOOOO  OOO   OOO
 *                 O  O   O O   O
 *                O   O   O O   O
 *              OOOOO  OOO   OOO
 * +-------------+-------------+-------------+
 * |    __v__    |             |             |
 * |v__( o o )   |             |             |
 * | o )(---)__v_|   EMPTY?!   |   EMPTY?!   |
 * |--) __|_( o o|             |             |
 * ||  /|. .|\---|             |             |
 * +-------------+-------------+-------------+
 *
 * Well... Even with your awesome error-checking in the
 * monkey cage system, it seems that the zookeepers still
 * can't manage the monkeys properly...
 *
 * Monkeys are ending up in the wrong cages, some are
 * squashed three-to-a-cage. It's just a mess!
 *
 * We need you to automate the cage system so that the
 * zookeepers don't have to make decisions.
 */


/**
 * Constant vales used to define some key values
 * which can then be used throughout the code...
 */
const val NUMCAGES = 8      // The total number of cages
const val EMPTY = "---"     // Represents an empty cage


/**
 * Program entry point
 */
fun main() {
    //-------------------------------------------------
    println("Setting up the cages...")

    val cages = setupCages()
    println()

    //-------------------------------------------------
    println("Placing existing monkeys into cages...")

    placeMonkeyInCage(cages, 1, "Kevin")
    placeMonkeyInCage(cages, 8, "Sally")
    placeMonkeyInCage(cages, 3, "Pam")
    println()

    showMonkeyCages(cages)
    println()

    check(cages == listOf("Kevin", EMPTY, "Pam", EMPTY, EMPTY, EMPTY, EMPTY, "Sally"))

    //-------------------------------------------------
    println("Automatically placing some new monkeys...")

    val nigelCage = placeMonkey(cages, "Nigel")
    showMonkeyCages(cages)
    check(nigelCage == 2)
    check(cages == listOf("Kevin", "Nigel", "Pam", EMPTY, EMPTY, EMPTY, EMPTY, "Sally"))

    val jimCage = placeMonkey(cages, "Jim")
    showMonkeyCages(cages)
    check(jimCage == 4)
    check(cages == listOf("Kevin", "Nigel", "Pam", "Jim", EMPTY, EMPTY, EMPTY, "Sally"))

    val kimCage = placeMonkey(cages, "Kim")
    showMonkeyCages(cages)
    check(kimCage == 5)
    check(cages == listOf("Kevin", "Nigel", "Pam", "Jim", "Kim", EMPTY, EMPTY, "Sally"))

    val daveCage = placeMonkey(cages, "Dave")
    showMonkeyCages(cages)
    check(daveCage == 6)
    check(cages == listOf("Kevin", "Nigel", "Pam", "Jim", "Kim", "Dave", EMPTY, "Sally"))

    val samCage = placeMonkey(cages, "Sam")
    showMonkeyCages(cages)
    check(samCage == 7)
    check(cages == listOf("Kevin", "Nigel", "Pam", "Jim", "Kim", "Dave", "Sam", "Sally"))

    //-------------------------------------------------
    println("Trying an invalid automatic placement (no room)...")

    val leoCage = placeMonkey(cages, "Leo")
    showMonkeyCages(cages)
    check(leoCage == -1)
    check(cages == listOf("Kevin", "Nigel", "Pam", "Jim", "Kim", "Dave", "Sam", "Sally"))

    println()

    //-------------------------------------------------
    println("Making some room for violent monkeys...")

    clearCage(cages, 1)
    clearCage(cages, 2)
    clearCage(cages, 5)
    clearCage(cages, 6)
    clearCage(cages, 7)
    clearCage(cages, 8)
    showMonkeyCages(cages)
    check(cages == listOf(EMPTY, EMPTY, "Pam", "Jim", EMPTY, EMPTY, EMPTY, EMPTY))

    println()

    //-------------------------------------------------
    println("Placing some violent monkeys...")

    val timCage = placeViolentMonkey(cages, "Tim")
    showMonkeyCages(cages)
    check(timCage == 1)
    check(cages == listOf("!Tim", EMPTY, "Pam", "Jim", EMPTY, EMPTY, EMPTY, EMPTY))

    val kellyCage = placeViolentMonkey(cages, "Kelly")
    showMonkeyCages(cages)
    check(kellyCage == 6)
    check(cages == listOf("!Tim", EMPTY, "Pam", "Jim", EMPTY, "!Kelly", EMPTY, EMPTY))

    val wandaCage = placeViolentMonkey(cages, "Wanda")
    showMonkeyCages(cages)
    check(wandaCage == 8)
    check(cages == listOf("!Tim", EMPTY, "Pam", "Jim", EMPTY, "!Kelly", EMPTY, "!Wanda"))

    println()

    //-------------------------------------------------
    println("Trying an invalid violent monkey placement (no room)...")

    val vinceCage = placeViolentMonkey(cages, "Vince")
    showMonkeyCages(cages)
    check(vinceCage == -1)
    check(cages == listOf("!Tim", EMPTY, "Pam", "Jim", EMPTY, "!Kelly", EMPTY, "!Wanda"))

    println()

    //-------------------------------------------------
    println("Making some room for normal monkeys...")

    clearCage(cages, 6)
    showMonkeyCages(cages)
    check(cages == listOf("!Tim", EMPTY, "Pam", "Jim", EMPTY, EMPTY, EMPTY, "!Wanda"))

    println()

    //-------------------------------------------------
    println("Trying to place normal monkey...")

    val terryCage = placeMonkey(cages, "Terry")
    showMonkeyCages(cages)
    check(terryCage == 5)
    check(cages == listOf("!Tim", EMPTY, "Pam", "Jim", "Terry", EMPTY, EMPTY, "!Wanda"))

    val garyCage = placeMonkey(cages, "Gary")
    showMonkeyCages(cages)
    check(garyCage == 6)
    check(cages == listOf("!Tim", EMPTY, "Pam", "Jim", "Terry", "Gary", EMPTY, "!Wanda"))

    println()

    //-------------------------------------------------
    println("Trying to invalidly place normal monkey (no free cages not next to violent ones)...")

    val tinaCage = placeMonkey(cages, "Tina")
    showMonkeyCages(cages)
    check(tinaCage == -1)
    check(cages == listOf("!Tim", EMPTY, "Pam", "Jim", "Terry", "Gary", EMPTY, "!Wanda"))

    println()
}


/**
 * Places a monkey in the first free, empty cage (starting from 1)
 * - If the monkey is placed successfully, returns the cage number
 * - If all cages are occupied, returns -1
 */
fun placeMonkey(cageList: MutableList<String>, name: String): Int {
    println("+++ Putting $name into a cage".green().bold())
    for((index,occupant) in cageList.withIndex()) {
        if(occupant!=EMPTY) continue

        //Make sure that we dont place next to a Violent monkey either side
        if(cageList.getOrElse(index-1){""}.startsWith('!')) continue
        if(cageList.getOrElse(index+1){""}.startsWith('!')) continue

        placeMonkeyInCage(cageList,index+1,name)
        println("+++ Automatically placed $name in cage ${index+1}".green())
        return index+1
    }

    println("+++ No room to place monkey".yellow())
    return -1

}


/**
 * Violent monkeys cannot be placed next to other monkeys!
 *
 * Places a monkey in the first free, empty cage (starting from 1)
 * which has empty cages either side, or is an end cage next to an
 * empty cage.
 *
 * Violent monkeys have an '!' added to start of their name
 *
 * - If the monkey is placed successfully, returns the cage number
 * - If all cages are occupied, returns -1
 */
fun placeViolentMonkey(cageList: MutableList<String>, name: String): Int {
    println("+++ Putting !$name (VIOLENT!) into a cage".red().bold())
    //Iterate over list and check each cage for criteria

    for(index in cageList.indices) {
        if(cageList[index]!=EMPTY) continue

        //Ensure that neighboring cages are empty or not present
        if(cageList.getOrElse(index-1){EMPTY}!=EMPTY) continue
        if(cageList.getOrElse(index+1){EMPTY}!=EMPTY) continue

        placeMonkeyInCage(cageList,index+1,"!$name")
        println("+++ Automatically placed !$name (VIOLENT) in cage ${index+1}".red())
        return index + 1
    }

    println("+++ No room to place monkey".yellow())
    return -1
}


/**
 * ========================================================
 * FUNCTIONS BELOW ARE PROVIDED FOR FREE!
 * ========================================================
 * The functions below are similar to the ones you have
 * already developed over the previous tasks
 * ========================================================
 */


/**
 * Creates and returns a Mutable List, size NUMCAGES,
 * populated with strings representing empty cages
 */
fun setupCages(): MutableList<String> {
    val cageList = mutableListOf<String>()
    for (i in 1..NUMCAGES) cageList.add(EMPTY)
    println(">>> Created $NUMCAGES empty cages".blue())
    return cageList
}


/**
 * Put a given monkey into the specified cage number (1...MAX)
 */
fun placeMonkeyInCage(cageList: MutableList<String>, cageNum: Int, name: String) {
    // Check for invalid cage number
    if (cageNum !in 1..NUMCAGES) return
    // Check for blank name
    if (name.isBlank()) return
    // Ok to go ahead and place the monkey
    println("+++ Placing $name into cage $cageNum".italic())
    cageList[cageNum - 1] = name
}


/**
 * Show all cages from the given list, formatted as a horizontal table:
 *
 * +--------+--------+--------+--------+----
 * | Cage 1 | Cage 2 | Cage 3 | Cage 4 | Etc.
 * +--------+--------+--------+--------+----
 * | Kevin  | ---    | Samson | Pam    | Etc.
 * +--------+--------+--------+--------+----
 */
fun showMonkeyCages(cageList: List<String>) {
    var banner = "+"
    var namesAndPipes = "|"
    var cagesAndPipes = "|"

    val longestEntry = max(cageList.maxBy{it.length}.length, "Cage $NUMCAGES".length)

    cageList.forEachIndexed { index, cageOccupant ->
        //Prepare cage num | Cage 1 | Cage 2| ...
        val cageDescriptor = "Cage ${index+1}"
        cagesAndPipes += " ${cageDescriptor.padEnd(longestEntry, ' ')} |"

        //Prepare banner +--------+--------+
        banner += "-".repeat(longestEntry + 2) + "+"

        //Prepare names | monkey | monkey | monkey |
        //Violent monkeys are highlighted in red
        namesAndPipes += if(cageOccupant.startsWith('!')) {
            " ${cageOccupant.padEnd(longestEntry, ' ')}".red().bold() + " |".magenta()
        } else {
            " ${cageOccupant.padEnd(longestEntry, ' ')} |".magenta()
        }
    }

    println("${banner.magenta()}\n${cagesAndPipes.magenta()}\n${banner.magenta()}\n${namesAndPipes.magenta()}\n${banner.magenta()}\n")
}


/**
 * Make a given cage empty (if a monkey was in it, it's gone now!)
 */
fun clearCage(cageList: MutableList<String>, cageNum: Int) {
    // Check for invalid cage num
    if (cageNum !in 1..cageList.size) return
    // Ok to clear the cage
    println("--- Clearing cage $cageNum".blue())
    cageList[cageNum - 1] = EMPTY
}