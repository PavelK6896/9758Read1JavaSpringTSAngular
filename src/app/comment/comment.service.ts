import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CommentPayload} from "./comment.payload";
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class CommentService {

    constructor(private httpClient: HttpClient) {
    }

    postComment(commentPayload: CommentPayload): Observable<[]> {
        return this.httpClient.post<any>(environment.URL +'/api/comments/', commentPayload);
    }

    getAllCommentsForPost(postId: number): Observable<CommentPayload[]> {
        return this.httpClient.get<CommentPayload[]>(environment.URL +'/api/comments/by-post/' + postId);
    }

    getAllCommentsByUser(name: string): Observable<CommentPayload[]> {
        return this.httpClient.get<CommentPayload[]>(environment.URL +'/api/comments/by-user/' + name);
    }
}
