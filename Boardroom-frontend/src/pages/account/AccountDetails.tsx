import * as Switch from "@radix-ui/react-switch";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogClose,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import {
  toggleAccountType,
  login,
  updateAccountInfo,
  updatePassword
} from "@/services/AccountDetailsService";

import { useEffect, useState } from "react";
import { useAuth } from "@/auth/UserAuth";
import { Label } from "@/components/ui/label";

function AccountDetails() {
  const { userData, setUserData } = useAuth();

  //the initial values aren't useful if i need to login on the page, so will only be useful when login is actually implemented
  const [name, setName] = useState<string>(userData?.name || "");
  const [email, setEmail] = useState<string>(userData?.email || "");
  const [accountType, setAccountType] = useState<string>(
    userData?.owner == "true" ? "owner" : "user"
  );

  // for switch
  const [isChecked, setIsChecked] = useState<boolean>(
    userData?.owner == "true" ? true : false
  );

  // this is what is defined in the update fields
  const [newName, setNewName] = useState<string>(name);

  const [oldPassword, setOldPassword] = useState<string>("");
  const [newPassword, setNewPassword] = useState<string>("");

  const handleToggle = async (checked: boolean) => {
    await toggleAccountType(userData, checked.toString(), setUserData);
  };

  const passwordUpdate = async () => {
    await updatePassword(userData, oldPassword, newPassword, setUserData);
  }

  const handleUpdate = async () => {
    await updateAccountInfo(userData, newName, setUserData);
  };

  // login on render -- to be removed
  useEffect(() => {
    login("jason@gmail.com", "pw1234", setUserData);
  }, []);

  // keep info up to date
  useEffect(() => {
    if (userData) {
      setEmail(userData.email);
      setName(userData.name);
      setNewName(userData.name);
      setAccountType(userData.owner == "true" ? "owner" : "user");
      setIsChecked(userData.owner == "true");
    }
  }, [userData]);

  return (
    <>
      <div className="flex flex-row justify-between items-center h-[120px] ml-10 mr-10">
        <p className="text-2xl">Account Details</p>

        <div className="flex items-center">
          <label className="text-lg">{accountType}</label>
          <Switch.Root
            checked={isChecked}
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

      <div className="flex flex-col ml-20 w-100 borderd rounded-lg">
        <div className="flex justify-between items-center mb-5">
          Email
          <Input disabled value={email} className="w-90 ml-10" />
        </div>

        <div className="flex justify-between items-center mb-5">
          Name
          <Input
            className={"w-90 ml-10"}
            placeholder="Name"
            value={newName}
            onChange={(e) => setNewName(e.target.value)}
          />
        </div>

        <Dialog>
          <DialogTrigger className="flex shrink self-center mb-10">
            <Button
              variant="outline"
              className="hover:bg-gray-700 hover:text-white w-50"
            >
              Update Password
            </Button>
          </DialogTrigger>
          <DialogContent className="sm:max-w-[425px] bg-white">
            <DialogHeader>
              <DialogTitle>Update Password</DialogTitle>
              <DialogDescription>
                Make changes to your password here. Click save when you're done.
              </DialogDescription>
            </DialogHeader>

            <div className="grid gap-4 py-4">
              <div className="grid grid-cols-4 items-center gap-4">
                <Label>Old Password</Label>
                <Input className="col-span-3" value={oldPassword} onChange={(e) => setOldPassword(e.target.value)}/>
              </div>
              <div className="grid grid-cols-4 items-center gap-4">
                <Label>New Password</Label>
                <Input className="col-span-3" value={newPassword} onChange={(e) => setNewPassword(e.target.value)}/>
              </div>
            </div>

            <DialogFooter>
              <DialogClose asChild>
                <Button
                  className="hover:bg-gray-700 hover:text-white"
                  variant="outline"
                  type="submit"
                  onClick={passwordUpdate}
                >
                  Save
                </Button>
              </DialogClose>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        {/* <div className="flex justify-between items-center mb-7">
          Password
          <Input
            className={"w-50 ml-10"}
            placeholder="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
        </div> */}

        <Button
          variant="outline"
          className="hover:bg-gray-700 hover:text-white w-30 self-end"
          onClick={handleUpdate}
        >
          Update
        </Button>
      </div>
    </>
  );
}

export default AccountDetails;
