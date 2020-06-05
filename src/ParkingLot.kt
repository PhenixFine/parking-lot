import java.util.*

object ParkingLot {
    private lateinit var spotEmpty: Array<Boolean>
    private lateinit var carColor: Array<String>
    private lateinit var carID: Array<String>

    private fun errorBounds() {
        println("was not given enough info, please try again")
    }

    private fun errorNumber(string: String) {
        println("${string[1]} is not a number")
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

    // find: 0 = status, 1 = reg_by_color, 2 = spot_by_color, 3 = spot_by_reg
    private fun info(find: Int, search: String) {
        var results = ""

        for (index in spotEmpty.indices) {
            if (!spotEmpty[index]) {
                when (find) {
                    0 -> results += "${index + 1} ${carID[index]} ${carColor[index]}\n"
                    1 -> if (carColor[index].toLowerCase() == search.toLowerCase()) results += "${carID[index]} "
                    in 2..3 -> if (carColor[index].toLowerCase() == search.toLowerCase() || carID[index] == search)
                        results += "${index + 1} "
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

    private fun isNumber(number: String): Boolean {
        return number.toIntOrNull() != null
    }

    private fun leave(number: String) {
        if (isNumber(number)) {
            if (number.toInt() <= spotEmpty.size) {
                if (spotEmpty[number.toInt() - 1]) println("There is no car in spot $number.") else {
                    println("Spot $number is free.")
                    spotEmpty[number.toInt() - 1] = true
                }
            } else println("there is no spot $number.")
        } else errorNumber(number)
    }

    private fun park(color: String, id: String) {
        for (index in spotEmpty.indices) {
            if (!spotEmpty[index]) {
                if (carID[index] == id) {
                    println("Please enter a unique ID for the car.")
                    return
                }
            }
        }
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
                "create" -> if (string.getOrNull(1) == null) errorBounds() else reset(string[1])
                else -> println("Sorry, a parking lot has not been created.")
            }
        } else {
            when (string[0]) {
                "park" -> if (string.getOrNull(1) == null || string.getOrNull(2) == null) errorBounds()
                else park(string[2], string[1])
                "leave" -> if (string.getOrNull(1) == null) errorBounds() else leave(string[1])
                "status" -> info(0, "")
                "reg_by_color" -> if (string.getOrNull(1) == null) errorBounds() else info(1, string[1])
                "spot_by_color" -> if (string.getOrNull(1) == null) errorBounds() else info(2, string[1])
                "spot_by_reg" -> if (string.getOrNull(1) == null) errorBounds() else info(3, string[1])
                "create" -> if (string.getOrNull(1) == null) errorBounds() else reset(string[1])
                "help" -> help()
            }
        }
    }
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