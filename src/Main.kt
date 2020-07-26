import utility.*

fun main() {
    val parkingLot = ParkingLot()
    val create = "Please enter the number of parking spaces for your parking lot: "
    val commandText = "Please enter a new command (help - for a list): "
    var command = arrayOf("create", getNum(create).toString())

    do {
        parkingLot.service(command)
        command = splitToArray(getString(commandText))
    } while (command[0] != "exit")
}