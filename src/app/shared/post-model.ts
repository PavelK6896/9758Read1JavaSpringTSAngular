export class PostModel {

    id: number;
    postName: string;
    url: string;
    description: string;
    voteCount: number;
    userName: string;
    subReadName: string;
    commentCount: number;
    duration: string;
    upVote: boolean;
    downVote: boolean;

    constructor(id: number, postName: string, url: string, description: string, voteCount:
                    number, userName: string, subReadName: string, commentCount: number,
                duration: string, upVote: boolean, downVote: boolean) {
        this.id = id;
        this.postName = postName;
        this.url = url;
        this.description = description;
        this.voteCount = voteCount;
        this.userName = userName;
        this.subReadName = subReadName;
        this.commentCount = commentCount;
        this.duration = duration;
        this.upVote = upVote;
        this.downVote = downVote;
    }
}


