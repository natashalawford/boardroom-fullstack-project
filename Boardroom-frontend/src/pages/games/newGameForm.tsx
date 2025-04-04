"use client";

// Forms
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";

// UI components
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardTitle } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import FileUpload from "@/components/imageUpload";
import { ArrowDownToLine } from "lucide-react";
import { Toaster } from "@/components/ui/sonner";
import { toast } from "sonner";

// Services
import { saveBoardGame, BoardGame } from "../../services/boardGameService";
import { useNavigate } from "react-router-dom";

const formSchema = z.object({
  title: z.string().min(1, { message: "Title is required." }),
  image: z.preprocess(
    (val) => {
      // If it's a FileList and has at least one File, extract that file
      if (val instanceof FileList && val.length > 0) {
        return val.item(0);
      }
      return val;
    },
    z.instanceof(File, { message: "Image of board game is required" }) // Validate that the processed value is a File or undefined
  ),
  description: z.string().min(1, { message: "Description is required." }),
  playersNeeded: z.preprocess(
    (value) => (typeof value === "string" ? parseInt(value, 10) : value),
    z.number().min(1, { message: "Number of players needed is required." })
  ),
});

export function NewGameForm() {
  const navigate = useNavigate();

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      title: "",
      description: "",
    },
  });

  // Submit handler
  function onSubmit(values: z.infer<typeof formSchema>) {
    // Logging the form values
    console.log("Form Values:", values);
    console.log("Title:", values.title);
    console.log("Description:", values.description);
    console.log("Players Needed:", values.playersNeeded);

    // Accessing the uploaded file
    if (values.image instanceof File) {
      console.log("Uploaded Image:", values.image);
      console.log("Image Name:", values.image.name);
      console.log("Image Size:", values.image.size);
    }

    const newBoardGame: BoardGame = {
      title: values.title,
      description: values.description,
      playersNeeded: values.playersNeeded,
      picture: 123, // TODO: replace with actual image URL or ID after upload
    };
    saveBoardGame(newBoardGame)
      .then(() => {
        toast.success("Board game saved.");
        setTimeout(() => {
          navigate("/games"); // Navigate after the toast is displayed
        }, 500);
      })
      .catch((error) => {
        console.error("Error saving board game: ", error.message);
        toast(`Error saving board game: ${error.message}`); // Display the error message in the toast
      });
  }

  return (
    <div className="h-screen w-screen flex justify-center items-center">
      <Card className="w-[700px] mx-auto p-10">
        <CardTitle className="font-bold text-xl">
          Add a new board game
        </CardTitle>
        <ScrollArea className="h-[400px] w-full">
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className="space-y-8 font-roboto"
            >
              <FormField
                control={form.control}
                name="title"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Board Game Title</FormLabel>
                    <FormControl>
                      <Input placeholder="Title of the board game" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="image"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Image</FormLabel>
                    <FormControl>
                      <FileUpload
                        id="file_input"
                        value={field.value} // pass the parent value down
                        onChange={(file) => {
                          // 1) Update the form field so the parent’s form state has the file
                          field.onChange(file);
                          // 2) Optionally log it or handle it however you'd like
                          console.log("Uploaded File:", file);
                        }}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="description"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Description</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="One to two line description of the board game"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="playersNeeded"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Minimum number of players needed</FormLabel>
                    <FormControl>
                      <Input
                        type="number"
                        placeholder="Minimum number of players needed"
                        value={field.value || ""}
                        onChange={(e) =>
                          field.onChange(
                            e.target.value ? parseInt(e.target.value, 10) : ""
                          )
                        }
                        //{...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <Button type="submit">
                Save
                <ArrowDownToLine strokeWidth={3} />
              </Button>
            </form>
          </Form>
        </ScrollArea>
      </Card>
      <Toaster />
    </div>
  );
}
