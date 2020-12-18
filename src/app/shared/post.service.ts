import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PostModel} from "./post-model";
import {CreatePostPayload} from "../post/create-post/create-post.payload";

@Injectable({
    providedIn: 'root'
})
export class PostService { // для постов

    constructor(private http: HttpClient) {
    }

    getAllPosts(): Observable<Array<PostModel>> {
        console.log("getAllPosts")
        return this.http.get<Array<PostModel>>('http://localhost:8080/api/posts/');
    }

    createPost(postPayload: CreatePostPayload): Observable<any> {
        console.log("createPost")
        return this.http.post('http://localhost:8080/api/posts/', postPayload);
    }

    getPostById(id: number): Observable<PostModel> {
        console.log("getPostById")
        return this.http.get<PostModel>('http://localhost:8080/api/posts/' + id);
    }

    getAllPostsByUser(name: string): Observable<PostModel[]> {
        console.log("getAllPostsByUser")
        return this.http.get<PostModel[]>('http://localhost:8080/api/posts/by-user/' + name);
    }
}
