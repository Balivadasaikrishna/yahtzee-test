package pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Data {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("value")
    private Integer value;

}
