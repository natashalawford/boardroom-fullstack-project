
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

// services/AccountDetailsService.ts

export async function getBorrowRequestsByPersonAndStatus(personId: number, status: string) {
    const response = await fetch(`http://localhost:8080/borrowRequests/pending/${personId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(status),
    });
  
    if (!response.ok) {
      throw new Error('Failed to fetch borrow requests');
    }
  
    return await response.json();
  }

  export async function updateBorrowRequestStatus(id: number, status: string) {
    const response = await fetch(`http://localhost:8080/borrowRequests/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(status), // assuming your RequestStatus class wraps it like { status: "APPROVED" }
    });
  
    if (!response.ok) {
      throw new Error("Failed to update borrow request status");
    }
  
    return await response.json();
  }
  

  
  
  
