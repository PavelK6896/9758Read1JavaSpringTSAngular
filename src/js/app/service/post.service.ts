import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

import {CreatePostPayload, PostModel} from "../utill/class1";
import {url1} from "../utill/url1";

@Injectable({
    providedIn: 'root'
})
export class PostService {

    constructor(private http: HttpClient) {
    }

    getAllPosts(): Observable<Array<PostModel>> {
        return this.http.get<Array<PostModel>>(url1.getAllPosts);
    }

    createPost(postPayload: CreatePostPayload): Observable<any> {

        console.log("----------------")
        console.log("----------------")
        console.log("----------------")
        console.log("----------------")
        return this.http.post(url1.createPost, postPayload);
    }

    getPostById(id: number): Observable<PostModel> {
        return this.http.get<PostModel>(url1.getPostById + id);
    }

    getAllPostsByUser(name: string): Observable<PostModel[]> {
        return this.http.get<PostModel[]>(url1.getAllPostsByUser + name);
    }

    getAllPostsBySub(subId: number): Observable<PostModel[]> {
        return this.http.get<PostModel[]>(url1.getAllPostsBySub + subId);
    }


}
