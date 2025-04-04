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
        setSuccessMessage(`Welcome back, ${userResponse.name}!`);
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
        setSuccessMessage(`Account created for ${newUser.name}!`);
        setUserData({
          id: newUser.id,
          name: newUser.name,
          email: newUser.email,
          owner: newUser.owner ? "true" : "false",
        });
      }
      // Show success message for 2s, then close the popup
      setTimeout(() => {
        onClose();
        resetForm();
      }, 2000);
    } catch (err: any) {
      console.error(err);
      setErrorMessage(err.message || "Something went wrong.");
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={handleOpenChange}>
      <DialogContent>
        {userData ? (
          // Logged inn View - show a Logout button
          <div className="py-4">
            <h2 className="text-xl font-bold mb-4">
              You are logged in as {userData.name}
            </h2>
            <Button
              variant="destructive"
              onClick={() => {
                logout(setUserData);
                setSuccessMessage("Logged out successfully!");
                setTimeout(() => {
                  onClose();
                  resetForm();
                  navigate("/"); // redirect to home page
                  window.location.reload(); // refresh the page content
                }, 1000);
              }}
            >
              Logout
            </Button>
            {successMessage && (
              <p className="text-green-500 text-sm mt-2">{successMessage}</p>
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
              <p className="text-green-500 text-sm mt-2">{successMessage}</p>
            )}

            {!isLoginMode && (
              <div className="mt-4">
                <Label htmlFor="name">Name</Label>
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
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>

            <div className="mt-4">
              <Label htmlFor="password">Password</Label>
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
