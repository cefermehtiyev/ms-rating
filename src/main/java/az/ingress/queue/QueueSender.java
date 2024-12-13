package az.ingress.queue;


public interface QueueSender<T> {
    void sendToQueue(T payload);
}


