// used for info on frontend
export interface User {
  id: number;
  name: string;
  email: string;
  owner: string;
}

// used for request to backend
export interface UserRequest {
  name: string;
  email: string;
  owner: boolean;
}

// used for response from backend
interface UserResponse {
  id: number;
  name: string;
  email: string;
  owner: boolean;
}

interface PasswordRequest {
  oldPassword: string;
  newPassword: string;
}

export interface ErrorMessage {
  errorMessage: string;
}

export const toggleAccountType = async (
  user: User | null,
  owner: string,
  setUserData: (user: User) => void
): Promise<void | ErrorMessage> => {
  if (user == null) {
    return {
      errorMessage: "User must be logged in",
    };
  }

  // create request for backend
  const updatedData: UserRequest = {
    name: user.name,
    email: user.email,
    owner: owner == "true" ? true : false,
  };

  try {
    const response = await fetch(`http://localhost:8080/people/${user.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updatedData),
    });

    if (!response.ok) {
      const error = await response.json();
      return {
        // design decision to only show first error
        errorMessage: error.errors[0]
      }
      // throw new Error(error.errors.join(", "));
    }

    const updatedUserResponse: UserResponse = await response.json();

    const updatedUser: User = {
      ...updatedUserResponse,
      owner: updatedUserResponse.owner ? "true" : "false",
    };

    // update info
    setUserData(updatedUser);

  } catch (error) {
    return {
      errorMessage: String(error),
    }
  }
};

export const updatePassword = async (
  user: User | null,
  oldPassword: string,
  newPassword: string,
  setUserData: (user: User) => void
): Promise<void | ErrorMessage> => {
  if (user == null) {
    return {
      errorMessage: "User must be logged in",
    };
  }
  // if (oldPassword == "" || newPassword == "") { -- this should be taken care of by form not here
  //   return {

  //   }
  // }

  const passwordRequest: PasswordRequest = {
    oldPassword: oldPassword,
    newPassword: newPassword,
  };

  try {
    const response = await fetch(
      `http://localhost:8080/people/${user.id}/password`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(passwordRequest),
      }
    );



    if (!response.ok) {
      const error = await response.json();
      return {
        // design decision to only show first error
        errorMessage: error.errors[0]
      }
    }

    const updatedPasswordResponse: UserResponse = await response.json();

    const updatedPasswordUser: User = {
      ...updatedPasswordResponse,
      owner: updatedPasswordResponse.owner ? "true" : "false",
    };

    setUserData(updatedPasswordUser);
  } catch (error) {
    return {
      errorMessage: String(error)
    }
  }
};

// you can still pass in user for the owner and id since those aren't changed and email
export const updateAccountInfo = async (
  user: User | null,
  name: string,
  setUserData: (user: User) => void
): Promise<void | ErrorMessage> => {
  if (user == null) {
    return {
      errorMessage: "User must be logged in"
    }
  };

  // do the fetch before updating here, fetch should be in the .then()
  const userToUpdate: UserRequest = {
    name: name,
    email: user.email,
    owner: user.owner == "true" ? true : false,
  };

  try {
    const response = await fetch(`http://localhost:8080/people/${user.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userToUpdate),
    });
    
    if (!response.ok) {
      const error = await response.json();
      return {
        // design decision to only show first error
        errorMessage: error.errors[0] 
      }
    }

    const updatedAccountResponse: UserResponse = await response.json();

    const updatedAccountUser: User = {
      ...updatedAccountResponse,
      owner: updatedAccountResponse.owner ? "true" : "false",
    };

    setUserData(updatedAccountUser);
  } catch (error) {
    return {
      errorMessage: String(error)
    }
  }
};

//   // update context
//   setUserData(updatedUser);
// };


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
  

