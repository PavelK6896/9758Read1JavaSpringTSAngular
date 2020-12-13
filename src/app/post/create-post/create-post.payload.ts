export class CreatePostPayload { // для создания поста
    postName: string;
    subredditName?: string;
    url?: string;
    description: string;
}
