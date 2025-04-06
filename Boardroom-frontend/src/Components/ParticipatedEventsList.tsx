import { ScrollArea } from "@/components/ui/scroll-area";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { useEffect, useState } from "react";
import { useAuth } from "@/auth/UserAuth";
import { getEventsByParticipant, unregisterFromEvent } from "@/services/AccountDetailsService";
import { toast } from "sonner";

type EventInfo = {
  id: number;
  title: string;
  description: string; // still exists in DTO but unused in table
  startDateTime: string;
  endDateTime: string;
  maxParticipants: number;
  location: string;
  hostId: number;
  boardGameName: string;
};

const formatDateTime = (dateTime: string) => {
  return new Date(dateTime).toLocaleString(undefined, {
    year: "numeric",
    month: "short",
    day: "numeric",
    hour: "numeric",
    minute: "2-digit",
  });
};

const ParticipantEventTable = () => {
  const { userData } = useAuth(); 
  const [events, setEvents] = useState<EventInfo[]>([]);

  useEffect(() => {
    if (userData?.id) {
      getEventsByParticipant(userData.id)
        .then(setEvents)
        .catch(console.error);
    }
  }, [userData]);

  const handleUnregister = async (eventId: number) => {
    if (!userData?.id) return;

    try {
      await unregisterFromEvent(userData.id, eventId);
      setEvents((prevEvents) => prevEvents.filter((event) => event.id !== eventId));
      toast.success("Successfully unregistered from the event.");
    } catch (error) {
      console.error("Error unregistering from event:", error);
      toast.error("Failed to unregister from the event.");
    }
  };

  return (
    <>
      <h2 className="text-lg font-semibold mb-2">My Registered Events</h2>
      <Card className="p-0 w-full max-w-4xl h-96 flex flex-col border rounded-lg p-4 shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200 bg-white outline outline-1 outline-gray-300">
        <ScrollArea className="h-full w-full">
          <table className="w-full text-sm text-left border-separate border-spacing-y-2">
            <thead className="text-xs uppercase text-muted-foreground">
              <tr>
                <th className="px-6 py-2">Title</th>
                <th className="px-6 py-2">Board Game</th>
                <th className="px-6 py-2">Start</th>
                <th className="px-6 py-2">End</th>
                <th className="px-6 py-2">Location</th>
                <th className="px-6 py-2">Actions</th>
              </tr>
            </thead>
            <tbody>
              {events.map((event) => (
                <tr key={event.id} className="bg-muted rounded-md">
                  <td className="px-6 py-2 border-t border-gray-200">{event.title}</td>
                  <td className="px-6 py-2 border-t border-gray-200">{event.boardGameName}</td>
                  <td className="px-6 py-2 border-t border-gray-200">{formatDateTime(event.startDateTime)}</td>
                  <td className="px-6 py-2 border-t border-gray-200">{formatDateTime(event.endDateTime)}</td>
                  <td className="px-6 py-2 border-t border-gray-200">{event.location}</td>
                  <td className="px-6 py-2 border-t border-gray-200">
                    <Button
                      variant="destructive"
                      onClick={() => handleUnregister(event.id)}
                      className="bg-red-500 text-white px-4 py-2 rounded-md transition duration-300 ease-in-out hover:bg-red-600 hover:scale-105"
                    >
                      Unregister
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </ScrollArea>
      </Card>
    </>
  );
};

export default ParticipantEventTable;
