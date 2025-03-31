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

export const login = async (
  email: string,
  password: string,
  setUserData: (user: User) => void
): Promise<void> => {
  const loginDto = {
    email: email,
    password: password,
  };

  try {
    const response = await fetch(`http://localhost:8080/people/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginDto),
    });

    if (!response.ok) {
      const error = await response.json();
      console.error(error);
      //   throw new Error(error.errors.join(", "));
    }

    const userResponse = await response.json();

    const user: User = {
      ...userResponse,
      owner: userResponse.owner ? "true" : "false",
    };

    setUserData(user);
  } catch (error) {
    console.error(error);
  }
};

export const toggleAccountType = async (
  user: User | null,
  owner: string,
  setUserData: (user: User) => void
): Promise<void> => {
  if (user == null) {
    return;
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
      throw new Error(error.errors.join(", "));
    }

    const updatedUserResponse: UserResponse = await response.json();

    const updatedUser: User = {
      ...updatedUserResponse,
      owner: updatedUserResponse.owner ? "true" : "false",
    };

    // update info
    setUserData(updatedUser);
  } catch (error) {
    console.error(error);
  }
};

export const updatePassword = async (
  user: User | null,
  oldPassword: string,
  newPassword: string,
  setUserData: (user: User) => void
): Promise<User | void> => {
  if (user == null || oldPassword == "" || newPassword == "") {
    return;
  }

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
      const errorMessage = await response.json();
      throw new Error(errorMessage.errors.join(", "));
    }

    const updatedPasswordResponse: UserResponse = await response.json();

    const updatedPasswordUser: User = {
      ...updatedPasswordResponse,
      owner: updatedPasswordResponse.owner ? "true" : "false",
    };

    setUserData(updatedPasswordUser);
  } catch (error) {
    console.error(error);
  }
};

// you can still pass in user for the owner and id since those aren't changed and email
export const updateAccountInfo = async (
  user: User | null,
  name: string,
  setUserData: (user: User) => void
): Promise<User | void> => {
  if (user == null) return;

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
      const errorMessage = await response.json();
      throw new Error(errorMessage.errors.join(", "));
    }

    const updatedAccountResponse: UserResponse = await response.json();

    const updatedAccountUser: User = {
      ...updatedAccountResponse,
      owner: updatedAccountResponse.owner ? "true" : "false",
    };

    setUserData(updatedAccountUser);
  } catch (error) {}
};
