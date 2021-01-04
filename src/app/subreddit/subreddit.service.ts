import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SubredditModel} from "./subreddit-response";
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class SubredditService {

    constructor(private http: HttpClient) {
    }

    getAllSubreddits(): Observable<Array<SubredditModel>> {
        return this.http.get<Array<SubredditModel>>(environment.URL + '/api/subreddit');
    }

    getSubredditsId(subId: number): Observable<SubredditModel> {
        return this.http.get<SubredditModel>(environment.URL + '/api/subreddit/' + subId);
    }

    createSubreddit(subredditModel: SubredditModel): Observable<SubredditModel> {
        return this.http.post<SubredditModel>(environment.URL + '/api/subreddit', subredditModel);
    }
}
