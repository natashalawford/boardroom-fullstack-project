import React, { useState, FormEvent } from 'react'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from '@/Components/ui/dialog'
import { Button } from '@/Components/ui/button'
import { Input } from '@/Components/ui/input'
import { Label } from '@/Components/ui/label'

import { loginUser, createUser } from '@/services/loginService'
import { useAuth } from '@/auth/UserAuth'  // <--- import your AuthContext hook

interface LoginPopupProps {
  isOpen: boolean
  onClose: () => void
}

export const LoginPopup: React.FC<LoginPopupProps> = ({ isOpen, onClose }) => {
  const [isLoginMode, setIsLoginMode] = useState(true)

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [name, setName] = useState('')
  const [isOwner, setIsOwner] = useState(false)

  const [errorMessage, setErrorMessage] = useState('')
  const [successMessage, setSuccessMessage] = useState('')

  // Pull in setUserData from AuthContext
  const { setUserData } = useAuth()

  // If user closes the dialog forcibly (ESC or outside click)
  const handleOpenChange = (openValue: boolean) => {
    if (!openValue) {
      resetForm()
      onClose()
    }
  }

  const resetForm = () => {
    setIsLoginMode(true)
    setEmail('')
    setPassword('')
    setName('')
    setIsOwner(false)
    setErrorMessage('')
    setSuccessMessage('')
  }

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setErrorMessage('')
    setSuccessMessage('')

    try {
      if (isLoginMode) {
        const userResponse = await loginUser(email, password)
        setSuccessMessage(`Welcome back, ${userResponse.name}!`)
        // Store the user in the global auth context
        setUserData({
          id: userResponse.id,
          name: userResponse.name,
          email: userResponse.email,
          // If your front-end expects "owner" as string:
          owner: userResponse.owner ? 'true' : 'false',
        })
      } else {
        const newUser = await createUser({
          name,
          email,
          password,
          owner: isOwner,
        })
        setSuccessMessage(`Account created for ${newUser.name}!`)
        setUserData({
          id: newUser.id,
          name: newUser.name,
          email: newUser.email,
          owner: newUser.owner ? 'true' : 'false',
        })
      }

      // Show success message for 2s, then close
      setTimeout(() => {
        onClose()
        resetForm()
      }, 2000)
    } catch (err: any) {
      console.error(err)
      setErrorMessage(err.message || 'Something went wrong.')
    }
  }

  return (
    <Dialog open={isOpen} onOpenChange={handleOpenChange}>
      <DialogContent>
        <form onSubmit={handleSubmit}>
          <DialogHeader>
            <DialogTitle>{isLoginMode ? 'Login' : 'Create Account'}</DialogTitle>
            <DialogDescription>
              {isLoginMode
                ? 'Enter your credentials below to log in.'
                : 'Fill in your details to create a new account.'}
            </DialogDescription>
          </DialogHeader>

          {errorMessage && (
            <p className="text-red-500 text-sm mt-2">{errorMessage}</p>
          )}
          {successMessage && (
            <p className="text-green-500 text-sm mt-2">{successMessage}</p>
          )}

          {/* If creating an account, show the Name and Owner checkbox */}
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
            <Button type="submit" className="text-black">
              {isLoginMode ? 'Login' : 'Create Account'}
            </Button>
            <Button
              variant="secondary"
              onClick={() => setIsLoginMode(!isLoginMode)}
            >
              {isLoginMode ? 'Need an account?' : 'Already have an account?'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}
