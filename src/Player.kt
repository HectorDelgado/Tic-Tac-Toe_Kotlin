class Player(var playerName: String, var playerGamePiece: String) {
    fun copy(player: Player) {
        playerName = player.playerName
        playerGamePiece = player.playerGamePiece
    }

}