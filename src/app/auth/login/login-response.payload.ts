export interface LoginResponse { //принемает при входе в систему
    authenticationToken: string;
    refreshToken: string;
    expiresAt: Date;
    username: string;
}
