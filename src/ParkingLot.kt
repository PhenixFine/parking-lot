import java.util.*

object ParkingLot {
    private lateinit var SPOT_EMPTY: Array<Boolean>
    private lateinit var CAR_COLOR: Array<String>
    private lateinit var CAR_ID: Array<String>

    fun service(string: Array<String>) {
        if (!this::SPOT_EMPTY.isInitialized) {
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
                "help" -> help()
            }
        }
    }

    private fun reset(number: String) {
        if (isNumber(number)) {
            SPOT_EMPTY = Array(number.toInt()) { true }
            CAR_COLOR = Array(number.toInt()) { "" }
            CAR_ID = Array(number.toInt()) { "" }
            println("Created a parking lot with $number spots.")
        } else errorNumber(number)
    }

    private fun park(color: String, id: String) {
        for (index in SPOT_EMPTY.indices) {
            if (!SPOT_EMPTY[index]) {
                if (CAR_ID[index] == id) {
                    println("Please enter a unique ID for the car.")
                    return
                }
            }
        }
        for (index in SPOT_EMPTY.indices) {
            if (SPOT_EMPTY[index]) {
                println("$color car parked in spot ${index + 1}.")
                SPOT_EMPTY[index] = false
                CAR_COLOR[index] = color
                CAR_ID[index] = id
                return
            }
        }
        println("Sorry, the parking lot is full.")
    }

    private fun leave(number: String) {
        if (isNumber(number)) {
            if (number.toInt() <= SPOT_EMPTY.size) {
                if (SPOT_EMPTY[number.toInt() - 1]) println("There is no car in spot $number.") else {
                    println("Spot $number is free.")
                    SPOT_EMPTY[number.toInt() - 1] = true
                }
            } else println("there is no spot $number.")
        } else errorNumber(number)
    }

    // find: 0 = status, 1 = reg_by_color, 2 = spot_by_color, 3 = spot_by_reg
    private fun info(find: Int, search: String) {
        var results = ""

        for (index in SPOT_EMPTY.indices) {
            if (!SPOT_EMPTY[index]) {
                when (find) {
                    0 -> results += "${index + 1} ${CAR_ID[index]} ${CAR_COLOR[index]}\n"
                    1 -> if (CAR_COLOR[index].toLowerCase() == search.toLowerCase()) results += "${CAR_ID[index]} "
                    2 -> if (CAR_COLOR[index].toLowerCase() == search.toLowerCase()) results += "${index + 1} "
                    3 -> if (CAR_ID[index] == search) results += "${index + 1} "
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
            if (find == 0 || find == 3) results.trim() else results.trim().splitToSequence(" ").toList()
                .toString().replace("[", "").replace("]", "")
        )
    }

    private fun help() {
        println(
            """
            park - parks a car. Please enter a unique car ID and the color of the car.
                Example: "park KA-01-HH-9999 Red"
            leave - removes a parked car from a numbered spot.
                Example: "leave 2"
            status - shows spot number, car id, and color of any parked cars
            reg_by_color - prints all registration numbers of cars of a particular color, taking color as a parameter.
                Example: "reg_by_color red"
            spot_by_color - prints the parking space numbers of all the cars of a particular color.
                Example: "spot_by_color red"
            spot_by_reg - returns the number of the spot where a car is located based on its registration number
                Example: "spot_by_reg KA-01-HH-9999"
            create - creates a new parking lot according to size received and erases previous parking lot
                Example: "create 20" 
            exit - exits the program
        """.trimIndent()
        )
    }

    private fun errorBounds() = println("was not given enough info, please try again")

    private fun errorNumber(string: String) = println("${string[1]} is not a number")

    private fun isNull(string: Array<String>, num1: Int = 0, num2: Int = 1): Boolean {
        return string.getOrNull(num1) == null || string.getOrNull(num2) == null
    }

    private fun isNumber(number: String) = number.toIntOrNull() != null
}

fun main() {
    val scanner = Scanner(System.`in`)
    print("Please enter the number of parking spaces for your parking lot: ")
    var string = ("create " + scanner.nextLine()).split(" ").toTypedArray()

    do {
        ParkingLot.service(string)
        print("Please enter a new command (help - for a list): ")
        string = scanner.nextLine().split(" ").toTypedArray()
    } while (string[0] != "exit")
}