package at.javatraining.admin;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.TopicDescription;

import java.util.Map;
import java.util.Set;

public class MainApp {
    public static void main(String[] args) throws Exception{
        Map<String, Object> properties = Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (
                Admin admin = Admin.create(properties);
                ) {
            Set<String> topics = admin.listTopics().names().get();

            Map<String, TopicDescription> topicDescriptions = admin.describeTopics(topics).allTopicNames().get();
            topicDescriptions.values().forEach(topicDescription -> System.out.println(">> topic=" + topicDescription.name() +
                    ", partitions=" + topicDescription.partitions().size()));
        }
    }
}
