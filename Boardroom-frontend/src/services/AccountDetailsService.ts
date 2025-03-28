
export interface User {
  id: number;
  email: string;
  // this is a string for display, before sending it to services turn it into boolean
  owner: string;
  name: string;
}

export interface UserRequest extends User {
    password: string;
}


// i actually dont need to fetch, it should be done on login


export const toggleAccountType = async (
    user: User | null,
    setUserData: (user: User | null) => void
): Promise<User | void> => {

    if (user == null) return
    
    // toggle the account type 
    const updatedUser: User = { 
      ... user 
    }
    
    
    // update context
    setUserData(updatedUser);
    
    
};


// you can still pass in user for the owner and id since those aren't changed and email
export const updateAccountInfo = async (
    user: User | null,
    name: string,
    password: string,
    setUserData: (user: User | null) => void
): Promise<User | void> => {

    if (user == null) return


    // do the fetch before updating here, fetch should be in the .then()


    const updatedUser: UserRequest = {
       ... user,
       name,
       password 
    }


    // update context
    setUserData(updatedUser);
}
