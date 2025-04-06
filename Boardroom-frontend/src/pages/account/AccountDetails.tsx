"use client";
import * as Switch from "@radix-ui/react-switch";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import BorrowRequestList from "@/components/BorrowRequestList";
import LendingHistoryList from "@/components/LendingHistoryList";
import ParticipatedEventsList from "@/components/ParticipatedEventsList";

import { Toaster } from "@/components/ui/sonner";
import { toast } from "sonner";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import {
  toggleAccountType,
  updateAccountInfo,
  updatePassword,
  updateSpecificGame,
} from "@/services/AccountDetailsService";

import { useEffect, useState } from "react";
import { useAuth } from "@/auth/UserAuth";

import {
  updateNameFormSchema,
  updatePasswordFormSchema,
  updateSpecificGameSchema,
} from "./AccountDetailFormSchemas";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import OwnedGamesList, { OwnedGameUpdate } from "@/components/ownedGamesList";

function AccountDetails() {
  const { userData, setUserData } = useAuth();

  //the initial values aren't useful if i need to login on the page, so will only be useful when login is actually implemented
  const [name, setName] = useState<string>(userData?.name || "");
  const [email, setEmail] = useState<string>(userData?.email || "");
  const [accountType, setAccountType] = useState<string>(
    userData?.owner == "true" ? "Owner" : "User"
  );

  // for switch
  const [isChecked, setIsChecked] = useState<boolean>(
    userData?.owner == "true" ? true : false
  );

  // this is what is defined in the update fields
  const [newName, setNewName] = useState<string>(name);

  const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
  // const [oldPassword, setOldPassword] = useState<string>("");
  // const [newPassword, setNewPassword] = useState<string>("");

  const [showUpdateGame, setShowUpdateGame] = useState<boolean>(false);
  const [specificGameInfo, setSpecificGameInfo] =
    useState<OwnedGameUpdate | null>();

  const handleToggle = async (checked: boolean) => {
    const errorMessage = await toggleAccountType(
      userData,
      checked.toString(),
      setUserData
    );

    if (errorMessage != null) {
      toast.error(errorMessage.errorMessage);
    }
  };

  const updateNameForm = useForm<z.infer<typeof updateNameFormSchema>>({
    resolver: zodResolver(updateNameFormSchema),
    defaultValues: {
      name: newName,
    },
  });

  // there should never really be any errors here
  async function handleUpdate(values: z.infer<typeof updateNameFormSchema>) {
    const errorMessage = await updateAccountInfo(
      userData,
      values.name,
      setUserData
    );

    if (errorMessage != null) {
      toast.error(errorMessage.errorMessage);
    } else {
      toast.success("Success");
    }
  }

  const updatePasswordForm = useForm<z.infer<typeof updatePasswordFormSchema>>({
    resolver: zodResolver(updatePasswordFormSchema),
    defaultValues: {
      oldPassword: "",
      newPassword: "",
    },
  });

  async function passwordUpdate(
    values: z.infer<typeof updatePasswordFormSchema>
  ) {
    const errorMessage = await updatePassword(
      userData,
      values.oldPassword,
      values.newPassword,
      setUserData
    );
    if (errorMessage != null) {
      toast.error(errorMessage.errorMessage);
    } else {
      setIsDialogOpen(false);
      toast.success("Success");
    }
  }

  const updateSpecificGameForm = useForm<
    z.infer<typeof updateSpecificGameSchema>
  >({
    resolver: zodResolver(updateSpecificGameSchema),
    defaultValues: {
      description: "",
    },
  });

  async function specificGameUpdate(
    values: z.infer<typeof updateSpecificGameSchema>
  ) {
    const errorMessage = await updateSpecificGame(
      specificGameInfo?.id,
      specificGameInfo?.status,
      values.description
    );

    if (errorMessage != null) {
      toast(errorMessage.errorMessage);
    } else {
      setShowUpdateGame(false);
      setSpecificGameInfo(null);
      toast.success("Success");
    }
  }

  // keep info up to date
  useEffect(() => {
    if (userData) {
      setEmail(userData.email);
      setName(userData.name);
      updateNameForm.setValue("name", userData.name);
      // setNewName(userData.name);
      setAccountType(userData.owner == "true" ? "Owner" : "User");
      setIsChecked(userData.owner == "true");
    }
  }, [userData]);

  return (
    <>
      {userData ? (
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

          <p className="ml-20 text-4xl">Hello, {name}</p>

          <div className="flex flex-row justify-between items-center min-h-[335px]">
            <div className="flex flex-col ml-20 w-100 borderd rounded-lg">
              <div className="flex justify-between items-center mb-5">
                Email
                <Input disabled value={email} className="w-80" />
              </div>

              <Form {...updateNameForm}>
                <form onSubmit={updateNameForm.handleSubmit(handleUpdate)}>
                  <FormField
                    control={updateNameForm.control}
                    name="name"
                    render={({ field }) => (
                      <FormItem className="mb-5">
                        <div className="flex justify-between">
                          <FormLabel>Name</FormLabel>
                          <FormControl>
                            <Input
                              className={"w-80"}
                              placeholder="Name"
                              {...field}
                            />
                          </FormControl>
                        </div>
                        <FormMessage className="ml-[85px] leading-none" />
                      </FormItem>
                    )}
                  />
                  <div className="flex justify-end mb-5">
                    <Button
                      type="submit"
                      variant="outline"
                      className="bg-black hover:bg-neutral-800 text-white hover:text-white"
                    >
                      Update Name
                    </Button>
                  </div>
                </form>
              </Form>

              <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <DialogTrigger asChild className="flex shrink self-center">
                  <Button
                    variant="outline"
                    className="bg-black hover:bg-neutral-800 text-white hover:text-white"
                  >
                    Update Password
                  </Button>
                </DialogTrigger>
                <DialogContent className="sm:max-w-[425px] bg-white">
                  <DialogHeader>
                    <DialogTitle>Update Password</DialogTitle>
                    <DialogDescription>
                      Make changes to your password here. Click save when you're
                      done.
                    </DialogDescription>
                  </DialogHeader>

                  <Form {...updatePasswordForm}>
                    <form
                      onSubmit={updatePasswordForm.handleSubmit(passwordUpdate)}
                    >
                      <FormField
                        control={updatePasswordForm.control}
                        name="oldPassword"
                        render={({ field }) => (
                          <FormItem className="mb-5">
                            <div className="flex justify-between">
                              <FormLabel className="w-[85px]">
                                Old Password
                              </FormLabel>
                              <FormControl>
                                <Input className="w-75" {...field} />
                              </FormControl>
                            </div>
                            <FormMessage className="ml-[90px] leading-none" />
                          </FormItem>
                        )}
                      />

                      <FormField
                        control={updatePasswordForm.control}
                        name="newPassword"
                        render={({ field }) => (
                          <FormItem className="mb-5">
                            <div className="flex justify-between">
                              <FormLabel className="w-[85px]">
                                New Password
                              </FormLabel>
                              <FormControl>
                                <Input className="w-75" {...field} />
                              </FormControl>
                            </div>
                            <FormMessage className="ml-[90px] leading-none" />
                          </FormItem>
                        )}
                      />
                      <div className="flex justify-end">
                        <Button
                          className="bg-black hover:bg-neutral-800 text-white hover:text-white"
                          variant="outline"
                          type="submit"
                        >
                          Save
                        </Button>
                      </div>
                    </form>
                  </Form>
                </DialogContent>
              </Dialog>
            </div>

            {userData?.owner == "false" ? (
              <></>
            ) : (
              <OwnedGamesList
                openModal={() => setShowUpdateGame(true)}
                setInfo={(gameInfo: OwnedGameUpdate) =>
                  setSpecificGameInfo(gameInfo)
                }
              />
            )}
          </div>

          <Dialog open={showUpdateGame} onOpenChange={setShowUpdateGame}>
            <DialogContent className="sm:max-w-[425px] bg-white">
              <DialogHeader>
                <DialogTitle>Update Game</DialogTitle>
                <DialogDescription>
                  Make changes to your game here. Click save when you're done.
                </DialogDescription>
              </DialogHeader>

              <Form {...updateSpecificGameForm}>
                <form
                  onSubmit={updateSpecificGameForm.handleSubmit(
                    specificGameUpdate
                  )}
                >
                  <FormField
                    control={updateSpecificGameForm.control}
                    name="description"
                    render={({ field }) => (
                      <FormItem className="mb-5">
                        <div className="flex justify-between">
                          <FormLabel className="w-[85px]">
                            Description
                          </FormLabel>
                          <FormControl>
                            <Input className="w-75" {...field} />
                          </FormControl>
                        </div>
                        <FormMessage className="ml-[90px] leading-none" />
                      </FormItem>
                    )}
                  />

                  <div className="flex justify-end">
                    <Button
                      className="hover:bg-gray-700 hover:text-white"
                      variant="outline"
                      type="submit"
                    >
                      Save
                    </Button>
                  </div>
                </form>
              </Form>
            </DialogContent>
          </Dialog>

          {userData?.owner == "false" ? (
            <></>
          ) : (
            <>
              <div className="flex flex-col justify-between items-left ml-15 mr-10 mb-10">
                <BorrowRequestList />
              </div>

              <div className="flex flex-col justify-between items-left ml-15 mr-10 mb-10">
                <LendingHistoryList />
              </div>
            </>
          )}
          <div className="flex flex-col justify-between items-left ml-15 mr-10 mb-10">
                <ParticipatedEventsList />
          </div>
        </>
      ) : (
        <>
          <div className='flex justify-center items-center h-screen mt-[-70px]'>
            <p className='text-6xl'>Please login before consulting account details.</p>
          </div>
        </>
      )}
    </>
  );
}

export default AccountDetails;
