import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SubredditModel} from "./subreddit-response";

@Injectable({
    providedIn: 'root'
})
export class SubredditService {

    constructor(private http: HttpClient) {
    }

    getAllSubreddits(): Observable<Array<SubredditModel>> {
        return this.http.get<Array<SubredditModel>>('http://localhost:8080/api/subreddit');
    }

    getSubredditsId(subId: number): Observable<SubredditModel> {
        return this.http.get<SubredditModel>('http://localhost:8080/api/subreddit/' + subId);
    }

    createSubreddit(subredditModel: SubredditModel): Observable<SubredditModel> {
        return this.http.post<SubredditModel>('http://localhost:8080/api/subreddit', subredditModel);
    }
}
