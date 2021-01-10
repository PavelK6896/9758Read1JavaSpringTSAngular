
export class SubredditModel {
    id?: number;
    name: string;
    description: string;
    numberOfPosts?: number;

    constructor(id: number, name: string, description: string, numberOfPosts: number) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfPosts = numberOfPosts;
    }
}


// export class PostModel {
//
//     id: number;
//     postName: string;
//     url: string;
//     description: string;
//     voteCount: number;
//     userName: string;
//     subReadName: string;
//     commentCount: number;
//     duration: string;
//     upVote: boolean;
//     downVote: boolean;
//
//     constructor(id: number, postName: string, url: string, description: string, voteCount:
//                     number, userName: string, subReadName: string, commentCount: number,
//                 duration: string, upVote: boolean, downVote: boolean) {
//         this.id = id;
//         this.postName = postName;
//         this.url = url;
//         this.description = description;
//         this.voteCount = voteCount;
//         this.userName = userName;
//         this.subReadName = subReadName;
//         this.commentCount = commentCount;
//         this.duration = duration;
//         this.upVote = upVote;
//         this.downVote = downVote;
//     }
// }






export class CreatePostPayload {
    postName: string;
    subReadName?: string;
    url?: string;
    description: string;
}


export class CommentPayload {
    text: string;
    postId: number;
    username?: string;
    duration?: string;
}
