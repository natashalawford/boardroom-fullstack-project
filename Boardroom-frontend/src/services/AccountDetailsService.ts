import monopoly from "../assets/monopoly.png";

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

export interface SpecificBoardGame {
  id: number;
  description: string;
  picture: number;
  status: string;
  boardGameTitle: string;
  ownerId: number;
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

  export async function getBorrowRequestsByOwnerAndStatus(ownerId: number, status: string) {
    const response = await fetch(`http://localhost:8080/borrowRequests/pending/owner/${ownerId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(status),
    });
  
    if (!response.ok) {
      throw new Error('Failed to fetch borrow requests for owner');
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
  

// Input: userId, Output: the name of the user
// This function is used to get the name of the user from the backend
export const getUserName = async (userId: number): Promise<string> => {
  try {
    const response = await fetch(`http://localhost:8080/people/${userId}`);
    if (!response.ok) {
      throw new Error("Failed to fetch user name");
    }
    const userResponse = await response.json();
    return userResponse.name;
  } catch (error) {
    console.error(error);
    return "Unknown User";
  }
}

export async function deleteSpecificBoardGame(id: number): Promise<void> {
  try {
    const response = await fetch(`http://localhost:8080/specificboardgame/${id}`, {
      method: "DELETE",
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Failed to delete game. Server said: ${errorText}`);
    }
  } catch (error) {
    console.error("Error deleting game:", error);
    throw error;
  }
}

export async function getSpecificBoardGamesByOwner(
  ownerId: number
): Promise<SpecificBoardGame[]> {
  try {
    const response = await fetch(`http://localhost:8080/specificboardgame/owner/${ownerId}`);

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Failed to fetch games. Server said: ${errorText}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching owned games:", error);
    throw error;
  }
}




export const updateSpecificGame = async (
  id : number | undefined,
  status: string | undefined,
  description: string
): Promise<ErrorMessage|void> => {
  if (id == undefined || status == undefined) {
    return {
      errorMessage: "An error occured, please try again."
    };
  }

  const requestBody = {
    status,
    monopoly,
    description
  }

  try {
    const response = await fetch((`http://localhost:8080/specificboardgame/${id}`), {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(requestBody)
    });

    if (!response.ok) {
      const error = await response.json();

      return {
        errorMessage: error.errors[0]
      }
    }

    // successfully updated in backend, need to update it in frontend again


  } catch (error) {
    return {
      errorMessage: String(error)
    }
  }


}

export async function getEventsByParticipant(personId: number) {
  try {
    const response = await fetch(`http://localhost:8080/registration/person/${personId}/events`);

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Failed to fetch events. Server said: ${errorText}`);
    }

    return await response.json(); // this will be a list of EventResponseDto
  } catch (error) {
    console.error("Error fetching participant events:", error);
    throw error;
  }
}

export async function getBoardGameByTitle(title: string) {
  try {
    const response = await fetch(`http://localhost:8080/boardgame/${encodeURIComponent(title)}`);
    if (!response.ok) {
      throw new Error("Failed to fetch board game by title");
    }
    return await response.json();
  } catch (error) {
    console.error("Error fetching board game:", error);
    throw error;
  }
}

