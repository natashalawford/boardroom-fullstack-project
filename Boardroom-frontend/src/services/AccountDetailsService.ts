// used for info on frontend
export interface User {
  id: number;
  name: string;
  email: string;
  owner: string;
}

// used for response from backend
interface UserResponse {
  id: number;
  name: string;
  email: string;
  owner: boolean;
}

// used for request to backend
export interface UserRequest {
  name: string;
  email: string;
  owner: boolean;
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

    console.log("in the service", updatedUserResponse.owner);

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

// you can still pass in user for the owner and id since those aren't changed and email
// export const updateAccountInfo = async (
//   user: User | null,
//   name: string,
//   password: string,
//   setUserData: (user: User | null) => void
// ): Promise<User | void> => {
//   if (user == null) return;

//   // do the fetch before updating here, fetch should be in the .then()

//   const updatedUser: UserRequest = {
//     ...user,
//     name,
//     password,
//   };

//   // update context
//   setUserData(updatedUser);
// };
