package app.web.pavelk.read1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
    private Long id;
    private Long postId;
    private Instant createdDate;
    private String text;
    private String userName;
}

/*
{
"postId":"3",
"text":"ccccccdfsdferew3",
"userName":"asdsdas"
}
 */

