'use client'

import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { z } from 'zod'

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
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card'
import FileUpload from '@/components/imageUpload'
import { ScrollArea } from '@/components/ui/scroll-area'

const formSchema = z.object({
  title: z.string().min(1, { message: 'Title is required.' }),
  image: z.instanceof(File).refine(image => !!image, {
    message: 'A photo of the board game is required.'
  }),
  description: z.string().min(1, { message: 'Description is required.' }),
  playersNeeded: z
    .number()
    .min(1, { message: 'Number of players needed is required.' })
})

export function NewGameForm () {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      title: '',
      image: undefined,
      description: '',
      playersNeeded: 1
    }
  })

  // 2. Define a submit handler.
  function onSubmit (values: z.infer<typeof formSchema>) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    console.log(values)
  }

  function handleFileChange (event: React.ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0]
    if (file) {
      console.log('Selected file:', file.name)
    }
  }

  return (
    <div className='h-screen w-screen flex justify-center items-center'>
      <Card className='w-[700px] mx-auto p-10'>
        <CardTitle className='font-bold'>Add a new board game</CardTitle>
        <ScrollArea className='h-[400px] w-full'>
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className='space-y-8 font-roboto'
            >
              <FormField
                control={form.control}
                name='title'
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Board Game Title</FormLabel>
                    <FormControl>
                      <Input placeholder='Title of the board game' {...field} />
                    </FormControl>
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
                      <FileUpload id='file_input' onChange={handleFileChange} />
                    </FormControl>
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
                        placeholder='One to two line description of the board game'
                        {...field}
                      />
                    </FormControl>
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name='playersNeeded'
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Minimum number of players needed</FormLabel>
                    <FormControl>
                      <Input
                        placeholder='Minimum number of players needed'
                        {...field}
                      />
                    </FormControl>
                  </FormItem>
                )}
              />

              <Button type='submit'>Submit</Button>
            </form>
          </Form>
        </ScrollArea>
      </Card>
    </div>
  )
}
