package me.deepak.rmq.tutorial3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import me.deepak.rmq.tutorial2.Task;

/*
 * https://www.rabbitmq.com/tutorials/tutorial-three-java.html
*/
public class EmitLog {

	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (Connection connection = factory.newConnection();
				Channel channel = connection.createChannel();
				BufferedReader reader = new BufferedReader(
						new FileReader(new File(Task.class.getResource("/input").getFile())))) {
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

			String message;
			while ((message = reader.readLine()) != null) {

				channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
				System.out.println(" [x] Sent '" + message + "'");
			}
		}
	}

}
