package fourconnect;

import network.*;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class ProtocolEngineTest {

    public static final int PORTNUMBER = 6666;
    public static final long TEST_THREAD_SLEEP_DURATION = 500;
    private static int port = 0;

    private FourConnect getFourConnectEngine(InputStream is, OutputStream os, FourConnect gameEngine) throws IOException{
        FourConnectProtocolEngine fcProtocolEngine = new FourConnectProtocolEngine(gameEngine);
        fcProtocolEngine.handleConnection(is, os);
        return fcProtocolEngine;
    }

    //Klasse FourConnectReadTester: Speichert die Informationen zu Methodenaufrufen fuer Testzwecke

    private class FourConnectReadTester implements FourConnect{

        private boolean lastCallSetPlayerOneOnline = false;
        private boolean lastCallSetPlayerName = false;
        private boolean lastCallPick = false;
        private boolean lastCallSet = false;
        private boolean lastCallReset = false;

        String playerName;
        int playernumber;
        int column;
        FourConnectPieceColour colour;
        boolean sender = true;

        @Override
        public void setPlayerOneOnline(boolean sender) throws GameException {
            lastCallSetPlayerOneOnline = true;
            lastCallSetPlayerName = false;
            lastCallPick = false;
            lastCallSet = false;
            lastCallReset = false;
            this.sender = sender;
        }

        @Override
        public void setPlayersName(String playerName, int playerNumber) throws GameException {
            lastCallSetPlayerOneOnline = false;
            lastCallSetPlayerName = true;
            lastCallPick = false;
            lastCallSet = false;
            lastCallReset = false;

            this.playerName = playerName;
            this.playernumber = playerNumber;
        }

        @Override
        public void pickColour(int playerNumber, FourConnectPieceColour colour) throws GameException {
            lastCallSetPlayerOneOnline = false;
            lastCallSetPlayerName = false;
            lastCallPick = true;
            lastCallSet = false;
            lastCallReset = false;

            this.playernumber = playerNumber;
            this.colour = colour;
        }

        @Override
        public boolean set(int x) throws GameException {
            lastCallSetPlayerOneOnline = false;
            lastCallSetPlayerName = false;
            lastCallPick = false;
            lastCallSet = true;
            lastCallReset = false;

            this.column = x;

            return false;
        }

        @Override
        public boolean reset(boolean sender) throws GameException {
            lastCallSetPlayerOneOnline = false;
            lastCallSetPlayerName = false;
            lastCallPick = false;
            lastCallSet = false;
            lastCallReset = true;
            this.sender = sender;
            return false;
        }
    }

    // Testet, ob die Spielernummerauswahl des Senders beim Empfänger ankommt
    @Test
    public void setPlayerOneOnlineTest1() throws GameException, IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FourConnect fcProtocolSender = this.getFourConnectEngine(null, baos, null);

        fcProtocolSender.setPlayerOneOnline(true);

        byte[] serializedBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

        FourConnectReadTester fcReceiver = new FourConnectReadTester();
        FourConnect fcProtocolReceiver = this.getFourConnectEngine(bais, null, fcReceiver);

        FourConnectProtocolEngine fcEngine = (FourConnectProtocolEngine) fcProtocolReceiver;
        fcEngine.read();

        assertTrue(fcReceiver.lastCallSetPlayerOneOnline);
        assertFalse(fcReceiver.sender);
    }

    // Testet, ob die Spielernamenauswahl des Senders beim Empfänger ankommt
    @Test
    public void setPlayersNameTest1() throws GameException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FourConnect fcProtocolSender = this.getFourConnectEngine(null, baos, null);

        fcProtocolSender.setPlayersName("Alex", 1);

        byte[] serializedBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

        FourConnectReadTester fcReceiver = new FourConnectReadTester();
        FourConnect fcProtocolReceiver = this.getFourConnectEngine(bais, null, fcReceiver);

        FourConnectProtocolEngine fcEngine = (FourConnectProtocolEngine) fcProtocolReceiver;
        fcEngine.read();

        assertTrue(fcReceiver.lastCallSetPlayerName);
        assertEquals("Alex", fcReceiver.playerName);
        assertEquals(1, fcReceiver.playernumber);
    }
/*
    // Testet, ob die Spielerfarbenauswahl des Senders beim Empfänger ankommt
    @Test
    public void pickColourTest1() throws GameException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FourConnect fcProtocolSender = this.getFourConnectEngine(null, baos, null);

        fcProtocolSender.pickColour(1, FourConnectPieceColour.BLUE);

        byte[] serializedBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

        FourConnectReadTester fcReceiver = new FourConnectReadTester();
        FourConnect fcProtocolReceiver = this.getFourConnectEngine(bais, null, fcReceiver);

        FourConnectProtocolEngine fcEngine = (FourConnectProtocolEngine) fcProtocolReceiver;
        fcEngine.read();

        assertTrue(fcReceiver.lastCallPick);
    }

 */

    // Testet, ob der Zug des Senders beim Empfänger ankommt
    @Test
    public void setTest1() throws GameException, IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FourConnect fcProtocolSender = this.getFourConnectEngine(null, baos, null);

        fcProtocolSender.set(3);

        byte[] serializedBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

        FourConnectReadTester fcReceiver = new FourConnectReadTester();
        FourConnect fcProtocolReceiver = this.getFourConnectEngine(bais, null, fcReceiver);

        FourConnectProtocolEngine fcEngine = (FourConnectProtocolEngine) fcProtocolReceiver;
        fcEngine.read();

        assertTrue(fcReceiver.lastCallSet);
        assertEquals(3, fcReceiver.column);
    }

    //// Testet, ob der Game Reset des Senders beim Empfänger ankommt
    @Test
    public void resetTest1() throws GameException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FourConnect fcProtocolSender = this.getFourConnectEngine(null, baos, null);

        fcProtocolSender.reset(true);

        byte[] serializedBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

        FourConnectReadTester fcReceiver = new FourConnectReadTester();
        FourConnect fcProtocolReceiver = this.getFourConnectEngine(bais, null, fcReceiver);

        FourConnectProtocolEngine fcEngine = (FourConnectProtocolEngine) fcProtocolReceiver;
        fcEngine.read();

        assertTrue(fcReceiver.lastCallReset);
        assertEquals(false, fcReceiver.sender);
    }

    // Regelt die zu verwendeten Ports und unterstuetzt determinismus der folgeden Tests (Code Uebernahme)
    private int getPortNumber() {
        if(ProtocolEngineTest.port == 0) {
            ProtocolEngineTest.port = PORTNUMBER;
        } else {
            ProtocolEngineTest.port++;
        }

        System.out.println("use portnumber " + ProtocolEngineTest.port);
        return ProtocolEngineTest.port;
    }

    // Testet, ob die Spielerauswahl des Senders die korrekte Spielerauswahl beim Empfaenger ausloest
    @Test
    public void networkTestSetPlayerOneOnline() throws GameException, IOException, InterruptedException {
        FourConnectGame aliceGameEngine = new FourConnectGame(true);
        FourConnectProtocolEngine aliceProtocolEngine = new FourConnectProtocolEngine(aliceGameEngine);
        aliceGameEngine.setProtocolEngine(aliceProtocolEngine);

        FourConnectGame bobGameEngine = new FourConnectGame(true);
        FourConnectProtocolEngine bobProtocolEngine = new FourConnectProtocolEngine(bobGameEngine);
        bobGameEngine.setProtocolEngine(bobProtocolEngine);

        int port = this.getPortNumber();

        TCPStream aliceSide = new TCPStream(port, true, "aliceSide");
        TCPStream bobSide = new TCPStream(port, false, "bobSide");

        aliceSide.start();
        bobSide.start();
        aliceSide.waitForConnection();
        bobSide.waitForConnection();

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        aliceProtocolEngine.handleConnection(aliceSide.getInputStream(), aliceSide.getOutputStream());
        bobProtocolEngine.handleConnection(bobSide.getInputStream(), bobSide.getOutputStream());

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        aliceGameEngine.setPlayerOneOnline(true);
        aliceProtocolEngine.setPlayerOneOnline(true);

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        assertEquals(2, bobGameEngine.players.getMyPlayerNumber());

        aliceProtocolEngine.close();
        bobProtocolEngine.close();
    }

    // Testet, ob die Namenswahl des Senders vom Empfaenger fuer den Sender uebernommen wird.
    @Test
    public void networkTestSetPlayerName() throws IOException, InterruptedException, GameException {
        FourConnectGame aliceGameEngine = new FourConnectGame(true);
        FourConnectProtocolEngine aliceProtocolEngine = new FourConnectProtocolEngine(aliceGameEngine);
        aliceGameEngine.setProtocolEngine(aliceProtocolEngine);

        FourConnectGame bobGameEngine = new FourConnectGame(true);
        FourConnectProtocolEngine bobProtocolEngine = new FourConnectProtocolEngine(bobGameEngine);
        bobGameEngine.setProtocolEngine(bobProtocolEngine);

        int port = this.getPortNumber();

        TCPStream aliceSide = new TCPStream(port, true, "aliceSide");
        TCPStream bobSide = new TCPStream(port, false, "bobSide");

        aliceSide.start();
        bobSide.start();
        aliceSide.waitForConnection();
        bobSide.waitForConnection();
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        aliceProtocolEngine.handleConnection(aliceSide.getInputStream(), aliceSide.getOutputStream());
        bobProtocolEngine.handleConnection(bobSide.getInputStream(), bobSide.getOutputStream());

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        aliceGameEngine.setPlayersName("Alice", 1);
        aliceProtocolEngine.setPlayersName("Alice", 1);

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        assertEquals(bobGameEngine.players.getPlayersName(1), "Alice");

        aliceProtocolEngine.close();
        bobProtocolEngine.close();
    }

    // Testet, ob die Farbenauswahl des Senders vom Empfaenger fuer den Sender uebernommen wird.
    @Test
    public void networkTestPickColour() throws GameException, IOException, InterruptedException {
        FourConnectGame aliceGameEngine = new FourConnectGame(true);
        FourConnectProtocolEngine aliceProtocolEngine = new FourConnectProtocolEngine(aliceGameEngine);
        aliceGameEngine.setProtocolEngine(aliceProtocolEngine);

        FourConnectGame bobGameEngine = new FourConnectGame(true);
        FourConnectProtocolEngine bobProtocolEngine = new FourConnectProtocolEngine(bobGameEngine);
        bobGameEngine.setProtocolEngine(bobProtocolEngine);

        int port = this.getPortNumber();

        TCPStream aliceSide = new TCPStream(port, true, "aliceSide");
        TCPStream bobSide = new TCPStream(port, false, "bobSide");

        aliceSide.start();
        bobSide.start();
        aliceSide.waitForConnection();
        bobSide.waitForConnection();

        aliceProtocolEngine.handleConnection(aliceSide.getInputStream(), aliceSide.getOutputStream());
        bobProtocolEngine.handleConnection(bobSide.getInputStream(), bobSide.getOutputStream());

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        aliceGameEngine.pickColour(1, FourConnectPieceColour.BLUE);
        aliceProtocolEngine.pickColour(1, FourConnectPieceColour.BLUE);

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        assertEquals(aliceGameEngine.players.getColourByPlayerNumber(1), FourConnectPieceColour.BLUE);
        assertEquals(bobGameEngine.players.getColourByPlayerNumber(1), FourConnectPieceColour.BLUE);
        assertEquals(aliceGameEngine.players.getColourByPlayerNumber(2), FourConnectPieceColour.RED);
        assertEquals(bobGameEngine.players.getColourByPlayerNumber(2), FourConnectPieceColour.RED);

        aliceProtocolEngine.close();
        bobProtocolEngine.close();
    }

    //Testet, ob der set des Senders auch in der Game Engine des Empfaengers gleichermassen ausgefuehrt wird
    @Test
    public void networkTestSet() throws GameException, IOException, InterruptedException {
        FourConnectGame aliceGameEngine = new FourConnectGame(true);
        FourConnectProtocolEngine aliceProtocolEngine = new FourConnectProtocolEngine(aliceGameEngine);
        aliceGameEngine.setProtocolEngine(aliceProtocolEngine);

        FourConnectGame bobGameEngine = new FourConnectGame(true);
        FourConnectProtocolEngine bobProtocolEngine = new FourConnectProtocolEngine(bobGameEngine);
        bobGameEngine.setProtocolEngine(bobProtocolEngine);

        int port = this.getPortNumber();

        TCPStream aliceSide = new TCPStream(port, true, "aliceSide");
        TCPStream bobSide = new TCPStream(port, false, "bobSide");

        aliceSide.start();
        bobSide.start();
        aliceSide.waitForConnection();
        bobSide.waitForConnection();

        aliceProtocolEngine.handleConnection(aliceSide.getInputStream(), aliceSide.getOutputStream());
        bobProtocolEngine.handleConnection(bobSide.getInputStream(), bobSide.getOutputStream());

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        aliceGameEngine.set(0);
        aliceProtocolEngine.set(0);

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        assertEquals(bobGameEngine.getMatchfield().getFieldValue(0,0), 1);

        aliceProtocolEngine.close();
        bobProtocolEngine.close();
    }

/*
In dem folgenden Test werden zwei komplette Spiele durchlaufen und zwischendurch wird immer gestestet, ob auf beiden
Seiten die entsprechenden Informationen korrekt gespeichert wurden, bzw. ob das Spiel auf beiden Geräten synchron lauft.

1. Alice ist Spieler 1 und faengt an
2. Beide Spieler geben Ihre Namen an
3. Alice waehlt die roten Spielsteine
4. Alice gewinnt
5. Bob faengt an
6. Bob gewinnt

1. Spiel - Alice gewinnt
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|_0_|___|___|___|___|___|___|
|_0_|_X_|___|___|___|___|___|
|_0_|_X_|___|___|___|___|___|
|_0_|_X_|___|___|___|___|___|
2. Spiel - Bob gewinnt
|_1_|_2_|_3_|_4_|_5_|_6_|_7_|
|___|___|___|___|___|___|___|
|___|___|___|___|___|___|___|
|_X_|___|___|___|___|___|___|
|_X_|_0_|___|___|___|___|___|
|_X_|_0_|___|___|___|___|___|
|_X_|_0_|___|___|___|___|___|
*/

    @Test
    public void networkTestTwoFullGames() throws GameException, IOException, InterruptedException {
        FourConnectGame aliceGameEngine = new FourConnectGame(true);
        FourConnectProtocolEngine aliceProtocolEngine = new FourConnectProtocolEngine(aliceGameEngine);
        aliceGameEngine.setProtocolEngine(aliceProtocolEngine);

        FourConnectGame bobGameEngine = new FourConnectGame(true);
        FourConnectProtocolEngine bobProtocolEngine = new FourConnectProtocolEngine(bobGameEngine);
        bobGameEngine.setProtocolEngine(bobProtocolEngine);

        int port = this.getPortNumber();

        TCPStream aliceSide = new TCPStream(port, true, "aliceSide");
        TCPStream bobSide = new TCPStream(port, false, "bobSide");

        aliceSide.start();
        bobSide.start();
        aliceSide.waitForConnection();
        bobSide.waitForConnection();

        aliceProtocolEngine.handleConnection(aliceSide.getInputStream(), aliceSide.getOutputStream());
        bobProtocolEngine.handleConnection(bobSide.getInputStream(), bobSide.getOutputStream());

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        aliceGameEngine.setPlayerOneOnline(true);
        aliceProtocolEngine.setPlayerOneOnline(true);

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        assertEquals(1, aliceGameEngine.players.getMyPlayerNumber());
        assertEquals(2, bobGameEngine.players.getMyPlayerNumber());

        aliceGameEngine.setPlayersName("Alice", 1);
        aliceProtocolEngine.setPlayersName("Alice", 1);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        bobGameEngine.setPlayersName("Bob", 2);
        bobProtocolEngine.setPlayersName("Bob", 2);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        assertEquals("Alice", bobGameEngine.getPlayersname(1));
        assertEquals("Bob", aliceGameEngine.getPlayersname(2));

        aliceGameEngine.pickColour(1, FourConnectPieceColour.RED);
        aliceProtocolEngine.pickColour(1, FourConnectPieceColour.RED);

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        assertEquals(FourConnectPieceColour.BLUE, bobGameEngine.getColourByPlayerNumber(2));

        aliceGameEngine.set(0);
        aliceProtocolEngine.set(0);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        bobGameEngine.set(1);
        bobProtocolEngine.set(1);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        aliceGameEngine.set(0);
        aliceProtocolEngine.set(0);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        bobGameEngine.set(1);
        bobProtocolEngine.set(1);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        aliceGameEngine.set(0);
        aliceProtocolEngine.set(0);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        bobGameEngine.set(1);
        bobProtocolEngine.set(1);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        aliceGameEngine.set(0);
        aliceProtocolEngine.set(0);

        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        assertEquals(aliceGameEngine.getScoreboard(1), 1);
        assertEquals(bobGameEngine.getScoreboard(1), 1);
        assertEquals(aliceGameEngine.getScoreboard(2), 0);
        assertEquals(bobGameEngine.getScoreboard(2), 0);

        bobGameEngine.set(1);
        bobProtocolEngine.set(1);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        aliceGameEngine.set(0);
        aliceProtocolEngine.set(0);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        bobGameEngine.set(1);
        bobProtocolEngine.set(1);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        aliceGameEngine.set(0);
        aliceProtocolEngine.set(0);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        bobGameEngine.set(1);
        bobProtocolEngine.set(1);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        aliceGameEngine.set(0);
        aliceProtocolEngine.set(0);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        bobGameEngine.set(1);
        bobProtocolEngine.set(1);
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        assertEquals(aliceGameEngine.getScoreboard(1), 1);
        assertEquals(bobGameEngine.getScoreboard(1), 1);
        assertEquals(aliceGameEngine.getScoreboard(2), 1);
        assertEquals(bobGameEngine.getScoreboard(2), 1);

        aliceProtocolEngine.close();
        bobProtocolEngine.close();
    }
}