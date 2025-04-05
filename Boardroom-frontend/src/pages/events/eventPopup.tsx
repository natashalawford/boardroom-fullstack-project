import React, { useEffect, useState } from 'react';
import { useAuth } from '../../auth/UserAuth';
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogFooter,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { fetchHostName, fetchRegistrationsForEvent, registerForEvent } from '../../services/eventService';
import { toast } from "sonner";

import image1 from '../../assets/games/image1.jpg';
import image2 from '../../assets/games/image2.jpg';
import image3 from '../../assets/games/image3.jpg';
import image4 from '../../assets/games/image4.jpg';
import image5 from '../../assets/games/image5.jpg';

const gameImages: { [key: number]: string } = {
    1: image1,
    2: image2,
    3: image3,
    4: image4,
    5: image5,
};

interface EventPopupProps {
    event: {
        id: number;
        title: string;
        boardGameName: string;
        description: string;
        location: string;
        startDateTime: string;
        endDateTime: string;
        maxParticipants: number;
        hostId: number;
    };
    pictureIndex: number;
    onClose: () => void;
}

const EventPopup: React.FC<EventPopupProps> = ({ event, pictureIndex, onClose }) => {
    const { userData } = useAuth();
    const [hostName, setHostName] = useState<string | null>(null); // For host name
    const [spotsLeft, setSpotsLeft] = useState<number | null>(null); // For spots left

    useEffect(() => {
        fetchHostNameWrapper();
        fetchSpotsLeft();
    }, [event.hostId]);

    const fetchHostNameWrapper = async () => {
        try {
            const name = await fetchHostName(event.hostId); // Use eventService
            setHostName(name);
        } catch (error) {
            console.error('Error fetching host name:', error);
            setHostName('Unknown Host');
        }
    };

    const fetchSpotsLeft = async () => {
        try {
            const registrations = await fetchRegistrationsForEvent(event.id); // Use eventService
            setSpotsLeft(event.maxParticipants - registrations.length);
        } catch (error) {
            console.error('Error fetching spots left:', error);
            setSpotsLeft(null);
        }
    };

    const handleRegister = async () => {
        if (!userData || !userData.id) {
            toast.error('Error: You must be logged in to register for an event.');
            return;
        }

        if (userData.id === event.hostId) {
            toast.error('Error: You cannot register for your own event.');
            return;
        }

        try {
            await registerForEvent(userData.id, event.id); // Use the service function
            toast.success('Successfully registered for the event!');
        } catch (error: any) {
            toast.error(`Error: ${error.message}`);
        }
    };

    return (
        <Dialog open={true} onOpenChange={onClose}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle className='font-bold text-xl ml-4 mt-4'>{event.title}</DialogTitle>
                </DialogHeader>
                <div className='flex items-start space-x-2 p-4'>
                    <div className="flex-1 space-y-1 self-center">
                        <p><strong>Game:</strong> {event.boardGameName}</p>
                        <p><strong>Description:</strong> {event.description}</p>
                        <p><strong>Location:</strong> {event.location}</p>
                        <p><strong>Start:</strong> {new Date(event.startDateTime).toLocaleString()}</p>
                        <p><strong>End:</strong> {new Date(event.endDateTime).toLocaleString()}</p>
                        <p><strong>Max Participants:</strong> {event.maxParticipants}</p>
                        <p><strong>Spots Left:</strong> {spotsLeft !== null ? spotsLeft : 'Loading...'}</p>
                        <p><strong>Host:</strong> {hostName || 'Loading...'}</p>
                    </div> 
                    <div className='w-40 h-25 flex-shrink-0'>
                        <img
                            alt='Board Game'
                            src={gameImages[pictureIndex]}
                            className='w-full h-full object-cover rounded-lg shadow-md'
                        />
                    </div>
                </div>
                <DialogFooter className="mt-4">
                    <Button
                        className="bg-black hover:bg-neutral-800 text-white"
                        onClick={handleRegister}
                    >
                        Register for Event
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};

export default EventPopup;