'use client'

// Forms
import { useState, useEffect } from 'react'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { z } from 'zod'

// UI components
import { Button } from '@/components/ui/button'
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage
} from '@/components/ui/form'
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList
} from '@/components/ui/command'
import {
  Popover,
  PopoverContent,
  PopoverTrigger
} from '@/components/ui/popover'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Card, CardTitle } from '@/components/ui/card'
import { ScrollArea } from '@/components/ui/scroll-area'
import FileUpload from '@/components/imageUpload'
import { ArrowDownToLine, Check, ChevronsUpDown } from 'lucide-react'
import { Toaster } from '@/components/ui/sonner'
import { toast } from 'sonner'
import { cn } from '@/lib/utils'

// Services
import {
  saveSpecificBoardGame,
  SpecificBoardGame,
  BoardGame,
  fetchBoardGames
} from '../../services/boardGameService'
import { useNavigate } from 'react-router-dom'

type GameStatus = {
  value: string
  label: string
}

const gameStatuses: GameStatus[] = [
  { value: 'AVAILABLE', label: 'Available' },
  { value: 'ONHOLD', label: 'On Hold' },
  { value: 'UNAVAILABLE', label: 'Unavailable' }
]

const formSchema = z.object({
  boardGame: z.string({
    required_error: 'Board game selection is required.'
  }),
  image: z.preprocess(
    val => {
      // If it's a FileList and has at least one File, extract that file
      if (val instanceof FileList && val.length > 0) {
        return val.item(0)
      }
      return val
    },
    z.instanceof(File, { message: 'Image of board game is required.' }) // Validate that the processed value is a File or undefined
  ),
  gameStatus: z.string().min(1, { message: 'Game status is required.' }),
  description: z.string().min(1, { message: 'Description is required.' })
})

export function NewSpecificGameForm () {
  const navigate = useNavigate()

  const [boardGames, setBoardGames] = useState<
    { label: string; value: string }[]
  >([])
  const [loading, setLoading] = useState(true)

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      boardGame: '',
      description: ''
    }
  })

  // Load all board games
  useEffect(() => {
    const loadBoardGames = async () => {
      setLoading(true)
      try {
        // Format games and map them to the required structure
        const games = await fetchBoardGames()
        const formattedGames = games.map(({ title }: { title: string }) => ({
          label: title,
          value: title
        }))
        setBoardGames(formattedGames)
      } catch (error: unknown) {
        console.error('Failed to fetch board games:', error)
        const errorMessage =
          error instanceof Error ? error.message : 'An unknown error occurred.'
        toast(`Error fetching board games: ${errorMessage}`)
      } finally {
        setLoading(false) // Ensure loading state is reset
      }
    }

    loadBoardGames()
  }, [])

  // Submit handler
  function onSubmit (values: z.infer<typeof formSchema>) {
    // Logging the form values
    console.log('Form Values:', values)
    console.log('Board game:', values.boardGame)
    console.log('Game status:', values.gameStatus)
    console.log('Description:', values.description)

    // Accessing the uploaded file
    if (values.image instanceof File) {
      console.log('Uploaded Image:', values.image)
      console.log('Image Name:', values.image.name)
      console.log('Image Size:', values.image.size)
    }

    const newBoardGame: SpecificBoardGame = {
      boardGame: values.boardGame,
      gameStatus: values.gameStatus,
      description: values.description,
      picture: 123 // TODO: replace with actual image URL or ID after upload
    }
    saveSpecificBoardGame(newBoardGame)
      .then(() => {
        toast('Board game saved.')
        setTimeout(() => {
          navigate('/games') // Navigate after the toast is displayed
        }, 500)
      })
      .catch(error => {
        console.error('Error saving your board game: ', error.message)
        toast(`Error saving your board game: ${error.message}`) // Display the error message in the toast
      })
  }

  return (
    <div className='h-screen w-screen flex justify-center items-center'>
      <Card className='w-[700px] mx-auto p-10'>
        <CardTitle className='font-bold text-xl'>
          Add a copy of your board game
        </CardTitle>
        <ScrollArea className='h-[400px] w-full'>
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className='space-y-8 font-roboto'
            >
              <FormField
                control={form.control}
                name='boardGame'
                render={({ field }) => (
                  <FormItem className='flex flex-col'>
                    <FormLabel>Select Board Game</FormLabel>
                    <Popover>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            role='combobox'
                            className={cn(
                              'w-[200px] justify-between',
                              !field.value && 'text-muted-foreground'
                            )}
                          >
                            {field.value
                              ? boardGames.find(
                                  game => game.value === field.value
                                )?.label
                              : 'Select board game'}
                            <ChevronsUpDown className='opacity-50' />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className='w-[200px] p-0'>
                        <Command>
                          <CommandInput
                            placeholder='Search board game...'
                            className='h-9'
                          />
                          <CommandList>
                            <CommandEmpty>No board games found.</CommandEmpty>
                            <CommandGroup>
                              {loading ? (
                                <CommandItem>Loading...</CommandItem>
                              ) : (
                                boardGames.map(game => (
                                  <CommandItem
                                    value={game.label}
                                    key={game.value}
                                    onSelect={() => {
                                      form.setValue('boardGame', game.value)
                                    }}
                                  >
                                    {game.label}
                                    <Check
                                      className={cn(
                                        'ml-auto',
                                        game.value === field.value
                                          ? 'opacity-100'
                                          : 'opacity-0'
                                      )}
                                    />
                                  </CommandItem>
                                ))
                              )}
                            </CommandGroup>
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name='image'
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Image</FormLabel>
                    <FormControl>
                      <FileUpload
                        id='file_input'
                        value={field.value} // pass the parent value down
                        onChange={file => {
                          // 1) Update the form field so the parent’s form state has the file
                          field.onChange(file)
                          // 2) Optionally log it or handle it however you'd like
                          console.log('Uploaded File:', file)
                        }}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name='gameStatus'
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Select Game Status</FormLabel>
                    <Popover>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            role='combobox'
                            className={cn(
                              'w-[200px] justify-between',
                              !field.value && 'text-muted-foreground'
                            )}
                          >
                            {field.value
                              ? gameStatuses.find(
                                  status => status.value === field.value
                                )?.label
                              : 'Select Game Status'}
                            <ChevronsUpDown className='opacity-50' />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className='w-[200px] p-0'>
                        <Command>
                          <CommandInput
                            placeholder='Search status...'
                            className='h-9'
                          />
                          <CommandList>
                            <CommandEmpty>No status found.</CommandEmpty>
                            <CommandGroup>
                              {gameStatuses.map(status => (
                                <CommandItem
                                  key={status.value}
                                  value={status.label}
                                  onSelect={() => {
                                    form.setValue('gameStatus', status.value)
                                  }}
                                >
                                  {status.label}
                                  <Check
                                    className={cn(
                                      'ml-auto',
                                      status.value === field.value
                                        ? 'opacity-100'
                                        : 'opacity-0'
                                    )}
                                  />
                                </CommandItem>
                              ))}
                            </CommandGroup>
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name='description'
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Description</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder='One to two line description of your copy of the board game (e.g. condition, missing pieces, etc.)'
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <Button type='submit'>
                Save
                <ArrowDownToLine strokeWidth={3} />
              </Button>
            </form>
          </Form>
        </ScrollArea>
      </Card>
      <Toaster />
    </div>
  )
}
