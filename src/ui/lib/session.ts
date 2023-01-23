import type { IronSessionOptions } from 'iron-session';

export type UserSession = {
    id: string;
    first_name: string;
    last_name: string;
    email_address: string;
};

export const sessionOptions: IronSessionOptions = {
    password: `${process.env.NEXT_SESSION_PASSWORD}`,
    cookieName: 'Groupevent-Session',
    cookieOptions: {
        secure: process.env.NODE_ENV === 'production',
    }
};

declare module 'iron-session' {
    interface IronSessionData {
        user?: UserSession
    }
}