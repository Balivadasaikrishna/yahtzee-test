package pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DieValidResponse {

    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private Data data;
}
