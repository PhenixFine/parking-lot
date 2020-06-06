package old

import java.util.*

object ParkingLot {
    private lateinit var spotEmpty: Array<Boolean>
    private lateinit var carColor: Array<String>
    private lateinit var carID: Array<String>

    private fun errorBounds() = println("was not given enough info, please try again")

    private fun errorNumber(string: String) = println("${string[1]} is not a number")

    // find: 0 = status, 1 = reg_by_color, 2 = spot_by_color, 3 = spot_by_reg
    private fun info(find: Int, search: String) {
        var results = ""

        for (index in spotEmpty.indices) {
            if (!spotEmpty[index]) {
                when (find) {
                    0 -> results += "${index + 1} ${carID[index]} ${carColor[index]}\n"
                    1 -> if (carColor[index].toLowerCase() == search.toLowerCase()) results += "${carID[index]} "
                    2 -> if (carColor[index].toLowerCase() == search.toLowerCase()) results += "${index + 1} "
                    3 -> if (carID[index] == search) results += "${index + 1} "
                }
            }
        }
        if (results == "") {
            println(
                when (find) {
                    0 -> "Parking lot is empty."
                    in 1..2 -> "No cars with color $search were found."
                    else -> "No cars with registration number $search were found."
                }
            )
        } else println(
            if (find == 0) results.trim() else results.trim().splitToSequence(" ").toList()
                .toString().replace("[", "").replace("]", "")
        )
    }

    private fun isNull(string: Array<String>, num1: Int = 0, num2: Int = 1): Boolean {
        return string.getOrNull(num1) == null || string.getOrNull(num2) == null
    }

    private fun isNumber(number: String) = number.toIntOrNull() != null

    private fun leave(number: String) {
        if (isNumber(number)) {
            if (number.toInt() <= spotEmpty.size) {
                if (spotEmpty[number.toInt() - 1]) {
                    println("There is no car in spot $number.")
                } else {
                    println("Spot $number is free.")
                    spotEmpty[number.toInt() - 1] = true
                }
            } else println("there is no spot $number.")
        } else errorNumber(number)
    }

    private fun park(color: String, id: String) {
        for (index in spotEmpty.indices) {
            if (spotEmpty[index]) {
                println("$color car parked in spot ${index + 1}.")
                spotEmpty[index] = false
                carColor[index] = color
                carID[index] = id
                return
            }
        }
        println("Sorry, the parking lot is full.")
    }

    private fun reset(number: String) {
        if (isNumber(number)) {
            spotEmpty = Array(number.toInt()) { true }
            carColor = Array(number.toInt()) { "" }
            carID = Array(number.toInt()) { "" }
            println("Created a parking lot with $number spots.")
        } else errorNumber(number)
    }

    fun service(string: Array<String>) {
        if (!this::spotEmpty.isInitialized) {
            when (string[0]) {
                "create" -> if (isNull(string)) errorBounds() else reset(string[1])
                else -> println("Sorry, a parking lot has not been created.")
            }
        } else {
            when (string[0]) {
                "park" -> if (isNull(string, 1, 2)) errorBounds() else park(string[2], string[1])
                "leave" -> if (isNull(string)) errorBounds() else leave(string[1])
                "status" -> info(0, "")
                "reg_by_color" -> if (isNull(string)) errorBounds() else info(1, string[1])
                "spot_by_color" -> if (isNull(string)) errorBounds() else info(2, string[1])
                "spot_by_reg" -> if (isNull(string)) errorBounds() else info(3, string[1])
                "create" -> if (isNull(string)) errorBounds() else reset(string[1])
            }
        }
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    var string = scanner.nextLine().split(" ").toTypedArray()

    while (string[0] != "exit") {
        ParkingLot.service(string)
        string = scanner.nextLine().split(" ").toTypedArray()
    }
}