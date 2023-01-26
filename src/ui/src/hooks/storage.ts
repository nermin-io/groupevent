import { useContext } from 'react';
import LocalStorageContext from "@/context/storage";

const useLocalStorage = () => {
    const context = useContext(LocalStorageContext);
    const hasContextProperties = Object.keys(context).length > 0;

    if(context === undefined || !hasContextProperties)
        throw new Error('Cannot call useLocalStorage() outside of LocalStorageProvider');

    return context;
};

export default useLocalStorage;
