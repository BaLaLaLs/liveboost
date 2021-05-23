package handler;

public interface IMessageHandler<T> {
    void handler(T msg);
}
