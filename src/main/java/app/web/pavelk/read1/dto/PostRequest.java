package app.web.pavelk.read1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {
    private Long postId;
    private String subredditName;
    private String postName;
    private String url;
    private String description;
}
/*


{
    "subredditName":"who 1",
    "postName":"who 1",
    "url":"who 1",
    "description":"who 1"
}


*/