import React, { createContext, useContext, useState  } from "react"

import { User } from "@/services/AccountDetailsService"


interface AuthContextType {
    userData: User | null,
    setUserData: (user: User) => void
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

    const setUserData = (user: User) => {
        setUser(user) 
    }

    return (
       <AuthContext.Provider value={{ userData, setUserData }}>
            { children }
       </AuthContext.Provider>
    )
}




