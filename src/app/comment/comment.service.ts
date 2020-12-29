import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CommentPayload} from "./comment.payload";

@Injectable({
    providedIn: 'root'
})
export class CommentService {

    constructor(private httpClient: HttpClient) {
    }

    postComment(commentPayload: CommentPayload): Observable<[]> {
        console.log("postComment")
        return this.httpClient.post<any>('http://localhost:8080/api/comments/', commentPayload);
    }

    getAllCommentsForPost(postId: number): Observable<CommentPayload[]> {
        console.log("getAllCommentsForPost")
        return this.httpClient.get<CommentPayload[]>('http://localhost:8080/api/comments/by-post/' + postId);
    }

    getAllCommentsByUser(name: string): Observable<CommentPayload[]> {
        console.log("getAllCommentsByUser")
        return this.httpClient.get<CommentPayload[]>('http://localhost:8080/api/comments/by-user/' + name);
    }

}
