package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int port = 8989;

    public static void main(String[] args) throws IOException {

        while (true) {
            try (Socket clientSocket = new Socket(HOST, port);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String json = in.readLine();
                System.out.println(json);//???

                Scanner scanner = new Scanner(System.in);
                json = scanner.nextLine();
                out.println(json);

                json = in.readLine();
                System.out.println(json);// ok

                json = in.readLine();
                System.out.println(json);
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(json);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String pretty = gson.toJson(jsonElement);
                System.out.println(pretty);
            }
        }

    }
}

