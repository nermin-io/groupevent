import AuthContext from "../context/auth";
import { useContext } from 'react';

const useAuth = () => {
    const context = useContext(AuthContext);
    const hasContextProperties = Object.keys(context).length > 0;

    if(context === undefined || !hasContextProperties)
        throw new Error('There was no session returned by getServerSideProps');

    return context;
};

export default useAuth;
