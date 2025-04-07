import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useAuth } from '../../auth/UserAuth';
import { Button } from "@/components/ui/button";
import { createEvent } from '../../services/eventService';
import { fetchBoardGames } from '../../services/boardGameService';
import { toast } from "sonner";

import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogFooter,
} from "@/components/ui/dialog";

const INPUT_BORDER_COLOR = '#808080';

const formSchema = z.object({
    title: z.string().min(1, { message: 'Title is required.' }),
    description: z.string().min(1, { message: 'Description is required.' }),
    boardGameName: z.string().min(1, { message: 'Board game name is required.' }),
    startDateTime: z.string().min(1, { message: 'Start date and time are required.' }),
    endDateTime: z.string().min(1, { message: 'End date and time are required.' }),
    maxParticipants: z
        .preprocess((value) => (typeof value === 'string' ? parseInt(value, 10) : value), z.number().min(1, { message: 'Max participants must be at least 1.' })),
    location: z.string().min(1, { message: 'Location is required.' }),
});

type FormData = z.infer<typeof formSchema>;

interface EventCreationPopupProps {
    onClose: () => void;
}

const EventCreationPopup: React.FC<EventCreationPopupProps> = ({ onClose }) => {
    const { userData } = useAuth();
    const [boardGames, setBoardGames] = useState<string[]>([]);
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<FormData>({
        resolver: zodResolver(formSchema),
    });

    useEffect(() => {
        const fetchBoardGamesWrapper = async () => {
            try {
                const boardGames = await fetchBoardGames();
                const boardGameTitles = boardGames.map((game) => game.title);
                setBoardGames(boardGameTitles);
            } catch (error) {
                console.error('Error fetching board games:', error);
                toast.error('Failed to load board games.');
            }
        };
        fetchBoardGamesWrapper();
    }, []);

    const onSubmit = async (data: FormData) => {
        const eventData = {
            ...data,
            hostId: userData?.id,
        };
        try {
            await createEvent(eventData);
            toast.success('Event created successfully!');
            onClose(); // Close the popup after successful creation
        } catch (error: any) {
            toast.error(`Error: ${error.message}`);
        }
    };

    return (
        <Dialog open={true} onOpenChange={onClose}>
            <DialogContent
                style={{
                    zIndex: 1050
                }}
            >
                <DialogHeader>
                    <DialogTitle className="font-bold text-xl">Create New Event</DialogTitle>
                </DialogHeader>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <div style={{ marginBottom: '10px' }}>
                        <label>Title:</label>
                        <input
                            type="text"
                            {...register('title')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                        {errors.title && <p style={{ color: 'red', marginTop: '5px' }}>{errors.title.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label>Description:</label>
                        <textarea
                            {...register('description')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                        {errors.description && <p style={{ color: 'red', marginTop: '5px' }}>{errors.description.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label>Board Game Name:</label>
                        <select
                            {...register('boardGameName')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        >
                            <option value="">Select a board game</option>
                            {boardGames.map((game) => (
                                <option key={game} value={game}>
                                    {game}
                                </option>
                            ))}
                        </select>
                        {errors.boardGameName && <p style={{ color: 'red', marginTop: '5px' }}>{errors.boardGameName.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label>Start Date & Time:</label>
                        <input
                            type="datetime-local"
                            {...register('startDateTime')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                                colorScheme: 'light',
                            }}
                        />
                        {errors.startDateTime && <p style={{ color: 'red', marginTop: '5px' }}>{errors.startDateTime.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label>End Date & Time:</label>
                        <input
                            type="datetime-local"
                            {...register('endDateTime')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                                colorScheme: 'light',
                            }}
                        />
                        {errors.endDateTime && <p style={{ color: 'red', marginTop: '5px' }}>{errors.endDateTime.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label>Max Participants:</label>
                        <input
                            type="number"
                            {...register('maxParticipants')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                        {errors.maxParticipants && <p style={{ color: 'red', marginTop: '5px' }}>{errors.maxParticipants.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label>Location:</label>
                        <input
                            type="text"
                            {...register('location')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                        {errors.location && <p style={{ color: 'red', marginTop: '5px' }}>{errors.location.message}</p>}
                    </div>
                    
                    <DialogFooter>
                        <Button
                            className="bg-black hover:bg-gray-800 text-white"
                            type="submit"
                            style={{marginTop: '10px'}}
                        >
                            Create
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
};

export default EventCreationPopup;