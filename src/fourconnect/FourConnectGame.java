package fourconnect;

import java.util.ArrayList;

public class FourConnectGame implements FourConnect {

    private Matchfield matchfield =new Matchfield();
    private boolean myTurn = true;
    Players players = new Players();
    private int scoreboard[] = new int[2];
    boolean computerEnabled;
    boolean onDeviceEnabled;
    ComputerPlayer comp;
    private FourConnectProtocolEngine protocolEngine;

    public FourConnectGame(boolean online){
        if(online)
            onDeviceEnabled = false;
        else
            onDeviceEnabled = true;
    }

    public FourConnectGame(int difficulty){
        computerEnabled = true;
        this.comp = new ComputerPlayer(difficulty);
    }

    @Override
    public void setPlayerOneOnline(boolean sender) throws GameException {
        if(sender)
            players.setPlayerOne(true);
        else
            players.setPlayerOne(false);

        myTurn = sender;

    }

    @Override
    public void setPlayersName(String playerName, int playerNumber) throws GameException {
        players.setPlayersName(playerName, playerNumber);
    }

    public String getPlayersname(int playernumber) throws GameException {
        return players.getPlayersName(playernumber);
    }

    @Override
    public void pickColour(int playerNumber, FourConnectPieceColour colour) throws GameException {
        players.pickColour(playerNumber, colour);
    }

    public FourConnectPieceColour getColourByPlayerNumber(int playerNumber) throws GameException {
        return players.getColourByPlayerNumber(playerNumber);
    }

    public int getScoreboard(int playernumber){
        return scoreboard[playernumber-1];
    }

    @Override
    public boolean set(int x) throws GameException{
        boolean setdone = false;

        if(computerEnabled){
            for (int y = 0; y < 6; y++)
                if (matchfield.getFieldValue(x, y) == 0) {
                    matchfield.setFieldValue(x, y, players.getMyPlayerNumber());
                    setdone = true; break;
                }
            if(setdone){
                x = comp.computerSet();
                for (int y = 0; y < 6; y++)
                    if (matchfield.getFieldValue(x, y) == 0) {
                        matchfield.setFieldValue(x, y, players.getOtherPlayerNumber());
                        break;
                    }
            }
        }
        else {
            for (int y = 0; y < 6; y++) {
                if (matchfield.getFieldValue(x, y) == 0) {
                    if (myTurn)
                        matchfield.setFieldValue(x, y, players.getMyPlayerNumber());
                    else
                        matchfield.setFieldValue(x, y, players.getOtherPlayerNumber());
                    setdone = true;
                    break;
                }
            }
            if(setdone)
                playerswitch();
            else
                throw new GameException("Spalte voll");
        }

        if(win())
            return true;
        else
            return false;
    }

    public void playerswitch(){
        if(myTurn)
            myTurn = false;
        else
            myTurn = true;
    }

    @Override
    public boolean reset(boolean sender) throws GameException {
        resetMatchfield();
        scoreboard[0] = 0;
        scoreboard[1] = 0;

        if(sender)
            myTurn = true;
        else
            myTurn = false;

        return true;
    }

    public void resetMatchfield() throws GameException {
        for(int x = 0; x < 7; x++)
            for(int y = 0; y < 6; y++)
                matchfield.setFieldValue(x,y,0);
    }

    public boolean win() throws GameException {
        if(winHorizontal() || winVertical() || winDiagonal())
            return true;
        else
            return false;
    }

    public boolean winHorizontal() throws GameException {
        for(int x = 0; x < 4; x++)
            for(int y = 0; y < 6; y++)
                if (fourInARow(x,y, 1, 0))
                    return true;

        return false;
    }

    public boolean winVertical() throws GameException {
        for(int x = 0; x < 7; x++)
            for(int y = 0; y < 3; y++)
                if (fourInARow(x,y, 0, 1))
                    return true;
        return false;
    }

    public boolean winDiagonal() throws GameException {
        for(int x = 0; x < 4; x++)
            for(int y = 0; y < 3; y++) {
                if (fourInARow(x,y, 1, 1))
                    return true;
            }

        for(int x = 6; x > 2; x--)
            for(int y = 2; y > -1; y--)
                if (fourInARow(x,y, -1, 1))
                    return true;
        return false;
    }

    public boolean fourInARow(int x, int y, int operatorX, int operatorY) throws GameException {
        if (matchfield.getFieldValue(x, y) != 0 &&
                matchfield.getFieldValue(x, y) == matchfield.getFieldValue(x + 1*operatorX, y + 1*operatorY) &&
                matchfield.getFieldValue(x, y) == matchfield.getFieldValue(x + 2*operatorX, y + 2*operatorY) &&
                matchfield.getFieldValue(x, y) == matchfield.getFieldValue(x + 3*operatorX, y + 3*operatorY)) {

            scoreboard[matchfield.getFieldValue(x, y) - 1]++;
            resetMatchfield();

            if(matchfield.getFieldValue(x, y) == 1)
                myTurn = false;
            else if(computerEnabled) {
                for (y = 0; y < 6; y++)
                    if (matchfield.getFieldValue(x, y) == 0) {
                        matchfield.setFieldValue(3, 0, 2);
                        myTurn = true;
                    }
            }
            return true;
        }
        else
            return false;
    }

    public Matchfield getMatchfield(){
        return matchfield;
    }

    public void setProtocolEngine(FourConnectProtocolEngine protocolEngine) {
        this.protocolEngine = protocolEngine;
    }

    public class ComputerPlayer{

        int difficulty;

        public ComputerPlayer(int difficulty){
            this.difficulty = difficulty;
        }

        public int computerSet(){
            int column = -1;
            ArrayList<Integer> possibleSets = new ArrayList<>();

            possibleSets.add(treeInARowHorizontal());
            possibleSets.add(treeInARowVertical());
            possibleSets.add(treeInARowDiagonal());
            possibleSets.add(treeInARowWithGapHorizontal());
            possibleSets.add(treeInARowWithGapDiagonal());

            //Mittlerer Modus
            if(difficulty > 1 && noSetFoundYet(possibleSets)){
                possibleSets.add(twoInARowVertical(2));
                possibleSets.add(twoInARowHorizontal(2));
                possibleSets.add(twoInARowDiagonal(2));
                possibleSets.add(twoInARowVertical(1));
                possibleSets.add(twoInARowHorizontal(1));
                possibleSets.add(twoInARowDiagonal(1));
            }
            //Schwerer Modus, 1 entspricht verteidigen, 2 entspricht angreifen
            if(difficulty > 2 && noSetFoundYet(possibleSets)){
                possibleSets.add(twoInARowWithGapHorizontal(2));
                possibleSets.add(twoInARowWithGapDiagonal(2));
                possibleSets.add(twoInARowWithGapHorizontal(1));
                possibleSets.add(twoInARowWithGapDiagonal(1));

                possibleSets.add(singleStone(2));
                possibleSets.add(singleStone(1));
            }

            for (int i = 0; i < possibleSets.size(); i ++)
                if(possibleSets.get(i) != -1) {
                    column = possibleSets.get(i);
                    break;
                }

            if(column != -1)
                return column;
            else
                return (int) (Math.random()*7);
        }

        private boolean noSetFoundYet(ArrayList<Integer> possibleSets){
            for(int i = 0; i < possibleSets.size(); i++)
                if(possibleSets.get(i) != -1) {
                    return false;
                }
            return true;
        }

        private int treeInARowHorizontal(){
            for(int x = 0; x < 5; x++)
                for(int y = 0; y < 6; y++) {
                    if (x < 4 && treeInARow(x, y, 1, 0) && matchfield.getFieldValue(x + 3, y) == 0) {
                        if(y == 0)
                            return x+3;
                        else
                            if(matchfield.getFieldValue(x + 3, y-1) != 0)
                                return x+3;
                    }
                    if((x-1) >= 0 && treeInARow(x, y, 1, 0) && matchfield.getFieldValue(x - 1, y) == 0)
                        if(y == 0)
                            return x-1;
                        else
                            if(matchfield.getFieldValue(x - 1, y-1) != 0)
                                return x-1;
                }
                    return -1;
        }

        private int treeInARowVertical(){
            for(int x = 0; x < 7; x++)
                for(int y = 0; y < 3; y++)
                    if (treeInARow(x,y, 0, 1) && matchfield.getFieldValue(x, y+3) == 0)
                        return x;
            return -1;
        }

        private int treeInARowDiagonal(){
            for(int x = 0; x < 4; x++)
                for(int y = 0; y < 3; y++)
                    if (treeInARow(x,y, 1, 1) && matchfield.getFieldValue(x+3, y+2) != 0 && matchfield.getFieldValue(x+3, y+3) == 0)
                        return x+3;

            for(int x = 6; x > 2; x--)
                for(int y = 2; y > -1; y--)
                    if (treeInARow(x,y, -1, 1) && matchfield.getFieldValue(x-3, y+2) != 0 && matchfield.getFieldValue(x-3, y+3) == 0)
                        return x-3;

            return -1;
        }

        private int treeInARowWithGapHorizontal(){
            for(int x = 0; x < 4; x++)
                for(int y = 0; y < 6; y++) {
                    if (treeInARowWithGap(x, y, 1, 0)) {
                        if (matchfield.getFieldValue(x + 1, y) == 0) {
                            if (y == 0)
                                return x + 1;
                            else if (matchfield.getFieldValue(x + 1, y - 1) != 0)
                                return x + 1;
                        }
                        else if(matchfield.getFieldValue(x + 2, y) == 0)
                            if (y == 0)
                                return x + 2;
                            else if (matchfield.getFieldValue(x + 2, y - 1) != 0)
                                return x + 2;
                    }
                }

            return -1;
        }

        //  if (y == 0) macht keinen sinn --> bearbeiten

        private int treeInARowWithGapDiagonal(){
            for(int x = 0; x < 4; x++)
                for(int y = 0; y < 3; y++)
                    if(treeInARowWithGap(x,y,1,1)) {
                        if (matchfield.getFieldValue(x + 1, y + 1) == 0) {
                            if (y == 0)
                                return x + 1;
                            else if (matchfield.getFieldValue(x + 1, y) != 0)
                                return x + 1;
                        }
                        else if (matchfield.getFieldValue(x + 2, y + 2) == 0)
                            if (y == 0)
                                return x + 2;
                            else if (matchfield.getFieldValue(x + 2, y + 1) != 0)
                                return x + 2;
                    }

            for(int x = 6; x > 2; x--)
                for(int y = 2; y > -1; y--)
                    if(treeInARowWithGap(x,y,-1,1)) {
                        if (matchfield.getFieldValue(x - 1, y + 1) == 0) {
                            if (y == 0)
                                return x - 1;
                            else if (matchfield.getFieldValue(x - 1, y) != 0)
                                return x - 1;
                        } else if (matchfield.getFieldValue(x - 2, y + 2) == 0)
                            if (y == 0)
                                return x - 2;
                            else if (matchfield.getFieldValue(x - 2, y + 1) != 0)
                                return x - 2;
                    }
            return -1;
        }

        private int twoInARowVertical(int defendOrAttack){
            for(int x = 0; x < 7; x++)
                for(int y = 0; y < 3; y++)
                    if (twoInARow(x,y, 0, 1, defendOrAttack) && matchfield.getFieldValue(x, y+2) == 0)
                        return x;
            return -1;
        }

        private int twoInARowHorizontal(int defendOrAttack){
            for(int x = 0; x < 5; x++)
                for(int y = 0; y < 6; y++) {
                    if (x < 4 && twoInARow(x, y, 1, 0, defendOrAttack) && matchfield.getFieldValue(x + 2, y) == 0) {
                        if(y == 0)
                            return x+2;
                        else if(matchfield.getFieldValue(x + 2, y-1) != 0)
                                return x+2;
                    }
                    if((x-1) >= 0 && twoInARow(x, y, 1, 0, defendOrAttack) && matchfield.getFieldValue(x - 1, y) == 0)
                        if(y == 0)
                            return x-1;
                        else if(matchfield.getFieldValue(x - 1, y-1) != 0)
                                return x-1;
                }
            return -1;
        }

        private int twoInARowDiagonal(int defendOrAttack){
            for(int x = 0; x < 4; x++)
                for(int y = 0; y < 3; y++)
                    if (twoInARow(x,y, 1, 1, defendOrAttack) && matchfield.getFieldValue(x+2, y+1) != 0 && matchfield.getFieldValue(x+2, y+2) == 0)
                        return x+2;

            for(int x = 6; x > 2; x--)
                for(int y = 2; y > -1; y--)
                    if (twoInARow(x,y, -1, 1, defendOrAttack) && matchfield.getFieldValue(x-2, y+1) != 0 && matchfield.getFieldValue(x-2, y+2) == 0)
                        return x-2;

            return -1;
        }

        public int twoInARowWithGapHorizontal(int defendOrAttack){
            for(int x = 0; x < 4; x++)
                for(int y = 0; y < 6; y++) {
                    if (twoInARowWithGap(x, y, 1, 0, defendOrAttack)) {
                        if (matchfield.getFieldValue(x + 1, y) == 0) {
                            if (y == 0 || matchfield.getFieldValue(x + 1, y - 1) != 0)
                                return x + 1;
                        }
                    }
                }
            return -1;
        }

        private int twoInARowWithGapDiagonal(int defendOrAttack){
            for(int x = 0; x < 4; x++)
                for(int y = 0; y < 3; y++)
                    if(twoInARowWithGap(x,y,1,1, defendOrAttack)) {
                        if (matchfield.getFieldValue(x + 1, y + 1) == 0 && matchfield.getFieldValue(x + 1, y) != 0) {
                                return x + 1;
                        }
                    }

            for(int x = 6; x > 2; x--)
                for(int y = 2; y > -1; y--)
                    if(twoInARowWithGap(x,y,-1,1, defendOrAttack)) {
                        if (matchfield.getFieldValue(x - 1, y + 1) == 0 && matchfield.getFieldValue(x - 1, y) != 0) {
                                return x - 1;
                        }
                    }
            return -1;
        }

        private int singleStone(int defendOrAttack){
            for(int x = 0; x < 7; x++)
                for(int y = 0; y < 6; y++)
                    if (matchfield.getFieldValue(x, y) == defendOrAttack){
                        //vertical
                        if(y < 5 && matchfield.getFieldValue(x, y + 1) == 0)
                            return x;
                        //horizontal
                        if(x < 6 && matchfield.getFieldValue(x + 1, y) == 0)
                            return x + 1;
                        if(x == 6 && matchfield.getFieldValue(x - 1, y) == 0)
                            return x - 1;
                        //diagonal right
                        if(x < 6 && y < 5 && matchfield.getFieldValue(x + 1, y + 1) == 0 && matchfield.getFieldValue(x + 1, y) != 0)
                            return x + 1;
                        // diagonal left
                        if(x > 0 && y < 5 && matchfield.getFieldValue(x - 1, y + 1) == 0 && matchfield.getFieldValue(x - 1, y) != 0)
                            return x - 1;
                    }
            return -1;
        }

        private boolean treeInARow(int x, int y, int operatorX, int operatorY){
            if (matchfield.getFieldValue(x, y) != 0 &&
                    matchfield.getFieldValue(x, y) == matchfield.getFieldValue(x + 1*operatorX, y + 1*operatorY) &&
                    matchfield.getFieldValue(x, y) == matchfield.getFieldValue(x + 2*operatorX, y + 2*operatorY))
                return true;
            else
                return false;
        }

        private boolean treeInARowWithGap(int x, int y, int operatorX, int operatorY){
            int count = 1;
            if (matchfield.getFieldValue(x, y) != 0)
                for(int i = 1; i < 4; i++)
                    if(matchfield.getFieldValue(x, y) == matchfield.getFieldValue(x + i*operatorX, y + i*operatorY))
                        count++;

            if(count == 3) {
                return true;
            }
            else
                return false;
        }

        private boolean twoInARow(int x, int y, int operatorX, int operatorY, int defendOrAttack){
            if (matchfield.getFieldValue(x, y) == defendOrAttack &&
                    matchfield.getFieldValue(x, y) == matchfield.getFieldValue(x + 1*operatorX, y + 1*operatorY))
                return true;
            else
                return false;
        }

        private boolean twoInARowWithGap(int x, int y, int operatorX, int operatorY, int defendOrAttack){
            if (matchfield.getFieldValue(x, y) == defendOrAttack &&
                    matchfield.getFieldValue(x + 1*operatorX, y + 1*operatorY) == 0 &&
                    matchfield.getFieldValue(x + 2*operatorX, y + 2*operatorY) == defendOrAttack)
                return true;
            else
                return false;
        }
    }
}
