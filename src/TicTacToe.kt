import java.lang.Exception
import java.util.*
import kotlin.random.Random

class TicTacToe {
    private val EMPTY_GAME_SPACE = "#"
    private val gamePieces = mutableListOf("X", "O")
    private val sc = Scanner(System.`in`);

    private val gameBoard = mutableListOf(
        mutableListOf("#", "#", "#"),
        mutableListOf("#", "#", "#"),
        mutableListOf("#", "#", "#"))

    private fun testing() {
        val isItRight = 0 in 1..3
        println("Is 1 in 1 to 3? $isItRight")

        val isThreeIn = 4 in 1..3
        println("Is 3 in 1 to 3? $isThreeIn")
    }

    fun runGame() {
        // Initialize game
        println("Welcome to Tic-Tac-Toe!")
        println("Create your player.")

        // Initialize players
        print("Choose a username player 1: ")
        val player1 = initializePlayer()
        print("Choose a username player 2: ")
        val player2 = initializePlayer()
        val currentPlayer = Player("", "")

        // randomly choose first player
        var player1Turn = Random.nextInt(0, 100) > 50
        var gameIsActive = true

        while (gameIsActive) {
            // Switch current player
            currentPlayer.copy(if (player1Turn) player1 else player2)

            // Get validated game coordinates from user and place game piece
            val gameCoordinates = getUserInput(currentPlayer)
            placeGamePiece(gameCoordinates.firstValue, gameCoordinates.secondValue, currentPlayer.playerGamePiece)
            println("Ending turn for ${currentPlayer.playerName}")

            // Check game status (win/loss/draw)
            if (!gameStillActive(currentPlayer.playerGamePiece)) {
                println("Game Over")
                displayWinningGameBoard(currentPlayer)
                gameIsActive = false
            } else if (gameIsDraw()) {
                println("GAme is bust")
                gameIsActive = false
            }

            player1Turn = !player1Turn
        }
    }

    /**
     * Creates and returns a new Player object
     * @return the newly created Player object.
     */
    private fun initializePlayer(): Player {
        val userName = sc.nextLine() ?: "Player"
        val random = Random.nextInt(1)
        val gamePiece = gamePieces[random]
        gamePieces.removeAt(random)
        return Player(userName, gamePiece)
    }

    /**
     * Prompts the user for new coordinates until they enter a valid location.
     * @param player the current Player who is attempting to place a game piece.
     * @return a GameCoordinate with validated coordinates.
     */
    private fun getUserInput(player: Player): GameCoordinate {
        var column = 0
        var row = 0
        var coordinatesAreValid = false

        // Continues to prompt until the coordinates are valid
        while (!coordinatesAreValid) {
            println("\n${player.playerName}'s turn! (game piece is ${player.playerGamePiece}")
            displayGameBoard()
            println("Choose a location (ex: A3, B1, C2): ")

            // Remove all whitespace from the users input and split into a list of strings
            val coordinates = sc.nextLine().replace(" ", "").split("")

            // Make sure the right amount of strings are in list (first and last elements are empty)
            if (coordinates.size == 4) {
                try {
                    column = columnToPosition(coordinates[1]) - 1
                    row = coordinates[2].toInt() - 1

                    // check if position is valid
                    if (positionIsValid(column, row)) {
                        if (positionIsAvailable(column, row)) {
                            coordinatesAreValid = true
                        } else {
                            println("Error, position is not available")
                        }
                    } else {
                        println("Error, illegal location. Column must be A-C and row must be 1-3")
                    }
                } catch (exception: Exception) {
                    println("Error, wrong format for coordinate.")
                    println(exception.toString())
                }
            } else {
                println("Error, size not met.")
            }
        }

        return GameCoordinate(column, row)
    }

    private fun positionIsValid(column: Int, row: Int) =
         column in 0..2 && row in 0..2

    private fun positionIsAvailable(column: Int, row: Int) =
        gameBoard[row][column] == EMPTY_GAME_SPACE


    private fun columnToPosition(column: String) =
        when(column.toUpperCase()) {
            "A" -> 1
            "B" -> 2
            "C" -> 3
            else -> -1
        }

    private fun placeGamePiece(column: Int, row: Int, gamePiece: String) {
        gameBoard[row][column] = gamePiece
    }

    private fun gameStillActive(gamePiece: String): Boolean {
        val horizontalStrategy1 = gameBoard[0][0] == gamePiece && gameBoard[0][1] == gamePiece && gameBoard[0][2] == gamePiece
        val horizontalStrategy2 = gameBoard[1][0] == gamePiece && gameBoard[1][1] == gamePiece && gameBoard[1][2] == gamePiece
        val horizontalStrategy3 = gameBoard[2][0] == gamePiece && gameBoard[2][1] == gamePiece && gameBoard[2][2] == gamePiece

        val verticalStrategy1 = gameBoard[0][0] == gamePiece && gameBoard[1][0] == gamePiece && gameBoard[2][0] == gamePiece
        val verticalStrategy2 = gameBoard[0][1] == gamePiece && gameBoard[1][1] == gamePiece && gameBoard[2][1] == gamePiece
        val verticalStrategy3 = gameBoard[0][2] == gamePiece && gameBoard[1][2] == gamePiece && gameBoard[2][2] == gamePiece

        val diagonalStrategy1 = gameBoard[0][0] == gamePiece && gameBoard[1][1] == gamePiece && gameBoard[2][2] == gamePiece
        val diagonalStrategy2 = gameBoard[2][0] == gamePiece && gameBoard[1][1] == gamePiece && gameBoard[0][2] == gamePiece

        return !horizontalStrategy1 && !horizontalStrategy2 && !horizontalStrategy3 &&
                !verticalStrategy1 && !verticalStrategy2 && !verticalStrategy3 &&
                !diagonalStrategy1 && !diagonalStrategy2
    }

    private fun gameIsDraw(): Boolean {
        gameBoard.forEach { row ->
            row.forEach { coordinate ->
                if (coordinate == EMPTY_GAME_SPACE)
                    return false
            }
        }

        return true
    }



    private fun displayGameBoard() {
        var rowHeaders = 1

        for (row in gameBoard.indices) {
            if (row == 0) {
                print("  A B C\n")
            }
            print("${rowHeaders++} ")

            for (column in gameBoard[row].indices) {
                print("${gameBoard[row][column]} ")
            }
            println()
        }
    }

    private fun displayWinningGameBoard(winningPlayer: Player) {
        for (i in 0..16) {
            print("!-!")
            Thread.sleep(100)
        }
        println("          *                                             *\n" +
                "                                               *\n" +
                "                    *\n" +
                "                                  *\n" +
                "                                                            *\n" +
                "         *\n" +
                "                                                  *\n" +
                "             *\n" +
                "                           *             *\n" +
                "                                                     *\n" +
                "      *                                                               *\n" +
                "               *\n" +
                "                               (             )\n" +
                "                       )      (*)           (*)      (\n" +
                "              *       (*)      |             |      (*)\n" +
                "                       |      |~|           |~|      |          *\n" +
                "                      |~|     | |           | |     |~|\n" +
                "                      | |     | |           | |     | |\n" +
                "                     ,| |a@@@@| |@@@@@@@@@@@| |@@@@a| |.\n" +
                "                .,a@@@| |@@@@@| |@@@@@@@@@@@| |@@@@@| |@@@@a,.\n" +
                "              ,a@@@@@@| |@@@@@@@@@@@@.@@@@@@@@@@@@@@| |@@@@@@@a,\n" +
                "             a@@@@@@@@@@@@@@@@@@@@@' . `@@@@@@@@@@@@@@@@@@@@@@@@a\n" +
                "             ;`@@@@@@@@@@@@@@@@@@'   .   `@@@@@@@@@@@@@@@@@@@@@';\n" +
                "             ;@@@`@@@@@@@@@@@@@'     .     `@@@@@@@@@@@@@@@@'@@@;\n" +
                "             ;@@@;,.aaaaaaaaaa       .       aaaaa,,aaaaaaa,;@@@;\n" +
                "             ;;@;;;;@@@@@@@@;@      @.@      ;@@@;;;@@@@@@;;;;@@;\n" +
                "             ;;;;;;;@@@@;@@;;@    @@ . @@    ;;@;;;;@@;@@@;;;;;;;\n" +
                "             ;;;;;;;;@@;;;;;;;  @@   .   @@  ;;;;;;;;;;;@@;;;;@;;\n" +
                "             ;;;;;;;;;;;;;;;;;@@     .     @@;;;;;;;;;;;;;;;;@@@;\n" +
                "         ,%%%;;;;;;;;@;;;;;;;;       .       ;;;;;;;;;;;;;;;;@@;;%%%,\n" +
                "      .%%%%%%;;;;;;;@@;;;;;;;;     ,%%%,     ;;;;;;;;;;;;;;;;;;;;%%%%%%,\n" +
                "     .%%%%%%%;;;;;;;@@;;;;;;;;   ,%%%%%%%,   ;;;;;;;;;;;;;;;;;;;;%%%%%%%,\n" +
                "     %%%%%%%%`;;;;;;;;;;;;;;;;  %%%%%%%%%%%  ;;;;;;;;;;;;;;;;;;;'%%%%%%%%\n" +
                "     %%%%%%%%%%%%`;;;;;;;;;;;;,%%%%%%%%%%%%%,;;;;;;;;;;;;;;;'%%%%%%%%%%%%\n" +
                "     `%%%%%%%%%%%%%%%%%,,,,,,,%%%%%%%%%%%%%%%,,,,,,,%%%%%%%%%%%%%%%%%%%%'\n" +
                "       `%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%'\n" +
                "           `%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%'\n" +
                "                  \"\"\"\"\"\"\"\"\"\"\"\"\"\"`,,,,,,,,,'\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\n" +
                "                                 `%%%%%%%'\n" +
                "                                  `%%%%%'\n" +
                "                                    %%%     \n" +
                "                                   %%%%%\n" +
                "                                .,%%%%%%%,.\n" +
                "                           ,%%%%%%%%%%%%%%%%%%%,\n" +
                "\n" +
                "------------------------------------------------\n")
        println("Congratulations ${winningPlayer.playerName}, you've won!\n")
        displayGameBoard()
        Thread.sleep(400)

        for (i in 0..16) {
            print("!-!")
            Thread.sleep(100)
        }
    }
}