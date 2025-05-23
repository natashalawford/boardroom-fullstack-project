import React, { useState, FormEvent } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'

import { loginUser, createUser, logout } from "@/services/loginService";
import { useAuth } from "@/auth/UserAuth";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

interface LoginPopupProps {
  isOpen: boolean;
  onClose: () => void;
}

export const LoginPopup: React.FC<LoginPopupProps> = ({ isOpen, onClose }) => {
  // Toggle between Login and Create Account modes
  const [isLoginMode, setIsLoginMode] = useState(true);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [isOwner, setIsOwner] = useState(false);

  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  // Get auth context for global user data
  const { userData, setUserData } = useAuth();

  // Nav hook to go to home page on logout
  const navigate = useNavigate();

  const handleOpenChange = (openValue: boolean) => {
    if (!openValue) {
      resetForm();
      onClose();
    }
  };

  const resetForm = () => {
    setIsLoginMode(true);
    setEmail("");
    setPassword("");
    setName("");
    setIsOwner(false);
    setErrorMessage("");
    setSuccessMessage("");
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setErrorMessage("");
    setSuccessMessage("");

    try {
      if (isLoginMode) {
        // LOGIN
        const userResponse = await loginUser(email, password);
        toast.success(`Welcome back, ${userResponse.name}!`);
        setUserData({
          id: userResponse.id,
          name: userResponse.name,
          email: userResponse.email,
          owner: userResponse.owner ? "true" : "false",
        });
      } else {
        // CREATE ACCOUNT
        const newUser = await createUser({
          name,
          email,
          password,
          owner: isOwner,
        });
        toast.success(`Account created for ${newUser.name}!`);
        setUserData({
          id: newUser.id,
          name: newUser.name,
          email: newUser.email,
          owner: newUser.owner ? "true" : "false",
        });
      }
      // Close the popup and reset the form
      onClose();
      resetForm();

    } catch (err: any) {
      console.error(err);
      toast.error(err.message || "Something went wrong.");
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={handleOpenChange}>
      <DialogContent className="border-gray-200">
        {userData ? (
          // Logged inn View - show a Logout button
          <div className="py-4">
            <h2 className="text-xl font-bold mb-4">
              You are logged in as {userData.name}
            </h2>
            <Button
              variant="destructive"
              onClick={() => {
                onClose();

                setTimeout(() => {
                  logout(setUserData);
                  toast.success("Logged out successfully!");
                  navigate("/"); // redirect to home page
                }, 100);
              }}
            >
              Logout
            </Button>
            {successMessage && (
              <p className="text-green-600 text-sm mt-3 font-semibold">{successMessage}</p>
            )}
          </div>
        ) : (
          // Not logged in - show the Login/Create form
          <form onSubmit={handleSubmit}>
            <DialogHeader>
              <DialogTitle>
                {isLoginMode ? "Login" : "Create Account"}
              </DialogTitle>
              <DialogDescription>
                {isLoginMode
                  ? "Enter your credentials below to log in."
                  : "Fill in your details to create a new account."}
              </DialogDescription>
            </DialogHeader>

            {errorMessage && (
              <p className="text-red-500 text-sm mt-2">{errorMessage}</p>
            )}
            {successMessage && (
              <p className="text-green-600 text-sm mt-3 font-semibold">{successMessage}</p>
            )}

            {!isLoginMode && (
              <div className="mt-4">
                <Label htmlFor="name" className="mb-2">Name</Label>
                <Input
                  id="name"
                  type="text"
                  required
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
              </div>
            )}

            <div className="mt-4">
              <Label htmlFor="email" className="mb-2">Email</Label>
              <Input
                id="email"
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>

            <div className="mt-4">
              <Label htmlFor="password" className="mb-2">Password</Label>
              <Input
                id="password"
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>

            {!isLoginMode && (
              <div className="mt-4 flex items-center space-x-2">
                <Label htmlFor="isOwner">I want to be a Game Owner</Label>
                <Input
                  id="isOwner"
                  type="checkbox"
                  checked={isOwner}
                  onChange={(e) => setIsOwner(e.target.checked)}
                  className="h-4 w-4"
                />
              </div>
            )}

            <DialogFooter className="mt-6">
              <Button type="submit">
                {isLoginMode ? 'Login' : 'Create Account'}
              </Button>
              <Button
                variant="secondary"
                className="hover:bg-gray-300 transition duration-300"
                onClick={() => setIsLoginMode(!isLoginMode)}
              >
                {isLoginMode ? "Need an account?" : "Already have an account?"}
              </Button>
            </DialogFooter>
          </form>
        )}
      </DialogContent>
    </Dialog>
  );
};
