package cn.balalals.liveboost.handler;

public interface IMessageHandler<T> {
    void handler(T msg);
}
