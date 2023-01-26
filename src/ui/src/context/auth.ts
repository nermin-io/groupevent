import React from 'react';
import {UserSession} from "@/lib/session";

export type OrganiserAuthContext = {
    session?: UserSession
};

const AuthContext = React.createContext<OrganiserAuthContext>({});

export default AuthContext;