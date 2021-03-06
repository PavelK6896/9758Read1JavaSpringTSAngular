export interface SignupRequestPayload {
    username: string;
    password: string;
    email: string;
}

export interface LoginResponse {
    authenticationToken: string;
    refreshToken: string;
    expiresAt: Date;
    username: string;
}


export interface LoginRequestPayload {
    username: string;
    password: string;
}


export interface PostRequestDto {
    subReadName: string
    postName: string
    description: string
}


export interface PostResponseDto {
    id: number
    postName: string
    description: string
    userName: string
    subReadName: string
    subReadId: number
    voteCount: number
    commentCount: number
    duration: string
    vote: string
}

export interface VoteDto {
    voteType: VoteType;
    postId: number;
}

export enum VoteType {
    UP_VOTE,
    DOWN_VOTE
}
