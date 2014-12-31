package cn.ac.iie.model;

/**
 * Created by wangyang on 14/12/31.
 */
public class BindRequest {
    private RequestHeader header;
    private String chanllenge;
    private Policy policy;
    private Transaction transaction;

    public RequestHeader getHeader() {
        return header;
    }

    public void setHeader(RequestHeader header) {
        this.header = header;
    }

    public String getChanllenge() {
        return chanllenge;
    }

    public void setChanllenge(String chanllenge) {
        this.chanllenge = chanllenge;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    class Transaction{
        String contentType;
        String content;
    }
}
