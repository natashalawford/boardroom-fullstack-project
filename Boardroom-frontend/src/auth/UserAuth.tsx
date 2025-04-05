import React, { createContext, useContext, useEffect, useState  } from "react"

import { User } from "@/services/AccountDetailsService"


interface AuthContextType {
    userData: User | null,
    setUserData: (user: User | null) => void
}


export const AuthContext = createContext<AuthContextType>({
    userData: null,
    setUserData: () => {},
});


export function useAuth() {
    return useContext(AuthContext);
}


export const AuthContextProvider = ({ children }: { children: React.ReactNode }) => {
    
    const [userData, setUser] = useState<User|null>(null);

    const setUserData = (user: User | null) => {
        setUser(user) 

        // update local storage
        if (user == null) {
            localStorage.removeItem('userAuth');
        } else {
            localStorage.setItem('userAuth', JSON.stringify(user));
        }
    }
    
    // check local storage on mount
    useEffect(() => {
        const storedInfo = localStorage.getItem('userAuth');

        const storedUser = storedInfo ? JSON.parse(storedInfo) : null;

        setUserData(storedUser);
    },[])

    return (
       <AuthContext.Provider value={{ userData, setUserData }}>
            { children }
       </AuthContext.Provider>
    )
}




