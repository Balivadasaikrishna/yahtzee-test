package pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.testng.collections.Lists;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllDices {

    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private List<Data> data = Lists.newArrayList();
}
