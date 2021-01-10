package fourconnect;

import network.ProtocolEngine;

import java.io.*;
import java.util.Random;

public class FourConnectProtocolEngine implements ProtocolEngine, FourConnect, Runnable {

    private OutputStream os;
    private InputStream is;
    private final FourConnect gameEngine;

    public static final int METHOD_SETPLAYERONE = 1;
    public static final int METHOD_SETPLAYERSNAME = 2;
    public static final int METHOD_PICKCOLOUR = 3;
    public static final int METHOD_SET = 4;
    public static final int METHOD_RESET = 5;

    private Thread protocolThread = null;
    private Thread pickWaitThread = null;
    private boolean oracle;
    private boolean oracleSet = false;

    public FourConnectProtocolEngine(FourConnect gameEngine){
        this.gameEngine = gameEngine;
    }

    @Override
    public void handleConnection(InputStream is, OutputStream os) throws IOException {
        this.is = is;
        this.os = os;

        this.protocolThread = new Thread(this);
        this.protocolThread.start();
    }

    @Override
    public void setPlayerOneOnline(boolean sender) throws GameException {
        DataOutputStream dos = new DataOutputStream(this.os);
        try{
            dos.writeInt(METHOD_SETPLAYERONE);
            if(sender)
                dos.writeBoolean(false);
            else
                dos.writeBoolean(true);
        } catch (IOException e) {
            throw new GameException("could not serialize command", e);
        }
    }

    public void deserializeSetPlayerOneOnline() throws GameException {
        DataInputStream dis = new DataInputStream(this.is);
        try{
            boolean sender = dis.readBoolean();
            this.gameEngine.setPlayerOneOnline(sender);
        } catch (IOException e) {
            throw new GameException("could not serialize command", e);
        }
    }

    @Override
    public void setPlayersName(String playerName, int playerNumber) throws GameException {
        DataOutputStream dos = new DataOutputStream(this.os);
        try{
            dos.writeInt(METHOD_SETPLAYERSNAME);
            dos.writeUTF(playerName);
            dos.writeInt(playerNumber);
        } catch (IOException e) {
            throw new GameException("could not serialize command", e);
        }
    }

    public void deserializesetPlayersName() throws GameException{
        DataInputStream dis = new DataInputStream(this.is);
        try{
            String playerName = dis.readUTF();
            int playerNumber = dis.readInt();
            this.gameEngine.setPlayersName(playerName, playerNumber);
        } catch (IOException e) {
            throw new GameException("could not deserialize command", e);
        }
    }

    @Override
    public void pickColour(int playerNumber, FourConnectPieceColour colour) throws GameException {
        DataOutputStream dos = new DataOutputStream(this.os);
        try{
            dos.writeInt(METHOD_PICKCOLOUR);
            dos.writeInt(playerNumber);
            dos.writeUTF(colour.toString());
        } catch (IOException e) {
            throw new GameException("could not serialize command", e);
        }
    }

    public void deserializePickColour() throws GameException {
        DataInputStream dis = new DataInputStream(this.is);
        try{
            int playerNumber = dis.readInt();
            String colour = dis.readUTF();
            if(colour.equals(FourConnectPieceColour.RED.toString()) || colour.equals(FourConnectPieceColour.BLUE.toString())) {
                if(colour.equals(FourConnectPieceColour.RED.toString()))
                    this.gameEngine.pickColour(playerNumber, FourConnectPieceColour.RED);
                else
                    this.gameEngine.pickColour(playerNumber, FourConnectPieceColour.BLUE);
            }
            else
                throw new IOException();
        } catch (IOException e) {
            throw new GameException("could not deserialize command", e);
        }
    }

    @Override
    public boolean set(int x) throws GameException {
        DataOutputStream dos = new DataOutputStream(this.os);
        try{
            dos.writeInt(METHOD_SET);
            dos.writeInt(x);
        } catch (IOException e) {
            throw new GameException("could not serialize command", e);
        }
        return false;
    }

    private void deserializeSet() throws GameException {
        DataInputStream dis = new DataInputStream(this.is);
        try{
            int column = dis.readInt();
            this.gameEngine.set(column);
        } catch (IOException e) {
            throw new GameException("could not deserialize command", e);
        }
    }

    @Override
    public boolean reset(boolean sender) throws GameException {
        DataOutputStream dos = new DataOutputStream(this.os);
        try{
            dos.writeInt(METHOD_RESET);
            if(sender)
                dos.writeBoolean(false);
            else
                dos.writeBoolean(true);
        } catch (IOException e) {
            throw new GameException("could not serialize command", e);
        }
        return false;
    }

    private void deserializeReset() throws GameException {
        DataInputStream dis = new DataInputStream(this.is);
        try{
            boolean sender = dis.readBoolean();
            this.gameEngine.reset(sender);
        } catch (GameException | IOException e) {
            throw new GameException("could not deserialize command", e);
        }
    }

    boolean read() throws GameException{
        DataInputStream dis = new DataInputStream(this.is);
        try{
            int commandID = dis.readInt();
            switch(commandID){
                case METHOD_SETPLAYERONE: this.deserializeSetPlayerOneOnline(); return true;
                case METHOD_SETPLAYERSNAME: this.deserializesetPlayersName(); return true;
                case METHOD_PICKCOLOUR: this.deserializePickColour(); return true;
                case METHOD_SET: this.deserializeSet(); return true;
                case METHOD_RESET: this.deserializeReset(); return true;
            }
        } catch (IOException e) {
            try{
                this.close();
            } catch (IOException ioException) {
                return false;
            }
        }
        return false;
    }

    //Code übernahme

    @Override
    public void run() {
        this.log("Protocol Engine started - flip a coin");
        long seed = this.hashCode() * System.currentTimeMillis();
        Random random = new Random(seed);

        int localInt = 0, remoteInt = 0;
        try {
            do {
                localInt = random.nextInt();
                this.log("flip and take number " + localInt);
                DataOutputStream dos = new DataOutputStream(this.os);
                dos.writeInt(localInt);
                DataInputStream dis = new DataInputStream(this.is);
                remoteInt = dis.readInt();
            } while(localInt == remoteInt);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.oracle = localInt < remoteInt;
        this.log("Flipped a coin and got an oracle == " + this.oracle);
        this.oracleSet = true;

        try {
            boolean again = true;
            while(again) {
                again = this.read();
            }
        } catch (GameException e) {
            this.logError("exception called in protocol engine thread - fatal and stop");
            e.printStackTrace();
            // leave while - end thread
        }
    }

    @Override
    public void close() throws IOException {
        if(this.os != null) { this.os.close();}
        if(this.is != null) { this.is.close();}
    }

    @Override
    public boolean getOracle() throws GameException {
        this.log("asked for an oracle - return " + this.oracle);
        return this.oracle;
    }

    private String produceLogString(String message) {
        StringBuilder sb = new StringBuilder();

        sb.append(message);

        return sb.toString();
    }

    private void log(String message) {
        System.out.println(this.produceLogString(message));
    }

    private void logError(String message) {
        System.err.println(this.produceLogString(message));
    }
}
