import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {VotePayload} from "../utill/class1";
import {url1} from "../utill/url1";

@Injectable({
    providedIn: 'root'
})
export class VoteService {

    constructor(private http: HttpClient) {
    }

    vote(votePayload: VotePayload): Observable<any> {
        return this.http.post(url1.vote, votePayload);
    }

}
