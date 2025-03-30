import * as Switch from "@radix-ui/react-switch";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { toggleAccountType, login } from "@/services/AccountDetailsService";
import BorrowRequestList from "@/Components/BorrowRequestList";
import LendingHistoryList from "@/Components/LendingHistoryList";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "@/auth/UserAuth";
import { useAuth } from "@/auth/UserAuth";

function AccountDetails() {
  const { userData, setUserData } = useAuth();

  const [email] = useState<string>(userData?.email || "");
  const [name] = useState<string>(userData?.name || "");
  const [accountType] = useState<string>(
    userData?.owner || "user"
  );

  // this is what is defined in the update fields
  const [newName, setNewName] = useState<string>("");
  const [newPassword, setNewPassword] = useState<string>("");


  const handleToggle = async () => {
    await toggleAccountType(userData, accountType, setUserData);
  }


  const handleUpdate = async () => {
    await login("jason@gmail.com", "pw123", setUserData);
  }
 
  return (
    <>
      <div className="flex flex-row justify-between items-center h-[120px] ml-10 mr-10">
        <p className="text-2xl">Account Details</p>

        <div className="flex items-center">
          <label className="text-lg">{accountType}</label>
          <Switch.Root
            onCheckedChange={handleToggle}
            className="ml-5 relative w-[50px] h-[30px] bg-gray-400 rounded-full shadow-lg 
                 transition-colors focus:outline-none focus:ring-2 focus:ring-black 
                 data-[state=checked]:bg-gray-700"
          >
            <Switch.Thumb
              className="block w-[25px] h-[25px] bg-white rounded-full shadow-md 
                   transition-transform duration-100 transform translate-x-[2px] 
                   data-[state=checked]:translate-x-[23px]"
            />
          </Switch.Root>
        </div>
      </div>

      <p className="ml-20 mb-10 text-4xl">Hello, {name}</p>

      <div className="flex flex-col ml-20 w-100">
        <div className="flex justify-between items-center mb-5">
          Email
          <Input disabled value={email} className="w-50 ml-10" />
        </div>

        <div className="flex justify-between items-center mb-5">
          Name
          <Input
            className={"w-50 ml-10"}
            placeholder="Name"
            value={newName}
            onChange={(e) => setNewName(e.target.value)}
          />
        </div>
        <div className="flex justify-between items-center mb-7">
          Password
          <Input
            className={"w-50 ml-10"}
            placeholder="Password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
        </div>

        <Button
          variant="outline"
          className="hover:bg-gray-700 hover:text-white w-30 self-end"
          onClick={handleUpdate}
        >
          Update
        </Button>
      </div>

      <div className="flex flex-col justify-between items-left ml-10 mr-10 mb-10">
        <BorrowRequestList />  
      </div>

      <div className="flex flex-col justify-between items-left ml-10 mr-10 mb-10">
        <LendingHistoryList />  
      </div>
    </>
  );
}

export default AccountDetails;
