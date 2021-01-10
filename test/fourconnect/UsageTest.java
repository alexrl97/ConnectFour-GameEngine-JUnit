package fourconnect;

import org.junit.Test;

import static org.junit.Assert.*;

public class UsageTest {

    public final String ALICE = "Alice";
    public final String BOB = "Bob";
/*
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
*/

    private FourConnect getFourConnect(){
        return new FourConnectGame(false);
    }

    @Test
    public void goodRegisterPlayerNames() throws GameException {
        FourConnectGame fc = (FourConnectGame) getFourConnect();
        fc.setPlayersName(ALICE, 1);
        fc.setPlayersName(BOB, 2);

        assertEquals(fc.getPlayersname(1), ALICE);
        assertEquals(fc.getPlayersname(2), BOB);
    }

    //Spieler drei gibt es nicht.
    @Test(expected=GameException.class)
    public void failurePickWrongPlayerNumber() throws GameException {
        FourConnect fc = getFourConnect();
        fc.setPlayersName(ALICE, 3);
    }

    @Test
    public void goodPickSymbol1() throws GameException {
        FourConnectGame fc = (FourConnectGame) getFourConnect();
        fc.pickColour(1, FourConnectPieceColour.RED);
        assertEquals(fc.getColourByPlayerNumber(2), FourConnectPieceColour.BLUE);
    }

    @Test
    public void goodPickSymbol2() throws GameException {
        FourConnectGame fc = (FourConnectGame) getFourConnect();
        fc.pickColour(1, FourConnectPieceColour.BLUE);
        assertEquals(fc.getColourByPlayerNumber(2), FourConnectPieceColour.RED);
    }

    @Test
    public void goodMatchfieldCreate(){
        Matchfield mf = new Matchfield();
        for(int x = 0; x < 7; x++)
            for(int y = 0; y < 6; y++)
                assertTrue(mf.getFieldValue(x,y) == 0);
    }
/*
  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |_X_|___|___|___|___|___|___|
  |_0_|___|___|___|___|___|___|
  |_X_|___|___|___|___|___|___|
  |_0_|___|___|___|___|___|___|
  |_X_|___|___|___|___|___|___|
  |_0_|___|___|___|___|___|___|
 */
    @Test(expected=GameException.class)
    public void failureSetInFullColumn() throws GameException {
        FourConnect fc = getFourConnect();
        //Spieler setzen abwechselnd insgesamt sieben mal in die gleiche Spalte
        for(int i = 0; i < 7; i++){
            fc.set(0);
        }
    }

/*
  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |_X_|_X_|_X_|___|___|___|___|
  |_0_|_0_|_0_|_0_|___|___|___|
 */
    @Test
    public void goodWinHorizontal() throws GameException {
        FourConnect fc = getFourConnect();
        fc.set(0);
        fc.set(0);
        fc.set(1);
        fc.set(1);
        fc.set(2);
        boolean noWinYet = fc.set(2);
        boolean winningSet = fc.set(3);
        assertFalse(noWinYet);
        assertTrue(winningSet);
    }
/*
  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|_X_|_X_|_X_|___|
  |___|___|___|_0_|_0_|_0_|_0_|
 */
    @Test
    public void goodWinHorizontalEdge() throws GameException {
        FourConnect fc = getFourConnect();
        fc.set(3);
        fc.set(3);
        fc.set(4);
        fc.set(4);
        fc.set(5);
        boolean noWinYet = fc.set(5);
        boolean winningSet = fc.set(6);
        assertFalse(noWinYet);
        assertTrue(winningSet);
    }
/*
  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |_0_|___|___|___|___|___|___|
  |_0_|_X_|___|___|___|___|___|
  |_0_|_X_|___|___|___|___|___|
  |_0_|_X_|___|___|___|___|___|
 */
    @Test
    public void goodWinVertical() throws GameException {
        FourConnect fc = getFourConnect();
        fc.set(0);
        fc.set(1);
        fc.set(0);
        fc.set(1);
        fc.set(0);
        boolean noWinYet = fc.set(1);
        boolean winningSet = fc.set(0);
        assertFalse(noWinYet);
        assertTrue(winningSet);
    }
/*
  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|_0_|
  |_X_|___|___|___|___|___|_0_|
  |_X_|___|___|___|___|___|_0_|
  |_X_|___|___|___|___|___|_0_|
 */
    @Test
    public void goodWinVerticalEdge() throws GameException {
        FourConnect fc = getFourConnect();
        fc.set(6);
        fc.set(1);
        fc.set(6);
        fc.set(1);
        fc.set(6);
        boolean noWinYet = fc.set(1);
        boolean winningSet = fc.set(6);
        assertFalse(noWinYet);
        assertTrue(winningSet);
    }

/*
  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|_0_|___|___|___|
  |___|___|_0_|_X_|___|___|___|
  |___|_0_|_X_|_X_|___|___|___|
  |_0_|_X_|_X_|_0_|_0_|___|___|
 */

    @Test
    public void goodWinDiagonal() throws GameException {
        FourConnect fc = getFourConnect();
        fc.set(0);//Spieler_1
        fc.set(1);
        fc.set(1);//Spieler_1
        fc.set(2);
        fc.set(3);//Spieler_1
        fc.set(2);
        fc.set(2);//Spieler_1
        fc.set(3);
        fc.set(4);//Spieler_1
        boolean noWinYet = fc.set(3);
        boolean winningSet = fc.set(3);//Spieler_1
        assertFalse(noWinYet);
        assertTrue(winningSet);
    }

/*
  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|_X_|___|___|___|
  |___|___|___|_0_|_X_|_0_|___|
  |___|___|___|_X_|_0_|_X_|_0_|
  |___|___|___|_0_|_X_|_0_|_X_|
  |___|___|___|_0_|_X_|_0_|_X_|
  |___|___|___|_0_|_X_|_0_|_X_|
 */
    @Test
    public void goodWinDiagonalEdgeDifferentDirection() throws GameException {
        FourConnect fc = getFourConnect();
        for(int i = 0; i < 3; i++) {
            fc.set(3);//Spieler_1
            fc.set(4);
            fc.set(5);//Spieler_1
            fc.set(6);
        }

        fc.set(4);//Spieler_1
        fc.set(5);
        fc.set(6);//Spieler_1
        fc.set(3);

        fc.set(3);//Spieler_1
        fc.set(4);
        boolean noWinYet = fc.set(5);//Spieler_1
        boolean winningSet = fc.set(3);

        assertFalse(noWinYet);
        assertTrue(winningSet);
    }
/* 1. Spiel komplett. Spieler eins gewinnt und das Scoreboard wird erhoeht.

  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|_0_|___|___|___|
  |___|___|___|_0_|_X_|_0_|_X_|
  |___|___|___|_0_|_X_|_0_|_X_|
  |___|___|___|_0_|_X_|_0_|_X_|

2. Spiel unvollstaendig, da reset () benutzt wird. Scoreboard wird ebenfalls zuruckgesetzt.
Spieler zwei hat dieses Spiel begonnen, da er das letzte verloren hat

  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|_X_|_O_|_X_|_O_|
  |___|___|___|_X_|_O_|_X_|_O_|
  |___|___|___|_X_|_O_|_X_|_O_|

3. Das dritte Spiel beginnt aufgrund des resets wieder Spieler 1 und gewinnt es.

  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|_0_|___|___|___|
  |___|___|___|_0_|_X_|_0_|_X_|
  |___|___|___|_0_|_X_|_0_|_X_|
  |___|___|___|_0_|_X_|_0_|_X_|

 */
    @Test
    public void goodResetAndScoreboard() throws GameException{
        FourConnectGame fc = (FourConnectGame) getFourConnect();

        //1. Spiel
        for(int i = 0; i < 3; i++) {
            fc.set(3);//Spieler_1
            fc.set(4);
            fc.set(5);//Spieler_1
            fc.set(6);
        }

        boolean winningSet1 = fc.set(3); //Spieler_1
        assertTrue(winningSet1);
        assertEquals(1, fc.getScoreboard(1));

        //2. Spiel
        for(int i = 0; i < 3; i++) {
            fc.set(3);//Spieler_2
            fc.set(4);
            fc.set(5);//Spieler_2
            fc.set(6);
        }

        //Abbruch/Reset
        fc.reset(true);

        assertEquals(0, fc.getScoreboard(1));

        //3. Spiel
        boolean noWinYet = true;
        for(int i = 0; i < 3; i++) {
            fc.set(3);//Spieler_2
            fc.set(4);
            fc.set(5);//Spieler_2
            noWinYet = fc.set(6);
        }
        boolean winningSet2 = fc.set(3); //Spieler_2

        assertFalse(noWinYet);
        assertTrue(winningSet2);
        assertEquals(fc.getScoreboard(1), 1);
        assertEquals(fc.getScoreboard(2), 0);
    }
/*
Testet, ob nach drei Spielzuegen des echten Computers im leichten Spielsmodus insgesamt sechs Spielsteine gesetzt wurden
 */
    @Test
    public void goodComputerPlayerDoesSet() throws GameException {
        FourConnectGame fc = new FourConnectGame(1);
        fc.set(0);
        fc.set(0);
        fc.set(0);

        Matchfield current = fc.getMatchfield();

        int counter = 0;
        for(int x = 0; x < 7; x++)
            for(int y = 0; y < 6; y++)
        if(current.getFieldValue(x,y) > 0)
            counter++;

            assertEquals(6, counter);
    }

/* Der Spieler bildet eine Viererreihe und gewinnt. Der Computer faengt das neue Spiel an und setzt seinen Zug in die Mitte

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|_0_|___|___|___|___|___|___|
|_0_|___|___|___|___|___|___|
|_0_|___|___|___|___|___|___|
|_0_|___|___|___|___|___|___|

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|_X_|___|___|___|
*/
    @Test
    public void goodComputerPlayerDoesFirstSetAfterLose() throws GameException{
        FourConnectGame fc = new FourConnectGame(1);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(0,0,1);
        current.setFieldValue(0,1,1);
        current.setFieldValue(0,2,1);

        fc.set(0);

        assertEquals(2, current.getFieldValue(3,0));
    }
/*
Der Computer muss erkennen, dass er seinen Stein in die erste Spalte setzen muss

  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |_0_|___|___|___|___|___|___|
  |_0_|___|___|___|___|___|___|
  |_0_|___|___|___|___|___|___|
 */
    @Test
    public void goodComputerBlockTreeStonesInARowVertical() throws GameException {
        FourConnectGame fc = new FourConnectGame(1);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(0,0,1);
        current.setFieldValue(0,1,1);
        current.setFieldValue(0,2,1);

        for(int i = 0; i < 100; i++)
        assertEquals(0, fc.comp.computerSet());
    }
/*
Der Computer muss erkennen, dass er seinen Stein in die letzte Spalte setzen muss

  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|_0_|
  |___|___|___|___|___|___|_0_|
  |___|___|___|___|___|___|_0_|
  |___|___|___|___|___|___|_X_|
  |___|___|___|___|___|___|_0_|
 */
    @Test
    public void goodComputerBlockTreeStonesInARowVertical2() throws GameException {
        FourConnectGame fc = new FourConnectGame(1);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(6,0,1);
        current.setFieldValue(6,1,2);
        current.setFieldValue(6,2,1);
        current.setFieldValue(6,3,1);
        current.setFieldValue(6,4,1);

        for(int i = 0; i < 100; i++)
            assertEquals(6, fc.comp.computerSet());
    }
/*
Der Computer muss erkennen, dass er seinen Stein in die vierte Spalte setzen muss

  |_1_|_2_|_3_|_4_|_5_|_6_|_7_|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |___|___|___|___|___|___|___|
  |_0_|_0_|_0_|___|___|___|___|
 */
    @Test
    public void goodComputerBlockTreeStonesInARowHorizontal() throws GameException {
        FourConnectGame fc = new FourConnectGame(1);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(0,0,1);
        current.setFieldValue(1,0,1);
        current.setFieldValue(2,0,1);

        for(int i = 0; i < 100; i++)
        assertEquals(3, fc.comp.computerSet());
    }

/*
Der Computer muss erkennen, dass er seinen Stein in die vierte Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|_0_|_0_|_0_|
*/
    @Test
    public void goodComputerBlockTreeStonesInARowHorizontal2() throws GameException {
        FourConnectGame fc = new FourConnectGame(1);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(4,0,1);
        current.setFieldValue(5,0,1);
        current.setFieldValue(6,0,1);

        for(int i = 0; i < 100; i++)
            assertEquals(3, fc.comp.computerSet());
    }
/*
Der Computer muss erkennen, dass er seinen Stein nicht in die vierte Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|_0_|_0_|_0_|
|___|___|___|___|_X_|_0_|_0_|
*/
    @Test
    public void badComputerBlockTreeStonesInARowHorizontal() throws GameException {
        FourConnectGame fc = new FourConnectGame(1);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(4,0,2);
        current.setFieldValue(5,0,1);
        current.setFieldValue(6,0,1);
        current.setFieldValue(4,1,1);
        current.setFieldValue(5,1,1);
        current.setFieldValue(6,1,1);

        int computerset = fc.comp.computerSet();

        while(computerset == 3)
            computerset = fc.comp.computerSet();

        assertNotEquals(3, computerset);
    }
/*
Der Computer muss erkennen, dass er seinen Stein in die vierte Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|_0_|_0_|___|___|___|
|___|_0_|_0_|_X_|___|___|___|
|_0_|_0_|_X_|_X_|___|___|___|
*/
    @Test
    public void goodComputerBlockTreeStonesInARowDiagonal() throws GameException {
        FourConnectGame fc = new FourConnectGame(1);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(0,0,1);
        current.setFieldValue(1,0,1);
        current.setFieldValue(1,1,1);
        current.setFieldValue(2,1,1);
        current.setFieldValue(2,2,1);
        current.setFieldValue(3,2,1);
        current.setFieldValue(2,0,2);
        current.setFieldValue(3,0,2);
        current.setFieldValue(3,1,2);

        for(int i = 0; i < 100; i++)
            assertEquals(3, fc.comp.computerSet());
    }
/*
Der Computer muss erkennen, dass er seinen Stein in die vierte Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|_0_|_0_|___|___|
|___|___|___|_X_|_0_|_0_|___|
|___|___|___|_X_|_X_|_0_|_0_|
*/
    @Test
    public void goodComputerBlockTreeStonesInARowDiagonal2() throws GameException {
        FourConnectGame fc = new FourConnectGame(1);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(6,0,1);
        current.setFieldValue(5,0,1);
        current.setFieldValue(5,1,1);
        current.setFieldValue(4,0,2);
        current.setFieldValue(4,1,1);
        current.setFieldValue(4,2,1);
        current.setFieldValue(3,0,2);
        current.setFieldValue(3,1,2);
        current.setFieldValue(3,2,1);

        for(int i = 0; i < 100; i++)
            assertEquals(3, fc.comp.computerSet());
    }
/*
Der Computer muss erkennen, dass er seinen Stein nicht in die vierte Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|_0_|___|___|
|___|___|___|_X_|_0_|_0_|___|
|___|___|___|_X_|_X_|_0_|_0_|
*/
    @Test
    public void badComputerBlockTreeStonesInARowDiagonal() throws GameException {
        FourConnectGame fc = new FourConnectGame(1);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(6,0,1);
        current.setFieldValue(5,0,1);
        current.setFieldValue(5,1,1);
        current.setFieldValue(4,0,2);
        current.setFieldValue(4,1,1);
        current.setFieldValue(4,2,1);
        current.setFieldValue(3,0,2);
        current.setFieldValue(3,1,2);

        int computerset = fc.comp.computerSet();

        while(computerset == 3)
            computerset = fc.comp.computerSet();

        assertNotEquals(3, computerset);
    }

/* Der Computer muss erkennen, dass er seinen Stein in die vierte Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|_0_|___|___|___|___|___|___|
|_X_|_0_|___|___|___|___|___|
|_0_|_X_|_0_|___|___|___|___|
|_X_|_X_|_X_|___|___|___|___|
*/
    @Test
    public void goodComputerBlockTreeRowDiagonalButtonLeft() throws GameException {
        FourConnectGame fc = new FourConnectGame(2);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(1,2,1);
        current.setFieldValue(2,0,2);
        current.setFieldValue(2,1,1);
        current.setFieldValue(1,0,2);
        current.setFieldValue(1,1,2);
        current.setFieldValue(0,0,2);
        current.setFieldValue(0,1,1);
        current.setFieldValue(0,2,2);
        current.setFieldValue(0,3,1);

        for(int i = 0; i < 100; i++)
            assertEquals(3, fc.comp.computerSet());
    }
/*
Der Computer muss ab dem mittleren Schwierigkeitesgrad erkennen, dass er seinen Stein in die vierte Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|_0_|___|_0_|_0_|___|
*/
    @Test
    public void goodComputerBlockTreeRowWithGapHorizontal() throws GameException {
        FourConnectGame fc = new FourConnectGame(2);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(5,0,1);
        current.setFieldValue(4,0,1);
        current.setFieldValue(2,0,1);

        for(int i = 0; i < 100; i++)
            assertEquals(3, fc.comp.computerSet());
    }
/*
Der Computer muss erkennen, dass er seinen Stein nicht in die vierte Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|_0_|___|_0_|_0_|___|
|___|___|_0_|___|_X_|_0_|___|
*/
    @Test
    public void badComputerBlockTreeRowWithGapHorizontal() throws GameException {
        FourConnectGame fc = new FourConnectGame(2);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(5,0,1);
        current.setFieldValue(4,0,2);
        current.setFieldValue(2,0,1);
        current.setFieldValue(5,1,1);
        current.setFieldValue(4,1,1);
        current.setFieldValue(2,1,1);

        int computerset = fc.comp.computerSet();

        while(computerset == 3)
            computerset = fc.comp.computerSet();

        assertNotEquals(3, computerset);
    }
/* Der Computer muss erkennen, dass er seinen Stein in die sechste Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|_0_|
|___|___|___|___|___|___|_X_|
|___|___|___|___|_0_|_X_|_0_|
|___|___|___|_0_|_X_|_X_|_X_|
*/
    @Test
    public void goodComputerBlockTreeRowWithGapDiagonalRight1() throws GameException {
        FourConnectGame fc = new FourConnectGame(2);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(3,0,1);
        current.setFieldValue(4,0,2);
        current.setFieldValue(5,0,2);
        current.setFieldValue(6,0,2);
        current.setFieldValue(4,1,1);
        current.setFieldValue(5,1,2);
        current.setFieldValue(6,1,1);
        current.setFieldValue(6,2,2);
        current.setFieldValue(6,3,1);

        for(int i = 0; i < 100; i++)
            assertEquals(5, fc.comp.computerSet());
    }
/* Der Computer muss erkennen, dass er seinen Stein in die sechste Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|_0_|
|___|___|___|___|___|_0_|_X_|
|___|___|___|___|___|_X_|_0_|
|___|___|___|_0_|_X_|_X_|_X_|
*/
    @Test
    public void goodComputerBlockTreeRowWithGapDiagonalRight2() throws GameException {
        FourConnectGame fc = new FourConnectGame(2);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(3,0,1);
        current.setFieldValue(4,0,2);
        current.setFieldValue(5,0,2);
        current.setFieldValue(6,0,2);
        current.setFieldValue(5,2,1);
        current.setFieldValue(5,1,2);
        current.setFieldValue(6,1,1);
        current.setFieldValue(6,2,2);
        current.setFieldValue(6,3,1);

        for(int i = 0; i < 100; i++)
            assertEquals(4, fc.comp.computerSet());
    }
/* Der Computer muss erkennen, dass er seinen Stein in die zweite Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|_0_|___|___|___|___|___|___|
|_X_|___|___|___|___|___|___|
|_0_|_X_|_0_|___|___|___|___|
|_X_|_X_|_X_|_0_|___|___|___|
*/
    @Test
    public void goodComputerBlockTreeRowWithGapDiagonalLeft1() throws GameException {
        FourConnectGame fc = new FourConnectGame(2);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(3,0,1);
        current.setFieldValue(2,0,2);
        current.setFieldValue(2,1,1);
        current.setFieldValue(1,0,2);
        current.setFieldValue(1,1,2);
        current.setFieldValue(0,0,2);
        current.setFieldValue(0,1,1);
        current.setFieldValue(0,2,2);
        current.setFieldValue(0,3,1);

        for(int i = 0; i < 100; i++)
            assertEquals(1, fc.comp.computerSet());
    }
/* Der Computer muss erkennen, dass er seinen Stein in die zweite Spalte setzen muss

|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|_0_|___|___|___|___|___|___|
|_X_|_0_|___|___|___|___|___|
|_0_|_X_|___|___|___|___|___|
|_X_|_X_|_X_|_0_|___|___|___|
*/
    @Test
    public void goodComputerBlockTreeRowWithGapDiagonalLeft2() throws GameException {
        FourConnectGame fc = new FourConnectGame(2);
        Matchfield current = fc.getMatchfield();
        current.setFieldValue(3,0,1);
        current.setFieldValue(2,0,2);
        current.setFieldValue(1,2,1);
        current.setFieldValue(1,0,2);
        current.setFieldValue(1,1,2);
        current.setFieldValue(0,0,2);
        current.setFieldValue(0,1,1);
        current.setFieldValue(0,2,2);
        current.setFieldValue(0,3,1);

        for(int i = 0; i < 100; i++)
            assertEquals(2, fc.comp.computerSet());
    }
/*
Im schweren Spielmodus kann der Computer anzugreifen und versucht eigene Reihen zu bilden
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|_X_|___|___|___|
|___|___|___|_X_|___|___|___|
 */
    @Test
    public void goodComputerAttackVertical() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(3,0,2);
        current.setFieldValue(3,1,2);

        for(int i = 0; i < 100; i++)
            assertEquals(3, fc.comp.computerSet());
    }
/*
Im schweren Spielmodus kann der Computer anzugreifen und versucht eigene Reihen zu bilden
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|_X_|_X_|___|___|___|
 */
    @Test
    public void goodComputerAttackHorizontal() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(2,0,2);
        current.setFieldValue(3,0,2);

        for(int i = 0; i < 100; i++)
            assertEquals(4, fc.comp.computerSet());
    }
/*
Im schweren Spielmodus kann der Computer anzugreifen und versucht eigene Reihen zu bilden
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|_X_|_0_|___|
|___|___|___|_X_|_0_|_X_|___|
*/
    @Test
    public void goodComputerAttackDiagonalRight() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(3,0,2);
        current.setFieldValue(4,0,1);
        current.setFieldValue(4,1,2);
        current.setFieldValue(5,0,2);
        current.setFieldValue(5,1,1);

        for(int i = 0; i < 100; i++)
            assertEquals(5, fc.comp.computerSet());
    }
    /*
Im schweren Spielmodus kann der Computer anzugreifen und versucht eigene Reihen zu bilden
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|_0_|_X_|___|___|___|___|
|___|_X_|_0_|_X_|___|___|___|
*/
    @Test
    public void goodComputerAttackDiagonalLeft() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(3,0,2);
        current.setFieldValue(2,0,1);
        current.setFieldValue(2,1,2);
        current.setFieldValue(1,0,2);
        current.setFieldValue(1,1,1);

        for(int i = 0; i < 100; i++)
            assertEquals(1, fc.comp.computerSet());
    }
/*
Der Computer verteidigt um zu verhindern, dass der Gegner eine Dreiherreihe bildet, wenn er selbst nicht effektiv angreifen kann
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|_0_|___|___|___|
|___|___|___|_0_|___|___|___|
 */
    @Test
    public void goodComputerDefendVertical() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(3,0,1);
        current.setFieldValue(3,1,1);

        for(int i = 0; i < 100; i++)
            assertEquals(3, fc.comp.computerSet());
    }
/*
Der Computer verteidigt um zu verhindern, dass der Gegner eine Dreiherreihe bildet, wenn er selbst nicht effektiv angreifen kann
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|_0_|_0_|___|___|___|
 */
    @Test
    public void goodComputerDefendHorizontal() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(2,0,1);
        current.setFieldValue(3,0,1);

        for(int i = 0; i < 100; i++)
            assertEquals(4, fc.comp.computerSet());
    }
/*
Der Computer verteidigt um zu verhindern, dass der Gegner eine Dreiherreihe bildet, wenn er selbst nicht effektiv angreifen kann
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|_0_|_X_|___|
|___|___|___|_0_|_X_|_0_|___|
*/
    @Test
    public void goodComputerDefendDiagonalRight() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(3,0,1);
        current.setFieldValue(4,0,2);
        current.setFieldValue(4,1,1);
        current.setFieldValue(5,0,1);
        current.setFieldValue(5,1,2);

        for(int i = 0; i < 100; i++)
            assertEquals(5, fc.comp.computerSet());
    }
/*
Der Computer verteidigt um zu verhindern, dass der Gegner eine Dreiherreihe bildet, wenn er selbst nicht effektiv angreifen kann
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|_X_|_0_|___|___|___|___|
|___|_0_|_X_|_0_|___|___|___|
*/
    @Test
    public void goodComputerDefendDiagonalLeft() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(3,0,1);
        current.setFieldValue(2,0,2);
        current.setFieldValue(2,1,1);
        current.setFieldValue(1,0,1);
        current.setFieldValue(1,1,2);

        for(int i = 0; i < 100; i++)
            assertEquals(1, fc.comp.computerSet());
    }
/*
Der Computer verteidigt um zu verhindern, dass der Gegner eine Dreiherreihe bildet, wenn er selbst nicht effektiv angreifen kann
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|_0_|___|_0_|___|___|_X_|
*/
    @Test
    public void goodComputerDefendWithGapHorizontal() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(1,0,1);
        current.setFieldValue(3,0,1);
        current.setFieldValue(6,0,2);

        for(int i = 0; i < 100; i++)
            assertEquals(2, fc.comp.computerSet());
    }

/*
Der Computer verteidigt um zu verhindern, dass der Gegner eine Dreiherreihe bildet, wenn er selbst nicht effektiv angreifen kann
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|_0_|___|
|___|___|___|___|___|_X_|___|
|___|___|___|_0_|_X_|_0_|___|
*/
    @Test
    public void goodComputerDefendDiagonalWithGapRight() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(3,0,1);
        current.setFieldValue(4,0,2);
        current.setFieldValue(5,2,1);
        current.setFieldValue(5,0,1);
        current.setFieldValue(5,1,2);

        for(int i = 0; i < 100; i++)
            assertEquals(4, fc.comp.computerSet());
    }

/*
Der Computer verteidigt um zu verhindern, dass der Gegner eine Dreiherreihe bildet, wenn er selbst nicht effektiv angreifen kann
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|_0_|___|___|___|___|___|
|___|_X_|___|___|___|___|___|
|___|_0_|_X_|_0_|___|___|___|
*/
    @Test
    public void goodComputerDefendDiagonalWithGapLeft() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(3,0,1);
        current.setFieldValue(2,0,2);
        current.setFieldValue(1,2,1);
        current.setFieldValue(1,0,1);
        current.setFieldValue(1,1,2);

        for(int i = 0; i < 100; i++)
            assertEquals(2, fc.comp.computerSet());
    }

/*
Der schwere Computer greift an wenn er im Vorteil ist und setzt seinen Stein in Spalte 7
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|_0_|_X_|
*/
    @Test
    public void goodComputerAttackVerticalForTwoInARow() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(6,0,2);
        current.setFieldValue(5,0,1);

        for(int i = 0; i < 100; i++)
            assertEquals(6, fc.comp.computerSet());
    }

/*
Der schwere Computer greift an wenn er im Vorteil ist und setzt seinen Stein in Spalte 6
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|_0_|
|___|___|___|___|___|___|_X_|
*/
    @Test
    public void goodComputerAttackHorizontalForTwoInARow() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(6,0,2);
        current.setFieldValue(6,1,1);

        for(int i = 0; i < 100; i++)
            assertEquals(5, fc.comp.computerSet());
    }

/*
Der schwere Computer greift an wenn er im Vorteil ist und setzt seinen Stein in Spalte 6
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|_0_|
|___|___|___|___|___|_0_|_X_|
*/
    @Test
    public void goodComputerAttackDiagonalForTwoInARowLeft() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(5,0,1);
        current.setFieldValue(6,0,2);
        current.setFieldValue(6,1,1);

        for(int i = 0; i < 100; i++)
            assertEquals(5, fc.comp.computerSet());
    }

/*
Der schwere Computer greift an wenn er im Vorteil ist und setzt seinen Stein in Spalte 2
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|_0_|___|___|___|___|___|___|
|_X_|_0_|___|___|___|___|___|
*/
    @Test
    public void goodComputerAttackDiagonalForTwoInARowRight() throws GameException {
        FourConnectGame fc = new FourConnectGame(3);
        Matchfield current = fc.getMatchfield();

        current.setFieldValue(1,0,1);
        current.setFieldValue(0,0,2);
        current.setFieldValue(0,1,1);

        for(int i = 0; i < 100; i++)
            assertEquals(1, fc.comp.computerSet());
    }
}
