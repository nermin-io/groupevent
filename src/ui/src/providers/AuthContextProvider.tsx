import AuthContext from "@/context/auth";
import React from 'react';
import { UserSession } from "@/lib/session";

interface Props {
    children: React.ReactNode | React.ReactNode[];
    session: UserSession;
}

const AuthContextProvider: React.FC<Props> = ({ children, session }) => {
    const contextValue = {
        session,
    };

    return (
        <AuthContext.Provider value={contextValue}>
            {children }
        </AuthContext.Provider>
    );
}

export default AuthContextProvider;