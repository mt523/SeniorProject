package com.mt523.backtalk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.mt523.backtalk.packets.client.CardPacket;
import com.mt523.backtalk.packets.client.ClientPacket;
import com.mt523.backtalk.packets.client.NothingPacket;
import com.mt523.backtalk.packets.server.ServerPacket;
import com.mt523.backtalk.packets.server.ServerPacket.IBackTalkServer;

class BackTalkServer {

    // ServerSocket to open socket connections with clients
    private ServerSocket serverSocket;

    // Port that server listens on
    private static final int PORT = 4242;

    // Database connection object
    private Connection connection;

    // Object to interface with database
    private Statement statement;

    // Queries to be run on the database
    private String query;

    // Set of cards to be served to clients
    private Vector<CardPacket> deck = new Vector<>();

    // Address of the database which holds the content
    private final String dbAddr = "jdbc:mysql://backtalk.cri0r2kpn2sn.us-west-1.rds.amazonaws.com/"
            + "backtalk?user=admin&password=theH3r0ofCanton";

    public BackTalkServer() {

        try {
            // Connect to database ---------------------------------------------
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(dbAddr);
            statement = connection.createStatement();

            // Populate Deck ---------------------------------------------------
            query = "SELECT * FROM cards";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                deck.add(new CardPacket(resultSet.getInt("id"), resultSet
                        .getString("question"), resultSet.getString("answer"),
                        resultSet.getString("category")));
                System.out.println(deck.lastElement().toString());
            }

            // Initialize server -----------------------------------------------
            serverSocket = new ServerSocket(PORT);
            System.out.printf("Server listening on port %d.\n", PORT);
            while (true) {
                new ServerWorker(serverSocket.accept()).start();
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ServerWorker extends Thread implements IBackTalkServer {

        private Socket socket;
        ObjectInputStream input;
        ObjectOutputStream output;
        private ServerPacket serverPacket;

        public ServerWorker(Socket socket) {
            this.socket = socket;
            System.out.printf("Connected to %s.\n",
                    socket.getRemoteSocketAddress());
        }

        @Override
        public void run() {
            super.run();
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                serverPacket = (ServerPacket) input.readObject();
                serverPacket.setServer(this);
                serverPacket.handlePacket();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void serveImage(int id) {
            try {
                output.writeObject(deck.get(id - 1));
            } catch (IndexOutOfBoundsException e) {
                try {
                    output.writeObject(new NothingPacket());
                } catch (IOException ee) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new BackTalkServer();
    }
}
