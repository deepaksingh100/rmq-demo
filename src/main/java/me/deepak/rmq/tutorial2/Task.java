package me.deepak.rmq.tutorial2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/*
 * https://www.rabbitmq.com/tutorials/tutorial-two-java.html
*/
public class Task {

	private final static String TASK_QUEUE_NAME = "task_q";

	public static void main(String[] a) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (Connection connection = factory.newConnection();
				Channel channel = connection.createChannel();
				BufferedReader reader = new BufferedReader(
						new FileReader(new File(Task.class.getResource("/input").getFile())))) {
			channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
			String message;
			while ((message = reader.readLine()) != null) {
				channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
						message.getBytes(StandardCharsets.UTF_8));
				System.out.println(" [x] Sent '" + message + "'");
			}
		}
	}
}