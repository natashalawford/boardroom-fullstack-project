import * as Switch from "@radix-ui/react-switch";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

import { useEffect, useState } from "react";

function AccountDetails() {
  const [existingName, setExistingName] = useState<string>();
  const [name, setName] = useState<string>();
  const [password, setPassword] = useState<string>();

  // use effect to get the name on the first load of the page
  useEffect(() => {});

  const toggleAccountType = () => {};

  return (
    <>
      <div className="flex flex-row justify-between items-center h-[120px] ml-10 mr-10">
        <p className="text-2xl">Account Details</p>

        <Switch.Root
          onCheckedChange={toggleAccountType}
          className="relative w-[50px] h-[30px] bg-gray-400 rounded-full shadow-lg 
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

      <p className="ml-20 mb-10 text-4xl">Hello, {existingName}</p>

      <div className="flex flex-col ml-20 w-100">
        <div className="flex justify-between items-center mb-5">
          Name
          <Input
            className={"w-50 ml-10"}
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div className="flex justify-between items-center mb-7">
          Password
          <Input
            className={"w-50 ml-10"}
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <Button
          variant="outline"
          className="hover:bg-gray-700 hover:text-white w-30 self-end"
        >
          Update
        </Button>
      </div>
    </>
  );
}

export default AccountDetails;
