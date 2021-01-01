export class SubredditModel {
    id?: number;
    name: string;
    description: string;
    numberOfPosts?: number;

    constructor(id: number, name: string, description: string, numberOfPosts: number) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfPosts = numberOfPosts;
    }
}
