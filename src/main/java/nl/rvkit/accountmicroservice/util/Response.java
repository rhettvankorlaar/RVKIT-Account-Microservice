package nl.rvkit.accountmicroservice.util;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Response<T> {
    private final T data;
    private final Object error;
    private final int status;
    private final Date timestamp;

    public Response(int status, T data, Object error){
        this.status = status;
        this.data = data;
        this.error = error;
        this.timestamp = new Date();
    }
}
