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

// Import the real functions from your loginService
import { loginUser, createUser } from '@/services/loginService'

interface LoginPopupProps {
  isOpen: boolean
  onClose: () => void
}

export const LoginPopup: React.FC<LoginPopupProps> = ({ isOpen, onClose }) => {
  // Toggle between Login and Create Account
  const [isLoginMode, setIsLoginMode] = useState(true)

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [name, setName] = useState('')
  const [isOwner, setIsOwner] = useState(false)

  const [errorMessage, setErrorMessage] = useState('')
  const [successMessage, setSuccessMessage] = useState('')

  const handleOpenChange = (openValue: boolean) => {
    if (!openValue) {
      // The user closed the popup, so reset form
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

  // submit
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setErrorMessage('')
    setSuccessMessage('')

    try {
      if (isLoginMode) {
        const userResponse = await loginUser(email, password)
        setSuccessMessage(`Welcome back, ${userResponse.name}!`)
      } else {
        const newUser = await createUser({
          name,
          email,
          password,
          owner: isOwner,
        })
        setSuccessMessage(`Account created for ${newUser.name}!`)
      }

      // If successful, close the popup and reset
      onClose()
      resetForm()
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

          {/* If creating an account, show the Name and "Owner" fields */}
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

          {/* If creating an account, show "isOwner" checkbox */}
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
            <Button variant="secondary" onClick={() => setIsLoginMode(!isLoginMode)}>
              {isLoginMode ? 'Need an account?' : 'Already have an account?'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}
