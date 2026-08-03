package tomato.com.tomato.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    private String message;
    private Object data;

    public ApiResponse(Object data){
        this.data = data;
    }
}
