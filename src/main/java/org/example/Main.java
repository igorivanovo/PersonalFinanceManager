package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int port = 8989;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Categories.readFile();
        Categories categories = new Categories();
        File file = new File("data.bin");
        file.createNewFile();
        if (file.length() > 0) {
            categories = Categories.loadFromBinFile(file);
        }
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            String json;
            while (true) {
                try (Socket clientSocket = serverSocket.accept(); // ждем подключения
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    System.out.println("New connection accepted  " + clientSocket.getPort());
                    out.println("???");

                    json = in.readLine();
                    out.println("OK");

                    Object obj = new JSONParser().parse(json);
                    JSONObject jo = (JSONObject) obj;
                    String data = (String) jo.get("date");
                    String title = (String) jo.get("title");
                    long sum = (long) jo.get("sum");
                    categories.add(title, data, sum);

                    json = categories.maxCategory(data);
                    out.println(json);
                    categories.saveBin(file);
                } catch (IOException e) {
                    System.out.println("Не могу стартовать сервер");
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}

