package fourconnect;

public interface FourConnect {

    void setPlayerOneOnline(boolean sender) throws GameException;

    void setPlayersName(String playerName, int playerNumber) throws GameException;

    void pickColour(int playerNumber, FourConnectPieceColour colour) throws GameException;

    boolean set(int x) throws GameException;

    boolean reset(boolean sender) throws GameException;
}
