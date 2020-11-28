import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PostModel} from "./post-model";

@Injectable({
  providedIn: 'root'
})
export class PostService { // для постов

  constructor(private http: HttpClient) {
  }

  getAllPosts(): Observable<Array<PostModel>> {
    return this.http.get<Array<PostModel>>('http://localhost:8080/api/posts/');
  }

  // createPost(postPayload: CreatePostPayload): Observable<any> {
  //   return this.http.post('http://localhost:8080/api/posts/', postPayload);
  // }

  getPost(id: number): Observable<PostModel> {
    return this.http.get<PostModel>('http://localhost:8080/api/posts/' + id);
  }

  getAllPostsByUser(name: string): Observable<PostModel[]> {
    return this.http.get<PostModel[]>('http://localhost:8080/api/posts/by-user/' + name);
  }
}