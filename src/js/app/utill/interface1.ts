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

