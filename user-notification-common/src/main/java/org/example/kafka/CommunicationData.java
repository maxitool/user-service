package org.example.kafka;

public class CommunicationData {
    private CommunicationData() {
    }

    public static final String TOPIC = "email-dto";
    public static final String RECORD_HEADER_OPERATION_KEY = "operation";
    public static final int KAFKA_SEND_TIMEOUT = 7;
}
