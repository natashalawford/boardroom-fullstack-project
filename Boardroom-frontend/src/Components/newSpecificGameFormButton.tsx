'use client'

// Forms
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { z } from 'zod'
import { useState, useEffect } from 'react'
import { useAuth } from '@/auth/UserAuth'

// UI components
import { Button } from '@/components/ui/button'
import {
  Form,
  FormControl,
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
import { Textarea } from '@/components/ui/textarea'
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogTitle
} from '@/components/ui/dialog'
// import FileUpload from '@/components/imageUpload'
import { ArrowDownToLine, Check, ChevronsUpDown } from 'lucide-react'
import { toast } from 'sonner'
import { cn } from '@/lib/utils'

// Services
import {
  saveSpecificBoardGame,
  SpecificBoardGame,
  fetchBoardGames
} from '../services/boardGameService'

interface NewSpecificGameFormProps {
  loadGames: () => Promise<void>;
}

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
  // image: z.preprocess(
  //   val => {
  //     // If it's a FileList and has at least one File, extract that file
  //     if (val instanceof FileList && val.length > 0) {
  //       return val.item(0)
  //     }
  //     return val
  //   },
  //   z.instanceof(File, { message: 'Image of board game is required.' }) // Validate that the processed value is a File or undefined
  // ),
  gameStatus: z.string().min(1, { message: 'Game status is required.' }),
  description: z.string().min(1, { message: 'Description is required.' })
})

export function NewSpecificGameForm({ loadGames }: NewSpecificGameFormProps) {
  const [isDialogOpen, setIsDialogOpen] = useState(false) // State to control dialog visibility

  const [boardGames, setBoardGames] = useState<
    { label: string; value: string }[]
  >([])
  const [loading, setLoading] = useState(true)

  // User details
  const { userData, setUserData } = useAuth()

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
        toast.error(`Error fetching board games: ${errorMessage}`)
      } finally {
        setLoading(false) // Ensure loading state is reset
      }
    }

    loadBoardGames()
  }, [loadGames])

  // Submit handler
  function onSubmit (values: z.infer<typeof formSchema>) {
    // Logging the form values
    console.log('Form Values:', values)
    console.log('Board game:', values.boardGame)
    console.log('Game status:', values.gameStatus)
    console.log('Description:', values.description)

    // Accessing the uploaded file
    // if (values.image instanceof File) {
    //   console.log('Uploaded Image:', values.image)
    //   console.log('Image Name:', values.image.name)
    //   console.log('Image Size:', values.image.size)
    // }

    // Check if logged in
    if (!userData || !userData.id) {
      toast.error('User is not authenticated or personId is missing.')
      return
    }

    const newBoardGame: SpecificBoardGame = {
      picture: 123, // TODO: replace with actual image URL or ID after upload
      description: values.description,
      gameStatus: values.gameStatus,
      boardGameTitle: values.boardGame,
      personId: userData?.id
    }

    saveSpecificBoardGame(newBoardGame)
      .then(() => {
        toast.success('Board game saved.')
        setIsDialogOpen(false)
        form.reset() // Reset the form after successful submission
      })
      .catch(error => {
        console.error('Error saving your board game: ', error.message)
        toast(`Error saving your board game: ${error.message}`) // Display the error message in the toast
      })
  }

  return (
    <div className='flex justify-center items-center'>
      <Dialog
        open={isDialogOpen}
        onOpenChange={(open: boolean | ((prevState: boolean) => boolean)) => {
          setIsDialogOpen(open)
          if (!open) form.reset() // Reset the form whenever the dialog is closed
        }}
      >
        <DialogTrigger asChild>
          <Button
            variant='default'
            onClick={() => setIsDialogOpen(true)}
            className='mr-2'
          >
            Add Your Copy of a Board Game
          </Button>
        </DialogTrigger>
        <DialogContent>
          <DialogTitle className='font-bold text-xl'>
            Add a copy of your board game
          </DialogTitle>
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className='space-y-5 font-roboto'
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
                              'w-[200px] justify-between bg-gray-200 text-black border border-gray-300 hover:bg-gray-300',
                              !field.value && 'text-gray-400'
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
              {/*}
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
*/}
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
                              'w-[200px] justify-between bg-gray-200 text-black border border-gray-300 hover:bg-gray-300',
                              !field.value && 'text-gray-400'
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
        </DialogContent>
      </Dialog>
    </div>
  )
}
