package fourconnect;

public class Players {

    private String playerOneName = "Player 1";
    private String playerTwoName = "Player 2";
    private FourConnectPieceColour playerOneColour;
    private FourConnectPieceColour playerTwoColour;
    private int myPlayerNumber = 1;


    public void setPlayerOne(boolean sender){
        if(sender)
            myPlayerNumber = 1;
        else
            myPlayerNumber = 2;
    }

    public int getMyPlayerNumber(){
        return myPlayerNumber;
    }

    public int getOtherPlayerNumber(){
        if(myPlayerNumber == 1)
            return 2;
        else
            return 1;
    }

    public void setPlayersName(String playerName, int playerNumber)throws GameException{
        if(playerNumber == 1)
            playerOneName = playerName;
        else if(playerNumber == 2)
            playerTwoName = playerName;
        else
            throw new GameException("invalid player number");
    }

    public String getPlayersName(int playerNumber)throws GameException{
        if(playerNumber == 1)
            return playerOneName;
        else if(playerNumber == 2)
            return playerTwoName;
        else
            throw new GameException("invalid player number");
    }

    public void pickColour(int playerNumber, FourConnectPieceColour Colour) throws GameException {
        if(playerNumber == 1) {
            playerOneColour = Colour;
            if(Colour == FourConnectPieceColour.RED)
                playerTwoColour = FourConnectPieceColour.BLUE;
            else
                playerTwoColour = FourConnectPieceColour.RED;
        }
        else if(playerNumber == 2) {
            playerTwoColour = Colour;
            if(Colour == FourConnectPieceColour.RED)
                playerOneColour = FourConnectPieceColour.BLUE;
            else
                playerOneColour = FourConnectPieceColour.RED;
        }
        else
            throw new GameException("invalid player number");
    }

    public FourConnectPieceColour getColourByPlayerNumber(int playerNumber) throws GameException {
        if(playerNumber == 1)
            return playerOneColour;
        else if(playerNumber == 2)
            return playerTwoColour;
        else
            throw new GameException("invalid player number");
    }
}
