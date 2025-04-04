import React from 'react'
import { useState, useEffect } from 'react'
import GameGrid from '../../components/gamegrid'
import { NewGameForm } from '../../components/newGameFormButton'
import { NewSpecificGameForm } from '../../components/newSpecificGameFormButton'
import { fetchBoardGames } from '@/services/boardGameService'
import { toast } from 'sonner'
import { useAuth } from '@/auth/UserAuth'

interface Game {
  title: string
  description: string
  playersNeeded: number
  picture: number
}

const Games: React.FC = () => {
  const [games, setGames] = useState<Game[]>([])
  const { userData } = useAuth()

  // Function to load games
  const loadGames = async () => {
    try {
      const data = await fetchBoardGames()
      setGames(data)
    } catch (error: unknown) {
      if (error instanceof Error) {
        toast.error(`Error loading games: ${error.message}`)
      } else {
        toast.error(`${String(error)}`)
      }
    }
  }

  useEffect(() => {
    loadGames()
  }, [])

  return (
    <div className='font-roboto p-8'>
      <h1 className='font-semibold'>Games Page</h1>
      <p className='pt-3'>
        Welcome to the Games Page! Here you can explore and play various games.
      </p>

      {userData?.owner && (
        <div className='flex justify-start pt-3'>
          <NewGameForm onGameAdded={loadGames} />
          <NewSpecificGameForm />
        </div>
      )}

      <div className='pt-3'>
        <GameGrid games={games} />
      </div>
    </div>
  )
}

export default Games
