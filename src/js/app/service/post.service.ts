import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

// import {CreatePostPayload, PostModel} from "../utill/class1";
import {url1} from "../utill/url1";
import {PostRequestDto, PostResponseDto} from "../utill/interface1";

@Injectable({
    providedIn: 'root'
})
export class PostService {

    constructor(private http: HttpClient) {
    }

    getAllPosts(): Observable<PostResponseDto[]> {
        return this.http.get<PostResponseDto[]>(url1.getAllPosts);
    }

    createPost(postPayload: PostRequestDto): Observable<any> {

        return this.http.post(url1.createPost, postPayload);
    }

    getPostById(id: number): Observable<PostResponseDto> {
        return this.http.get<PostResponseDto>(url1.getPostById + id);
    }

    getAllPostsByUser(name: string): Observable<PostResponseDto[]> {
        return this.http.get<PostResponseDto[]>(url1.getAllPostsByUser + name);
    }

    getAllPostsBySub(subId: number): Observable<PostResponseDto[]> {
        return this.http.get<PostResponseDto[]>(url1.getAllPostsBySub + subId);
    }


}
