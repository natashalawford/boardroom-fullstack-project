import React from 'react'
import { useState } from 'react'
import { Button } from '@/components/ui/button'
import GameGrid from '../../components/gamegrid'
import { NewGameForm } from './newGameForm'
import { NewSpecificGameForm } from './newSpecificGameForm'

const Games: React.FC = () => {
  const [isGameOpen, openCreateGame] = useState(false)
  const [isSpecificGameOpen, openCreateSpecificGame] = useState(false)

  return (
    <div className='font-roboto p-8'>
      <h1 className='font-semibold'>Games Page</h1>
      <p className='pt-3'>
        Welcome to the Games Page! Here you can explore and play various games.
      </p>
      <div className='flex justify-start pt-3'>
        <NewGameForm />
        <NewSpecificGameForm />
      </div>

      <div className='pt-3'>
        <GameGrid />
      </div>
    </div>
  )
}

export default Games
