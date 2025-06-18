package client;

import transfer.Request;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.io.IOException;

public class Client {
    private DatagramChannel datagramChannel;
    private final String HOST;
    private final int PORT;
    private final boolean isBlocking;

    public Client(String host, int port, boolean isBlocking) {
        this.HOST = host;
        this.PORT = port;
        this.isBlocking = isBlocking;
    }

    public void open() throws IOException {
        this.datagramChannel = DatagramChannel.open();
        this.datagramChannel.configureBlocking(this.isBlocking);
    }


    public void sendRequest(Request request) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            byte[] objectBytes = byteArrayOutputStream.toByteArray();
            sendRequest(objectBytes);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void sendRequest(byte[] message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message);
        SocketAddress serverAdress = new InetSocketAddress(this.HOST, this.PORT);
        this.datagramChannel.send(buffer, serverAdress);
        System.out.println("Отправка запроса серверу. Пожалуйста не отключайтесь, скоро получим ответ...");
    }
    public Object receiveResponse(int timeout) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(65536);
        if(!isBlocking) {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < timeout) {
                buffer.clear();
                SocketAddress senderAdress = this.datagramChannel.receive(buffer);
                if (senderAdress != null) {
                    buffer.flip();
                    byte[] receivedBytes = new byte[buffer.remaining()];
                    buffer.get(receivedBytes);
                    try {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receivedBytes);
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        return objectInputStream.readObject();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Клиент бяка( - разорвал соединение");
                }
            }
            throw new IOException("Сервер бука - не отправил ответа");
        }
        return null;
    }
}
